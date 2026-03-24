package integration;

import static org.junit.jupiter.api.Assertions.*;

import integration.util.IntegrationTestAssisstant;
import io.kristixlab.notion.api.http.error.ValidationException;
// canonical sync marker v2
import io.kristixlab.notion.api.model.blocks.*;
import io.kristixlab.notion.api.model.comments.Comment;
import io.kristixlab.notion.api.model.comments.CommentAttachment;
import io.kristixlab.notion.api.model.comments.CommentDisplayName;
import io.kristixlab.notion.api.model.comments.CreateCommentParams;
import io.kristixlab.notion.api.model.comments.CustomDisplayName;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.common.CoverParams;
import io.kristixlab.notion.api.model.pages.CreatePageParams;
import io.kristixlab.notion.api.model.pages.Page;
import io.kristixlab.notion.api.model.pages.PageAsMarkdown;
import io.kristixlab.notion.api.model.pages.UpdatePageAsMarkdownParams;
import io.kristixlab.notion.api.model.pages.UpdatePageAsMarkdownParams.ContentUpdate;
import io.kristixlab.notion.api.model.pages.UpdatePageParams;
import io.kristixlab.notion.api.model.pages.properties.TitleProperty;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import io.kristixlab.notion.api.util.PagePropertyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.*;

public class PagesIT extends BaseIntegrationTest {

  private static String pageTestsPageId;
  private String currTestPageId;

  @BeforeAll
  public static void setup() {
    pageTestsPageId = IntegrationTestAssisstant.createPageForTests("Pages");
  }

  @BeforeEach
  public void beforeEachTest(TestInfo info) {
    super.beforeEach(info);
    currTestPageId =
        IntegrationTestAssisstant.createPageForTests(info.getDisplayName(), pageTestsPageId);
  }

  @Test
  @DisplayName("[IT-1]: Pages - Create empty page nested into another page and retrieve it")
  public void createPageNestedIntoPage() {
    String title = "Empty nested page";
    CreatePageParams newPage = createPageParams(title, currTestPageId);
    Page page = getNotion().pages().create(newPage);

    assertNotNull(page);
    assertNotNull(page.getId());
    assertNotNull(page.getUrl());
    assertNotNull(page.getCreatedTime());
    assertNotNull(page.getCreatedBy());
    assertNotNull(page.getLastEditedTime());
    assertNotNull(page.getLastEditedBy());

    assertNotNull(page.getProperties());
    assertEquals(1, page.getProperties().size());
    assertNotNull(page.getProperties().get("title"));
    assertEquals(
        title,
        PagePropertyUtil.asTitle(page.getProperties().get("title"))
            .getTitle()
            .get(0)
            .getText()
            .getContent());
  }

  @Test
  @DisplayName("[IT-13]: Pages - Create page with icon, cover and content")
  public void createPageWithIconCoverContent() {
    CreatePageParams newPage =
        createPageParams("Page with icon and cover and content", currTestPageId);

    String cover = IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl();
    newPage.setCover(CoverParams.fromExternalUrl(cover));

    String icon = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();
    newPage.setIcon(IconParams.fromEmoji(icon));
    newPage.setChildren(new ArrayList<>());
    newPage
        .getChildren()
        .addAll(
            List.of(
                ToggleBlock.of("Toggle block inside the page"),
                ParagraphBlock.of("Child page block inside the page")));

    Page page = getNotion().pages().create(newPage);

    assertNotNull(page.getIcon());
    assertNotNull(page.getCover());
  }

  @Test
  @DisplayName("[INT-35]: Pages - Create page and duplicate it using its id as template_id")
  public void duplicatePageViaTemplateId() {
    // Step 1: Create the source page with content
    CreatePageParams sourceParams = createPageParams("Page to be duplicated", currTestPageId);
    sourceParams.setChildren(List.of(ParagraphBlock.of("source content")));
    Page sourcePage = getNotion().pages().create(sourceParams);

    // Step 2: Duplicate the source page using its id as template_id
    CreatePageParams duplicateParams = new CreatePageParams();
    duplicateParams.setParent(Parent.pageParent(currTestPageId));
    duplicateParams.setTemplate(new TemplateParams());
    duplicateParams.getTemplate().setType("template_id");
    duplicateParams.getTemplate().setTemplateId(sourcePage.getId());

    Page duplicatePage = getNotion().pages().create(duplicateParams);

    assertNotNull(duplicatePage);
    assertNotNull(duplicatePage.getId());
    assertNotEquals(sourcePage.getId(), duplicatePage.getId());
  }

