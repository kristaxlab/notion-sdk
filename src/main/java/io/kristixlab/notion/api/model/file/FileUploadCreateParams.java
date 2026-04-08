package io.kristixlab.notion.api.model.file;

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
    private final FileUploadCreateParams params;

    public Builder() {
      this.params = new FileUploadCreateParams();
    }

    public Builder mode(String mode) {
      params.setMode(mode);
      return this;
    }

    public Builder filename(String filename) {
      params.setFilename(filename);
      return this;
    }

    public Builder contentType(String contentType) {
      params.setContentType(contentType);
      return this;
    }

    public Builder numberOfParts(Integer numberOfParts) {
      params.setNumberOfParts(numberOfParts);
      return this;
    }

    public Builder externalUrl(String externalUrl) {
      params.setExternalUrl(externalUrl);
      return this;
    }

    public FileUploadCreateParams build() {
      return params;
    }
  }
}
