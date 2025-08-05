package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
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
public class Block {

  @JsonProperty("object")
  private String object;

  @JsonProperty("id")
  private String id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("created_time")
  private String createdTime;

  @JsonProperty("last_edited_time")
  private String lastEditedTime;

  @JsonProperty("archived")
  private boolean archived;

  @Accessors(fluent = true)
  @JsonProperty("has_children")
  private boolean hasChildren;

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("created_by")
  private User createdBy;

  @JsonProperty("last_edited_by")
  private User lastEditedBy;

  @JsonProperty("in_trash")
  private boolean inTrash;
}

// https://developers.notion.com/reference/block
