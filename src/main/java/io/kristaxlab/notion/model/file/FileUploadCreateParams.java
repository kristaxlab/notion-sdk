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

  public static FileUploadCreateParams external(String filename, String externalUrl) {
    return builder()
        .mode(FileUploadMode.EXTERNAL_URL.getValue())
        .filename(filename)
        .externalUrl(externalUrl)
        .build();
  }

  public static FileUploadCreateParams singlePart(String filename) {
    return builder().mode(FileUploadMode.SINGLE_PART.getValue()).filename(filename).build();
  }

  public static FileUploadCreateParams multiPart(String filename, int numberOfParts) {
    return builder()
        .mode(FileUploadMode.MULTI_PART.getValue())
        .filename(filename)
        .numberOfParts(numberOfParts)
        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String mode;
    private String filename;
    private String contentType;
    private Integer numberOfParts;
    private String externalUrl;

    public Builder mode(String mode) {
      this.mode = mode;
      return this;
    }

    public Builder filename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder numberOfParts(Integer numberOfParts) {
      this.numberOfParts = numberOfParts;
      return this;
    }

    public Builder externalUrl(String externalUrl) {
      this.externalUrl = externalUrl;
      return this;
    }

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
