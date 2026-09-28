package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.fluent.NotionTextBuilder;
import io.kristaxlab.notion.model.common.FileUploadRef;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion embed block that displays embedded external content or an HTML embed.
 *
 * <p>Create instances with {@link #builder()}. Write payloads accept either a {@code url} or a
 * {@code file_upload} (with {@code type}), never both. Responses always carry a temporary {@code
 * url} plus an optional caption — an HTML embed created from an uploaded {@code .html}/{@code .htm}
 * file reads back the same way.
 */
@Getter
@Setter
public class EmbedBlock extends Block {
  private Embed embed;

  /** Creates an embed block initialized with an empty embed payload. */
  public EmbedBlock() {
    setType(BlockType.EMBED.getValue());
    embed = new Embed();
  }

  /**
   * The inner content object of an embed block.
   *
   * <p>On write, set either {@code url} or {@code fileUpload} (with {@code type} {@code
   * "file_upload"}). On read, Notion returns a temporary {@code url} and caption only.
   */
  @Getter
  @Setter
  public static class Embed {

    /**
     * Discriminator used on file-upload write payloads. Expected value is {@code "file_upload"}.
     * Absent on URL writes and on responses.
     */
    private String type;

    /** Embed URL. Present on URL writes and on every response (temporary when file-backed). */
    private String url;

    /** File upload reference. Create/update only; never returned on retrieve. */
    private FileUploadRef fileUpload;

    private List<RichText> caption;
  }

  /**
   * Returns a new builder for constructing an {@link EmbedBlock} with URL or file upload and
   * optional caption.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link EmbedBlock}. Exactly one of URL or file upload must be set. */
  public static class Builder {

    private String url;

    private FileUploadRef fileUpload;

    private List<RichText> caption = new ArrayList<>();

    private Builder() {}

    /**
     * Sets the embed URL. Mutually exclusive with {@link #fileUpload(String)}.
     *
     * @param url the URL to embed
     * @return this builder
     */
    public Builder url(String url) {
      this.url = url;
      return this;
    }

    /**
     * Sets the file upload id for an HTML embed (or other file-backed embed). Mutually exclusive
     * with {@link #url(String)}.
     *
     * @param fileUploadId id of a completed file upload
     * @return this builder
     */
    public Builder fileUpload(String fileUploadId) {
      this.fileUpload = new FileUploadRef();
      this.fileUpload.setId(fileUploadId);
      return this;
    }

    /**
     * Adds caption text as a plain rich-text fragment.
     *
     * @param caption caption text
     * @return this builder
     */
    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    /**
     * Adds caption fragments.
     *
     * @param caption rich-text fragments to append
     * @return this builder
     */
    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    /**
     * Adds caption fragments.
     *
     * @param caption rich-text fragments to append
     * @return this builder
     */
    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    /**
     * Builds caption fragments with the fluent text builder and appends them.
     *
     * @param consumer callback that populates a {@link NotionTextBuilder}
     * @return this builder
     */
    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
      return this;
    }

    /**
     * Builds the {@link EmbedBlock}.
     *
     * @return a new EmbedBlock
     * @throws IllegalStateException if neither URL nor file upload is set, or if both are set
     */
    public EmbedBlock build() {
      boolean hasUrl = url != null;
      boolean hasFileUpload = fileUpload != null;
      if (!hasUrl && !hasFileUpload) {
        throw new IllegalStateException("Either url or fileUpload must be set");
      }
      if (hasUrl && hasFileUpload) {
        throw new IllegalStateException("url and fileUpload cannot both be set");
      }

      EmbedBlock block = new EmbedBlock();
      if (hasUrl) {
        block.getEmbed().setUrl(url);
      } else {
        block.getEmbed().setType("file_upload");
        block.getEmbed().setFileUpload(fileUpload);
      }
      if (!caption.isEmpty()) {
        block.getEmbed().setCaption(new ArrayList<>(caption));
      }
      return block;
    }
  }
}
