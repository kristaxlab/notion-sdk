package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.Icon;
import org.junit.jupiter.api.Test;

class CalloutBlockTest {

  @Test
  void constructor_setsTypeAndInitializesCallout() {
    CalloutBlock block = new CalloutBlock();

    assertEquals("callout", block.getType());
    assertNotNull(block.getCallout());
  }

  @Test
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
  void builder_withTextAndEmoji() {
    CalloutBlock block = CalloutBlock.builder().text("Info").emoji("ℹ️").build();

    assertEquals("callout", block.getType());
    assertEquals("Info", block.getCallout().getRichText().get(0).getPlainText());
    assertEquals("ℹ️", block.getCallout().getIcon().getEmoji());
    assertEquals("emoji", block.getCallout().getIcon().getType());
  }

  @Test
  void builder_withIcon() {
    Icon externalIcon = Icon.external("https://example.com/icon.png");

    CalloutBlock block = CalloutBlock.builder().text("Custom icon").icon(externalIcon).build();

    assertSame(externalIcon, block.getCallout().getIcon());
  }

  @Test
  void builder_noIcon_noIconSet() {
    CalloutBlock block = CalloutBlock.builder().text("No icon").build();

    assertNull(block.getCallout().getIcon());
  }

  @Test
  void builder_withColor() {
    CalloutBlock block =
        CalloutBlock.builder().text("Colored").emoji("🔴").blockColor(Color.RED_BACKGROUND).build();

    assertEquals("red_background", block.getCallout().getColor());
  }

  @Test
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
  void callout_iconGetterSetter() {
    CalloutBlock.Callout callout = new CalloutBlock.Callout();

    assertNull(callout.getIcon());
    Icon icon = Icon.emoji("✅");
    callout.setIcon(icon);
    assertSame(icon, callout.getIcon());
  }
}
