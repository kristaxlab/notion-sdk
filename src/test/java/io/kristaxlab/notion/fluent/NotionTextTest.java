package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.richtext.RichText;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionTextTest {

  @Nested
  class PlainText {

    @Test
    void setsTypeToText() {
      assertEquals("text", NotionText.plainText("hello").getType());
    }

    @Test
    void setsPlainTextContent() {
      assertEquals("hello", NotionText.plainText("hello").getPlainText());
    }

    @Test
    void setsTextContent() {
      assertEquals("hello", NotionText.plainText("hello").getText().getContent());
    }
  }

  @Nested
  class Expression {

    @Test
    void setsTypeToEquation() {
      assertEquals("equation", NotionText.expression("x^2").getType());
    }

    @Test
    void setsEquationExpression() {
      assertEquals("x^2", NotionText.expression("x^2").getEquation().getExpression());
    }

    @Test
    void setsPlainText() {
      assertEquals("x^2", NotionText.expression("x^2").getPlainText());
    }
  }

  @Nested
  class Url {

    @Test
    void setsTypeToText() {
      assertEquals("text", NotionText.url("https://example.com").getType());
    }

    @Test
    void setsHref() {
      assertEquals("https://example.com", NotionText.url("https://example.com").getHref());
    }

    @Test
    void setsLinkUrl() {
      assertEquals(
          "https://example.com",
          NotionText.url("https://example.com").getText().getLink().getUrl());
    }
  }

  @Nested
  class DateMention {

    @Test
    void setsTypeToMention() {
      assertEquals("mention", NotionText.dateMention("2024-01-01").getType());
    }

    @Test
    void setsMentionTypeToDate() {
      assertEquals("date", NotionText.dateMention("2024-01-01").getMention().getType());
    }

    @Test
    void setsDateStart() {
      assertEquals(
          "2024-01-01", NotionText.dateMention("2024-01-01").getMention().getDate().getStart());
    }

    @Test
    void withEndDate_setsEnd() {
      assertEquals(
          "2024-01-31",
          NotionText.dateMention("2024-01-01", "2024-01-31").getMention().getDate().getEnd());
    }

    @Test
    void withoutEndDate_endIsNull() {
      assertNull(NotionText.dateMention("2024-01-01").getMention().getDate().getEnd());
    }

    @Test
    void withTimeZone_setsTimeZone() {
      assertEquals(
          "UTC",
          NotionText.dateMention("2024-01-01", null, "UTC").getMention().getDate().getTimeZone());
    }
  }

  @Nested
  class UserMention {

    @Test
    void setsTypeToMention() {
      assertEquals("mention", NotionText.userMention("u-1").getType());
    }

    @Test
    void setsMentionTypeToUser() {
      assertEquals("user", NotionText.userMention("u-1").getMention().getType());
    }

    @Test
    void setsUserId() {
      assertEquals("u-1", NotionText.userMention("u-1").getMention().getUser().getId());
    }
  }

  @Nested
  class BlockMention {

    @Test
    void setsTypeToMention() {
      assertEquals("mention", NotionText.blockMention("block-1").getType());
    }

    @Test
    void setsPageId() {
      assertEquals("block-1", NotionText.blockMention("block-1").getMention().getPage().getId());
    }
  }

  @Nested
  class CustomEmoji {

    @Test
    void setsTypeToMention() {
      assertEquals("mention", NotionText.customEmoji("e-id").getType());
    }

    @Test
    void setsMentionTypeToCustomEmoji() {
      assertEquals("custom_emoji", NotionText.customEmoji("e-id").getMention().getType());
    }

    @Test
    void setsCustomEmojiId() {
      assertEquals("e-id", NotionText.customEmoji("e-id").getMention().getCustomEmoji().getId());
    }
  }

  @Nested
  class Bold {

    @Test
    void fromString_setsBold() {
      assertTrue(NotionText.bold("hi").getAnnotations().getBold());
    }

    @Test
    void fromRichText_setsBoldOnExistingInstance() {
      RichText rt = NotionText.plainText("text");
      NotionText.bold(rt);
      assertTrue(rt.getAnnotations().getBold());
    }

    @Test
    void fromRichText_returnsSameInstance() {
      RichText rt = NotionText.plainText("text");
      assertSame(rt, NotionText.bold(rt));
    }

    @Test
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.bold((RichText) null));
    }
  }

  @Nested
  class Italic {

    @Test
    void fromString_setsItalic() {
      assertTrue(NotionText.italic("hi").getAnnotations().getItalic());
    }

    @Test
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.italic((RichText) null));
    }
  }

  @Nested
  class Underline {

    @Test
    void fromString_setsUnderline() {
      assertTrue(NotionText.underline("hi").getAnnotations().getUnderline());
    }

    @Test
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.underline((RichText) null));
    }
  }

  @Nested
  class Strikethrough {

    @Test
    void fromString_setsStrikethrough() {
      assertTrue(NotionText.strikethrough("hi").getAnnotations().getStrikethrough());
    }

    @Test
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.strikethrough((RichText) null));
    }
  }

  @Nested
  class Code {

    @Test
    void fromString_setsCode() {
      assertTrue(NotionText.code("x").getAnnotations().getCode());
    }

    @Test
    void nullRichText_throwsNullPointerException() {
      assertThrows(NullPointerException.class, () -> NotionText.code((RichText) null));
    }
  }

  @Nested
  class Colors {

    @Test
    void blue_setsBlueColor() {
      assertEquals("blue", NotionText.blue("text").getAnnotations().getColor());
    }

    @Test
    void blueBackground_setsColor() {
      assertEquals(
          "blue_background", NotionText.blueBackground("text").getAnnotations().getColor());
    }

    @Test
    void red_setsRedColor() {
      assertEquals("red", NotionText.red("text").getAnnotations().getColor());
    }

    @Test
    void redBackground_setsColor() {
      assertEquals("red_background", NotionText.redBackground("text").getAnnotations().getColor());
    }

    @Test
    void green_setsGreenColor() {
      assertEquals("green", NotionText.green("text").getAnnotations().getColor());
    }

    @Test
    void greenBackground_setsColor() {
      assertEquals(
          "green_background", NotionText.greenBackground("text").getAnnotations().getColor());
    }

    @Test
    void gray_setsGrayColor() {
      assertEquals("gray", NotionText.gray("text").getAnnotations().getColor());
    }

    @Test
    void grayBackground_setsColor() {
      assertEquals(
          "gray_background", NotionText.grayBackground("text").getAnnotations().getColor());
    }

    @Test
    void pink_setsPinkColor() {
      assertEquals("pink", NotionText.pink("text").getAnnotations().getColor());
    }

    @Test
    void pinkBackground_setsColor() {
      assertEquals(
          "pink_background", NotionText.pinkBackground("text").getAnnotations().getColor());
    }

    @Test
    void purple_setsPurpleColor() {
      assertEquals("purple", NotionText.purple("text").getAnnotations().getColor());
    }

    @Test
    void purpleBackground_setsColor() {
      assertEquals(
          "purple_background", NotionText.purpleBackground("text").getAnnotations().getColor());
    }

    @Test
    void orange_setsOrangeColor() {
      assertEquals("orange", NotionText.orange("text").getAnnotations().getColor());
    }

    @Test
    void orangeBackground_setsColor() {
      assertEquals(
          "orange_background", NotionText.orangeBackground("text").getAnnotations().getColor());
    }

    @Test
    void yellow_setsYellowColor() {
      assertEquals("yellow", NotionText.yellow("text").getAnnotations().getColor());
    }

    @Test
    void yellowBackground_setsColor() {
      assertEquals(
          "yellow_background", NotionText.yellowBackground("text").getAnnotations().getColor());
    }

    @Test
    void brown_setsBrownColor() {
      assertEquals("brown", NotionText.brown("text").getAnnotations().getColor());
    }

    @Test
    void brownBackground_setsColor() {
      assertEquals(
          "brown_background", NotionText.brownBackground("text").getAnnotations().getColor());
    }

    @Test
    void colorOnExistingRichText_overwritesPreviousColor() {
      RichText rt = NotionText.blue("text");
      NotionText.red(rt);
      assertEquals("red", rt.getAnnotations().getColor());
    }
  }
}
