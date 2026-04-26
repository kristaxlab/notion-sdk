package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Color;
import io.kristaxlab.notion.model.common.Icon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalloutBlockTest {

  @Test
  @DisplayName("constructor sets type and initializes callout")
  void constructor_setsTypeAndInitializesCallout() {
    CalloutBlock block = new CalloutBlock();

    assertEquals("callout", block.getType());
    assertNotNull(block.getCallout());
  }

  @Test
  @DisplayName("of sets emoji and text")
  void of_setsEmojiAndText() {
    CalloutBlock block = CalloutBlock.builder().text("Warning message").emoji("⚠️").build();

    assertEquals("callout", block.getType());
    assertEquals(1, block.getCallout().getRichText().size());
    assertEquals("Warning message", block.getCallout().getRichText().get(0).getPlainText());
    assertNotNull(block.getCallout().getIcon());
    assertEquals("emoji", block.getCallout().getIcon().getType());
    assertEquals("⚠️", block.getCallout().getIcon().getEmoji());
  }

  @Test
  @DisplayName("builder with text and emoji")
  void builder_withTextAndEmoji() {
    CalloutBlock block = CalloutBlock.builder().text("Info").emoji("ℹ️").build();

    assertEquals("callout", block.getType());
    assertEquals("Info", block.getCallout().getRichText().get(0).getPlainText());
    assertEquals("ℹ️", block.getCallout().getIcon().getEmoji());
    assertEquals("emoji", block.getCallout().getIcon().getType());
  }

  @Test
  @DisplayName("builder with icon")
  void builder_withIcon() {
    Icon externalIcon = Icon.external("https://example.com/icon.png");

    CalloutBlock block = CalloutBlock.builder().text("Custom icon").icon(externalIcon).build();

    assertSame(externalIcon, block.getCallout().getIcon());
  }

  @Test
  @DisplayName("builder no icon no icon set")
  void builder_noIcon_noIconSet() {
    CalloutBlock block = CalloutBlock.builder().text("No icon").build();

    assertNull(block.getCallout().getIcon());
  }

  @Test
  @DisplayName("builder with color")
  void builder_withColor() {
    CalloutBlock block =
        CalloutBlock.builder().text("Colored").emoji("🔴").blockColor(Color.RED_BACKGROUND).build();

    assertEquals("red_background", block.getCallout().getColor());
  }

  @Test
  @DisplayName("builder with children")
  void builder_withChildren() {
    CalloutBlock block =
        CalloutBlock.builder()
            .text("Callout")
            .emoji("📌")
            .children(c -> c.paragraph("Detail"))
            .build();

    assertNotNull(block.getCallout().getChildren());
    assertEquals(1, block.getCallout().getChildren().size());
  }

  @Test
  @DisplayName("callout icon getter setter")
  void callout_iconGetterSetter() {
    CalloutBlock.Callout callout = new CalloutBlock.Callout();

    assertNull(callout.getIcon());
    Icon icon = Icon.emoji("✅");
    callout.setIcon(icon);
    assertSame(icon, callout.getIcon());
  }
}
