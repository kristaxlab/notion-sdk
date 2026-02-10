package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class FileData {

  @JsonProperty("type")
  private String type;

  @JsonProperty("external")
  private ExternalFile external;

  /** TODO check if file and file_upload are mutually exclusive */
  @JsonProperty("file")
  private File file;

  @JsonProperty("file_upload")
  private FileUpload fileUpload;

  // TODO no caption for covers and emojis, do I have to divide this class to two?
  // check if captionas are for page props.
  @JsonProperty("caption")
  private List<RichText> caption;

  @JsonProperty("name")
  private String name;

  public static FileData fromExternalUrl(String url) {
    FileData fileData = new FileData();
    fileData.setType("external");
    ExternalFile external = new ExternalFile();
    external.setUrl(url);
    fileData.setExternal(external);
    return fileData;
  }

  public static FileData fromFileUpload(String url) {
    FileData fileData = new FileData();
    fileData.setType("file");
    File file = new File();
    file.setUrl(url);
    fileData.setFile(file);
    return fileData;
  }
}
