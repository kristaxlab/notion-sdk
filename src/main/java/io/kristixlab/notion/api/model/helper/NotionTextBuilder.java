package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;

public class NotionTextBuilder {

  private List<RichText> richTexts = new ArrayList<>();

  public List<RichText> build() {
    return new ArrayList<>(richTexts);
  }

  public NotionTextBuilder plainText(String plainText) {
    richTexts.add(NotionText.plainText(plainText));
    return this;
  }

  public NotionTextBuilder expression(String expression) {
    richTexts.add(NotionText.expression(expression));
    return this;
  }

  public NotionTextBuilder url(String url) {
    richTexts.add(NotionText.url(url));
    return this;
  }

  public NotionTextBuilder dateMention(String dateFrom) {
    richTexts.add(NotionText.dateMention(dateFrom));
    return this;
  }

  public NotionTextBuilder dateMention(String dateFrom, String dateTo) {
    richTexts.add(NotionText.dateMention(dateFrom, dateTo));
    return this;
  }

  public NotionTextBuilder dateMention(String dateFrom, String dateTo, String timeZone) {
    richTexts.add(NotionText.dateMention(dateFrom, dateTo, timeZone));
    return this;
  }

  public NotionTextBuilder userMention(String userId) {
    richTexts.add(NotionText.userMention(userId));
    return this;
  }

  public NotionTextBuilder blockMention(String id) {
    richTexts.add(NotionText.blockMention(id));
    return this;
  }

  public NotionTextBuilder customEmoji(String id) {
    richTexts.add(NotionText.customEmoji(id));
    return this;
  }

  // Formatting

  public NotionTextBuilder bold() {
    return bold(true);
  }

  public NotionTextBuilder bold(boolean bold) {
    currentStyle().setBold(bold);
    return this;
  }

  public NotionTextBuilder italic() {
    return italic(true);
  }

  public NotionTextBuilder italic(boolean italic) {
    currentStyle().setItalic(italic);
    return this;
  }

  public NotionTextBuilder strikethrough() {
    return strikethrough(true);
  }

  public NotionTextBuilder strikethrough(boolean strikethrough) {
    currentStyle().setStrikethrough(strikethrough);
    return this;
  }

  public NotionTextBuilder underline() {
    return underline(true);
  }

  public NotionTextBuilder underline(boolean underline) {
    currentStyle().setUnderline(underline);
    return this;
  }

  public NotionTextBuilder code() {
    return code(true);
  }

  public NotionTextBuilder code(boolean code) {
    currentStyle().setCode(code);
    return this;
  }

  // Color
  public NotionTextBuilder color(Color color) {
    if (color == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    return color(color.getValue());
  }

  public NotionTextBuilder color(String color) {
    currentStyle().setColor(color);
    return this;
  }

  public NotionTextBuilder blue() {
    currentStyle().setColor(Color.BLUE.getValue());
    return this;
  }

  public NotionTextBuilder blueBackground() {
    currentStyle().setColor(Color.BLUE_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder red() {
    currentStyle().setColor(Color.RED.getValue());
    return this;
  }

  public NotionTextBuilder redBackground() {
    currentStyle().setColor(Color.RED_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder green() {
    currentStyle().setColor(Color.GREEN.getValue());
    return this;
  }

  public NotionTextBuilder greenBackground() {
    currentStyle().setColor(Color.GREEN_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder gray() {
    currentStyle().setColor(Color.GRAY.getValue());
    return this;
  }

  public NotionTextBuilder grayBackground() {
    currentStyle().setColor(Color.GRAY_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder pink() {
    currentStyle().setColor(Color.PINK.getValue());
    return this;
  }

  public NotionTextBuilder pinkBackground() {
    currentStyle().setColor(Color.PINK_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder purple() {
    currentStyle().setColor(Color.PURPLE.getValue());
    return this;
  }

  public NotionTextBuilder purpleBackground() {
    currentStyle().setColor(Color.PURPLE_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder orange() {
    currentStyle().setColor(Color.ORANGE.getValue());
    return this;
  }

  public NotionTextBuilder orangeBackground() {
    currentStyle().setColor(Color.ORANGE_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder yellow() {
    currentStyle().setColor(Color.YELLOW.getValue());
    return this;
  }

  public NotionTextBuilder yellowBackground() {
    currentStyle().setColor(Color.YELLOW_BACKGROUND.getValue());
    return this;
  }

  public NotionTextBuilder brown() {
    currentStyle().setColor(Color.BROWN.getValue());
    return this;
  }

  public NotionTextBuilder brownBackground() {
    currentStyle().setColor(Color.BROWN_BACKGROUND.getValue());
    return this;
  }

  private RichText getLast() {
    if (richTexts.isEmpty()) {
      throw new IllegalStateException(
          "There is no rich text item defined in the builder, start with text(), url(), etc.");
    }
    return richTexts.get(richTexts.size() - 1);
  }

  private RichText.Annotations currentStyle() {
    RichText last = getLast();
    if (last.getAnnotations() == null) {
      last.setAnnotations(new RichText.Annotations());
    }
    return last.getAnnotations();
  }
}
