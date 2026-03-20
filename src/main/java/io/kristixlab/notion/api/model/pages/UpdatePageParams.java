package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.CoverParams;
import io.kristixlab.notion.api.model.common.FileData;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.pages.properties.*;
import io.kristixlab.notion.api.model.pages.templates.TemplateParams;
import java.util.*;
import lombok.Data;

@Data
public class UpdatePageParams {

  @JsonProperty("properties")
  private Map<String, PageProperty> properties;

  @JsonProperty("icon")
  private IconParams icon;

  @JsonProperty("cover")
  private CoverParams cover;

  @JsonProperty("in_trash")
  private Boolean inTrash;

  @JsonProperty("is_locked")
  private Boolean isLocked;

  @JsonProperty("template")
  private TemplateParams template;

  @JsonProperty("erase_content")
  private Boolean eraseContent;

  public static Builder builder() {
    return new Builder();
  }

  /**
   * Fluent builder for {@link UpdatePageParams}.
   *
   * <p>Provides the same typed DSL as {@link CreatePageParams.Builder} for property values, plus
   * update-specific flags:
   *
   * <pre>{@code
   * UpdatePageParams params = UpdatePageParams.builder()
   *     .title("Updated entry")
   *     .number("Price", 59.99)
   *     .select("Category", "Books")
   *     .checkbox("Published", false)
   *     .email("Contact Email", "new@example.com")
   *     .url("Website", "https://updated.example.com")
   *     .build();
   * }</pre>
   */
  public static class Builder {

    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private IconParams icon;
    private CoverParams cover;
    private Boolean inTrash;
    private Boolean isLocked;

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

    /** Sets a {@code place} property by location name and coordinates. */
    public Builder place(String name, String locationName, double lat, double lon) {
      return property(name, PlaceProperty.of(locationName, lat, lon));
    }

    /** Sets a {@code place} property by location name and address. */
    public Builder place(String name, String locationName, String address) {
      return property(name, PlaceProperty.of(locationName, address));
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

    // ── Flags ────────────────────────────────────────────────────────────

    /** Moves the page to the trash. */
    public Builder inTrash(boolean inTrash) {
      this.inTrash = inTrash;
      return this;
    }

    /** Locks or unlocks the page. */
    public Builder isLocked(boolean isLocked) {
      this.isLocked = isLocked;
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
