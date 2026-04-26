package io.kristaxlab.notion.model.common;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.fluent.NotionTextBuilder;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents file data in the Notion API. This class supports both external files and file uploads.
 * It also includes captions and metadata for the file.
 */
@Getter
@Setter
public class FileData {

  /**
   * The type of the file. Possible values are "file" (only in responses), "external", and
   * "file_upload".
   */
  private String type;

  /** The caption associated with the file, represented as a list of rich text objects. */
  private List<RichText> caption;

  /** Metadata for external files. */
  private ExternalFile external;

  /** Metadata for file uploads. Only used in requests. */
  private FileUploadRef fileUpload;

  /** Metadata for Notion files. Only returned in responses. */
  private NotionFile file;

  /**
   * Creates a new builder instance for constructing {@link FileData} objects.
   *
   * @return a new {@link Builder} instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder class for constructing {@link FileData} objects. */
  public static class Builder {
    private ExternalFile external;
    private FileUploadRef fileUpload;
    private final List<RichText> caption = new ArrayList<>();

    /**
     * Sets the URL for an external file.
     *
     * @param url the URL of the external file
     * @return the builder instance
     */
    public Builder externalUrl(String url) {
      this.external = new ExternalFile();
      this.external.setUrl(url);
      return this;
    }

    /**
     * Sets the file upload ID for a file upload.
     *
     * @param fileUploadId the ID of the file upload
     * @return the builder instance
     */
    public Builder fileUpload(String fileUploadId) {
      this.fileUpload = new FileUploadRef();
      this.fileUpload.setId(fileUploadId);
      return this;
    }

    /**
     * Adds a plain text caption to the file.
     *
     * @param caption the plain text caption
     * @return the builder instance
     */
    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    /**
     * Adds multiple rich text captions to the file.
     *
     * @param caption an array of {@link RichText} objects
     * @return the builder instance
     */
    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    /**
     * Adds a list of rich text captions to the file.
     *
     * @param caption a list of {@link RichText} objects
     * @return the builder instance
     */
    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    /**
     * Adds captions using a consumer of {@link NotionTextBuilder}.
     *
     * @param consumer a consumer that configures the {@link NotionTextBuilder}
     * @return the builder instance
     */
    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
      return this;
    }

    /**
     * Builds the {@link FileData} object.
     *
     * @return the constructed {@link FileData} object
     * @throws IllegalStateException if neither external nor fileUpload is set, or if both are set
     */
    public FileData build() {
      FileData fileData = new FileData();
      if (external == null && fileUpload == null) {
        throw new IllegalStateException("Either external or fileUpload must be set");
      }

      if (external != null && fileUpload != null) {
        throw new IllegalStateException("External and fileUpload cannot both be set");
      }

      if (external != null) {
        fileData.setType("external");
      } else {
        fileData.setType("file_upload");
      }
      fileData.setExternal(external);
      fileData.setFileUpload(fileUpload);
      if (!caption.isEmpty()) {
        fileData.setCaption(new ArrayList<>(caption));
      }
      return fileData;
    }
  }
}
