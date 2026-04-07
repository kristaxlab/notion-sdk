package io.kristixlab.notion.api.model.common.richtext;

import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.users.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichText {

  private String type;

  private String plainText;

  private String href;

  private Annotations annotations;

  private Text text;

  private Mention mention;

  private Equation equation;

  public RichText() {
    annotations = new Annotations();
  }

  public static List<RichText> of(String text) {
    RichText richText = new RichText();
    richText.setPlainText(text);
    richText.setType(RichTextType.TEXT.getValue());

    Text textObj = new Text();
    textObj.setContent(text);
    richText.setText(textObj);

    List<RichText> richTexts = new ArrayList<>();
    richTexts.add(richText);
    return richTexts;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Getter
  @Setter
  public static class Annotations {

    private Boolean bold;

    private Boolean italic;

    private Boolean strikethrough;

    private Boolean underline;

    private Boolean code;

    private String color;
  }

  public static class Builder {
    private List<RichText> richTexts = new ArrayList<>();
    private RichText currentRichText;

    private Builder() {}

    public List<RichText> buildList() {
      List<RichText> readyRichTexts = new ArrayList<>(richTexts);
      readyRichTexts.add(currentRichText);
      richTexts = null;
      return readyRichTexts;
    }

    public RichText build() {
      RichText readyRichText = currentRichText;
      return readyRichText;
    }

    private RichText another() {
      if (currentRichText != null) {
        richTexts.add(currentRichText);
      }
      currentRichText = new RichText();
      return currentRichText;
    }

    private Annotations currentStyle() {
      if (currentRichText == null) {
        throw new IllegalStateException(
            "No current rich text segment, start with text(), url(), etc.");
      }
      if (currentRichText.getAnnotations() == null) {
        currentRichText.setAnnotations(new Annotations());
      }
      return currentRichText.getAnnotations();
    }

    public Builder text(String plainText) {
      RichText rt = another();
      rt.setPlainText(plainText);
      rt.setType(RichTextType.TEXT.getValue());

      Text text = new Text();
      text.setContent(plainText);
      rt.setText(text);

      return this;
    }

    public Builder expression(String expression) {
      RichText rt = another();
      rt.setPlainText(expression);
      rt.setType(RichTextType.EQUATION.getValue());
      rt.setEquation(Equation.of(expression));
      return this;
    }

    public Builder url(String url) {
      RichText rt = another();
      rt.setPlainText(url);
      rt.setHref(url);
      rt.setType(RichTextType.TEXT.getValue());

      Text text = new Text();
      text.setContent(url);
      text.setLink(new Text.Link());
      text.getLink().setUrl(url);
      rt.setText(text);

      return this;
    }

    public Builder dateMention(String dateFrom) {
      dateMention(dateFrom, null, null);
      return this;
    }

    public Builder dateMention(String dateFrom, String dateTo) {
      dateMention(dateFrom, dateTo, null);
      return this;
    }

    public Builder dateMention(String dateFrom, String dateTo, String timeZone) {
      RichText rt = another();
      rt.setType(RichTextType.MENTION.getValue());

      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.DATE.getValue());
      rt.setMention(mentionObj);

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

    public Builder userMention(String userId) {
      RichText rt = another();
      rt.setType(RichTextType.MENTION.getValue());

      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.USER.getValue());
      rt.setMention(mentionObj);

      mentionObj.setUser(new User());
      mentionObj.getUser().setObject(ObjectType.USER.getValue());
      mentionObj.getUser().setId(userId);
      return this;
    }

    public Builder blockMention(String id) {
      RichText rt = another();
      rt.setType(RichTextType.MENTION.getValue());
      Mention mentionObj = new Mention();
      rt.setMention(mentionObj);

      // works for block id, page id and database id, mind that for database ids Notion API
      // will replace page with database in the response
      mentionObj.setPage(new BlockReference());
      mentionObj.getPage().setId(id);

      return this;
    }

    public Builder customEmoji(String id) {
      RichText rt = another();
      rt.setType(RichTextType.MENTION.getValue());
      Mention mentionObj = new Mention();
      mentionObj.setType(MentionType.CUSTOM_EMOJI.getValue());
      rt.setMention(mentionObj);

      mentionObj.setCustomEmoji(new Mention.CustomEmojiRef());
      mentionObj.getCustomEmoji().setId(id);

      return this;
    }

    public Builder bold() {
      return bold(true);
    }

    public Builder bold(boolean bold) {
      currentStyle().setBold(bold);
      return this;
    }

    public Builder italic() {
      return italic(true);
    }

    public Builder italic(boolean italic) {
      currentStyle().setItalic(italic);
      return this;
    }

    public Builder strikethrough() {
      return strikethrough(true);
    }

    public Builder strikethrough(boolean strikethrough) {
      currentStyle().setStrikethrough(strikethrough);
      return this;
    }

    public Builder underline() {
      return underline(true);
    }

    public Builder underline(boolean underline) {
      currentStyle().setUnderline(underline);
      return this;
    }

    public Builder code() {
      return code(true);
    }

    public Builder code(boolean code) {
      currentStyle().setCode(code);
      return this;
    }

    public Builder color(Color color) {
      if (color == null) {
        throw new IllegalArgumentException("Color cannot be null");
      }
      return color(color.getValue());
    }

    public Builder color(String color) {
      currentStyle().setColor(color);
      return this;
    }
  }
}
