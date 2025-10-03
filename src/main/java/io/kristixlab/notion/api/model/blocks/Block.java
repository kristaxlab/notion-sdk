package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.common.NotionObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Abstract base class representing a Notion block.
 * <p>
 * Blocks are the fundamental building blocks of content in Notion. They can contain text,
 * media, or other structured content. This class serves as the base for all specific block
 * types and provides common properties and functionality.
 * </p>
 * <p>
 * The class uses Jackson annotations for JSON serialization/deserialization and includes
 * type information to properly deserialize to specific block subtypes based on the 'type' field.
 * </p>
 *
 * @author KristaxLab
 * @see NotionObject
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = UnknownBlock.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ParagraphBlock.class, name = "paragraph"),
        @JsonSubTypes.Type(value = BreadcrumbBlock.class, name = "breadcrumb"),
        @JsonSubTypes.Type(value = HeadingOneBlock.class, name = "heading_1"),
        @JsonSubTypes.Type(value = HeadingTwoBlock.class, name = "heading_2"),
        @JsonSubTypes.Type(value = HeadingThreeBlock.class, name = "heading_3"),
        @JsonSubTypes.Type(value = ToDoBlock.class, name = "to_do"),
        @JsonSubTypes.Type(value = BulletedListItemBlock.class, name = "bulleted_list_item"),
        @JsonSubTypes.Type(value = NumberedListItemBlock.class, name = "numbered_list_item"),
        @JsonSubTypes.Type(value = QuoteBlock.class, name = "quote"),
        @JsonSubTypes.Type(value = CalloutBlock.class, name = "callout"),
        @JsonSubTypes.Type(value = CodeBlock.class, name = "code"),
        @JsonSubTypes.Type(value = ToggleBlock.class, name = "toggle"),
        @JsonSubTypes.Type(value = DividerBlock.class, name = "divider"),
        @JsonSubTypes.Type(value = ColumnListBlock.class, name = "column_list"),
        @JsonSubTypes.Type(value = BookmarkBlock.class, name = "bookmark"),
        @JsonSubTypes.Type(value = ChildPageBlock.class, name = "child_page"),
        @JsonSubTypes.Type(value = ChildDatabaseBlock.class, name = "child_database"),
        @JsonSubTypes.Type(value = EmbedBlock.class, name = "embed"),
        @JsonSubTypes.Type(value = ImageBlock.class, name = "image"),
        @JsonSubTypes.Type(value = PdfBlock.class, name = "pdf"),
        @JsonSubTypes.Type(value = FileBlock.class, name = "file"),
        @JsonSubTypes.Type(value = VideoBlock.class, name = "video"),
        @JsonSubTypes.Type(value = AudioBlock.class, name = "audio"),
        @JsonSubTypes.Type(value = TableBlock.class, name = "table"),
        @JsonSubTypes.Type(value = TableRowBlock.class, name = "table_row"),
        @JsonSubTypes.Type(value = TableOfContentsBlock.class, name = "table_of_contents"),
        @JsonSubTypes.Type(value = LinkToPageBlock.class, name = "link_to_page"),
        @JsonSubTypes.Type(value = LinkPreviewBlock.class, name = "link_preview"),
        @JsonSubTypes.Type(value = SyncedBlock.class, name = "synced_block"),
        @JsonSubTypes.Type(value = TemplateBlock.class, name = "template"),
        @JsonSubTypes.Type(value = EquationBlock.class, name = "equation"),
        @JsonSubTypes.Type(value = UnsupportedBlock.class, name = "unsupported")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Block extends NotionObject {

  /**
   * The type of the block (e.g., "paragraph", "heading_1", "image", etc.)
   */
  @JsonProperty("type")
  private String type;

  /**
   * Indicates whether this block has child blocks
   */
  @Accessors(fluent = true)
  @JsonProperty("has_children")
  private boolean hasChildren;

  /**
   * Casts this block to a ParagraphBlock.
   *
   * @return this block cast to ParagraphBlock
   * @throws ClassCastException if this block is not a ParagraphBlock
   */
  public ParagraphBlock asParagraph() {
    return (ParagraphBlock) this;
  }

  /**
   * Casts this block to a BreadcrumbBlock.
   *
   * @return this block cast to BreadcrumbBlock
   * @throws ClassCastException if this block is not a BreadcrumbBlock
   */
  public BreadcrumbBlock asBreadcrumb() {
    return (BreadcrumbBlock) this;
  }

  /**
   * Casts this block to a HeadingOneBlock.
   *
   * @return this block cast to HeadingOneBlock
   * @throws ClassCastException if this block is not a HeadingOneBlock
   */
  public HeadingOneBlock asHeadingOne() {
    return (HeadingOneBlock) this;
  }

  /**
   * Casts this block to a HeadingTwoBlock.
   *
   * @return this block cast to HeadingTwoBlock
   * @throws ClassCastException if this block is not a HeadingTwoBlock
   */
  public HeadingTwoBlock asHeadingTwo() {
    return (HeadingTwoBlock) this;
  }

  /**
   * Casts this block to a HeadingThreeBlock.
   *
   * @return this block cast to HeadingThreeBlock
   * @throws ClassCastException if this block is not a HeadingThreeBlock
   */
  public HeadingThreeBlock asHeadingThree() {
    return (HeadingThreeBlock) this;
  }

  /**
   * Casts this block to a ToDoBlock.
   *
   * @return this block cast to ToDoBlock
   * @throws ClassCastException if this block is not a ToDoBlock
   */
  public ToDoBlock asToDo() {
    return (ToDoBlock) this;
  }

  /**
   * Casts this block to a BulletedListItemBlock.
   *
   * @return this block cast to BulletedListItemBlock
   * @throws ClassCastException if this block is not a BulletedListItemBlock
   */
  public BulletedListItemBlock asBulletedListItem() {
    return (BulletedListItemBlock) this;
  }

  /**
   * Casts this block to a NumberedListItemBlock.
   *
   * @return this block cast to NumberedListItemBlock
   * @throws ClassCastException if this block is not a NumberedListItemBlock
   */
  public NumberedListItemBlock asNumberedListItem() {
    return (NumberedListItemBlock) this;
  }

  /**
   * Casts this block to a QuoteBlock.
   *
   * @return this block cast to QuoteBlock
   * @throws ClassCastException if this block is not a QuoteBlock
   */
  public QuoteBlock asQuote() {
    return (QuoteBlock) this;
  }

  /**
   * Casts this block to a CalloutBlock.
   *
   * @return this block cast to CalloutBlock
   * @throws ClassCastException if this block is not a CalloutBlock
   */
  public CalloutBlock asCallout() {
    return (CalloutBlock) this;
  }

  /**
   * Casts this block to a CodeBlock.
   *
   * @return this block cast to CodeBlock
   * @throws ClassCastException if this block is not a CodeBlock
   */
  public CodeBlock asCode() {
    return (CodeBlock) this;
  }

  /**
   * Casts this block to a ToggleBlock.
   *
   * @return this block cast to ToggleBlock
   * @throws ClassCastException if this block is not a ToggleBlock
   */
  public ToggleBlock asToggle() {
    return (ToggleBlock) this;
  }

  /**
   * Casts this block to a DividerBlock.
   *
   * @return this block cast to DividerBlock
   * @throws ClassCastException if this block is not a DividerBlock
   */
  public DividerBlock asDivider() {
    return (DividerBlock) this;
  }

  /**
   * Casts this block to a ColumnListBlock.
   *
   * @return this block cast to ColumnListBlock
   * @throws ClassCastException if this block is not a ColumnListBlock
   */
  public ColumnListBlock asColumnList() {
    return (ColumnListBlock) this;
  }

  /**
   * Casts this block to a BookmarkBlock.
   *
   * @return this block cast to BookmarkBlock
   * @throws ClassCastException if this block is not a BookmarkBlock
   */
  public BookmarkBlock asBookmark() {
    return (BookmarkBlock) this;
  }

  /**
   * Casts this block to a ChildPageBlock.
   *
   * @return this block cast to ChildPageBlock
   * @throws ClassCastException if this block is not a ChildPageBlock
   */
  public ChildPageBlock asChildPage() {
    return (ChildPageBlock) this;
  }

  /**
   * Casts this block to a ChildDatabaseBlock.
   *
   * @return this block cast to ChildDatabaseBlock
   * @throws ClassCastException if this block is not a ChildDatabaseBlock
   */
  public ChildDatabaseBlock asChildDatabase() {
    return (ChildDatabaseBlock) this;
  }

  /**
   * Casts this block to an EmbedBlock.
   *
   * @return this block cast to EmbedBlock
   * @throws ClassCastException if this block is not an EmbedBlock
   */
  public EmbedBlock asEmbed() {
    return (EmbedBlock) this;
  }

  /**
   * Casts this block to an ImageBlock.
   *
   * @return this block cast to ImageBlock
   * @throws ClassCastException if this block is not an ImageBlock
   */
  public ImageBlock asImage() {
    return (ImageBlock) this;
  }

  /**
   * Casts this block to a PdfBlock.
   *
   * @return this block cast to PdfBlock
   * @throws ClassCastException if this block is not a PdfBlock
   */
  public PdfBlock asPdf() {
    return (PdfBlock) this;
  }

  /**
   * Casts this block to a FileBlock.
   *
   * @return this block cast to FileBlock
   * @throws ClassCastException if this block is not a FileBlock
   */
  public FileBlock asFile() {
    return (FileBlock) this;
  }

  /**
   * Casts this block to a VideoBlock.
   *
   * @return this block cast to VideoBlock
   * @throws ClassCastException if this block is not a VideoBlock
   */
  public VideoBlock asVideo() {
    return (VideoBlock) this;
  }

  /**
   * Casts this block to an AudioBlock.
   *
   * @return this block cast to AudioBlock
   * @throws ClassCastException if this block is not an AudioBlock
   */
  public AudioBlock asAudio() {
    return (AudioBlock) this;
  }

  /**
   * Casts this block to a TableBlock.
   *
   * @return this block cast to TableBlock
   * @throws ClassCastException if this block is not a TableBlock
   */
  public TableBlock asTable() {
    return (TableBlock) this;
  }

  /**
   * Casts this block to a TableRowBlock.
   *
   * @return this block cast to TableRowBlock
   * @throws ClassCastException if this block is not a TableRowBlock
   */
  public TableRowBlock asTableRow() {
    return (TableRowBlock) this;
  }

  /**
   * Casts this block to a TableOfContentsBlock.
   *
   * @return this block cast to TableOfContentsBlock
   * @throws ClassCastException if this block is not a TableOfContentsBlock
   */
  public TableOfContentsBlock asTableOfContents() {
    return (TableOfContentsBlock) this;
  }

  /**
   * Casts this block to a LinkToPageBlock.
   *
   * @return this block cast to LinkToPageBlock
   * @throws ClassCastException if this block is not a LinkToPageBlock
   */
  public LinkToPageBlock asLinkToPage() {
    return (LinkToPageBlock) this;
  }

  /**
   * Casts this block to a LinkPreviewBlock.
   *
   * @return this block cast to LinkPreviewBlock
   * @throws ClassCastException if this block is not a LinkPreviewBlock
   */
  public LinkPreviewBlock asLinkPreview() {
    return (LinkPreviewBlock) this;
  }

  /**
   * Casts this block to a SyncedBlock.
   *
   * @return this block cast to SyncedBlock
   * @throws ClassCastException if this block is not a SyncedBlock
   */
  public SyncedBlock asSynced() {
    return (SyncedBlock) this;
  }

  /**
   * Casts this block to a TemplateBlock.
   *
   * @return this block cast to TemplateBlock
   * @throws ClassCastException if this block is not a TemplateBlock
   */
  public TemplateBlock asTemplate() {
    return (TemplateBlock) this;
  }

  /**
   * Casts this block to an EquationBlock.
   *
   * @return this block cast to EquationBlock
   * @throws ClassCastException if this block is not an EquationBlock
   */
  public EquationBlock asEquation() {
    return (EquationBlock) this;
  }

  /**
   * Casts this block to an UnsupportedBlock.
   *
   * @return this block cast to UnsupportedBlock
   * @throws ClassCastException if this block is not an UnsupportedBlock
   */
  public UnsupportedBlock asUnsupported() {
    return (UnsupportedBlock) this;
  }

  /**
   * Casts this block to an UnknownBlock.
   *
   * @return this block cast to UnknownBlock
   * @throws ClassCastException if this block is not an UnknownBlock
   */
  public UnknownBlock asUnknown() {
    return (UnknownBlock) this;
  }
}

// https://developers.notion.com/reference/block
