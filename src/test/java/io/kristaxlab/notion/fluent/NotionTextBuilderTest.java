package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionTextBuilderTest {

  private NotionTextBuilder builder() {
    return NotionText.textBuilder();
  }

  @Nested
  class Build {

    @Test
    @DisplayName("empty builder returns empty list")
    void emptyBuilder_returnsEmptyList() {
      assertTrue(builder().build().isEmpty());
    }

    @Test
    @DisplayName("build returns new list instance each call")
    void build_returnsNewListInstanceEachCall() {
      NotionTextBuilder b = builder().plainText("x");
      assertNotSame(b.build(), b.build());
    }

    @Test
    @DisplayName("build contains all added items")
    void build_containsAllAddedItems() {
      assertEquals(2, builder().plainText("a").plainText("b").build().size());
    }

    @Test
    @DisplayName("mutating returned list does not affect builder")
    void mutatingReturnedList_doesNotAffectBuilder() {
      NotionTextBuilder b = builder().plainText("x");
      b.build().clear();
      assertEquals(1, b.build().size());
    }
  }

  @Nested
  class PlainText {

    @Test
    @DisplayName("adds one rich text")
    void addsOneRichText() {
      assertEquals(1, builder().plainText("x").build().size());
    }

    @Test
    @DisplayName("sets type to text")
    void setsTypeToText() {
      assertEquals("text", builder().plainText("x").build().get(0).getType());
    }

    @Test
    @DisplayName("sets text content")
    void setsTextContent() {
      assertEquals("hello", builder().plainText("hello").build().get(0).getText().getContent());
    }
  }

  @Nested
  class Expression {

    @Test
    @DisplayName("sets type to equation")
    void setsTypeToEquation() {
      assertEquals("equation", builder().expression("x^2").build().get(0).getType());
    }

    @Test
    @DisplayName("sets equation expression")
    void setsEquationExpression() {
      assertEquals("x^2", builder().expression("x^2").build().get(0).getEquation().getExpression());
    }
  }

  @Nested
  class Url {

    @Test
    @DisplayName("sets href")
    void setsHref() {
      assertEquals("https://x.com", builder().url("https://x.com").build().get(0).getHref());
    }

    @Test
    @DisplayName("sets link url")
    void setsLinkUrl() {
      assertEquals(
          "https://x.com",
          builder().url("https://x.com").build().get(0).getText().getLink().getUrl());
    }
  }

  @Nested
  class DateMention {

    @Test
    @DisplayName("sets date start")
    void setsDateStart() {
      assertEquals(
          "2024-01-01",
          builder().dateMention("2024-01-01").build().get(0).getMention().getDate().getStart());
    }

    @Test
    @DisplayName("with range sets end")
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
    @DisplayName("with time zone sets time zone")
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
    @DisplayName("sets user id")
    void setsUserId() {
      assertEquals(
          "u-1", builder().userMention("u-1").build().get(0).getMention().getUser().getId());
    }
  }

  @Nested
  class BlockMention {

    @Test
    @DisplayName("sets page id")
    void setsPageId() {
      assertEquals(
          "b-1", builder().blockMention("b-1").build().get(0).getMention().getPage().getId());
    }
  }

  @Nested
  class CustomEmoji {

    @Test
    @DisplayName("sets custom emoji id")
    void setsCustomEmojiId() {
      assertEquals(
          "e-1", builder().customEmoji("e-1").build().get(0).getMention().getCustomEmoji().getId());
    }
  }

  @Nested
  class Formatting {

    @Test
    @DisplayName("bold sets bold on last item")
    void bold_setsBoldOnLastItem() {
      assertTrue(builder().plainText("x").bold().build().get(0).getAnnotations().getBold());
    }

    @Test
    @DisplayName("italic sets italic on last item")
    void italic_setsItalicOnLastItem() {
      assertTrue(builder().plainText("x").italic().build().get(0).getAnnotations().getItalic());
    }

    @Test
    @DisplayName("underline sets underline on last item")
    void underline_setsUnderlineOnLastItem() {
      assertTrue(
          builder().plainText("x").underline().build().get(0).getAnnotations().getUnderline());
    }

    @Test
    @DisplayName("strikethrough sets strikethrough on last item")
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
    @DisplayName("code sets code on last item")
    void code_setsCodeOnLastItem() {
      assertTrue(builder().plainText("x").code().build().get(0).getAnnotations().getCode());
    }

    @Test
    @DisplayName("formatting with no items throws illegal state")
    void formattingWithNoItems_throwsIllegalState() {
      assertThrows(IllegalStateException.class, () -> builder().bold());
    }

    @Test
    @DisplayName("formatting does not affect previous items")
    void formatting_doesNotAffectPreviousItems() {
      List<RichText> items = builder().plainText("a").plainText("b").bold().build();
      assertNull(items.get(0).getAnnotations().getBold());
      assertTrue(items.get(1).getAnnotations().getBold());
    }
  }

  @Nested
  class ColorEnum {

    @Test
    @DisplayName("color sets color value")
    void color_setsColorValue() {
      assertEquals(
          "blue",
          builder().plainText("x").color(Color.BLUE).build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("null color throws illegal argument")
    void nullColor_throwsIllegalArgument() {
      assertThrows(
          IllegalArgumentException.class, () -> builder().plainText("x").color((Color) null));
    }
  }

  @Nested
  class ColorString {

    @Test
    @DisplayName("sets arbitrary color value")
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
    @DisplayName("blue sets blue")
    void blue_setsBlue() {
      assertEquals(
          "blue", builder().plainText("x").blue().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("blue background sets color")
    void blueBackground_setsColor() {
      assertEquals(
          "blue_background",
          builder().plainText("x").blueBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("red sets red")
    void red_setsRed() {
      assertEquals(
          "red", builder().plainText("x").red().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("red background sets color")
    void redBackground_setsColor() {
      assertEquals(
          "red_background",
          builder().plainText("x").redBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("green sets green")
    void green_setsGreen() {
      assertEquals(
          "green", builder().plainText("x").green().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("green background sets color")
    void greenBackground_setsColor() {
      assertEquals(
          "green_background",
          builder().plainText("x").greenBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("gray sets gray")
    void gray_setsGray() {
      assertEquals(
          "gray", builder().plainText("x").gray().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("gray background sets color")
    void grayBackground_setsColor() {
      assertEquals(
          "gray_background",
          builder().plainText("x").grayBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("pink sets pink")
    void pink_setsPink() {
      assertEquals(
          "pink", builder().plainText("x").pink().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("pink background sets color")
    void pinkBackground_setsColor() {
      assertEquals(
          "pink_background",
          builder().plainText("x").pinkBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("purple sets purple")
    void purple_setsPurple() {
      assertEquals(
          "purple", builder().plainText("x").purple().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("purple background sets color")
    void purpleBackground_setsColor() {
      assertEquals(
          "purple_background",
          builder().plainText("x").purpleBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("orange sets orange")
    void orange_setsOrange() {
      assertEquals(
          "orange", builder().plainText("x").orange().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("orange background sets color")
    void orangeBackground_setsColor() {
      assertEquals(
          "orange_background",
          builder().plainText("x").orangeBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("yellow sets yellow")
    void yellow_setsYellow() {
      assertEquals(
          "yellow", builder().plainText("x").yellow().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("yellow background sets color")
    void yellowBackground_setsColor() {
      assertEquals(
          "yellow_background",
          builder().plainText("x").yellowBackground().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("brown sets brown")
    void brown_setsBrown() {
      assertEquals(
          "brown", builder().plainText("x").brown().build().get(0).getAnnotations().getColor());
    }

    @Test
    @DisplayName("brown background sets color")
    void brownBackground_setsColor() {
      assertEquals(
          "brown_background",
          builder().plainText("x").brownBackground().build().get(0).getAnnotations().getColor());
    }
  }
}
