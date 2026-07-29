package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.fluent.NotionProperties;
import io.kristaxlab.notion.fluent.NotionPropertiesBuilder;
import io.kristaxlab.notion.model.common.Cover;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.templates.TemplateParams;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
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
    private TemplateParams template;
    private Boolean eraseContent;

    /** Sets the title property. */
    public Builder title(String text) {
      return property(NotionProperties.TITLE, NotionProperties.title(text));
    }

    /**
     * Sets an arbitrary property under the given key. Use as an escape hatch for property types not
     * covered by the named convenience methods above.
     *
     * <p>The key may be either the schema property name (e.g. {@code "Status"}) or its id (e.g.
     * {@code "%5B%3DZf"}); the Notion API accepts both interchangeably.
     *
     * @param nameOrId schema property name or id
     * @param property property payload
     * @return this builder
     */
    public Builder property(String nameOrId, PageProperty property) {
      this.properties.put(nameOrId, property);
      return this;
    }

    public Builder properties(Map<String, PageProperty> properties) {
      this.properties.putAll(properties);
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
      NotionPropertiesBuilder propertiesBuilder = NotionProperties.builder();
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

    /** Sets the page icon using an emoji. */
    public Builder icon(String emoji) {
      this.icon = new Icon();
      this.icon.setEmoji(emoji);
      return this;
    }

    /** Sets the page cover. */
    public Builder cover(Cover cover) {
      this.cover = cover;
      return this;
    }

    /**
     * Cover image: UUID string is treated as a file upload ID; otherwise as an external image URL.
     *
     * @param fileUploadId file upload id or external image URL
     * @return this builder
     */
    public Builder cover(String fileUploadId) {
      try {
        UUID.fromString(fileUploadId);
        this.cover = Cover.fileUpload(fileUploadId);
      } catch (IllegalArgumentException e) {
        // If the string is not a valid UUID, treat it as an external URL
        this.cover = Cover.external(fileUploadId);
      }
      return this;
    }

    /**
     * Applies a data source template to the page. By default Notion appends the template content;
     * use {@link #eraseContent(boolean)} to replace existing content instead.
     *
     * @param template template descriptor ({@code default} or {@code template_id})
     * @return this builder
     */
    public Builder template(TemplateParams template) {
      this.template = template;
      return this;
    }

    /**
     * When {@code true}, existing page content is erased before the template is applied (replace
     * mode). When {@code false} or omitted, template content is appended.
     *
     * @param eraseContent whether to erase existing content
     * @return this builder
     */
    public Builder eraseContent(boolean eraseContent) {
      this.eraseContent = eraseContent;
      return this;
    }

    public UpdatePageParams build() {
      UpdatePageParams params = new UpdatePageParams();
      params.setProperties(properties.isEmpty() ? null : new LinkedHashMap<>(properties));
      params.setIcon(icon);
      params.setCover(cover);
      params.setInTrash(inTrash);
      params.setIsLocked(isLocked);
      params.setTemplate(template);
      params.setEraseContent(eraseContent);
      return params;
    }
  }
}
