package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {

  private String type;

  private FileData.External external;

  private FileData.File file;

  private FileData.FileUpload fileUpload;

  private List<RichText> caption;

  private String name;

  public static FileData external(String url) {
    FileData fileData = new FileData();
    fileData.setType("external");
    FileData.External external = new FileData.External();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static FileData file(String url) {
    FileData fileData = new FileData();
    fileData.setType("file");
    FileData.File file = new FileData.File();
    file.setUrl(url);
    fileData.setFile(file);
    return fileData;
  }

  public static FileData fileUpload(String id) {
    FileData fileData = new FileData();
    fileData.setType("file_upload");
    FileData.FileUpload fileUpload = new FileData.FileUpload();
    fileUpload.setId(id);
    fileData.setFileUpload(fileUpload);
    return fileData;
  }

  @Getter
  @Setter
  public static class External {

    private String url;
  }

  @Getter
  @Setter
  public static class File {

    private String url;

    private String expiryTime;
  }

  @Getter
  @Setter
  public static class FileUpload {

    private String id;
  }
}
