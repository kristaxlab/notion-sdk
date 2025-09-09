package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
  public static class Equation {

    @JsonProperty("expression")
    private String expression;
  }

  public static RichText of(String plainText) {
    return of(plainText, "default");
  }

  public static RichText of(String plainText, String color) {
    RichText richText = new RichText();
    richText.setType("text");
    richText.setPlainText(plainText);
    Text text = new Text();
    text.setContent(plainText);
    richText.setText(text);
    Annotations annotations = new Annotations();
    annotations.setBold(false);
    annotations.setItalic(false);
    annotations.setStrikethrough(false);
    annotations.setUnderline(false);
    annotations.setCode(false);
    annotations.setColor(color);
    richText.setAnnotations(annotations);
    return richText;
  }

  public static List<RichText> asList(String plainText) {
    List<RichText> list = new ArrayList<>();
    list.add(of(plainText));
    return list;
  }
}
