package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CodeBlockTest {

  @Test
  void constructor_setsTypeAndInitializesCode() {
    CodeBlock block = new CodeBlock();

    assertEquals("code", block.getType());
    assertNotNull(block.getCode());
  }

  @Test
  void builder_withTextAndLanguage() {
    CodeBlock block = CodeBlock.builder().code("print('hello')").language("python").build();

    assertEquals("print('hello')", block.getCode().getRichText().get(0).getPlainText());
    assertEquals("python", block.getCode().getLanguage());
  }

  @Test
  void builder_withCaptionConsumer() {
    CodeBlock block =
        CodeBlock.builder()
            .code("def foo(): pass")
            .language("python")
            .caption(c -> c.plainText("Python function").italic().plainText(" example"))
            .build();

    assertNotNull(block.getCode().getCaption());
    assertEquals(2, block.getCode().getCaption().size());
    assertTrue(block.getCode().getCaption().get(0).getAnnotations().getItalic());
  }

  @Test
  void builder_noCaptionSet_captionIsNull() {
    CodeBlock block = CodeBlock.builder().code("code").language("java").build();

    assertNull(block.getCode().getCaption());
  }
}
