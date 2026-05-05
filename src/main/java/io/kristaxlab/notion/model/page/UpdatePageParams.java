package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionPropertiesBuilder;
import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import java.util.*;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for {@code PATCH /pages/{page_id}}: property updates, icon, cover, trash, lock, etc.
 * Use {@link #builder()} for the request construction.
 */
@Getter
@Setter
public class UpdatePageParams {

  private Map<String, PageProperty> properties;

  private Icon icon;

  private Cover cover;

  private Boolean inTrash;

  private Boolean isLocked;

  private TemplateParams template;

  private Boolean eraseContent;

  public static Builder builder() {
    return new Builder();
  }

  /** Fluent builder (title, properties, trash, lock, icon, cover). */
  public static class Builder {

    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private Icon icon;
    private Cover cover;
    private Boolean inTrash;
    private Boolean isLocked;

    /** Sets the title property. */
    public Builder title(String text) {
      return property(NotionProperties.TITLE, NotionProperties.title(text));
    }

    /**
     * Sets an arbitrary property. Use as an escape hatch for property types not covered by the
     * named convenience methods above.
     */
    public Builder property(String name, PageProperty property) {
      this.properties.put(name, property);
      return this;
    }

    /**
     * Updates multiple properties with the {@link NotionPropertiesBuilder} DSL.
     *
     * <pre>{@code
     * .properties(p -> p
     *     .number("Priority", 7)
     *     .status("Status", "Done")
     *     .checked("Approved"))
     * }</pre>
     *
     * @param consumer callback used to populate properties
     * @return this builder
     */
    public Builder properties(Consumer<NotionPropertiesBuilder> consumer) {
      NotionPropertiesBuilder propertiesBuilder = NotionProperties.propertiesBuilder();
      consumer.accept(propertiesBuilder);
      this.properties.putAll(propertiesBuilder.build());
      return this;
    }

    /** Moves the page to the trash. */
    public Builder inTrash(boolean inTrash) {
      this.inTrash = inTrash;
      return this;
    }

    /** Locks or unlocks the page. */
    public Builder locked(boolean isLocked) {
      this.isLocked = isLocked;
      return this;
    }

    /** Sets the page icon. */
    public Builder icon(Icon icon) {
      this.icon = icon;
      return this;
    }

    /** Sets the page cover. */
    public Builder cover(Cover cover) {
      this.cover = cover;
      return this;
    }

    public UpdatePageParams build() {
      UpdatePageParams params = new UpdatePageParams();
      params.setProperties(properties.isEmpty() ? null : new LinkedHashMap<>(properties));
      params.setIcon(icon);
      params.setCover(cover);
      params.setInTrash(inTrash);
      params.setIsLocked(isLocked);
      return params;
    }
  }
}
