package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.common.NotionObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

  @JsonProperty("type")
  private String type;

  @Accessors(fluent = true)
  @JsonProperty("has_children")
  private boolean hasChildren;

  public ParagraphBlock asParagraph() {
    return (ParagraphBlock) this;
  }

  public BreadcrumbBlock asBreadcrumb() {
    return (BreadcrumbBlock) this;
  }

  public HeadingOneBlock asHeadingOne() {
    return (HeadingOneBlock) this;
  }

  public HeadingTwoBlock asHeadingTwo() {
    return (HeadingTwoBlock) this;
  }

  public HeadingThreeBlock asHeadingThree() {
    return (HeadingThreeBlock) this;
  }

  public ToDoBlock asToDo() {
    return (ToDoBlock) this;
  }

  public BulletedListItemBlock asBulletedListItem() {
    return (BulletedListItemBlock) this;
  }

  public NumberedListItemBlock asNumberedListItem() {
    return (NumberedListItemBlock) this;
  }

  public QuoteBlock asQuote() {
    return (QuoteBlock) this;
  }

  public CalloutBlock asCallout() {
    return (CalloutBlock) this;
  }

  public CodeBlock asCode() {
    return (CodeBlock) this;
  }

  public ToggleBlock asToggle() {
    return (ToggleBlock) this;
  }

  public DividerBlock asDivider() {
    return (DividerBlock) this;
  }

  public ColumnListBlock asColumnList() {
    return (ColumnListBlock) this;
  }

  public BookmarkBlock asBookmark() {
    return (BookmarkBlock) this;
  }

  public ChildPageBlock asChildPage() {
    return (ChildPageBlock) this;
  }

  public ChildDatabaseBlock asChildDatabase() {
    return (ChildDatabaseBlock) this;
  }

  public EmbedBlock asEmbed() {
    return (EmbedBlock) this;
  }

  public ImageBlock asImage() {
    return (ImageBlock) this;
  }

  public PdfBlock asPdf() {
    return (PdfBlock) this;
  }

  public FileBlock asFile() {
    return (FileBlock) this;
  }

  public VideoBlock asVideo() {
    return (VideoBlock) this;
  }

  public AudioBlock asAudio() {
    return (AudioBlock) this;
  }

  public TableBlock asTable() {
    return (TableBlock) this;
  }

  public TableRowBlock asTableRow() {
    return (TableRowBlock) this;
  }

  public TableOfContentsBlock asTableOfContents() {
    return (TableOfContentsBlock) this;
  }

  public LinkToPageBlock asLinkToPage() {
    return (LinkToPageBlock) this;
  }

  public LinkPreviewBlock asLinkPreview() {
    return (LinkPreviewBlock) this;
  }

  public SyncedBlock asSynced() {
    return (SyncedBlock) this;
  }

  public TemplateBlock asTemplate() {
    return (TemplateBlock) this;
  }

  public EquationBlock asEquation() {
    return (EquationBlock) this;
  }

  public UnsupportedBlock asUnsupported() {
    return (UnsupportedBlock) this;
  }

  public UnknownBlock asUnknown() {
    return (UnknownBlock) this;
  }
}

// https://developers.notion.com/reference/block
