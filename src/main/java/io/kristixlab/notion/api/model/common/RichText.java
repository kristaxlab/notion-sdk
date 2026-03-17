package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.users.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RichText {

  /* text | mention | equation */
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

  public RichText() {
    annotations = new Annotations();
  }

  public static RichTextBuilder builder() {
    return new RichTextBuilder();
  }

  @Data
  public static class Annotations {

    @JsonProperty("bold")
    private Boolean bold;

    @JsonProperty("italic")
    private Boolean italic;

    @JsonProperty("strikethrough")
    private Boolean strikethrough;

    @JsonProperty("underline")
    private Boolean underline;

    @JsonProperty("code")
    private Boolean code;

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

  public static class RichTextBuilder {
    private List<RichText> richTexts = new ArrayList<>();
    private RichText currentRichText;

    public RichTextBuilder() {
      currentRichText = new RichText();
    }

    private void ensureCurrentRichText() {
      if (richTexts == null || currentRichText == null) {
        throw new IllegalStateException(
            "Builder is already done, create a new builder for another rich text");
      }
    }

    public List<RichText> buildAsList() {
      ensureCurrentRichText();
      List<RichText> readyRichTexts = new ArrayList<>(richTexts);
      readyRichTexts.add(currentRichText);
      richTexts = null;
      return readyRichTexts;
    }

    public RichText build() {
      ensureCurrentRichText();
      RichText readyRichText = currentRichText;
      currentRichText = null;
      return readyRichText;
    }

    public RichTextBuilder addAnother() {
      ensureCurrentRichText();
      richTexts.add(currentRichText);
      currentRichText = new RichText();
      return this;
    }

    public RichTextBuilder fromText(String plainText) {
      currentRichText.setPlainText(plainText);
      currentRichText.setType(RichTextType.TEXT.getValue());
      Text text = new Text();
      text.setContent(plainText);
      currentRichText.setText(text);
      return this;
    }

    public RichTextBuilder fromExpression(String expression) {
      currentRichText.setPlainText(expression);
      currentRichText.setType(RichTextType.EQUATION.getValue());
      currentRichText.setEquation(Equation.fromExpression(expression));
      return this;
    }

    public RichTextBuilder fromUrl(String url) {
      currentRichText.setPlainText(url);
      currentRichText.setHref(url);
      currentRichText.setType(RichTextType.TEXT.getValue());
      Text text = new Text();
      text.setContent(url);
      text.setLink(new Link());
      text.getLink().setUrl(url);
      currentRichText.setText(text);
      return this;
    }

    public RichTextBuilder fromDateMention(String dateFrom) {
      fromDateMention(dateFrom, null, null);
      return this;
    }

    public RichTextBuilder fromDateMention(String dateFrom, String dateTo) {
      fromDateMention(dateFrom, dateTo, null);
      return this;
    }

    public RichTextBuilder fromDateMention(String dateFrom, String dateTo, String timeZone) {
      currentRichText.setType(RichTextType.MENTION.getValue());

      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.DATE.getValue());
      currentRichText.setMention(mentionObj);

      mentionObj.setDate(new DateData());
      mentionObj.getDate().setStart(dateFrom);
      if (dateTo != null) {
        mentionObj.getDate().setEnd(dateTo);
      }
      if (timeZone != null) {
        mentionObj.getDate().setTimeZone(timeZone);
      }
      return this;
    }

    public RichTextBuilder fromUserMention(String userId) {
      currentRichText.setType(RichTextType.MENTION.getValue());

      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.USER.getValue());
      currentRichText.setMention(mentionObj);

      mentionObj.setUser(new User());
      mentionObj.getUser().setObject(ObjectType.USER.getValue());
      mentionObj.getUser().setId(userId);
      return this;
    }

    public RichTextBuilder fromBlockMention(String id) {
      currentRichText.setType(RichTextType.MENTION.getValue());
      Mention mentionObj = new Mention();
      currentRichText.setMention(mentionObj);

      // works for block id, page id and database id, mind that for database ids Notion API
      // will replace page with database in the response
      mentionObj.setPage(new BlockReference());
      mentionObj.getPage().setId(id);

      return this;
    }

    public RichTextBuilder fromLinkMention(String url) {
      currentRichText.setType(RichTextType.MENTION.getValue());
      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.LINK_PREVIEW.getValue());
      currentRichText.setMention(mentionObj);

      mentionObj.setLinkPreview(new Mention.LinkPreview());

      return this;
    }

    public RichTextBuilder fromCustomEmoji(String id) {
      currentRichText.setType(RichTextType.MENTION.getValue());
      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.CUSTOM_EMOJI.getValue());
      currentRichText.setMention(mentionObj);

      mentionObj.setCustomEmoji(new Mention.CustomEmoji());
      mentionObj.getCustomEmoji().setId(id);

      return this;
    }

    // TODO template mention and link preview

    public RichTextBuilder withBold(boolean bold) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setBold(bold);
      return this;
    }

    public RichTextBuilder withItalic(boolean italic) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setItalic(italic);
      return this;
    }

    public RichTextBuilder withStrikethrough(boolean strikethrough) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setStrikethrough(strikethrough);
      return this;
    }

    public RichTextBuilder withUnderline(boolean underline) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setUnderline(underline);
      return this;
    }

    public RichTextBuilder withCode(boolean code) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setCode(code);
      return this;
    }

    public RichTextBuilder withColor(Color color) {
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      currentRichText.getAnnotations().setColor(color.getValue());
      return this;
    }
  }
}
