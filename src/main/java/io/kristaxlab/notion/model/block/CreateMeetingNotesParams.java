package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.Parent;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for creating a meeting notes block.
 *
 * <p>Provide either a {@code file_upload} source with a page parent, or a {@code block} source (no
 * parent). Prefer {@link #fromFileUpload(String, String)} / {@link #fromBlock(String)} or the
 * builder.
 *
 * @see io.kristaxlab.notion.endpoints.BlocksEndpoint#createMeetingNotes(CreateMeetingNotesParams)
 */
@Getter
@Setter
public class CreateMeetingNotesParams {

  private String title;

  private String language;

  private Options options;

  private Source source;

  private Parent parent;

  /**
   * Creates meeting notes from a completed file upload under a page parent.
   *
   * @param pageId parent page id
   * @param fileUploadId completed file upload id
   * @return params with file_upload source and page parent
   */
  public static CreateMeetingNotesParams fromFileUpload(String pageId, String fileUploadId) {
    CreateMeetingNotesParams params = new CreateMeetingNotesParams();
    Source source = new Source();
    source.setType("file_upload");
    source.setFileUploadId(fileUploadId);
    params.setSource(source);
    params.setParent(Parent.pageParent(pageId));
    return params;
  }

  /**
   * Creates meeting notes from an existing audio, video, or file block.
   *
   * @param blockId source block id
   * @return params with block source and no parent
   */
  public static CreateMeetingNotesParams fromBlock(String blockId) {
    CreateMeetingNotesParams params = new CreateMeetingNotesParams();
    Source source = new Source();
    source.setType("block");
    source.setBlockId(blockId);
    params.setSource(source);
    return params;
  }

  /**
   * Creates a builder for meeting-notes create params.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Optional processing settings. */
  @Getter
  @Setter
  public static class Options {

    /** When false, Notion transcribes without kicking off summary generation. */
    private Boolean kickoffSummary;
  }

  /** Media source: {@code file_upload} or {@code block}. */
  @Getter
  @Setter
  public static class Source {

    private String type;

    private String fileUploadId;

    private String blockId;
  }

  /** Builder for {@link CreateMeetingNotesParams}. */
  public static class Builder {

    private String title;
    private String language;
    private Options options;
    private Source source;
    private Parent parent;

    /**
     * Sets the meeting notes title.
     *
     * @param title plain-text title
     * @return this builder
     */
    public Builder title(String title) {
      this.title = title;
      return this;
    }

    /**
     * Sets the language hint.
     *
     * @param language language enum
     * @return this builder
     */
    public Builder language(MeetingNotesLanguage language) {
      this.language = language == null ? null : language.getValue();
      return this;
    }

    /**
     * Sets the language hint as a raw token.
     *
     * @param language language token
     * @return this builder
     */
    public Builder language(String language) {
      this.language = language;
      return this;
    }

    /**
     * Sets whether to kick off summary generation after transcription.
     *
     * @param kickoffSummary {@code true} to generate summary/notes; {@code false} to skip
     * @return this builder
     */
    public Builder kickoffSummary(boolean kickoffSummary) {
      if (this.options == null) {
        this.options = new Options();
      }
      this.options.setKickoffSummary(kickoffSummary);
      return this;
    }

    /**
     * Sets a file-upload source and page parent.
     *
     * @param pageId parent page id
     * @param fileUploadId completed file upload id
     * @return this builder
     */
    public Builder fileUpload(String pageId, String fileUploadId) {
      Source source = new Source();
      source.setType("file_upload");
      source.setFileUploadId(fileUploadId);
      this.source = source;
      this.parent = Parent.pageParent(pageId);
      return this;
    }

    /**
     * Sets a block source (clears any parent).
     *
     * @param blockId source block id
     * @return this builder
     */
    public Builder block(String blockId) {
      Source source = new Source();
      source.setType("block");
      source.setBlockId(blockId);
      this.source = source;
      this.parent = null;
      return this;
    }

    /**
     * Builds the create params.
     *
     * @return new params instance
     */
    public CreateMeetingNotesParams build() {
      CreateMeetingNotesParams params = new CreateMeetingNotesParams();
      params.setTitle(title);
      params.setLanguage(language);
      params.setOptions(options);
      params.setSource(source);
      params.setParent(parent);
      return params;
    }
  }
}
