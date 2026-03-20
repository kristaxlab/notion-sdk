package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.common.*;
import io.kristixlab.notion.api.model.pages.properties.*;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.*;
import lombok.Data;

@Data
public class CreatePageParams {

  @JsonProperty("parent")
  private Parent parent;

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  // TODO
  @JsonProperty("content")
  private List<Block> content;

  // TODO
  @JsonProperty("children")
  private List<Block> children;

  @JsonProperty("markdown")
  private String markdown;

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("position")
  private Position position;

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Fluent builder for {@link CreatePageParams}.
   *
   * <p>Provides typed DSL methods for every common page property so that page creation reads like a
   * declarative spec rather than a sequence of map operations:
   *
   * <pre>{@code
   * CreatePageParams params = CreatePageParams.builder()
   *     .inDatasource(dsId)
   *     .title("Full entry")
   *     .richText("Description", "A detailed description")
   *     .number("Price", 49.99)
   *     .select("Category", "Electronics")
   *     .multiSelect("Tags", SelectValue.of("Sale"), SelectValue.of("New"))
   *     .date("Due Date", "2026-04-01")
   *     .checkbox("Published", true)
   *     .email("Contact Email", "hi@example.com")
   *     .phone("Contact Phone", "+1234567890")
   *     .url("Website", "https://example.com")
   *     .children(ParagraphBlock.of("Hello"), ParagraphBlock.of("World"))
   *     .build();
   * }</pre>
   */
  public static class Builder {

    private Parent parent;
    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private List<Block> children;
    private IconParams icon;
    private CoverParams cover;

    // ── Parent ───────────────────────────────────────────────────────────

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

    // ── Properties ───────────────────────────────────────────────────────

    /** Sets the title property. */
    public Builder title(String text) {
      return property(TitleProperty.NAME, TitleProperty.of(text));
    }

    /** Sets a {@code rich_text} property. */
    public Builder text(String name, String text) {
      return property(name, RichTextProperty.of(text));
    }

    /** Sets a {@code number} property. */
    public Builder number(String name, Number value) {
      return property(name, NumberProperty.of(value));
    }

    /** Sets a {@code select} property by option name. */
    public Builder select(String name, String value) {
      return property(name, SelectProperty.of(value));
    }

    /** Sets a {@code multi_select} property from the given {@link SelectValue} entries. */
    public Builder multiSelect(String name, SelectValue... values) {
      return property(name, MultiSelectProperty.of(values));
    }

    /** Sets a {@code date} property from an ISO-8601 date string (e.g. {@code "2026-04-01"}). */
    public Builder date(String name, String isoDate) {
      return property(name, DateProperty.of(isoDate));
    }

    /** Sets a {@code checkbox} property. */
    public Builder checkbox(String name, boolean checked) {
      return property(name, checked ? CheckboxProperty.checked() : CheckboxProperty.unchecked());
    }

    /** Sets an {@code email} property. */
    public Builder email(String name, String email) {
      return property(name, EmailProperty.of(email));
    }

    /** Sets a {@code phone_number} property. */
    public Builder phone(String name, String phone) {
      return property(name, PhoneNumberProperty.of(phone));
    }

    /** Sets a {@code url} property. */
    public Builder url(String name, String url) {
      return property(name, UrlProperty.of(url));
    }

    /** Sets a {@code people} property from one or more Notion user IDs. */
    public Builder people(String name, String... userIds) {
      return property(name, PeopleProperty.of(userIds));
    }

    /** Sets a {@code files} property from one or more {@link FileData} entries. */
    public Builder files(String name, FileData... files) {
      return property(name, FilesProperty.of(files));
    }

    /** Sets a {@code relation} property from one or more related page IDs. */
    public Builder relation(String name, String... pageIds) {
      return property(name, RelationProperty.of(pageIds));
    }

    /** Sets a {@code status} property by option name. */
    public Builder status(String name, String optionName) {
      return property(name, StatusProperty.of(optionName));
    }

    /**
     * Sets an arbitrary property. Use as an escape hatch for property types not covered by the
     * named convenience methods above.
     */
    public Builder property(String name, PageProperty property) {
      this.properties.put(name, property);
      return this;
    }

    // ── Content ──────────────────────────────────────────────────────────

    /** Sets the page body blocks (varargs convenience overload). */
    public Builder children(Block... blocks) {
      return children(Arrays.asList(blocks));
    }

    /** Sets the page body blocks from a list. */
    public Builder children(List<Block> blocks) {
      this.children = new ArrayList<>(blocks);
      return this;
    }

    // ── Decoration ───────────────────────────────────────────────────────

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

    // ── Build ────────────────────────────────────────────────────────────

    public CreatePageParams build() {
      CreatePageParams params = new CreatePageParams();
      params.setParent(parent);
      params.setProperties(properties.isEmpty() ? null : new LinkedHashMap<>(properties));
      params.setChildren(children);
      params.setIcon(icon);
      params.setCover(cover);
      return params;
    }
  }
}
