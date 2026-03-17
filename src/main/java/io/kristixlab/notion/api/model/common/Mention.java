package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;

@Data
public class Mention {

  @JsonProperty("type")
  private String type;

  @JsonProperty("user")
  private User user;

  @JsonProperty("page")
  private BlockReference page;

  @JsonProperty("database")
  private BlockReference database;

  @JsonProperty("date")
  private DateData date;

  @JsonProperty("custom_emoji")
  private CustomEmoji customEmoji;

  // @notionapinotes outdated (only may return in response for blocks created with older Notion
  // versions)
  @JsonProperty("link_mention")
  private Mention.LinkMention linkMention;

  /*
   * @notionapinotes
   * Read only, the link_preview block can only be returned as part of a response.
   * The API does not support creating or appending link_preview blocks.
   * more details: https://developers.notion.com/reference/block#link-preview
   */
  @JsonProperty("link_preview")
  private LinkPreview linkPreview;

  /*
   * @notionapinotes deprecated after March 27, 2023
   * Read only, may return in response, but can not be created/updated with Notion API
   * more details: https://developers.notion.com/reference/block#template and
   * https://developers.notion.com/reference/rich-text#template-mention-type-object
   */
  @JsonProperty("template_mention")
  private TemplateMention templateMention;

  /*
   * @notionapinotes deprecated after March 27, 2023
   * Read only, may return in response, but can not be created/updated with Notion API
   * more details: https://developers.notion.com/reference/block#template and
   * https://developers.notion.com/reference/rich-text#template-mention-type-object
   */
  @Data
  public static class TemplateMention {

    @JsonProperty("template_mention_date")
    private String templateMentionDate;

    @JsonProperty("template_mention_user")
    private String templateMentionUser;
  }

  @Data
  public static class CustomEmoji {

    @JsonProperty("id")
    private String id;
  }

  /*
   * @notionapinotes
   * Read only, the link_preview block can only be returned as part of a response.
   * The API does not support creating or appending link_preview blocks.
   * more details: https://developers.notion.com/reference/block#link-preview
   */
  @Data
  public static class LinkPreview {

    @JsonProperty("url")
    private String url;
  }

  // @notionapinotes not documented but returns for url mention blocks
  @Data
  public static class LinkMention {

    @JsonProperty("href")
    private String href;

    @JsonProperty("title")
    private String title;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("link_provider")
    private String linkProvider;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
  }
}
