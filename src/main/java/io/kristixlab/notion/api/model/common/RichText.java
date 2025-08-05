package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;

@Data
public class RichText {

  @JsonProperty("type")
  private String type;

  @JsonProperty("plain_text")
  private String plainText;

  @JsonProperty("href")
  private String href;

  @JsonProperty("annotations")
  private Annotations annotations;

  @JsonProperty("text")
  private Text text;

  @JsonProperty("mention")
  private Mention mention;

  @JsonProperty("equation")
  private Equation equation;

  @Data
  public static class Annotations {

    @JsonProperty("bold")
    private boolean bold;

    @JsonProperty("italic")
    private boolean italic;

    @JsonProperty("strikethrough")
    private boolean strikethrough;

    @JsonProperty("underline")
    private boolean underline;

    @JsonProperty("code")
    private boolean code;

    @JsonProperty("color")
    private String color;
  }

  @Data
  public static class Text {

    @JsonProperty("content")
    private String content;

    @JsonProperty("link")
    private Link link;
  }

  @Data
  public static class Link {

    @JsonProperty("url")
    private String url;
  }

  @Data
  public static class Mention {

    @JsonProperty("type")
    private String type;

    @JsonProperty("user")
    private User user;

    @JsonProperty("page")
    private Page page;

    @JsonProperty("database")
    private Database database;

    @JsonProperty("date")
    private Date date;

    // TODO
    @JsonProperty("link_preview")
    private Object linkPreview;

    // TODO
    @JsonProperty("template_mention")
    private Object templateMention;

    @JsonProperty("link_mention")
    private LinkMention linkMention;
  }

  @Data
  public static class Equation {

    @JsonProperty("expression")
    private String expression;
  }

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

  @Data
  public static class Database {

    @JsonProperty("id")
    private String id;
  }

  @Data
  public static class Page {

    @JsonProperty("id")
    private String id;
  }

  @Data
  public static class Date {

    @JsonProperty("start")
    private String start;

    @JsonProperty("end")
    private String end;

    @JsonProperty("time_zone")
    private String timeZone;
  }
}
