package io.kristaxlab.notion.model.file;

/** Supported file upload modes accepted by Notion's file upload API. */
public enum FileUploadMode {
  /** Single-part upload. */
  SINGLE_PART("single_part"),
  /** Multipart upload. */
  MULTI_PART("multi_part"),
  /** Remote file import via URL. */
  EXTERNAL_URL("external_url");

  private final String value;

  FileUploadMode(String value) {
    this.value = value;
  }

  /**
   * Returns the raw API token for this mode.
   *
   * @return raw mode value
   */
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return getValue();
  }
}