  /** IT-12: Create page with empty parent */
  @Test
  @DisplayName("[IT-12]: Pages - Create page with empty parent")
  public void createPageWithoutParent() {
    String title = "IT-12 page " + System.currentTimeMillis();
    assertThrows(
        ValidationException.class, () -> getNotion().pages().create(createPageParams(title)));
  }

  /** IT-4: Update page content */
  @Test
  @DisplayName("[IT-4]: Pages - Update page title, icon and cover")
  public void updatePageTitleIconCover() {
    // Step 1: Create a page to update
    CreatePageParams createParams = createPageParams("A page to be updated", currTestPageId);
    createParams.setChildren(
        List.of(
            ParagraphBlock.of("Original content"), ParagraphBlock.of("Soon it will be updated")));
    Page created = getNotion().pages().create(createParams);
    assertNotNull(created);

    // Step 2: Update title, icon, and cover
    String updatedTitle = "Updated title";
    String emojiIcon = IntegrationTestAssisstant.getPrerequisites().getEmojiIcon();
    String coverUrl = IntegrationTestAssisstant.getPrerequisites().getExternalImageUrl();

    UpdatePageParams updateParams = new UpdatePageParams();
    updateParams.setProperties(java.util.Map.of("title", TitleProperty.of(updatedTitle)));
    updateParams.setIcon(IconParams.fromEmoji(emojiIcon));
    updateParams.setCover(CoverParams.fromExternalUrl(coverUrl));
    Page updated = getNotion().pages().update(created.getId(), updateParams);

    assertNotNull(updated);
    assertEquals(created.getId(), updated.getId());

    // Verify updated title
    assertEquals(
        updatedTitle,
        PagePropertyUtil.asTitle(updated.getProperties().get("title"))
            .getTitle()
            .get(0)
            .getText()
            .getContent());

    // Verify updated icon
    assertNotNull(updated.getIcon());
    assertEquals("emoji", updated.getIcon().getType());

    // Verify updated cover
    assertNotNull(updated.getCover());
    assertEquals("external", updated.getCover().getType());
  }

  @Test
  @DisplayName("[IT-34]: Pages - Add comments to a page")
  public void addCommentsToPage() {
    // Step 1: Create a new page
    Page page =
        getNotion().pages().create(createPageParams("A page with comments", currTestPageId));
    String pageId = page.getId();

    // Step 2: Add a plain-text comment to the page
    CreateCommentParams firstCommentRq = new CreateCommentParams();
    firstCommentRq.setParent(Parent.pageParent(pageId));
    firstCommentRq.setRichText(
        RichText.builder().fromText("First comment on the page").buildAsList());
    Comment firstComment = getNotion().comments().create(firstCommentRq);

    assertNotNull(firstComment);
    assertNotNull(firstComment.getId());
    assertNotNull(firstComment.getDiscussionId());
    assertEquals("First comment on the page", firstComment.getRichText().get(0).getPlainText());

    // Step 3: Add a second comment with an image attachment
    CommentAttachment imageAttachment = new CommentAttachment();
    imageAttachment.setFileUploadId(
        IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId());

    CreateCommentParams secondComment = new CreateCommentParams();
    secondComment.setParent(Parent.pageParent(pageId));
    secondComment.setRichText(RichText.builder().fromText("Comment with image").buildAsList());
    secondComment.setAttachments(List.of(imageAttachment));
    Comment attachmentComment = getNotion().comments().create(secondComment);

    assertNotNull(attachmentComment);
    assertNotNull(attachmentComment.getId());
    assertNotNull(attachmentComment.getDiscussionId());

    // Step 4: Reply to the attachment comment with a custom display name
    CustomDisplayName customName = new CustomDisplayName();
    customName.setName("Hejka");
    CommentDisplayName displayName = new CommentDisplayName();
    displayName.setType("custom");
    displayName.setCustom(customName);

    CreateCommentParams replyRq = new CreateCommentParams();
    replyRq.setDiscussionId(attachmentComment.getDiscussionId());
    replyRq.setRichText(
        RichText.builder().fromText("Reply to the attachment comment").buildAsList());
    replyRq.setDisplayName(displayName);
    Comment reply = getNotion().comments().create(replyRq);

    assertNotNull(reply);
    assertNotNull(reply.getId());
    assertEquals(attachmentComment.getDiscussionId(), reply.getDiscussionId());
  }

