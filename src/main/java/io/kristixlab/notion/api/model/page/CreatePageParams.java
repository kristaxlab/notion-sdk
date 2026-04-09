package io.kristixlab.notion.api.model.page;

import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.helper.BlocksBuilder;
import io.kristixlab.notion.api.model.page.property.*;
import io.kristixlab.notion.api.model.page.templates.TemplateParams;
import java.util.*;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePageParams {

  private Parent parent;

  private Map<String, PageProperty> properties;

  private IconParams icon;

  private CoverParams cover;

  private List<Block> children;

  private String markdown;

  private TemplateParams template;

  private Position position;

  public static CreatePageParams of(Parent parent, String title) {
    return CreatePageParams.builder().parent(parent).title(title).build();
  }

  public static CreatePageParams of(Parent parent, String title, String markdown) {
    return CreatePageParams.builder().parent(parent).title(title).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Parent parent;
    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private List<Block> children = new ArrayList<>();
    private IconParams icon;
    private CoverParams cover;
    private String markdown;
    private TemplateParams templateParams;

    /** Sets the parent to the data source with the given ID. */
    public Builder inDatasource(String datasourceId) {
      return parent(Parent.datasourceParent(datasourceId));
    }

    /** Sets the parent to the page with the given ID. */
    public Builder inPage(String pageId) {
      return parent(Parent.pageParent(pageId));
    }

    /** Sets a fully constructed {@link Parent}. */
    public Builder parent(Parent parent) {
      this.parent = parent;
      return this;
    }

    /** Sets the title property. */
    public Builder title(String text) {
      return property(TitleProperty.NAME, TitleProperty.of(text));
    }

    /**
     * Sets an arbitrary property. Use as an escape hatch for property types not covered by the
     * named convenience methods above.
     */
    public Builder property(String name, PageProperty property) {
      this.properties.put(name, property);
      return this;
    }

    /** Sets the page body blocks (varargs convenience overload). */
    public Builder children(Block... blocks) {
      return children(Arrays.asList(blocks));
    }

    /** Sets the page body blocks from a list. */
    public Builder children(List<Block> blocks) {
      this.children.addAll(new ArrayList<>(blocks));
      return this;
    }

    /** Sets the page body blocks from a list. */
    public Builder children(Consumer<BlocksBuilder> consumer) {
      BlocksBuilder blocksBuilder = BlocksBuilder.of();
      consumer.accept(blocksBuilder);
      this.children.addAll(blocksBuilder.build());
      return this;
    }

    public Builder template(TemplateParams templateParams) {
      this.templateParams = templateParams;
      return this;
    }

    public Builder markdown(String markdown) {
      this.markdown = markdown;
      return this;
    }

    /** Sets the page icon. */
    public Builder icon(IconParams icon) {
      this.icon = icon;
      return this;
    }

    /** Sets the page cover. */
    public Builder cover(CoverParams cover) {
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
