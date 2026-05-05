package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.common.DateData;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PlaceProperty;
import io.kristaxlab.notion.model.page.property.SelectValue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Fluent builder for assembling a {@code property name -> value} map for create / update page
 * payloads.
 *
 * <p>Each method delegates to the static factories in {@link NotionProperties} and adds the
 * resulting {@link PageProperty} under the provided property name. Call {@link #build()} to get the
 * accumulated map, or pass the builder via the {@code properties(Consumer)} hook on the page
 * request builders.
 *
 * <pre>{@code
 * Map<String, PageProperty> props = NotionProperties.propertiesBuilder()
 *     .title("Build a SaaS")
 *     .number("Priority", 5)
 *     .select("Status", "In progress")
 *     .build();
 * }</pre>
 */
public class NotionPropertiesBuilder {

  private final Map<String, PageProperty> properties = new LinkedHashMap<>();

  protected NotionPropertiesBuilder() {}

  /**
   * Builds and returns the accumulated properties.
   *
   * @return a copy of the property map preserving insertion order
   */
  public Map<String, PageProperty> build() {
    return new LinkedHashMap<>(properties);
  }

  /**
   * Adds a pre-built property under the given name. Use this escape hatch when shorthand methods
   * don't cover your use case.
   *
   * @param name schema property name (or id)
   * @param property property payload
   * @return this builder
   */
  public NotionPropertiesBuilder property(String name, PageProperty property) {
    properties.put(name, property);
    return this;
  }

  /**
   * Adds all properties from a pre-built map.
   *
   * @param values name -> property mapping
   * @return this builder
   */
  public NotionPropertiesBuilder properties(Map<String, ? extends PageProperty> values) {
    properties.putAll(values);
    return this;
  }

  /**
   * Removes a property from the map.
   *
   * @param name schema property name to remove
   * @return this builder
   */
  public NotionPropertiesBuilder clear(String name) {
    properties.remove(name);
    return this;
  }

  // Title

  /**
   * Sets the page title under Notion's special {@code "title"} key. Use this when the parent is a
   * page or when the data source's title column is available under the conventional key.
   *
   * @param text title text
   * @return this builder
   */
  public NotionPropertiesBuilder title(String text) {
    return title(NotionProperties.TITLE, text);
  }

  /**
   * Sets a title property by explicit name.
   *
   * @param name schema property name
   * @param text title text
   * @return this builder
   */
  public NotionPropertiesBuilder title(String name, String text) {
    return property(name, NotionProperties.title(text));
  }

  /**
   * Sets a title property by explicit name from rich text fragments.
   *
   * @param name schema property name
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder title(String name, RichText... richTexts) {
    return property(name, NotionProperties.title(richTexts));
  }

  /**
   * Sets a title property by explicit name from a list of rich text fragments.
   *
   * @param name schema property name
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder title(String name, List<RichText> richTexts) {
    return property(name, NotionProperties.title(richTexts));
  }

  /**
   * Sets a title property by explicit name using the rich text DSL.
   *
   * @param name schema property name
   * @param consumer rich text builder configurator
   * @return this builder
   */
  public NotionPropertiesBuilder title(String name, Consumer<NotionTextBuilder> consumer) {
    return property(name, NotionProperties.title(consumer));
  }

  // Rich text

  /**
   * Sets a rich-text property from plain text.
   *
   * @param name schema property name
   * @param text plain text content
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String name, String text) {
    return property(name, NotionProperties.richText(text));
  }

  /**
   * Sets a rich-text property from rich text fragments.
   *
   * @param name schema property name
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String name, RichText... richTexts) {
    return property(name, NotionProperties.richText(richTexts));
  }

  /**
   * Sets a rich-text property from a list of rich text fragments.
   *
   * @param name schema property name
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String name, List<RichText> richTexts) {
    return property(name, NotionProperties.richText(richTexts));
  }

  /**
   * Sets a rich-text property using the rich text DSL.
   *
   * @param name schema property name
   * @param consumer rich text builder configurator
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String name, Consumer<NotionTextBuilder> consumer) {
    return property(name, NotionProperties.richText(consumer));
  }

  // Number

  /**
   * Sets a number property.
   *
   * @param name schema property name
   * @param value numeric value
   * @return this builder
   */
  public NotionPropertiesBuilder number(String name, Number value) {
    return property(name, NotionProperties.number(value));
  }

  // Select

  /**
   * Sets a select property by option name.
   *
   * @param name schema property name
   * @param optionName option name in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder select(String name, String optionName) {
    return property(name, NotionProperties.select(optionName));
  }

  /**
   * Sets a select property from a fully prepared {@link SelectValue}.
   *
   * @param name schema property name
   * @param value option payload
   * @return this builder
   */
  public NotionPropertiesBuilder select(String name, SelectValue value) {
    return property(name, NotionProperties.select(value));
  }

  // Multi-select

  /**
   * Sets a multi-select property from option names.
   *
   * @param name schema property name
   * @param optionNames option names in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder multiSelect(String name, String... optionNames) {
    return property(name, NotionProperties.multiSelect(optionNames));
  }

  /**
   * Sets a multi-select property from a list of option names.
   *
   * @param name schema property name
   * @param optionNames option names in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder multiSelect(String name, List<String> optionNames) {
    return property(name, NotionProperties.multiSelect(optionNames));
  }

  // Date

  /**
   * Sets a single-day date property.
   *
   * @param name schema property name
   * @param date date value
   * @return this builder
   */
  public NotionPropertiesBuilder date(String name, LocalDate date) {
    return property(name, NotionProperties.date(date));
  }

  /**
   * Sets a date-time property.
   *
   * @param name schema property name
   * @param dateTime date-time value
   * @return this builder
   */
  public NotionPropertiesBuilder date(String name, LocalDateTime dateTime) {
    return property(name, NotionProperties.date(dateTime));
  }

  /**
   * Sets a date property from an ISO 8601 string.
   *
   * @param name schema property name
   * @param iso8601 ISO 8601 date or date-time
   * @return this builder
   */
  public NotionPropertiesBuilder date(String name, String iso8601) {
    return property(name, NotionProperties.date(iso8601));
  }

  /**
   * Sets a date property from a fully prepared {@link DateData} payload.
   *
   * @param name schema property name
   * @param dateData date payload
   * @return this builder
   */
  public NotionPropertiesBuilder date(String name, DateData dateData) {
    return property(name, NotionProperties.date(dateData));
  }

  /**
   * Sets a date-range property from {@link LocalDate} bounds.
   *
   * @param name schema property name
   * @param start range start
   * @param end range end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String name, LocalDate start, LocalDate end) {
    return property(name, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from {@link LocalDateTime} bounds.
   *
   * @param name schema property name
   * @param start range start
   * @param end range end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String name, LocalDateTime start, LocalDateTime end) {
    return property(name, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from ISO 8601 string bounds.
   *
   * @param name schema property name
   * @param start ISO 8601 start
   * @param end ISO 8601 end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String name, String start, String end) {
    return property(name, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from ISO 8601 string bounds and a time zone.
   *
   * @param name schema property name
   * @param start ISO 8601 start
   * @param end ISO 8601 end
   * @param timeZone IANA time zone
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String name, String start, String end, String timeZone) {
    return property(name, NotionProperties.dateRange(start, end, timeZone));
  }

  // Checkbox

  /**
   * Sets a checkbox property.
   *
   * @param name schema property name
   * @param checked whether the checkbox is checked
   * @return this builder
   */
  public NotionPropertiesBuilder checkbox(String name, boolean checked) {
    return property(name, NotionProperties.checkbox(checked));
  }

  /**
   * Sets a checkbox property to checked.
   *
   * @param name schema property name
   * @return this builder
   */
  public NotionPropertiesBuilder checked(String name) {
    return property(name, NotionProperties.checked());
  }

  /**
   * Sets a checkbox property to unchecked.
   *
   * @param name schema property name
   * @return this builder
   */
  public NotionPropertiesBuilder unchecked(String name) {
    return property(name, NotionProperties.unchecked());
  }

  // URL, email, phone

  /**
   * Sets a URL property.
   *
   * @param name schema property name
   * @param url URL value
   * @return this builder
   */
  public NotionPropertiesBuilder url(String name, String url) {
    return property(name, NotionProperties.url(url));
  }

  /**
   * Sets an email property.
   *
   * @param name schema property name
   * @param email email address
   * @return this builder
   */
  public NotionPropertiesBuilder email(String name, String email) {
    return property(name, NotionProperties.email(email));
  }

  /**
   * Sets a phone-number property.
   *
   * @param name schema property name
   * @param phoneNumber phone number
   * @return this builder
   */
  public NotionPropertiesBuilder phoneNumber(String name, String phoneNumber) {
    return property(name, NotionProperties.phoneNumber(phoneNumber));
  }

  // People

  /**
   * Sets a people property from one or more user ids.
   *
   * @param name schema property name
   * @param userIds Notion user ids
   * @return this builder
   */
  public NotionPropertiesBuilder people(String name, String... userIds) {
    return property(name, NotionProperties.people(userIds));
  }

  /**
   * Sets a people property from a list of user ids.
   *
   * @param name schema property name
   * @param userIds Notion user ids
   * @return this builder
   */
  public NotionPropertiesBuilder people(String name, List<String> userIds) {
    return property(name, NotionProperties.people(userIds));
  }

  // Files

  /**
   * Sets a files property.
   *
   * @param name schema property name
   * @param files file payloads
   * @return this builder
   */
  public NotionPropertiesBuilder files(String name, FileData... files) {
    return property(name, NotionProperties.files(files));
  }

  /**
   * Sets a files property.
   *
   * @param name schema property name
   * @param files file payloads
   * @return this builder
   */
  public NotionPropertiesBuilder files(String name, List<FileData> files) {
    return property(name, NotionProperties.files(files));
  }

  // Relation

  /**
   * Sets a relation property from one or more related page ids.
   *
   * @param name schema property name
   * @param pageIds related page ids
   * @return this builder
   */
  public NotionPropertiesBuilder relation(String name, String... pageIds) {
    return property(name, NotionProperties.relation(pageIds));
  }

  /**
   * Sets a relation property from a list of related page ids.
   *
   * @param name schema property name
   * @param pageIds related page ids
   * @return this builder
   */
  public NotionPropertiesBuilder relation(String name, List<String> pageIds) {
    return property(name, NotionProperties.relation(pageIds));
  }

  // Status

  /**
   * Sets a status property by option name.
   *
   * @param name schema property name
   * @param optionName option name in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder status(String name, String optionName) {
    return property(name, NotionProperties.status(optionName));
  }

  // Place

  /**
   * Sets a place property with latitude and longitude only.
   *
   * @param name schema property name
   * @param lat latitude
   * @param lon longitude
   * @return this builder
   */
  public NotionPropertiesBuilder place(String name, double lat, double lon) {
    return property(name, NotionProperties.place(lat, lon));
  }

  /**
   * Sets a place property with latitude, longitude and a display name.
   *
   * @param name schema property name
   * @param lat latitude
   * @param lon longitude
   * @param displayName display name for the location
   * @return this builder
   */
  public NotionPropertiesBuilder place(String name, double lat, double lon, String displayName) {
    return property(name, NotionProperties.place(lat, lon, displayName));
  }

  /**
   * Sets a place property by configuring its {@link PlaceProperty.Place} payload.
   *
   * @param name schema property name
   * @param consumer callback that mutates the place payload
   * @return this builder
   */
  public NotionPropertiesBuilder place(String name, Consumer<PlaceProperty.Place> consumer) {
    return property(name, NotionProperties.place(consumer));
  }
}