  @Test
  @DisplayName("[IT-36]: Pages - Move a page to another page")
  public void movePageToAnotherPage() {
    // Step 1: Create the destination page
    Page destination =
        getNotion().pages().create(createPageParams("Destination page", currTestPageId));

    // Step 2: Create the page to move
    Page pageToMove = getNotion().pages().create(createPageParams("Page to move", currTestPageId));

    // Step 3: Move pageToMove under destination
    Page moved =
        getNotion().pages().move(pageToMove.getId(), Parent.pageParent(destination.getId()));

    assertNotNull(moved);
    assertEquals(pageToMove.getId(), moved.getId());
    assertEquals(destination.getId(), moved.getParent().getPageId());
  }

  @Test
  @DisplayName("[IT-37]: Pages - Apply template to an existing page")
  public void applyTemplateToExistingPage() throws InterruptedException {
    // Step 1: Create the template source page with content
    CreatePageParams sourceParams = createPageParams("Template source page", currTestPageId);
    sourceParams.setChildren(
        List.of(
            ParagraphBlock.of("Template content line 1"),
            ParagraphBlock.of("Template content line 2")));
    Page sourcePage = getNotion().pages().create(sourceParams);

    // Step 2: Create the target page with its own content
    CreatePageParams targetParams = createPageParams("Page to receive template", currTestPageId);
    targetParams.setChildren(List.of(ParagraphBlock.of("Original content to be erased")));
    Page targetPage = getNotion().pages().create(targetParams);

    // Step 3: Update the target page – erase its content and apply the source page as template
    UpdatePageParams updateParams = new UpdatePageParams();
    updateParams.setEraseContent(true);
    updateParams.setTemplate(new TemplateParams());
    updateParams.getTemplate().setType("template_id");
    updateParams.getTemplate().setTemplateId(sourcePage.getId());

    Page updated = getNotion().pages().update(targetPage.getId(), updateParams);

    assertNotNull(updated);
    assertEquals(targetPage.getId(), updated.getId());

    BlockList content = getNotion().blocks().retrieveChildren(updated.getId());
    assertNotNull(content);
  }

  @Test
  @DisplayName("[IT-38]: Pages - Delete and restore a page")
  public void deleteAndRestorePage() {
    // Step 1: Create a page
    Page page = getNotion().pages().create(createPageParams("Page to delete", currTestPageId));
    assertNotNull(page);
    String pageId = page.getId();

    // Step 2: Delete the page
    Page deleted = getNotion().pages().delete(pageId);
    assertNotNull(deleted);
    assertTrue(deleted.getInTrash());

    // Step 3: Restore the page
    Page restored = getNotion().pages().restore(pageId);
    assertNotNull(restored);
    assertFalse(restored.getInTrash());
  }

  @Test
  @DisplayName("[IT-40]: Pages - Lock and unlock a page")
  public void lockAndUnlockPage() {
    // Step 1: Create a page
    Page page = getNotion().pages().create(createPageParams("Page to lock", currTestPageId));
    String pageId = page.getId();

    // Step 2: Lock the page
    UpdatePageParams lockParams = new UpdatePageParams();
    lockParams.setIsLocked(true);
    Page locked = getNotion().pages().update(pageId, lockParams);
    assertTrue(locked.getIsLocked());

    // Step 3: Unlock the page
    UpdatePageParams unlockParams = new UpdatePageParams();
    unlockParams.setIsLocked(false);
    Page unlocked = getNotion().pages().update(pageId, unlockParams);
    assertFalse(unlocked.getIsLocked());
  }

  @Test
  @DisplayName("[IT-39]: Pages - Create page with content and then add more content to it")
  public void createPageWithContentAndAppendMore() {
    // Step 1: Create a page with some initial blocks
    CreatePageParams createParams = createPageParams("Page with content", currTestPageId);
    createParams.setChildren(
        List.of(
            ParagraphBlock.of("Initial paragraph 1"), ParagraphBlock.of("Initial paragraph 2")));
    Page page = getNotion().pages().create(createParams);
    assertNotNull(page);

    // Step 2: Append new blocks via the blocks endpoint
    AppendBlockChildrenParams appendParams =
        AppendBlockChildrenParams.of(
            List.of(
                ParagraphBlock.of("Appended paragraph 3"),
                BulletedListItemBlock.of("Appended bullet item")));
    BlockList appendResult = getNotion().blocks().appendChildren(page.getId(), appendParams);

    assertNotNull(appendResult);
    assertEquals(2, appendResult.getResults().size());

    // Verify total content
    BlockList allBlocks = getNotion().blocks().retrieveChildren(page.getId());
    assertNotNull(allBlocks);
    assertEquals(4, allBlocks.getResults().size());
  }

