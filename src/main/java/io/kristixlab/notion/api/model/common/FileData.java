package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {

  /** "file", "external", "file_upload" */
  private String type;

  private ExternalFile external;

  private File file;

  private FileUploadRef fileUpload;

  private List<RichText> caption;

  private String name;

  public static FileData external(String url) {
    FileData fileData = new FileData();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static FileData file(File file) {
    FileData fileData = new FileData();
    fileData.setType("file");
    fileData.setFile(file);
    return fileData;
  }

  public static FileData fileUpload(String id) {
    FileData fileData = new FileData();
    fileData.setType("file_upload");
    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(id);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String type;
    private ExternalFile external;
    private File file;
    private FileUploadRef fileUpload;
    private List<RichText> caption;
    private String name;

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder external(String url) {
      this.external = new ExternalFile();
      this.external.setUrl(url);
      return this;
    }

    public Builder file(File file) {
      this.file = file;
      return this;
    }

    public Builder fileUpload(String fileUploadId) {
      this.fileUpload = new FileUploadRef();
      this.fileUpload.setId(fileUploadId);
      return this;
    }

    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public FileData build() {
      FileData fileData = new FileData();
      fileData.setType(type);
      fileData.setExternal(external);
      fileData.setFile(file);
      fileData.setFileUpload(fileUpload);
      fileData.setCaption(caption);
      fileData.setName(name);
      return fileData;
    }
  }
}
