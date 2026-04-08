package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.Test;

class CodeBlockTest {

  @Test
  void constructor_setsTypeAndInitializesCode() {
    CodeBlock block = new CodeBlock();

    assertEquals("code", block.getType());
    assertNotNull(block.getCode());
  }

  @Test
  void of_setsContentAndLanguage() {
    CodeBlock block = CodeBlock.of("System.out.println()", "java");

    assertEquals("code", block.getType());
    assertEquals(1, block.getCode().getRichText().size());
    assertEquals("System.out.println()", block.getCode().getRichText().get(0).getPlainText());
    assertEquals("java", block.getCode().getLanguage());
  }

  @Test
  void builder_withTextAndLanguage() {
    CodeBlock block = CodeBlock.builder().text("print('hello')").language("python").build();

    assertEquals("print('hello')", block.getCode().getRichText().get(0).getPlainText());
    assertEquals("python", block.getCode().getLanguage());
  }

  @Test
  void builder_withRichTextList() {
    List<RichText> richTexts = RichText.of("console.log('hi')");

    CodeBlock block = CodeBlock.builder().richText(richTexts).language("javascript").build();

    assertSame(richTexts, block.getCode().getRichText());
  }

  @Test
  void builder_withRichTextConsumer() {
    CodeBlock block =
        CodeBlock.builder().richText(rt -> rt.text("code").bold()).language("java").build();

    assertEquals(1, block.getCode().getRichText().size());
    assertTrue(block.getCode().getRichText().get(0).getAnnotations().getBold());
  }

  @Test
  void builder_withCaptionString() {
    CodeBlock block =
        CodeBlock.builder()
            .text("SELECT * FROM users")
            .language("sql")
            .caption(RichText.of("User query"))
            .build();

    assertNotNull(block.getCode().getCaption());
    assertEquals(1, block.getCode().getCaption().size());
    assertEquals("User query", block.getCode().getCaption().get(0).getPlainText());
  }

  @Test
  void builder_withCaptionConsumer() {
    CodeBlock block =
        CodeBlock.builder()
            .text("def foo(): pass")
            .language("python")
            .caption(c -> c.text("Python function").italic())
            .build();

    assertNotNull(block.getCode().getCaption());
    assertEquals(1, block.getCode().getCaption().size());
    assertTrue(block.getCode().getCaption().get(0).getAnnotations().getItalic());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    CodeBlock block = CodeBlock.builder().text("code").language("java").build();

    assertNull(block.getCode().getCaption());
  }

  @Test
  void code_getterSetter() {
    CodeBlock.Code code = new CodeBlock.Code();

    assertNull(code.getRichText());
    assertNull(code.getLanguage());
    assertNull(code.getCaption());

    code.setLanguage("rust");
    code.setRichText(RichText.of("fn main()"));
    code.setCaption(RichText.of("Entry point"));

    assertEquals("rust", code.getLanguage());
    assertEquals(1, code.getRichText().size());
    assertEquals(1, code.getCaption().size());
  }
}
