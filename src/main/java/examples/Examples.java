package examples;

import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;
import static io.kristixlab.notion.api.model.helper.NotionText.*;

import io.kristixlab.notion.api.NotionClient;
import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.helper.NotionBlocks;
import io.kristixlab.notion.api.model.page.CreatePageParams;
import io.kristixlab.notion.api.model.page.Page;
import java.util.List;

public class Examples {

  private static final String PAGE_ID = "some-parent-page-id";
  private static final String DATABASE_ID = "some-database-id";

  public void addBlocksToPageShort(NotionClient client) {

    // adding on block
    client.blocks().appendChildren("page_id", paragraph("paragraph text"));

    // adding multiple blocks
    client.blocks().appendChildren("page_id", List.of(bullet("item 1"), bullet("item 2")));

    // adding multiple blocks with a builder — no need to manually construct block objects
    client
        .blocks()
        .appendChildren(
            "page_id", content -> content.heading1("To do list").todo("todo 1").todo("todo 2"));
  }

  public void blocksBuilding(NotionClient client) {

    // using List.of() to prepare a list of blocks to append
    List<Block> todos =
        List.of(
            heading1("Github project"), todo("Define CI pipeline"), todo("Write initial tests"));

    // using the builder to prepare a list of blocks to append
    List<Block> dayPlan =
        blocksBuilder()
            .heading2("What I'm doing today")
            .bullet("Implement user search endpoint")
            .bullet("Write integration tests")
            .divider()
            .heading2("Blockers")
            .callout(Icon.emoji("🚧"), "Waiting on API credentials from the platform team.")
            .build();

    // aggregating multiple lists into a single list to append
    List<Block> allBlocks = blocksBuilder().blocks(dayPlan).blocks(todos).build();

    // appending the combined list to the page
    client.blocks().appendChildren("page_id", allBlocks, Position.pageStart());
  }

  public void stylesAndFormatting(NotionClient client) {

    // Many blocks support rich text formatting
    ParagraphBlock paragraph =
        paragraph(
            plainText("This is a "),
            bold("bold"),
            plainText(" word, this is "),
            italic("italic").blue(),
            plainText(", and this is "),
            code("code"),
            plainText("."));
  }

  public void columnedLayout(NotionClient notion) {
    notion
        .blocks()
        .appendChildren(
            PAGE_ID,
            NotionBlocks.columns(
                left -> left.heading1("To Do list").todo("Item 1").todo("Item 2"),
                middle -> middle.heading1("Numbered list").numbered("Item 1").numbered("Item 2"),
                right -> right.heading1("Bulleted list").bullet("Item 1").bullet("Item 2")));

    notion
        .blocks()
        .appendChildren(
            PAGE_ID,
            NotionBlocks.columns(
                List.of(heading1("column 1"), paragraph("text 1"), paragraph("text 2")),
                List.of(), // empty column
                List.of(heading1("column 3"), paragraph("text 1"), paragraph("text 2"))));

    notion
        .blocks()
        .appendChildren(
            PAGE_ID,
            c ->
                c.columns(
                    column(0.33, heading1("narrow column"), paragraph("text")),
                    column(0.66, heading1("wide column"), paragraph("text"))));
  }

  public void knowledgeBaseArticle(NotionClient client) {

    Page page =
        client
            .pages()
            .create(
                create ->
                    create
                        .inPage(PAGE_ID)
                        .title("Error Handling Best Practices")
                        .icon("🛡️")
                        .cover("https://images.unsplash.com/photo-error-handling.jpg")
                        .children(
                            content ->
                                content
                                    .heading1("Page Heading")
                                    .numbered(
                                        "Fail fast — detect errors at the boundary, not deep inside.")
                                    .numbered(
                                        "Be specific — throw the most precise exception type available.")
                                    .numbered(
                                        "Log once — avoid duplicate log entries for the same error.")
                                    .divider()));
  }

  public void projectDashboardInline(NotionClient client) {
    Page page =
        client
            .pages()
            .create(
                CreatePageParams.builder()
                    .inDatasource(DATABASE_ID)
                    .title("Project Aurora — Q2 Dashboard")
                    .icon("https://images.unsplash.com/photo-error-handling.jpg")
                    .cover("file-upload-uuid")
                    .children(
                        content ->
                            content
                                .heading1("Hello, Notion!")
                                .divider()
                                .todo("Set up project dashboard")
                                .todo("Gather metrics")
                                .todo("Share with team")
                                .image("https://example.com/images.png"))
                    .build());
  }

  /**
   * Example 5 — Markdown-hybrid: write prose in markdown, switch to blocks for layout.
   *
   * <p>Developers think in markdown. This style lets you write the text-heavy parts as markdown
   * strings (headings, paragraphs, lists, code fences, quotes) while seamlessly mixing in
   * structured blocks (columns, tables, callouts, images) that markdown can't express. The SDK
   * parses markdown sections into blocks automatically.
   */
  public void markdownHybrid(NotionClient client) {
    CreatePageParams.builder()
        .inPage(PAGE_ID)
        .title("Project Aurora — Q2 Dashboard")
        .icon(Icon.emoji("📊"))
        .cover(Cover.external("https://images.unsplash.com/photo-dashboard.jpg"))
        .markdown(
            """
 ---

 # Architecture

 The system uses a `microservices` architecture with event-driven
 communication via Kafka.

 ```java
 NotionClient client = NotionClient.forToken("secret_xxx");
 Page page = client.pages().retrieve("page-id");
 ```
 """)
        .build();
  }
}
