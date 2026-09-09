package io.kristaxlab.notion.model.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CommentCreateParamsTest {

  @Nested
  class BuilderTests {

    @Test
    @DisplayName("build empty all fields are null")
    void buildEmpty_allFieldsAreNull() {
      CommentCreateParams params = CommentCreateParams.builder().build();

      assertNull(params.getParent());
      assertNull(params.getDiscussionId());
      assertNull(params.getRichText());
      assertNull(params.getMarkdown());
      assertNull(params.getAttachments());
      assertNull(params.getDisplayName());
    }

    @Test
    @DisplayName("in page sets page parent")
    void inPage_setsPageParent() {
      CommentCreateParams params = CommentCreateParams.builder().inPage("page-1").build();

      assertEquals("page_id", params.getParent().getType());
      assertEquals("page-1", params.getParent().getPageId());
    }

    @Test
    @DisplayName("in block sets block parent")
    void inBlock_setsBlockParent() {
      CommentCreateParams params = CommentCreateParams.builder().inBlock("block-1").build();

      assertEquals("block_id", params.getParent().getType());
      assertEquals("block-1", params.getParent().getBlockId());
    }

    @Test
    @DisplayName("parent sets supplied parent")
    void parent_setsSuppliedParent() {
      Parent parent = Parent.pageParent("page-2");

      CommentCreateParams params = CommentCreateParams.builder().parent(parent).build();

      assertSame(parent, params.getParent());
    }

    @Test
    @DisplayName("discussion id sets discussion id")
    void discussionId_setsDiscussionId() {
      CommentCreateParams params =
          CommentCreateParams.builder().discussionId("discussion-1").build();

      assertEquals("discussion-1", params.getDiscussionId());
    }

    @Test
    @DisplayName("rich text string sets one plain text run")
    void richTextString_setsOnePlainTextRun() {
      CommentCreateParams params = CommentCreateParams.builder().richText("Hello").build();

      assertEquals(1, params.getRichText().size());
      assertEquals("Hello", params.getRichText().get(0).getPlainText());
      assertEquals("Hello", params.getRichText().get(0).getText().getContent());
    }

    @Test
    @DisplayName("rich text varargs sets runs")
    void richTextVarargs_setsRuns() {
      RichText first = NotionText.plainText("A");
      RichText second = NotionText.plainText("B");

      CommentCreateParams params = CommentCreateParams.builder().richText(first, second).build();

      assertEquals(List.of(first, second), params.getRichText());
    }

    @Test
    @DisplayName("markdown sets markdown")
    void markdown_setsMarkdown() {
      CommentCreateParams params = CommentCreateParams.builder().markdown("**Hi**").build();

      assertEquals("**Hi**", params.getMarkdown());
    }

    @Test
    @DisplayName("attachment adds one attachment")
    void attachment_addsOneAttachment() {
      CommentAttachment attachment = CommentAttachment.fileUpload("upload-1");

      CommentCreateParams params = CommentCreateParams.builder().attachment(attachment).build();

      assertEquals(1, params.getAttachments().size());
      assertSame(attachment, params.getAttachments().get(0));
    }

    @Test
    @DisplayName("attachments replaces the list")
    void attachments_replacesList() {
      CommentAttachment first = CommentAttachment.fileUpload("upload-1");
      CommentAttachment second = CommentAttachment.fileUpload("upload-2");

      CommentCreateParams params =
          CommentCreateParams.builder().attachments(List.of(first, second)).build();

      assertEquals(List.of(first, second), params.getAttachments());
    }

    @Test
    @DisplayName("display name sets display name")
    void displayName_setsDisplayName() {
      CommentDisplayName displayName = CommentDisplayName.custom("Bot");

      CommentCreateParams params = CommentCreateParams.builder().displayName(displayName).build();

      assertSame(displayName, params.getDisplayName());
    }

    @Test
    @DisplayName("builder method chaining returns same builder")
    void builderMethodChaining_returnsSameBuilder() {
      CommentCreateParams.Builder builder = CommentCreateParams.builder();

      assertSame(builder, builder.inPage("page-1"));
      assertSame(builder, builder.discussionId("discussion-1"));
      assertSame(builder, builder.richText("Hello"));
      assertSame(builder, builder.markdown("Hi"));
      assertSame(builder, builder.displayName(CommentDisplayName.user()));
    }

    @Test
    @DisplayName("build returns new instance each call")
    void buildReturnsNewInstanceEachCall() {
      CommentCreateParams.Builder builder = CommentCreateParams.builder().inPage("page-1");

      CommentCreateParams first = builder.build();
      CommentCreateParams second = builder.build();

      assertNotSame(first, second);
      assertEquals("page-1", first.getParent().getPageId());
      assertEquals("page-1", second.getParent().getPageId());
    }

    @Test
    @DisplayName("mutating params after build does not affect subsequent build")
    void mutatingParamsAfterBuild_doesNotAffectSubsequentBuild() {
      CommentCreateParams.Builder builder =
          CommentCreateParams.builder().inPage("page-1").richText("Hello");

      CommentCreateParams first = builder.build();
      first.setMarkdown("changed");

      CommentCreateParams second = builder.build();
      assertEquals("changed", first.getMarkdown());
      assertNull(second.getMarkdown());
    }
  }
}
