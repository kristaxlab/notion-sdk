package io.kristaxlab.notion.model.helper;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionTextBuilderTest {

  private NotionTextBuilder builder() {
    return NotionText.textBuilder();
  }

  @Nested
  class Build {

    @Test
    void emptyBuilder_returnsEmptyList() {
      assertTrue(builder().build().isEmpty());
    }

    @Test
    void build_returnsNewListInstanceEachCall() {
      NotionTextBuilder b = builder().plainText("x");
      assertNotSame(b.build(), b.build());
    }

    @Test
    void build_containsAllAddedItems() {
      assertEquals(2, builder().plainText("a").plainText("b").build().size());
    }

    @Test
    void mutatingReturnedList_doesNotAffectBuilder() {
      NotionTextBuilder b = builder().plainText("x");
      b.build().clear();
      assertEquals(1, b.build().size());
    }
  }

  @Nested
  class PlainText {

    @Test
    void addsOneRichText() {
      assertEquals(1, builder().plainText("x").build().size());
    }

    @Test
    void setsTypeToText() {
      assertEquals("text", builder().plainText("x").build().get(0).getType());
    }

    @Test
    void setsTextContent() {
      assertEquals("hello", builder().plainText("hello").build().get(0).getText().getContent());
    }
  }

  @Nested
  class Expression {

    @Test
    void setsTypeToEquation() {
      assertEquals("equation", builder().expression("x^2").build().get(0).getType());
    }

    @Test
    void setsEquationExpression() {
      assertEquals("x^2", builder().expression("x^2").build().get(0).getEquation().getExpression());
    }
  }

  @Nested
  class Url {

    @Test
    void setsHref() {
      assertEquals("https://x.com", builder().url("https://x.com").build().get(0).getHref());
    }

    @Test
    void setsLinkUrl() {
      assertEquals(
          "https://x.com",
          builder().url("https://x.com").build().get(0).getText().getLink().getUrl());
    }
  }

  @Nested
  class DateMention {

    @Test
    void setsDateStart() {
      assertEquals(
          "2024-01-01",
          builder().dateMention("2024-01-01").build().get(0).getMention().getDate().getStart());
    }

    @Test
    void withRange_setsEnd() {
      assertEquals(
          "2024-12-31",
          builder()
              .dateMention("2024-01-01", "2024-12-31")
              .build()
              .get(0)
              .getMention()
              .getDate()
              .getEnd());
    }

    @Test
    void withTimeZone_setsTimeZone() {
      assertEquals(
          "America/New_York",
          builder()
              .dateMention("2024-01-01", null, "America/New_York")
              .build()
              .get(0)
              .getMention()
              .getDate()
              .getTimeZone());
    }
  }

  @Nested
  class UserMention {

    @Test
    void setsUserId() {
      assertEquals(
          "u-1", builder().userMention("u-1").build().get(0).getMention().getUser().getId());
    }
  }

  @Nested
  class BlockMention {

    @Test
    void setsPageId() {
      assertEquals(
          "b-1", builder().blockMention("b-1").build().get(0).getMention().getPage().getId());
    }
  }

  @Nested
  class CustomEmoji {

    @Test
    void setsCustomEmojiId() {
      assertEquals(
          "e-1", builder().customEmoji("e-1").build().get(0).getMention().getCustomEmoji().getId());
    }
  }

  @Nested
  class Formatting {

    @Test
    void bold_setsBoldOnLastItem() {
      assertTrue(builder().plainText("x").bold().build().get(0).getAnnotations().getBold());
    }

    @Test
    void italic_setsItalicOnLastItem() {
      assertTrue(builder().plainText("x").italic().build().get(0).getAnnotations().getItalic());
    }

    @Test
    void underline_setsUnderlineOnLastItem() {
      assertTrue(
          builder().plainText("x").underline().build().get(0).getAnnotations().getUnderline());
    }

    @Test
    void strikethrough_setsStrikethroughOnLastItem() {
      assertTrue(
          builder()
              .plainText("x")
              .strikethrough()
              .build()
              .get(0)
              .getAnnotations()
              .getStrikethrough());
    }

    @Test
    void code_setsCodeOnLastItem() {
      assertTrue(builder().plainText("x").code().build().get(0).getAnnotations().getCode());
    }

    @Test
    void formattingWithNoItems_throwsIllegalState() {
      assertThrows(IllegalStateException.class, () -> builder().bold());
    }

    @Test
    void formatting_doesNotAffectPreviousItems() {
      List<RichText> items = builder().plainText("a").plainText("b").bold().build();
      assertNull(items.get(0).getAnnotations().getBold());
      assertTrue(items.get(1).getAnnotations().getBold());
    }
  }

  @Nested
  class ColorEnum {

    @Test
    void color_setsColorValue() {
      assertEquals(
          "blue",
          builder().plainText("x").color(Color.BLUE).build().get(0).getAnnotations().getColor());
    }

    @Test
    void nullColor_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> builder().plainText("x").color((Color) null));
    }
  }

  @Nested
  class ColorString {

    @Test
    void setsArbitraryColorValue() {
      assertEquals(
          "custom_color",
          builder()
              .plainText("x")
              .color("custom_color")
              .build()
              .get(0)
              .getAnnotations()
              .getColor());
    }
  }

  @Nested
  class NamedColors {

    @Test
    void blue_setsBlue() {
      assertEquals(
          "blue", builder().plainText("x").blue().build().get(0).getAnnotations().getColor());
    }

    @Test
    void blueBackground_setsColor() {
      assertEquals(
          "blue_background",
          builder().plainText("x").blueBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void red_setsRed() {
      assertEquals(
          "red", builder().plainText("x").red().build().get(0).getAnnotations().getColor());
    }

    @Test
    void redBackground_setsColor() {
      assertEquals(
          "red_background",
          builder().plainText("x").redBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void green_setsGreen() {
      assertEquals(
          "green", builder().plainText("x").green().build().get(0).getAnnotations().getColor());
    }

    @Test
    void greenBackground_setsColor() {
      assertEquals(
          "green_background",
          builder().plainText("x").greenBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void gray_setsGray() {
      assertEquals(
          "gray", builder().plainText("x").gray().build().get(0).getAnnotations().getColor());
    }

    @Test
    void grayBackground_setsColor() {
      assertEquals(
          "gray_background",
          builder().plainText("x").grayBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void pink_setsPink() {
      assertEquals(
          "pink", builder().plainText("x").pink().build().get(0).getAnnotations().getColor());
    }

    @Test
    void pinkBackground_setsColor() {
      assertEquals(
          "pink_background",
          builder().plainText("x").pinkBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void purple_setsPurple() {
      assertEquals(
          "purple", builder().plainText("x").purple().build().get(0).getAnnotations().getColor());
    }

    @Test
    void purpleBackground_setsColor() {
      assertEquals(
          "purple_background",
          builder().plainText("x").purpleBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void orange_setsOrange() {
      assertEquals(
          "orange", builder().plainText("x").orange().build().get(0).getAnnotations().getColor());
    }

    @Test
    void orangeBackground_setsColor() {
      assertEquals(
          "orange_background",
          builder().plainText("x").orangeBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void yellow_setsYellow() {
      assertEquals(
          "yellow", builder().plainText("x").yellow().build().get(0).getAnnotations().getColor());
    }

    @Test
    void yellowBackground_setsColor() {
      assertEquals(
          "yellow_background",
          builder().plainText("x").yellowBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    void brown_setsBrown() {
      assertEquals(
          "brown", builder().plainText("x").brown().build().get(0).getAnnotations().getColor());
    }

    @Test
    void brownBackground_setsColor() {
      assertEquals(
          "brown_background",
          builder().plainText("x").brownBackground().build().get(0).getAnnotations().getColor());
    }
  }
}
