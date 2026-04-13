package io.kristaxlab.notion.model.common.richtext;

import io.kristaxlab.notion.model.common.*;
import io.kristaxlab.notion.model.common.Color;
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

  public List<RichText> asList() {
    List<RichText> richTexts = new ArrayList<>();
    richTexts.add(this);
    return richTexts;
  }

  public RichText bold() {
    return bold(true);
  }

  public RichText bold(boolean bold) {
    ensureAnnotations().setBold(bold);
    return this;
  }

  public RichText italic() {
    return italic(true);
  }

  public RichText italic(boolean italic) {
    ensureAnnotations().setItalic(italic);
    return this;
  }

  public RichText strikethrough() {
    return strikethrough(true);
  }

  public RichText strikethrough(boolean strikethrough) {
    ensureAnnotations().setStrikethrough(strikethrough);
    return this;
  }

  public RichText underline() {
    return underline(true);
  }

  public RichText underline(boolean underline) {
    ensureAnnotations().setUnderline(underline);
    return this;
  }

  public RichText code() {
    return code(true);
  }

  public RichText code(boolean code) {
    ensureAnnotations().setCode(code);
    return this;
  }

  // Color

  /**
   * Sets the text color and returns {@code this}.
   *
   * @param color the color to apply
   * @return this RichText
   */
  public RichText color(Color color) {
    ensureAnnotations().setColor(color.getValue());
    return this;
  }

  /** Sets the color to {@code default} and returns {@code this}. */
  public RichText defaultColor() {
    return color(Color.DEFAULT);
  }

  /** Sets the text color to gray and returns {@code this}. */
  public RichText gray() {
    return color(Color.GRAY);
  }

  /** Sets the background color to gray and returns {@code this}. */
  public RichText grayBackground() {
    return color(Color.GRAY_BACKGROUND);
  }

  /** Sets the text color to brown and returns {@code this}. */
  public RichText brown() {
    return color(Color.BROWN);
  }

  /** Sets the background color to brown and returns {@code this}. */
  public RichText brownBackground() {
    return color(Color.BROWN_BACKGROUND);
  }

  /** Sets the text color to orange and returns {@code this}. */
  public RichText orange() {
    return color(Color.ORANGE);
  }

  /** Sets the background color to orange and returns {@code this}. */
  public RichText orangeBackground() {
    return color(Color.ORANGE_BACKGROUND);
  }

  /** Sets the text color to yellow and returns {@code this}. */
  public RichText yellow() {
    return color(Color.YELLOW);
  }

  /** Sets the background color to yellow and returns {@code this}. */
  public RichText yellowBackground() {
    return color(Color.YELLOW_BACKGROUND);
  }

  /** Sets the text color to green and returns {@code this}. */
  public RichText green() {
    return color(Color.GREEN);
  }

  /** Sets the background color to green and returns {@code this}. */
  public RichText greenBackground() {
    return color(Color.GREEN_BACKGROUND);
  }

  /** Sets the text color to blue and returns {@code this}. */
  public RichText blue() {
    return color(Color.BLUE);
  }

  /** Sets the background color to blue and returns {@code this}. */
  public RichText blueBackground() {
    return color(Color.BLUE_BACKGROUND);
  }

  /** Sets the text color to purple and returns {@code this}. */
  public RichText purple() {
    return color(Color.PURPLE);
  }

  /** Sets the background color to purple and returns {@code this}. */
  public RichText purpleBackground() {
    return color(Color.PURPLE_BACKGROUND);
  }

  /** Sets the text color to pink and returns {@code this}. */
  public RichText pink() {
    return color(Color.PINK);
  }

  /** Sets the background color to pink and returns {@code this}. */
  public RichText pinkBackground() {
    return color(Color.PINK_BACKGROUND);
  }

  /** Sets the text color to red and returns {@code this}. */
  public RichText red() {
    return color(Color.RED);
  }

  /** Sets the background color to red and returns {@code this}. */
  public RichText redBackground() {
    return color(Color.RED_BACKGROUND);
  }

  private RichText.Annotations ensureAnnotations() {
    if (this.getAnnotations() == null) {
      this.setAnnotations(new RichText.Annotations());
    }
    return this.getAnnotations();
  }
}
