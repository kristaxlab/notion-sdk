package io.kristaxlab.notion.model.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentUpdateParamsTest {

  @Test
  @DisplayName("build empty all fields are null")
  void buildEmpty_allFieldsAreNull() {
    CommentUpdateParams params = CommentUpdateParams.builder().build();

    assertNull(params.getRichText());
    assertNull(params.getMarkdown());
  }

  @Test
  @DisplayName("rich text string sets one plain text run")
  void richTextString_setsOnePlainTextRun() {
    CommentUpdateParams params = CommentUpdateParams.builder().richText("Updated").build();

    assertEquals(1, params.getRichText().size());
    assertEquals("Updated", params.getRichText().get(0).getPlainText());
  }

  @Test
  @DisplayName("rich text varargs sets runs")
  void richTextVarargs_setsRuns() {
    RichText first = NotionText.plainText("A");
    RichText second = NotionText.plainText("B");

    CommentUpdateParams params = CommentUpdateParams.builder().richText(first, second).build();

    assertEquals(List.of(first, second), params.getRichText());
  }

  @Test
  @DisplayName("markdown sets markdown")
  void markdown_setsMarkdown() {
    CommentUpdateParams params = CommentUpdateParams.builder().markdown("**Updated**").build();

    assertEquals("**Updated**", params.getMarkdown());
  }

  @Test
  @DisplayName("builder method chaining returns same builder")
  void builderMethodChaining_returnsSameBuilder() {
    CommentUpdateParams.Builder builder = CommentUpdateParams.builder();

    assertSame(builder, builder.richText("Updated"));
    assertSame(builder, builder.markdown("Updated"));
  }

  @Test
  @DisplayName("build returns new instance each call")
  void buildReturnsNewInstanceEachCall() {
    CommentUpdateParams.Builder builder = CommentUpdateParams.builder().markdown("Updated");

    CommentUpdateParams first = builder.build();
    CommentUpdateParams second = builder.build();

    assertNotSame(first, second);
    assertEquals("Updated", first.getMarkdown());
    assertEquals("Updated", second.getMarkdown());
  }
}
