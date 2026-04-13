package io.kristaxlab.notion.model.file;

public enum FileUploadMode {
  SINGLE_PART("single_part"),
  MULTI_PART("multi_part"),
  EXTERNAL_URL("external_url");

  private final String value;

  FileUploadMode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return getValue();
  }
}
