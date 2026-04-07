package io.kristixlab.notion.api.model.common;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO check if I should do a separation for blocks the way it is done for pages (icon/iconparams)
public class FileData {

  private String type;

  private ExternalFile external;

  /** TODO check if file and file_upload are mutually exclusive */
  private File file;

  private FileUploadRef fileUpload;

  private List<RichText> caption;

  private String name;

  public static FileData external(String url, String fileName) {
    FileData fileData = new FileData();
    fileData.setType("external");
    fileData.setName(fileName);
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static FileData external(String url) {
    return external(url, "file");
  }

  public static FileData file(String url) {
    FileData fileData = new FileData();
    fileData.setType("file");
    File file = new File();
    file.setUrl(url);
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
}