  @Test
  @DisplayName(
      "[IT-28]: Pages & File Uploads  - Use an uploaded file as an icon and as a cover for a new page")
  public void useUploadedFileAsIconAndCover() {
    String fileUploadId = IntegrationTestAssisstant.getPrerequisites().getImageFileUploadId();

    FileUploadRef fileUpload = new FileUploadRef();
    fileUpload.setId(fileUploadId);

    CreatePageParams params = createPageParams("Page with uploaded icon and cover", currTestPageId);
    params.setIcon(IconParams.fromFileUpload(fileUploadId));
    params.setCover(CoverParams.fromFileUpload(fileUpload));

    Page page = getNotion().pages().create(params);

    assertNotNull(page);
    assertNotNull(page.getIcon());
    assertEquals("file", page.getIcon().getType());
    assertNotNull(page.getCover());
  }

  @Test
  @DisplayName("[IT-41]: Pages - Create a page with markdown in request")
  public void createPageWithMarkdown() {
    // Markdown containing a heading, a paragraph, and a bullet list
    String markdown = "\n# Heading\n\nThis is a paragraph.\n\n- bullet item 1\n- bullet item 2";

    CreatePageParams params = createPageParams("Page from markdown", currTestPageId);
    params.setMarkdown(markdown);

    Page page = getNotion().pages().create(params);

    assertNotNull(page);
    assertNotNull(page.getId());

    // Verify Notion converted the markdown into blocks
    BlockList blocks = getNotion().blocks().retrieveChildren(page.getId());
    assertNotNull(blocks);
    assertFalse(blocks.getResults().isEmpty());

    // First block must be a heading rendered from "# IT-41 Heading"
    assertEquals("heading_1", blocks.getResults().get(0).getType());
    assertEquals("paragraph", blocks.getResults().get(1).getType());
    assertEquals("bulleted_list_item", blocks.getResults().get(2).getType());
    assertEquals("bulleted_list_item", blocks.getResults().get(3).getType());
  }

  @Test
  @DisplayName("[IT-43]: Pages - Retrieve page content as markdown")
  public void retrievePageContentAsMarkdown() {
    // Step 1: Create a page with some content
    CreatePageParams params = createPageParams("Page to retrieve as markdown", currTestPageId);
    params.setChildren(
        List.of(ParagraphBlock.of("First paragraph"), ParagraphBlock.of("Second paragraph")));
    Page page = getNotion().pages().create(params);

    // Step 2: Retrieve the page content as markdown
    PageAsMarkdown result = getNotion().pages().retrieveAsMarkdown(page.getId());

    assertNotNull(result);
    assertNotNull(result.getMarkdown());
    assertFalse(result.getMarkdown().isBlank());
  }

  @Test
  @DisplayName("[IT-44]: Pages - Update page content with markdown")
  public void updatePageContentWithMarkdown() {
    // Step 1: Create a page with some initial content
    CreatePageParams params = createPageParams("Page to update via markdown", currTestPageId);
    params.setChildren(List.of(ParagraphBlock.of("Original content to be replaced")));
    Page page = getNotion().pages().create(params);

    // Step 2: Replace the entire page content using the markdown endpoint
    String newMarkdown = "# Updated heading\nUpdated paragraph content.";
    PageAsMarkdown replaced =
        getNotion()
            .pages()
            .updateAsMarkdown(page.getId(), UpdatePageAsMarkdownParams.replaceContent(newMarkdown));

    assertNotNull(replaced);
    assertNotNull(replaced.getMarkdown());
    assertEquals(newMarkdown, replaced.getMarkdown());

    // Step 3: Replace all occurrences of "Updated"→"Changed" and "content"→"text"
    PageAsMarkdown updated =
        getNotion()
            .pages()
            .updateAsMarkdown(
                page.getId(),
                UpdatePageAsMarkdownParams.updateContent(
                    List.of(
                        ContentUpdate.of("Updated", "Changed"),
                        ContentUpdate.of("content", "text"))));

    assertNotNull(updated);
    assertNotNull(updated.getMarkdown());
    assertFalse(updated.getMarkdown().contains("Updated"));
    assertFalse(updated.getMarkdown().contains("content"));
    assertTrue(updated.getMarkdown().contains("Changed"));
    assertTrue(updated.getMarkdown().contains("text"));
  }

  // --- helpers ---

  private CreatePageParams createPageParams(String title, String pageId) {
    return createPageParams(title, Parent.pageParent(pageId));
  }

  private CreatePageParams createPageParams(String title, Parent parent) {
    CreatePageParams newPage = createPageParams(title);
    newPage.setParent(parent);
    return newPage;
  }

  private CreatePageParams createPageParams(String title) {
    CreatePageParams newPage = new CreatePageParams();
    newPage.setProperties(new HashMap<>());
    newPage.getProperties().put("title", TitleProperty.of(title));
    return newPage;
  }
}
