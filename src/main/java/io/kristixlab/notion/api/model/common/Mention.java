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
  private PageReference page;

  @JsonProperty("database")
  private PageReference database;

  @JsonProperty("date")
  private DateData date;

  @JsonProperty("link_mention")
  private Mention.LinkMention linkMention;

  // TODO
  @JsonProperty("link_preview")
  private Object linkPreview;

  // TODO
  @JsonProperty("template_mention")
  private Object templateMention;


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
