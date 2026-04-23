package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/** Represents a page/block icon payload in Notion requests and responses. */
@Getter
@Setter
public class Icon {

  private String type;

  private String emoji;

  private CustomEmoji customEmoji;

  private NotionIcon icon;

  private ExternalFile external;

  /** Only in request */
  private FileUploadRef fileUpload;

  /** Only in response */
  private NotionFile file;

  /**
   * Creates a built-in Notion icon payload.
   *
   * @param name icon name
   * @param color icon color token
   * @return icon payload
   */
  public static Icon notionIcon(String name, String color) {
    Icon icon = new Icon();
    icon.setType("icon");
    NotionIcon notionIcon = new NotionIcon();
    notionIcon.setName(name);
    notionIcon.setColor(color);
    icon.setIcon(notionIcon);
    return icon;
  }

  /**
   * Creates an emoji icon payload.
   *
   * @param emoji emoji character
   * @return icon payload
   */
  public static Icon emoji(String emoji) {
    Icon icon = new Icon();
    icon.setType("emoji");
    icon.setEmoji(emoji);
    return icon;
  }

  /**
   * Creates a custom-emoji icon payload.
   *
   * @param id custom emoji id
   * @param name custom emoji name
   * @return icon payload
   */
  public static Icon customEmoji(String id, String name) {
    Icon icon = new Icon();
    icon.setType("custom_emoji");
    CustomEmoji customEmoji = new CustomEmoji();
    customEmoji.setId(id);
    customEmoji.setName(name);
    icon.setCustomEmoji(customEmoji);
    return icon;
  }

  /**
   * Creates an external icon payload.
   *
   * @param url externally hosted icon URL
   * @return icon payload
   */
  public static Icon external(String url) {
    Icon icon = new Icon();
    icon.setType("external");
    ExternalFile externalFile = new ExternalFile();
    externalFile.setUrl(url);
    icon.setExternal(externalFile);
    return icon;
  }

  /**
   * Creates a file-upload icon payload.
   *
   * @param fileUploadId file upload id
   * @return icon payload
   */
  public static Icon fileUpload(String fileUploadId) {
    Icon icon = new Icon();
    icon.setType("file_upload");
    FileUploadRef fu = new FileUploadRef();
    fu.setId(fileUploadId);
    icon.setFileUpload(fu);
    return icon;
  }
}
