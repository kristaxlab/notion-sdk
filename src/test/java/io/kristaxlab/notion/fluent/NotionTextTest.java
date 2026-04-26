package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.richtext.RichText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionTextTest {

  @Nested
  class PlainText {

    @Test
    @DisplayName("sets type to text")
    void setsTypeToText() {
      assertEquals("text", NotionText.plainText("hello").getType());
    }

    @Test
    @DisplayName("sets plain text content")
    void setsPlainTextContent() {
      assertEquals("hello", NotionText.plainText("hello").getPlainText());
    }

    @Test
    @DisplayName("sets text content")
    void setsTextContent() {
      assertEquals("hello", NotionText.plainText("hello").getText().getContent());
    }
  }

  @Nested
  class Expression {

    @Test
    @DisplayName("sets type to equation")
    void setsTypeToEquation() {
      assertEquals("equation", NotionText.expression("x^2").getType());
    }

    @Test
    @DisplayName("sets equation expression")
    void setsEquationExpression() {
      assertEquals("x^2", NotionText.expression("x^2").getEquation().getExpression());
    }

    @Test
    @DisplayName("sets plain text")
    void setsPlainText() {
      assertEquals("x^2", NotionText.expression("x^2").getPlainText());
    }
  }

  @Nested
  class Url {

    @Test
    @DisplayName("sets type to text")
    void setsTypeToText() {
      assertEquals("text", NotionText.url("https://example.com").getType());
    }

    @Test
    @DisplayName("sets href")
    void setsHref() {
      assertEquals("https://example.com", NotionText.url("https://example.com").getHref());
    }

    @Test
    @DisplayName("sets link url")
    void setsLinkUrl() {
      assertEquals(
          "https://example.com",
          NotionText.url("https://example.com").getText().getLink().getUrl());
    }
  }

  @Nested
  class DateMention {

    @Test
    @DisplayName("sets type to mention")
    void setsTypeToMention() {
      assertEquals("mention", NotionText.dateMention("2024-01-01").getType());
    }

    @Test
    @DisplayName("sets mention type to date")
    void setsMentionTypeToDate() {
      assertEquals("date", NotionText.dateMention("2024-01-01").getMention().getType());
    }

    @Test
    @DisplayName("sets date start")
    void setsDateStart() {
      assertEquals(
          "2024-01-01", NotionText.dateMention("2024-01-01").getMention().getDate().getStart());
    }

    @Test
    @DisplayName("with end date sets end")
    void withEndDate_setsEnd() {
      assertEquals(
          "2024-01-31",
          NotionText.dateMention("2024-01-01", "2024-01-31").getMention().getDate().getEnd());
    }

    @Test
    @DisplayName("without end date end is null")
    void withoutEndDate_endIsNull() {
      assertNull(NotionText.dateMention("2024-01-01").getMention().getDate().getEnd());
    }

    @Test
    @DisplayName("with time zone sets time zone")
    void withTimeZone_setsTimeZone() {
      assertEquals(
          "UTC",
          NotionText.dateMention("2024-01-01", null, "UTC").getMention().getDate().getTimeZone());
    }
  }

  @Nested
  class UserMention {

    @Test
    @DisplayName("sets type to mention")
    void setsTypeToMention() {
      assertEquals("mention", NotionText.userMention("u-1").getType());
    }

    @Test
    @DisplayName("sets mention type to user")
    void setsMentionTypeToUser() {
      assertEquals("user", NotionText.userMention("u-1").getMention().getType());
    }

    @Test
    @DisplayName("sets user id")
    void setsUserId() {
      assertEquals("u-1", NotionText.userMention("u-1").getMention().getUser().getId());
    }
  }

  @Nested
  class BlockMention {

    @Test
    @DisplayName("sets type to mention")
    void setsTypeToMention() {
      assertEquals("mention", NotionText.blockMention("block-1").getType());
    }

    @Test
    @DisplayName("sets page id")
    void setsPageId() {
      assertEquals("block-1", NotionText.blockMention("block-1").getMention().getPage().getId());
    }
  }

  @Nested
  class CustomEmoji {

    @Test
    @DisplayName("sets type to mention")
    void setsTypeToMention() {
      assertEquals("mention", NotionText.customEmoji("e-id").getType());
    }

    @Test
    @DisplayName("sets mention type to custom emoji")
    void setsMentionTypeToCustomEmoji() {
      assertEquals("custom_emoji", NotionText.customEmoji("e-id").getMention().getType());
    }

    @Test
    @DisplayName("sets custom emoji id")
    void setsCustomEmojiId() {
      assertEquals("e-id", NotionText.customEmoji("e-id").getMention().getCustomEmoji().getId());
    }
  }

  @Nested
  class Bold {

    @Test
    @DisplayName("from string sets bold")
    void fromString_setsBold() {
      assertTrue(NotionText.bold("hi").getAnnotations().getBold());
    }

    @Test
    @DisplayName("from rich text sets bold on existing instance")
    void fromRichText_setsBoldOnExistingInstance() {
      RichText rt = NotionText.plainText("text");
      NotionText.bold(rt);
      assertTrue(rt.getAnnotations().getBold());
    }

    @Test
    @DisplayName("from rich text returns same instance")
    void fromRichText_returnsSameInstance() {
      RichText rt = NotionText.plainText("text");
      assertSame(rt, NotionText.bold(rt));
    }

    @Test
    @DisplayName("null rich text throws null pointer exception")
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.bold((RichText) null));
    }
  }

  @Nested
  class Italic {

    @Test
    @DisplayName("from string sets italic")
    void fromString_setsItalic() {
      assertTrue(NotionText.italic("hi").getAnnotations().getItalic());
    }

    @Test
    @DisplayName("null rich text throws null pointer exception")
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.italic((RichText) null));
    }
  }

  @Nested
  class Underline {

    @Test
    @DisplayName("from string sets underline")
    void fromString_setsUnderline() {
      assertTrue(NotionText.underline("hi").getAnnotations().getUnderline());
    }

    @Test
    @DisplayName("null rich text throws null pointer exception")
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.underline((RichText) null));
    }
  }

  @Nested
  class Strikethrough {

    @Test
    @DisplayName("from string sets strikethrough")
    void fromString_setsStrikethrough() {
      assertTrue(NotionText.strikethrough("hi").getAnnotations().getStrikethrough());
    }

    @Test
    @DisplayName("null rich text throws null pointer exception")
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.strikethrough((RichText) null));
    }
  }

  @Nested
  class Code {

    @Test
    @DisplayName("from string sets code")
    void fromString_setsCode() {
      assertTrue(NotionText.code("x").getAnnotations().getCode());
    }

    @Test
    @DisplayName("null rich text throws null pointer exception")
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.code((RichText) null));
    }
  }

  @Nested
  class Colors {

    @Test
    @DisplayName("blue sets blue color")
    void blue_setsBlueColor() {
      assertEquals("blue", NotionText.blue("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("blue background sets color")
    void blueBackground_setsColor() {
      assertEquals(
          "blue_background", NotionText.blueBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("red sets red color")
    void red_setsRedColor() {
      assertEquals("red", NotionText.red("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("red background sets color")
    void redBackground_setsColor() {
      assertEquals("red_background", NotionText.redBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("green sets green color")
    void green_setsGreenColor() {
      assertEquals("green", NotionText.green("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("green background sets color")
    void greenBackground_setsColor() {
      assertEquals(
          "green_background", NotionText.greenBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("gray sets gray color")
    void gray_setsGrayColor() {
      assertEquals("gray", NotionText.gray("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("gray background sets color")
    void grayBackground_setsColor() {
      assertEquals(
          "gray_background", NotionText.grayBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("pink sets pink color")
    void pink_setsPinkColor() {
      assertEquals("pink", NotionText.pink("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("pink background sets color")
    void pinkBackground_setsColor() {
      assertEquals(
          "pink_background", NotionText.pinkBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("purple sets purple color")
    void purple_setsPurpleColor() {
      assertEquals("purple", NotionText.purple("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("purple background sets color")
    void purpleBackground_setsColor() {
      assertEquals(
          "purple_background", NotionText.purpleBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("orange sets orange color")
    void orange_setsOrangeColor() {
      assertEquals("orange", NotionText.orange("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("orange background sets color")
    void orangeBackground_setsColor() {
      assertEquals(
          "orange_background", NotionText.orangeBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("yellow sets yellow color")
    void yellow_setsYellowColor() {
      assertEquals("yellow", NotionText.yellow("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("yellow background sets color")
    void yellowBackground_setsColor() {
      assertEquals(
          "yellow_background", NotionText.yellowBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("brown sets brown color")
    void brown_setsBrownColor() {
      assertEquals("brown", NotionText.brown("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("brown background sets color")
    void brownBackground_setsColor() {
      assertEquals(
          "brown_background", NotionText.brownBackground("text").getAnnotations().getColor());
    }

    @Test
    @DisplayName("color on existing rich text overwrites previous color")
    void colorOnExistingRichText_overwritesPreviousColor() {
      RichText rt = NotionText.blue("text");
      NotionText.red(rt);
      assertEquals("red", rt.getAnnotations().getColor());
    }
  }
}
