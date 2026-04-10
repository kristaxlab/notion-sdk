package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.common.richtext.*;
import io.kristixlab.notion.api.model.user.User;
import java.util.Objects;

public class NotionText {

  public static NotionTextBuilder textBuilder() {
    return new NotionTextBuilder();
  }

  public static RichText plainText(String plainText) {
    RichText rt = new RichText();
    rt.setPlainText(plainText);
    rt.setType(RichTextType.TEXT.getValue());

    Text text = new Text();
    text.setContent(plainText);
    rt.setText(text);

    return rt;
  }

  public static RichText expression(String expression) {
    RichText rt = new RichText();
    rt.setPlainText(expression);
    rt.setType(RichTextType.EQUATION.getValue());

    Equation equation = new Equation();
    equation.setExpression(expression);
    rt.setEquation(equation);
    return rt;
  }

  public static RichText url(String url) {
    RichText rt = new RichText();
    rt.setPlainText(url);
    rt.setHref(url);
    rt.setType(RichTextType.TEXT.getValue());

    Text text = new Text();
    text.setContent(url);
    text.setLink(new Text.Link());
    text.getLink().setUrl(url);
    rt.setText(text);

    return rt;
  }

  public static RichText dateMention(String dateFrom) {
    return dateMention(dateFrom, null, null);
  }

  public static RichText dateMention(String dateFrom, String dateTo) {
    return dateMention(dateFrom, dateTo, null);
  }

  public static RichText dateMention(String dateFrom, String dateTo, String timeZone) {
    RichText rt = new RichText();
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
    return rt;
  }

  public static RichText userMention(String userId) {
    RichText rt = new RichText();
    rt.setType(RichTextType.MENTION.getValue());

    Mention mentionObj = new Mention();
    mentionObj.setType(MentionType.USER.getValue());
    rt.setMention(mentionObj);

    mentionObj.setUser(new User());
    mentionObj.getUser().setObject(ObjectType.USER.getValue());
    mentionObj.getUser().setId(userId);
    return rt;
  }

  public static RichText blockMention(String id) {
    RichText rt = new RichText();
    rt.setType(RichTextType.MENTION.getValue());
    Mention mentionObj = new Mention();
    rt.setMention(mentionObj);

    // works for block id, page id and database id, mind that for database ids Notion API
    // will replace page with database in the response
    mentionObj.setPage(new BlockReference());
    mentionObj.getPage().setId(id);

    return rt;
  }

  public static RichText customEmoji(String id) {
    RichText rt = new RichText();
    rt.setType(RichTextType.MENTION.getValue());
    Mention mentionObj = new Mention();
    mentionObj.setType(MentionType.CUSTOM_EMOJI.getValue());
    rt.setMention(mentionObj);

    mentionObj.setCustomEmoji(new Mention.CustomEmojiRef());
    mentionObj.getCustomEmoji().setId(id);

    return rt;
  }

  public static RichText bold(String text) {
    RichText richText = plainText(text);
    return bold(richText);
  }

  public static RichText bold(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setBold(true);
    return richText;
  }

  public static RichText italic(String text) {
    RichText richText = plainText(text);
    return italic(richText);
  }

  public static RichText italic(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setItalic(true);
    return richText;
  }

  public static RichText underline(String text) {
    RichText richText = plainText(text);
    return underline(richText);
  }

  public static RichText underline(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setUnderline(true);
    return richText;
  }

  public static RichText strikethrough(String text) {
    RichText richText = plainText(text);
    return strikethrough(richText);
  }

  public static RichText strikethrough(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setStrikethrough(true);
    return richText;
  }

  public static RichText code(String text) {
    RichText richText = plainText(text);
    return code(richText);
  }

  public static RichText code(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setCode(true);
    return richText;
  }

  public static RichText blue(String text) {
    RichText richText = plainText(text);
    return blue(richText);
  }

  public static RichText blue(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.BLUE.getValue());
    return richText;
  }

  public static RichText blueBackground(String text) {
    RichText richText = plainText(text);
    return blueBackground(richText);
  }

  public static RichText blueBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.BLUE_BACKGROUND.getValue());
    return richText;
  }

  public static RichText red(String text) {
    RichText richText = plainText(text);
    return red(richText);
  }

  public static RichText red(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.RED.getValue());
    return richText;
  }

  public static RichText redBackground(String text) {
    RichText richText = plainText(text);
    return redBackground(richText);
  }

  public static RichText redBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.RED_BACKGROUND.getValue());
    return richText;
  }

  public static RichText green(String text) {
    RichText richText = plainText(text);
    return green(richText);
  }

  public static RichText green(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.GREEN.getValue());
    return richText;
  }

  public static RichText greenBackground(String text) {
    RichText richText = plainText(text);
    return greenBackground(richText);
  }

  public static RichText greenBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.GREEN_BACKGROUND.getValue());
    return richText;
  }

  public static RichText gray(String text) {
    RichText richText = plainText(text);
    return gray(richText);
  }

  public static RichText gray(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.GRAY.getValue());
    return richText;
  }

  public static RichText grayBackground(String text) {
    RichText richText = plainText(text);
    return grayBackground(richText);
  }

  public static RichText grayBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.GRAY_BACKGROUND.getValue());
    return richText;
  }

  public static RichText pink(String text) {
    RichText richText = plainText(text);
    return pink(richText);
  }

  public static RichText pink(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.PINK.getValue());
    return richText;
  }

  public static RichText pinkBackground(String text) {
    RichText richText = plainText(text);
    return pinkBackground(richText);
  }

  public static RichText pinkBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.PINK_BACKGROUND.getValue());
    return richText;
  }

  public static RichText purple(String text) {
    RichText richText = plainText(text);
    return purple(richText);
  }

  public static RichText purple(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.PURPLE.getValue());
    return richText;
  }

  public static RichText purpleBackground(String text) {
    RichText richText = plainText(text);
    return purpleBackground(richText);
  }

  public static RichText purpleBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.PURPLE_BACKGROUND.getValue());
    return richText;
  }

  public static RichText orange(String text) {
    RichText richText = plainText(text);
    return orange(richText);
  }

  public static RichText orange(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.ORANGE.getValue());
    return richText;
  }

  public static RichText orangeBackground(String text) {
    RichText richText = plainText(text);
    return orangeBackground(richText);
  }

  public static RichText orangeBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.ORANGE_BACKGROUND.getValue());
    return richText;
  }

  public static RichText yellow(String text) {
    RichText richText = plainText(text);
    return yellow(richText);
  }

  public static RichText yellow(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.YELLOW.getValue());
    return richText;
  }

  public static RichText yellowBackground(String text) {
    RichText richText = plainText(text);
    return yellowBackground(richText);
  }

  public static RichText yellowBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.YELLOW_BACKGROUND.getValue());
    return richText;
  }

  public static RichText brown(String text) {
    RichText richText = plainText(text);
    return brown(richText);
  }

  public static RichText brown(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.BROWN.getValue());
    return richText;
  }

  public static RichText brownBackground(String text) {
    RichText richText = plainText(text);
    return brownBackground(richText);
  }

  public static RichText brownBackground(RichText richText) {
    Objects.requireNonNull(richText, "RichText cannot be null");
    ensureAnnotations(richText).setColor(Color.BROWN_BACKGROUND.getValue());
    return richText;
  }

  private static RichText.Annotations ensureAnnotations(RichText richText) {
    if (richText.getAnnotations() == null) {
      richText.setAnnotations(new RichText.Annotations());
    }
    return richText.getAnnotations();
  }
}
