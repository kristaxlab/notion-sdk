package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.fluent.NotionBlocks;
import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.Position;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import java.util.*;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for {@link io.kristaxlab.notion.endpoints.PagesEndpoint#create(CreatePageParams)}.
 * Use {@link #builder()} for the request construction.
 */
@Getter
@Setter
public class CreatePageParams {

  private Parent parent;

  private Map<String, PageProperty> properties;

  private Icon icon;

  private Cover cover;

  private List<Block> children;

  private String markdown;

  private TemplateParams template;

  /** Optional placement for new content when the parent is a block (see Notion API). */
  private Position position;

  /** Shorthand for {@code builder().parent(parent).title(title).build()}. */
  public static CreatePageParams of(Parent parent, String title) {
    return CreatePageParams.builder().parent(parent).title(title).build();
  }

  /** Shorthand for a titled page with Markdown body content. */
  public static CreatePageParams of(Parent parent, String title, String markdown) {
    return CreatePageParams.builder().parent(parent).title(title).markdown(markdown).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  /** Fluent builder for {@link CreatePageParams}. */
  public static class Builder {

    private Parent parent;
    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private final List<Block> children = new ArrayList<>();
    private Icon icon;
    private Cover cover;
    private String markdown;
    private TemplateParams templateParams;

    /** Parent: data source (database) ID. */
    public Builder underDataSource(String dataSourceId) {
      return parent(Parent.dataSourceParent(dataSourceId));
    }

    /** Parent: existing page ID. */
    public Builder underPage(String pageId) {
      return parent(Parent.pageParent(pageId));
    }

    /**
     * Parent: workspace (top-level page). Availability depends on your Notion integration and API
     * version.
     */
    public Builder underWorkspace() {
      return parent(Parent.workspaceParent());
    }

    /** Sets a fully constructed {@link Parent} (overrides any prior {@code under*} call). */
    public Builder parent(Parent parent) {
      this.parent = parent;
      return this;
    }

    /** Sets the title property ({@link io.kristaxlab.notion.fluent.NotionProperties#TITLE}). */
    public Builder title(String text) {
      return property(NotionProperties.TITLE, NotionProperties.title(text));
    }

    /**
     * Sets a schema property by name. Use for types beyond {@link #title(String)} (e.g. select,
     * date).
     */
    public Builder property(String name, PageProperty property) {
      this.properties.put(name, property);
      return this;
    }

    /** Replaces body content with the given blocks. */
    public Builder children(Block... blocks) {
      return children(Arrays.asList(blocks));
    }

    /** Replaces body content with the given blocks. */
    public Builder children(List<Block> blocks) {
      this.children.addAll(new ArrayList<>(blocks));
      return this;
    }

    /** Builds body blocks with the {@link NotionBlocksBuilder} DSL. */
    public Builder children(Consumer<NotionBlocksBuilder> consumer) {
      NotionBlocksBuilder blocksBuilder = NotionBlocks.blocksBuilder();
      consumer.accept(blocksBuilder);
      this.children.addAll(blocksBuilder.build());
      return this;
    }

    /** Appends blocks from several lists (e.g. multiple {@code build()} calls). */
    public Builder childrenAll(List<List<Block>> blocksList) {
      for (List<Block> blocks : blocksList) {
        this.children.addAll(new ArrayList<>(blocks));
      }
      return this;
    }

    /** Applies a database template when creating in a data source. */
    public Builder template(TemplateParams templateParams) {
      this.templateParams = templateParams;
      return this;
    }

    /** Page body as Markdown (alternative to {@link #children}). */
    public Builder markdown(String markdown) {
      this.markdown = markdown;
      return this;
    }

    /**
     * Emoji or external URL string. Strings starting with {@code http://} or {@code https://} use
     * an external icon.
     */
    public Builder icon(String icon) {
      if (icon.startsWith("https://") || icon.startsWith("http://")) {
        this.icon = Icon.external(icon);
      }
      this.icon = Icon.emoji(icon);
      return this;
    }

    /** Sets the page icon. */
    public Builder icon(Icon icon) {
      this.icon = icon;
      return this;
    }

    /**
     * Cover image: UUID string is treated as a file upload ID; otherwise as an external image URL.
     */
    public Builder cover(String coverRef) {
      try {
        UUID.fromString(coverRef);
        this.cover = Cover.fileUpload(coverRef);
      } catch (IllegalArgumentException e) {
        // If the string is not a valid UUID, treat it as an external URL
        this.cover = Cover.external(coverRef);
      }
      return this;
    }

    /** Sets the page cover. */
    public Builder cover(Cover cover) {
      this.cover = cover;
      return this;
    }

    public CreatePageParams build() {
      CreatePageParams params = new CreatePageParams();
      params.setParent(parent);
      params.setProperties(properties.isEmpty() ? null : new LinkedHashMap<>(properties));
      params.setChildren(children.isEmpty() ? null : new ArrayList<>(children));
      params.setIcon(icon);
      params.setCover(cover);
      params.setMarkdown(markdown);
      params.setTemplate(templateParams);
      return params;
    }
  }
}
