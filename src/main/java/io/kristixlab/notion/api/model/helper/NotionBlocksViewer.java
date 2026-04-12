package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A read-only, immutable view over a list of {@link Block} objects that provides convenient
 * filtering, navigation, and text extraction.
 *
 * <p>{@code BlockListViewer} eliminates the boilerplate of iterating, casting, and extracting
 * content from block lists returned by the Notion API. Every method that narrows or transforms the
 * view returns a new {@code BlockListViewer}, so operations can be chained fluently:
 *
 * <pre>{@code
 * BlockListViewer content = BlockListViewer.of(response.getResults());
 *
 * // Extract all paragraph text
 * String body = content.paragraphs().plainText();
 *
 * // List unchecked tasks
 * List<String> pending = content.todos().unchecked().plainTextList();
 *
 * // Find the first heading
 * Optional<Block> title = content.headings().first();
 *
 * // Count all images at every depth
 * int images = content.flatten().images().size();
 * }</pre>
 *
 * <p>Instances are created via the {@link #of(List)}, {@link #of(Block...)}, or {@link
 * #of(BlockList)} factories. The wrapped list is defensively copied — mutations to the source list
 * do not affect the view.
 *
 * @see Block
 */
public final class NotionBlocksViewer implements Iterable<Block> {

  private static final NotionBlocksViewer EMPTY = new NotionBlocksViewer(Collections.emptyList());

  private final List<Block> blocks;

  private NotionBlocksViewer(List<Block> blocks) {
    this.blocks = blocks;
  }

  /**
   * Creates a view over the given block list. The list is defensively copied.
   *
   * @param blocks the blocks to wrap (must not be {@code null})
   * @return a new view, or an empty view if the list is {@code null} or empty
   */
  public static NotionBlocksViewer of(List<Block> blocks) {
    if (blocks == null || blocks.isEmpty()) {
      return EMPTY;
    }
    return new NotionBlocksViewer(new ArrayList<>(blocks));
  }

  /**
   * Creates a view over the given blocks.
   *
   * @param blocks the blocks to wrap
   * @return a new view, or an empty view if no blocks are provided
   */
  public static NotionBlocksViewer of(Block... blocks) {
    if (blocks == null || blocks.length == 0) {
      return EMPTY;
    }
    return new NotionBlocksViewer(new ArrayList<>(Arrays.asList(blocks)));
  }

  /**
   * Creates a view over the results of a {@link BlockList} returned by the Notion API.
   *
   * <p>This is a convenience overload for the common pattern of wrapping the result of {@code
   * blocks.retrieveChildren(pageId)}:
   *
   * <pre>{@code
   * BlockList response = notion.blocks().retrieveChildren(pageId);
   * BlockListViewer viewer = BlockListViewer.of(response);
   * }</pre>
   *
   * @param blockList the API response to wrap (may be {@code null})
   * @return a new view over the results, or an empty view if {@code blockList} is {@code null} or
   *     contains no results
   */
  public static NotionBlocksViewer of(BlockList blockList) {
    if (blockList == null) {
      return EMPTY;
    }
    return of(blockList.getResults());
  }

  /**
   * Returns a view containing only blocks that are instances of the given type.
   *
   * @param type the block class to filter by (e.g., {@code ParagraphBlock.class})
   * @param <T> the block type
   * @return a new view containing only matching blocks
   */
  public <T extends Block> NotionBlocksViewer ofType(Class<T> type) {
    return where(type::isInstance);
  }

  /**
   * Returns a view containing only {@link ParagraphBlock} instances.
   *
   * @return a new view of paragraphs
   */
  public NotionBlocksViewer paragraphs() {
    return ofType(ParagraphBlock.class);
  }

  /**
   * Returns a view containing all heading blocks (H1 through H4).
   *
   * @return a new view of headings
   */
  public NotionBlocksViewer headings() {
    return where(
        b ->
            b instanceof HeadingOneBlock
                || b instanceof HeadingTwoBlock
                || b instanceof HeadingThreeBlock
                || b instanceof HeadingFourBlock);
  }

  /**
   * Returns a view containing only {@link ToDoBlock} instances.
   *
   * @return a new view of to-do blocks
   */
  public NotionBlocksViewer todos() {
    return ofType(ToDoBlock.class);
  }

  /**
   * Returns a view containing only {@link BulletedListItemBlock} instances.
   *
   * @return a new view of bulleted list items
   */
  public NotionBlocksViewer bullets() {
    return ofType(BulletedListItemBlock.class);
  }

  /**
   * Returns a view containing only {@link NumberedListItemBlock} instances.
   *
   * @return a new view of numbered list items
   */
  public NotionBlocksViewer numbered() {
    return ofType(NumberedListItemBlock.class);
  }

  /**
   * Returns a view containing only {@link ToggleBlock} instances.
   *
   * @return a new view of toggle blocks
   */
  public NotionBlocksViewer toggles() {
    return ofType(ToggleBlock.class);
  }

  /**
   * Returns a view containing only {@link QuoteBlock} instances.
   *
   * @return a new view of quote blocks
   */
  public NotionBlocksViewer quotes() {
    return ofType(QuoteBlock.class);
  }

  /**
   * Returns a view containing only {@link CalloutBlock} instances.
   *
   * @return a new view of callout blocks
   */
  public NotionBlocksViewer callouts() {
    return ofType(CalloutBlock.class);
  }

  /**
   * Returns a view containing only {@link CodeBlock} instances.
   *
   * @return a new view of code blocks
   */
  public NotionBlocksViewer code() {
    return ofType(CodeBlock.class);
  }

  /**
   * Returns a view containing only {@link ImageBlock} instances.
   *
   * @return a new view of image blocks
   */
  public NotionBlocksViewer images() {
    return ofType(ImageBlock.class);
  }

  /**
   * Returns a view containing only blocks that carry textual (rich text) content intended for
   * reading: paragraphs, headings, bulleted/numbered list items, to-do items, toggles, quotes, and
   * callouts.
   *
   * <p>Code blocks are deliberately excluded because they represent source code rather than prose.
   *
   * @return a new view of textual blocks
   */
  public NotionBlocksViewer textual() {
    return where(
        b ->
            b instanceof ParagraphBlock
                || b instanceof HeadingOneBlock
                || b instanceof HeadingTwoBlock
                || b instanceof HeadingThreeBlock
                || b instanceof HeadingFourBlock
                || b instanceof BulletedListItemBlock
                || b instanceof NumberedListItemBlock
                || b instanceof ToDoBlock
                || b instanceof ToggleBlock
                || b instanceof QuoteBlock
                || b instanceof CalloutBlock);
  }

  /**
   * Returns a view containing only media blocks: images, videos, audio, PDFs, and generic file
   * blocks whose URL ends with a common media extension (image, video, audio, or PDF).
   *
   * <p>A {@link FileBlock} is included when its external or hosted URL ends with an extension from
   * one of these categories:
   *
   * <ul>
   *   <li>Image — jpg, jpeg, png, gif, bmp, webp, svg, tiff, tif, ico, heic, heif, avif
   *   <li>Video — mp4, mov, avi, wmv, flv, mkv, webm, mpeg, mpg, m4v, 3gp
   *   <li>Audio — mp3, wav, ogg, flac, aac, wma, m4a, opus, aiff
   *   <li>PDF — pdf
   * </ul>
   *
   * @return a new view of media blocks
   */
  public NotionBlocksViewer media() {
    return where(
        b ->
            b instanceof ImageBlock
                || b instanceof VideoBlock
                || b instanceof AudioBlock
                || b instanceof PdfBlock
                || (b instanceof FileBlock fb && hasMediaExtension(fb)));
  }

  /**
   * Returns a view containing only checked {@link ToDoBlock} instances. Non-ToDo blocks are
   * silently filtered out.
   *
   * @return a new view of checked to-do blocks
   */
  public NotionBlocksViewer checked() {
    return where(b -> b instanceof ToDoBlock t && Boolean.TRUE.equals(t.getToDo().getChecked()));
  }

  /**
   * Returns a view containing only unchecked {@link ToDoBlock} instances. A to-do is considered
   * unchecked when its checked field is {@code false} or {@code null}. Non-ToDo blocks are silently
   * filtered out.
   *
   * @return a new view of unchecked to-do blocks
   */
  public NotionBlocksViewer unchecked() {
    return where(b -> b instanceof ToDoBlock t && !Boolean.TRUE.equals(t.getToDo().getChecked()));
  }

  /**
   * Returns a view containing only blocks whose textual content — or any descendant's content —
   * contains the given keyword (case-insensitive). A block is included if the keyword is found in
   * its own content or recursively in any of its children (depth-first, short-circuits on first
   * match). The search covers:
   *
   * <ul>
   *   <li>Plain text from all rich text segments (concatenated, so a keyword that spans multiple
   *       {@link RichText} objects is found)
   *   <li>{@link ChildPageBlock} and {@link ChildDatabaseBlock} titles
   *   <li>{@link EquationBlock} expressions
   *   <li>URLs from hyperlinks within rich text ({@link RichText#getHref()})
   *   <li>Bookmark, embed, and link preview URLs
   *   <li>File-based block (image, video, audio, PDF, file) external URLs
   * </ul>
   *
   * @param keyword the text to search for (case-insensitive, must not be {@code null})
   * @return a new view containing only blocks that contain the keyword (directly or in children)
   * @throws NullPointerException if {@code keyword} is {@code null}
   */
  public NotionBlocksViewer containing(String keyword) {
    Objects.requireNonNull(keyword, "keyword must not be null");
    String lower = keyword.toLowerCase(Locale.ROOT);
    return where(b -> blockContainsKeyword(b, lower));
  }

  /**
   * Returns a view containing only blocks that match the given predicate.
   *
   * @param predicate the condition blocks must satisfy
   * @return a new view containing only matching blocks
   */
  public NotionBlocksViewer where(Predicate<Block> predicate) {
    List<Block> filtered = new ArrayList<>();
    for (Block block : blocks) {
      if (predicate.test(block)) {
        filtered.add(block);
      }
    }
    return filtered.isEmpty() ? EMPTY : new NotionBlocksViewer(filtered);
  }

  /**
   * Extracts the plain text from all textual blocks, joining each block's text with a newline
   * ({@code \n}) delimiter. Non-textual blocks (images, dividers, etc.) are silently skipped.
   *
   * @return the combined plain text, or an empty string if no textual blocks are present
   */
  public String plainText() {
    return plainText("\n");
  }

  /**
   * Extracts the plain text from all textual blocks, joining each block's text with the given
   * delimiter. Non-textual blocks are silently skipped.
   *
   * @param delimiter the string to place between each block's text
   * @return the combined plain text, or an empty string if no textual blocks are present
   */
  public String plainText(String delimiter) {
    return plainTextList().stream().collect(Collectors.joining(delimiter));
  }

  /**
   * Extracts the plain text from each textual block as a separate list entry. Non-textual blocks
   * are silently skipped.
   *
   * @return a list of plain text strings, one per textual block
   */
  public List<String> plainTextList() {
    List<String> texts = new ArrayList<>();
    for (Block block : blocks) {
      String text = extractPlainText(block);
      if (text != null) {
        texts.add(text);
      }
    }
    return texts;
  }

  /**
   * Collects all URLs found across the blocks in this view and their descendants (depth-first).
   * Sources include:
   *
   * <ul>
   *   <li>Hyperlinks in rich text segments ({@link RichText#getHref()})
   *   <li>{@link BookmarkBlock} URLs
   *   <li>{@link EmbedBlock} URLs
   *   <li>{@link LinkPreviewBlock} URLs
   *   <li>{@link ImageBlock}, {@link VideoBlock}, {@link AudioBlock}, {@link PdfBlock}, and {@link
   *       FileBlock} external URLs
   * </ul>
   *
   * <p>Each block's own links are collected first, then its children are visited recursively. This
   * means links in a parent block appear before links in its children.
   *
   * <p>Duplicate URLs are included as many times as they appear. The returned list preserves
   * encounter order.
   *
   * @return a list of all URLs found, or an empty list if none are present
   */
  public List<String> links() {
    List<String> urls = new ArrayList<>();
    for (Block block : blocks) {
      collectLinks(block, urls);
    }
    return urls;
  }

  /**
   * Returns the first block in this view.
   *
   * @return an {@code Optional} containing the first block, or empty if the view is empty
   */
  public Optional<Block> first() {
    return blocks.isEmpty() ? Optional.empty() : Optional.of(blocks.get(0));
  }

  /**
   * Returns the first block of the given type.
   *
   * @param type the block class to look for
   * @param <T> the block type
   * @return an {@code Optional} containing the first matching block, or empty if none match
   */
  public <T extends Block> Optional<T> first(Class<T> type) {
    for (Block block : blocks) {
      if (type.isInstance(block)) {
        return Optional.of(type.cast(block));
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the first block matching the given predicate.
   *
   * @param predicate the condition the block must satisfy
   * @return an {@code Optional} containing the first matching block, or empty if none match
   */
  public Optional<Block> first(Predicate<Block> predicate) {
    for (Block block : blocks) {
      if (predicate.test(block)) {
        return Optional.of(block);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the last block in this view.
   *
   * @return an {@code Optional} containing the last block, or empty if the view is empty
   */
  public Optional<Block> last() {
    return blocks.isEmpty() ? Optional.empty() : Optional.of(blocks.get(blocks.size() - 1));
  }

  /**
   * Returns a flattened view that includes every block at every depth, using depth-first traversal.
   * Parent blocks appear before their children, matching the natural reading order of a Notion
   * page.
   *
   * <p>Children are collected from:
   *
   * <ul>
   *   <li>{@link BlockWithChildren} subclasses (paragraph, toggle, quote, callout, bullets,
   *       numbered, to-do, headings)
   *   <li>{@link ColumnListBlock} → {@link ColumnBlock} children
   *   <li>{@link ColumnBlock} → children
   *   <li>{@link SyncedBlock} → children
   *   <li>{@link TableBlock} → {@link TableRowBlock} children
   *   <li>{@link TemplateBlock} → children
   * </ul>
   *
   * @return a new view containing all blocks at all depths
   */
  public NotionBlocksViewer flatten() {
    List<Block> flat = new ArrayList<>();
    for (Block block : blocks) {
      flattenInto(block, flat);
    }
    return flat.isEmpty() ? EMPTY : new NotionBlocksViewer(flat);
  }

  /**
   * Casts all blocks in this view to the given type and returns them as a typed list. This is
   * intended for use after a type filter (e.g., {@code
   * view.paragraphs().as(ParagraphBlock.class)}).
   *
   * @param type the target class
   * @param <T> the block type
   * @return a new list of cast blocks
   * @throws ClassCastException if any block is not an instance of the given type
   */
  public <T extends Block> List<T> as(Class<T> type) {
    List<T> result = new ArrayList<>(blocks.size());
    for (Block block : blocks) {
      result.add(type.cast(block));
    }
    return result;
  }

  /**
   * Returns the underlying blocks as an unmodifiable list.
   *
   * @return an unmodifiable view of the blocks
   */
  public List<Block> blocks() {
    return Collections.unmodifiableList(blocks);
  }

  /**
   * Returns a sequential {@link Stream} over the blocks in this view.
   *
   * @return a stream of blocks
   */
  public Stream<Block> stream() {
    return blocks.stream();
  }

  /**
   * Returns the number of blocks in this view.
   *
   * @return the block count
   */
  public int size() {
    return blocks.size();
  }

  /**
   * Returns {@code true} if this view contains no blocks.
   *
   * @return {@code true} if empty
   */
  public boolean isEmpty() {
    return blocks.isEmpty();
  }

  /**
   * Performs the given action for each block of the specified type. Blocks that are not instances
   * of the given type are silently skipped.
   *
   * @param type the block class to filter by
   * @param action the action to perform on each matching block
   * @param <T> the block type
   */
  public <T extends Block> void forEach(Class<T> type, Consumer<T> action) {
    for (Block block : blocks) {
      if (type.isInstance(block)) {
        action.accept(type.cast(block));
      }
    }
  }

  @Override
  public Iterator<Block> iterator() {
    return Collections.unmodifiableList(blocks).iterator();
  }

  /**
   * Extracts plain text from a single block by concatenating the {@code plainText} field of each
   * {@link RichText} element. Returns {@code null} for non-textual blocks.
   */
  private static String extractPlainText(Block block) {
    List<RichText> richTexts = extractRichText(block);
    if (richTexts != null) {
      return joinPlainText(richTexts);
    }

    if (block instanceof ChildPageBlock cpb) {
      String title = cpb.getChildPage().getTitle();
      return title != null ? title : "";
    }
    if (block instanceof ChildDatabaseBlock cdb) {
      String title = cdb.getChildDatabase().getTitle();
      return title != null ? title : "";
    }
    if (block instanceof EquationBlock eb) {
      String expr = eb.getEquation().getExpression();
      return expr != null ? expr : "";
    }

    return null;
  }

  /**
   * Extracts the {@code List<RichText>} from any block that carries rich text content. Returns
   * {@code null} for blocks that do not have rich text.
   */
  private static List<RichText> extractRichText(Block block) {
    if (block instanceof ParagraphBlock pb) {
      return pb.getParagraph().getRichText();
    }
    if (block instanceof HeadingOneBlock hb) {
      return hb.getHeading1().getRichText();
    }
    if (block instanceof HeadingTwoBlock hb) {
      return hb.getHeading2().getRichText();
    }
    if (block instanceof HeadingThreeBlock hb) {
      return hb.getHeading3().getRichText();
    }
    if (block instanceof HeadingFourBlock hb) {
      return hb.getHeading4().getRichText();
    }
    if (block instanceof ToggleBlock tb) {
      return tb.getToggle().getRichText();
    }
    if (block instanceof QuoteBlock qb) {
      return qb.getQuote().getRichText();
    }
    if (block instanceof CalloutBlock cb) {
      return cb.getCallout().getRichText();
    }
    if (block instanceof BulletedListItemBlock bb) {
      return bb.getBulletedListItem().getRichText();
    }
    if (block instanceof NumberedListItemBlock nb) {
      return nb.getNumberedListItem().getRichText();
    }
    if (block instanceof ToDoBlock td) {
      return td.getToDo().getRichText();
    }
    if (block instanceof CodeBlock cb) {
      return cb.getCode().getRichText();
    }
    if (block instanceof TemplateBlock tb) {
      return tb.getTemplate().getRichText();
    }
    return null;
  }

  private static String joinPlainText(List<RichText> richTexts) {
    if (richTexts == null || richTexts.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (RichText rt : richTexts) {
      if (rt.getPlainText() != null) {
        sb.append(rt.getPlainText());
      }
    }
    return sb.toString();
  }

  private static void flattenInto(Block block, List<Block> result) {
    result.add(block);
    List<Block> children = extractChildren(block);
    if (children != null) {
      for (Block child : children) {
        flattenInto(child, result);
      }
    }
  }

  /**
   * Extracts child blocks from any block type that can contain them. Returns {@code null} if the
   * block has no children or the children list is empty.
   */
  private static List<Block> extractChildren(Block block) {
    List<Block> children = null;

    if (block instanceof ParagraphBlock pb) {
      children = pb.getParagraph().getChildren();
    } else if (block instanceof ToggleBlock tb) {
      children = tb.getToggle().getChildren();
    } else if (block instanceof QuoteBlock qb) {
      children = qb.getQuote().getChildren();
    } else if (block instanceof CalloutBlock cb) {
      children = cb.getCallout().getChildren();
    } else if (block instanceof BulletedListItemBlock bb) {
      children = bb.getBulletedListItem().getChildren();
    } else if (block instanceof NumberedListItemBlock nb) {
      children = nb.getNumberedListItem().getChildren();
    } else if (block instanceof ToDoBlock td) {
      children = td.getToDo().getChildren();
    } else if (block instanceof HeadingOneBlock hb) {
      children = hb.getHeading1().getChildren();
    } else if (block instanceof HeadingTwoBlock hb) {
      children = hb.getHeading2().getChildren();
    } else if (block instanceof HeadingThreeBlock hb) {
      children = hb.getHeading3().getChildren();
    } else if (block instanceof HeadingFourBlock hb) {
      children = hb.getHeading4().getChildren();
    } else if (block instanceof ColumnListBlock clb) {
      List<ColumnBlock> cols = clb.getColumnList().getChildren();
      if (cols != null && !cols.isEmpty()) {
        children = new ArrayList<>(cols);
      }
    } else if (block instanceof ColumnBlock cb) {
      children = cb.getColumn().getChildren();
    } else if (block instanceof SyncedBlock sb) {
      children = sb.getSyncedBlock().getChildren();
    } else if (block instanceof TableBlock tb) {
      List<TableRowBlock> rows = tb.getTable().getChildren();
      if (rows != null && !rows.isEmpty()) {
        children = new ArrayList<>(rows);
      }
    } else if (block instanceof TemplateBlock tb) {
      children = tb.getTemplate().getChildren();
    }

    return (children != null && !children.isEmpty()) ? children : null;
  }

  /**
   * Tests whether a block's textual content, titles, or URLs contain the given keyword (already
   * lower-cased). If the block itself does not match, its children are checked recursively
   * (depth-first). The method short-circuits as soon as a match is found.
   */
  private static boolean blockContainsKeyword(Block block, String lowerKeyword) {
    if (blockOwnContentContainsKeyword(block, lowerKeyword)) {
      return true;
    }

    List<Block> children = extractChildren(block);
    if (children != null) {
      for (Block child : children) {
        if (blockContainsKeyword(child, lowerKeyword)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Tests whether a single block's own textual content, titles, or URLs contain the given keyword
   * (already lower-cased), without considering children.
   */
  private static boolean blockOwnContentContainsKeyword(Block block, String lowerKeyword) {
    String text = extractPlainText(block);
    if (text != null && text.toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }

    List<RichText> richTexts = extractRichText(block);
    if (richTexts != null) {
      for (RichText rt : richTexts) {
        if (rt.getHref() != null && rt.getHref().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
          return true;
        }
      }
    }

    if (block instanceof BookmarkBlock bb
        && bb.getBookmark().getUrl() != null
        && bb.getBookmark().getUrl().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }
    if (block instanceof EmbedBlock eb
        && eb.getEmbed().getUrl() != null
        && eb.getEmbed().getUrl().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }
    if (block instanceof LinkPreviewBlock lpb
        && lpb.getLinkPreview().getUrl() != null
        && lpb.getLinkPreview().getUrl().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }

    return fileDataContainsKeyword(extractFileData(block), lowerKeyword);
  }

  /**
   * Collects all URLs found in a single block and its descendants (depth-first) into the target
   * list. Checks rich text hrefs, bookmark/embed/link-preview URLs, and file-based block external
   * URLs, then recurses into children.
   */
  private static void collectLinks(Block block, List<String> urls) {
    List<RichText> richTexts = extractRichText(block);
    if (richTexts != null) {
      for (RichText rt : richTexts) {
        if (rt.getHref() != null) {
          urls.add(rt.getHref());
        }
      }
    }

    if (block instanceof BookmarkBlock bb && bb.getBookmark().getUrl() != null) {
      urls.add(bb.getBookmark().getUrl());
    }
    if (block instanceof EmbedBlock eb && eb.getEmbed().getUrl() != null) {
      urls.add(eb.getEmbed().getUrl());
    }
    if (block instanceof LinkPreviewBlock lpb && lpb.getLinkPreview().getUrl() != null) {
      urls.add(lpb.getLinkPreview().getUrl());
    }

    collectFileDataUrl(extractFileData(block), urls);

    List<Block> children = extractChildren(block);
    if (children != null) {
      for (Block child : children) {
        collectLinks(child, urls);
      }
    }
  }

  private static FileData extractFileData(Block block) {
    if (block instanceof ImageBlock ib) {
      return ib.getImage();
    }
    if (block instanceof VideoBlock vb) {
      return vb.getVideo();
    }
    if (block instanceof AudioBlock ab) {
      return ab.getAudio();
    }
    if (block instanceof PdfBlock pb) {
      return pb.getPdf();
    }
    if (block instanceof FileBlock fb) {
      return fb.getFile();
    }
    return null;
  }

  private static void collectFileDataUrl(FileData fileData, List<String> urls) {
    if (fileData == null) {
      return;
    }
    if (fileData.getExternal() != null && fileData.getExternal().getUrl() != null) {
      urls.add(fileData.getExternal().getUrl());
    }
    if (fileData.getFile() != null && fileData.getFile().getUrl() != null) {
      urls.add(fileData.getFile().getUrl());
    }
  }

  private static boolean fileDataContainsKeyword(FileData fileData, String lowerKeyword) {
    if (fileData == null) {
      return false;
    }
    if (fileData.getExternal() != null
        && fileData.getExternal().getUrl() != null
        && fileData.getExternal().getUrl().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }
    if (fileData.getFile() != null
        && fileData.getFile().getUrl() != null
        && fileData.getFile().getUrl().toLowerCase(Locale.ROOT).contains(lowerKeyword)) {
      return true;
    }
    return false;
  }

  private static final Set<String> MEDIA_EXTENSIONS =
      Set.of(
          "jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "tiff", "tif", "ico", "heic", "heif",
          "avif", "mp4", "mov", "avi", "wmv", "flv", "mkv", "webm", "mpeg", "mpg", "m4v", "3gp",
          "mp3", "wav", "ogg", "flac", "aac", "wma", "m4a", "opus", "aiff", "pdf");

  private static boolean hasMediaExtension(FileBlock fb) {
    String url = extractFileBlockUrl(fb);
    if (url == null) {
      return false;
    }
    String lower = url.toLowerCase(Locale.ROOT);
    int queryStart = lower.indexOf('?');
    if (queryStart >= 0) {
      lower = lower.substring(0, queryStart);
    }
    int dotPos = lower.lastIndexOf('.');
    return dotPos >= 0 && MEDIA_EXTENSIONS.contains(lower.substring(dotPos + 1));
  }

  private static String extractFileBlockUrl(FileBlock fb) {
    FileData data = fb.getFile();
    if (data == null) {
      return null;
    }
    if (data.getExternal() != null && data.getExternal().getUrl() != null) {
      return data.getExternal().getUrl();
    }
    if (data.getFile() != null && data.getFile().getUrl() != null) {
      return data.getFile().getUrl();
    }
    return null;
  }
}
