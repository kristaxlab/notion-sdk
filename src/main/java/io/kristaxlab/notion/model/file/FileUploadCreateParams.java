package io.kristaxlab.notion.model.file;

import lombok.Getter;
import lombok.Setter;

/** Request object for uploading a file to Notion. */
@Getter
@Setter
public class FileUploadCreateParams {

  /** multi_part / external_url / single_part */
  private String mode;

  /**
   * Name of the file to be created. Required when mode is multi_part or external_url. Otherwise
   * optional, and used to override the filename. Must include an extension, or have one inferred
   * from the content_type parameter.
   */
  private String filename;

  /** The MIME type of the file. */
  private String contentType;

  /**
   * When mode is multi_part, the number of parts you are uploading. Must be between 1 and 1,000.
   * This must match the number of parts as well as the final part_number you send.
   *
   * <p>Not allowed when mode is single_part or external_url.
   */
  private Integer numberOfParts;

  /**
   * When mode is external_url, provide the HTTPS URL of a publicly accessible file to import into
   * your workspace.
   */
  private String externalUrl;

  /**
   * Creates params for external-url import mode.
   *
   * @param filename destination filename
   * @param externalUrl publicly accessible HTTPS file URL
   * @return create params for external import
   */
  public static FileUploadCreateParams external(String filename, String externalUrl) {
    return builder()
        .mode(FileUploadMode.EXTERNAL_URL.getValue())
        .filename(filename)
        .externalUrl(externalUrl)
        .build();
  }

  /**
   * Creates params for single-part upload mode.
   *
   * @param filename filename to assign in Notion
   * @return create params for single-part upload
   */
  public static FileUploadCreateParams singlePart(String filename) {
    return builder().mode(FileUploadMode.SINGLE_PART.getValue()).filename(filename).build();
  }

  /**
   * Creates params for multipart upload mode.
   *
   * @param filename filename to assign in Notion
   * @param numberOfParts number of upload parts
   * @return create params for multipart upload
   */
  public static FileUploadCreateParams multiPart(String filename, int numberOfParts) {
    return builder()
        .mode(FileUploadMode.MULTI_PART.getValue())
        .filename(filename)
        .numberOfParts(numberOfParts)
        .build();
  }

  /**
   * Creates a builder for custom upload-create payloads.
   *
   * @return new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link FileUploadCreateParams}. */
  public static class Builder {
    private String mode;
    private String filename;
    private String contentType;
    private Integer numberOfParts;
    private String externalUrl;

    /**
     * Sets upload mode.
     *
     * @param mode mode token from {@link FileUploadMode}
     * @return this builder
     */
    public Builder mode(String mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Sets filename.
     *
     * @param filename destination filename
     * @return this builder
     */
    public Builder filename(String filename) {
      this.filename = filename;
      return this;
    }

    /**
     * Sets content type.
     *
     * @param contentType MIME type
     * @return this builder
     */
    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * Sets number of parts for multipart uploads.
     *
     * @param numberOfParts number of parts
     * @return this builder
     */
    public Builder numberOfParts(Integer numberOfParts) {
      this.numberOfParts = numberOfParts;
      return this;
    }

    /**
     * Sets external URL for external import mode.
     *
     * @param externalUrl publicly accessible HTTPS file URL
     * @return this builder
     */
    public Builder externalUrl(String externalUrl) {
      this.externalUrl = externalUrl;
      return this;
    }

    /**
     * Builds an immutable create params payload.
     *
     * @return upload create params
     */
    public FileUploadCreateParams build() {
      FileUploadCreateParams params = new FileUploadCreateParams();
      params.setMode(mode);
      params.setFilename(filename);
      params.setContentType(contentType);
      params.setNumberOfParts(numberOfParts);
      params.setExternalUrl(externalUrl);
      return params;
    }
  }
}
