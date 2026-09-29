package io.kristaxlab.notion.model.page.markdown;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UpdatePageAsMarkdownParamsTest {

  @Test
  @DisplayName("builder replaceContent produces replace_content params")
  void builderReplaceContent() {
    UpdatePageAsMarkdownParams params =
        UpdatePageAsMarkdownParams.builder().replaceContent("# Release notes").build();

    assertEquals("replace_content", params.getType());
    assertNull(params.getUpdateContent());
    assertNotNull(params.getReplaceContent());
    assertEquals("# Release notes", params.getReplaceContent().getNewStr());
    assertEquals(false, params.getReplaceContent().getAllowDeletingContent());
  }

  @Test
  @DisplayName("builder propagates allowDeletingContent to replace_content")
  void builderReplaceContentAllowsDeleting() {
    UpdatePageAsMarkdownParams params =
        UpdatePageAsMarkdownParams.builder()
            .replaceContent("# Starting over")
            .allowDeletingContent(true)
            .build();

    assertEquals(true, params.getReplaceContent().getAllowDeletingContent());
  }

  @Test
  @DisplayName("builder updateContent batches every search-replace operation")
  void builderUpdateContentBatches() {
    UpdatePageAsMarkdownParams params =
        UpdatePageAsMarkdownParams.builder()
            .updateContent("IN PROGRESS", "SHIPPED", true)
            .updateContent("v1", "v2", false)
            .allowDeletingContent(true)
            .build();

    assertEquals("update_content", params.getType());
    assertNull(params.getReplaceContent());

    UpdateContent updateContent = params.getUpdateContent();
    assertEquals(true, updateContent.getAllowDeletingContent());
    assertEquals(2, updateContent.getContentUpdates().size());

    ContentUpdate first = updateContent.getContentUpdates().get(0);
    assertEquals("IN PROGRESS", first.getOldStr());
    assertEquals("SHIPPED", first.getNewStr());
    assertEquals(true, first.getReplaceAllMatches());
  }

  @Test
  @DisplayName("builder rejects mixing replaceContent and updateContent")
  void builderRejectsMixedModes() {
    assertThrows(
        IllegalStateException.class,
        () ->
            UpdatePageAsMarkdownParams.builder()
                .replaceContent("# Release notes")
                .updateContent("a", "b", false));

    assertThrows(
        IllegalStateException.class,
        () ->
            UpdatePageAsMarkdownParams.builder()
                .updateContent("a", "b", false)
                .replaceContent("# Release notes"));
  }

  @Test
  @DisplayName("builder rejects a second replaceContent call")
  void builderRejectsRepeatedReplaceContent() {
    assertThrows(
        IllegalStateException.class,
        () ->
            UpdatePageAsMarkdownParams.builder().replaceContent("first").replaceContent("second"));
  }

  @Test
  @DisplayName("builder requires an operation before build")
  void builderRequiresOperation() {
    assertThrows(IllegalStateException.class, () -> UpdatePageAsMarkdownParams.builder().build());
  }

  @Test
  @DisplayName("static factories mirror the builder output")
  void staticFactories() {
    UpdatePageAsMarkdownParams replace =
        UpdatePageAsMarkdownParams.replaceContent("# Release notes");
    assertEquals("replace_content", replace.getType());
    assertEquals("# Release notes", replace.getReplaceContent().getNewStr());
    assertEquals(false, replace.getReplaceContent().getAllowDeletingContent());

    ContentUpdate update = new ContentUpdate();
    update.setOldStr("a");
    update.setNewStr("b");
    update.setReplaceAllMatches(false);

    UpdatePageAsMarkdownParams patch =
        UpdatePageAsMarkdownParams.updateContent(List.of(update), true);
    assertEquals("update_content", patch.getType());
    assertEquals(1, patch.getUpdateContent().getContentUpdates().size());
    assertEquals(true, patch.getUpdateContent().getAllowDeletingContent());
  }
}
