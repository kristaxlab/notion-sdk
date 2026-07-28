package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.common.DateData;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.page.property.PageProperty;
import io.kristaxlab.notion.model.page.property.PlaceProperty;
import io.kristaxlab.notion.model.page.property.SelectValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Fluent builder for assembling a {@code property key -> value} map for create / update page
 * payloads.
 *
 * <p>The Notion API treats <b>property names and property ids interchangeably</b> as map keys, so
 * every method on this builder accepts a {@code nameOrId} string — either the schema column name
 * (e.g. {@code "Status"}) or its stable id (e.g. {@code "%5B%3DZf"}). Names are easier to write,
 * ids survive renames; pick whichever fits your use case.
 *
 * <p>Each method delegates to the matching static factory in {@link NotionProperties} and adds the
 * resulting {@link PageProperty} under the supplied key. Call {@link #build()} for the accumulated
 * map, or pass the builder via the {@code properties(Consumer)} hook on the page request builders.
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

  protected NotionPropertiesBuilder() {
  }

  /**
   * Builds and returns the accumulated properties.
   *
   * @return a copy of the property map preserving insertion order
   */
  public Map<String, PageProperty> build() {
    return new LinkedHashMap<>(properties);
  }

  /**
   * Adds a pre-built property under the given key. Use this escape hatch when shorthand methods
   * don't cover your use case.
   *
   * @param nameOrId schema property name or id
   * @param property property payload
   * @return this builder
   */
  public NotionPropertiesBuilder property(String nameOrId, PageProperty property) {
    properties.put(nameOrId, property);
    return this;
  }

  /**
   * Adds all properties from a pre-built map. Keys are passed through as-is and may be either names
   * or ids.
   *
   * @param values name-or-id to property mapping
   * @return this builder
   */
  public NotionPropertiesBuilder properties(Map<String, ? extends PageProperty> values) {
    properties.putAll(values);
    return this;
  }

  /**
   * Removes a previously added property from the map.
   *
   * @param nameOrId schema property name or id to remove
   * @return this builder
   */
  public NotionPropertiesBuilder remove(String nameOrId) {
    properties.remove(nameOrId);
    return this;
  }

  // Title

  /**
   * Sets the page title under Notion's special {@code "title"} key. Use this when the parent is a
   * page or when the data source's title column is reachable via the conventional key.
   *
   * @param text title text
   * @return this builder
   */
  public NotionPropertiesBuilder title(String text) {
    return title(NotionProperties.TITLE, text);
  }

  /**
   * Sets a title property by explicit key.
   *
   * @param nameOrId schema property name or id
   * @param text     title text
   * @return this builder
   */
  public NotionPropertiesBuilder title(String nameOrId, String text) {
    return property(nameOrId, NotionProperties.title(text));
  }

  /**
   * Sets a title property by explicit key from rich text fragments.
   *
   * @param nameOrId  schema property name or id
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder title(String nameOrId, RichText... richTexts) {
    return property(nameOrId, NotionProperties.title(richTexts));
  }

  /**
   * Sets a title property by explicit key from a list of rich text fragments.
   *
   * @param nameOrId  schema property name or id
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder title(String nameOrId, List<RichText> richTexts) {
    return property(nameOrId, NotionProperties.title(richTexts));
  }

  /**
   * Sets a title property by explicit key using the rich text DSL.
   *
   * @param nameOrId schema property name or id
   * @param consumer rich text builder configurator
   * @return this builder
   */
  public NotionPropertiesBuilder title(String nameOrId, Consumer<NotionTextBuilder> consumer) {
    return property(nameOrId, NotionProperties.title(consumer));
  }

  public NotionPropertiesBuilder clearTitle(String nameOrId) {
    return property(nameOrId, NotionProperties.title(Collections.emptyList()));
  }

  // Rich text

  /**
   * Sets a rich-text property from plain text.
   *
   * @param nameOrId schema property name or id
   * @param text     plain text content
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String nameOrId, String text) {
    return property(nameOrId, NotionProperties.richText(text));
  }

  /**
   * Sets a rich-text property from rich text fragments.
   *
   * @param nameOrId  schema property name or id
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String nameOrId, RichText... richTexts) {
    return property(nameOrId, NotionProperties.richText(richTexts));
  }

  /**
   * Sets a rich-text property from a list of rich text fragments.
   *
   * @param nameOrId  schema property name or id
   * @param richTexts rich text fragments
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String nameOrId, List<RichText> richTexts) {
    return property(nameOrId, NotionProperties.richText(richTexts));
  }

  /**
   * Sets a rich-text property using the rich text DSL.
   *
   * @param nameOrId schema property name or id
   * @param consumer rich text builder configurator
   * @return this builder
   */
  public NotionPropertiesBuilder richText(String nameOrId, Consumer<NotionTextBuilder> consumer) {
    return property(nameOrId, NotionProperties.richText(consumer));
  }

  public NotionPropertiesBuilder clearRichText(String nameOrId) {
    return property(nameOrId, NotionProperties.richText(Collections.emptyList()));
  }

  // Number

  /**
   * Sets a number property.
   *
   * @param nameOrId schema property name or id
   * @param value    numeric value
   * @return this builder
   */
  public NotionPropertiesBuilder number(String nameOrId, Number value) {
    return property(nameOrId, NotionProperties.number(value));
  }

  public NotionPropertiesBuilder clearNumber(String nameOrId) {
    return property(nameOrId, NotionProperties.number(null));
  }

  // Select

  /**
   * Sets a select property by option name.
   *
   * @param nameOrId   schema property name or id
   * @param optionName option name in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder select(String nameOrId, String optionName) {
    return property(nameOrId, NotionProperties.select(optionName));
  }

  /**
   * Sets a select property from a fully prepared {@link SelectValue}.
   *
   * @param nameOrId schema property name or id
   * @param value    option payload
   * @return this builder
   */
  public NotionPropertiesBuilder select(String nameOrId, SelectValue value) {
    return property(nameOrId, NotionProperties.select(value));
  }

  public NotionPropertiesBuilder clearSelect(String nameOrId) {
    return property(nameOrId, NotionProperties.select((SelectValue) null));
  }

  // Multi-select

  /**
   * Sets a multi-select property from option names.
   *
   * @param nameOrId    schema property name or id
   * @param optionNames option names in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder multiSelect(String nameOrId, String... optionNames) {
    return property(nameOrId, NotionProperties.multiSelect(optionNames));
  }

  /**
   * Sets a multi-select property from a list of option names.
   *
   * @param nameOrId    schema property name or id
   * @param optionNames option names in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder multiSelect(String nameOrId, List<String> optionNames) {
    return property(nameOrId, NotionProperties.multiSelect(optionNames));
  }

  public NotionPropertiesBuilder clearMultiSelect(String nameOrId) {
    return property(nameOrId, NotionProperties.multiSelect(Collections.emptyList()));
  }

  // Date

  /**
   * Sets a single-day date property.
   *
   * @param nameOrId schema property name or id
   * @param date     date value
   * @return this builder
   */
  public NotionPropertiesBuilder date(String nameOrId, LocalDate date) {
    return property(nameOrId, NotionProperties.date(date));
  }

  /**
   * Sets a date-time property.
   *
   * @param nameOrId schema property name or id
   * @param dateTime date-time value
   * @return this builder
   */
  public NotionPropertiesBuilder date(String nameOrId, LocalDateTime dateTime) {
    return property(nameOrId, NotionProperties.date(dateTime));
  }

  /**
   * Sets a date property from an ISO 8601 string.
   *
   * @param nameOrId schema property name or id
   * @param iso8601  ISO 8601 date or date-time
   * @return this builder
   */
  public NotionPropertiesBuilder date(String nameOrId, String iso8601) {
    return property(nameOrId, NotionProperties.date(iso8601));
  }

  /**
   * Sets a date property from a fully prepared {@link DateData} payload.
   *
   * @param nameOrId schema property name or id
   * @param dateData date payload
   * @return this builder
   */
  public NotionPropertiesBuilder date(String nameOrId, DateData dateData) {
    return property(nameOrId, NotionProperties.date(dateData));
  }

  /**
   * Sets a date-range property from {@link LocalDate} bounds.
   *
   * @param nameOrId schema property name or id
   * @param start    range start
   * @param end      range end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String nameOrId, LocalDate start, LocalDate end) {
    return property(nameOrId, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from {@link LocalDateTime} bounds.
   *
   * @param nameOrId schema property name or id
   * @param start    range start
   * @param end      range end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(
          String nameOrId, LocalDateTime start, LocalDateTime end) {
    return property(nameOrId, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from ISO 8601 string bounds.
   *
   * @param nameOrId schema property name or id
   * @param start    ISO 8601 start
   * @param end      ISO 8601 end
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(String nameOrId, String start, String end) {
    return property(nameOrId, NotionProperties.dateRange(start, end));
  }

  /**
   * Sets a date-range property from ISO 8601 string bounds and a time zone.
   *
   * @param nameOrId schema property name or id
   * @param start    ISO 8601 start
   * @param end      ISO 8601 end
   * @param timeZone IANA time zone
   * @return this builder
   */
  public NotionPropertiesBuilder dateRange(
          String nameOrId, String start, String end, String timeZone) {
    return property(nameOrId, NotionProperties.dateRange(start, end, timeZone));
  }

  public NotionPropertiesBuilder clearDate(String nameOrId) {
    return property(nameOrId, NotionProperties.date((DateData) null));
  }

  // Checkbox

  /**
   * Sets a checkbox property.
   *
   * @param nameOrId schema property name or id
   * @param checked  whether the checkbox is checked
   * @return this builder
   */
  public NotionPropertiesBuilder checkbox(String nameOrId, boolean checked) {
    return property(nameOrId, NotionProperties.checkbox(checked));
  }

  public NotionPropertiesBuilder clearCheckbox(String nameOrId) {
    return property(nameOrId, NotionProperties.checkbox(false));
  }

  /**
   * Sets a checkbox property to checked.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionPropertiesBuilder checked(String nameOrId) {
    return property(nameOrId, NotionProperties.checked());
  }

  /**
   * Sets a checkbox property to unchecked.
   *
   * @param nameOrId schema property name or id
   * @return this builder
   */
  public NotionPropertiesBuilder unchecked(String nameOrId) {
    return property(nameOrId, NotionProperties.unchecked());
  }

  // URL, email, phone

  /**
   * Sets a URL property.
   *
   * @param nameOrId schema property name or id
   * @param url      URL value
   * @return this builder
   */
  public NotionPropertiesBuilder url(String nameOrId, String url) {
    return property(nameOrId, NotionProperties.url(url));
  }

  public NotionPropertiesBuilder clearUrl(String nameOrId) {
    return property(nameOrId, NotionProperties.url((String) null));
  }

  /**
   * Sets an email property.
   *
   * @param nameOrId schema property name or id
   * @param email    email address
   * @return this builder
   */
  public NotionPropertiesBuilder email(String nameOrId, String email) {
    return property(nameOrId, NotionProperties.email(email));
  }

  public NotionPropertiesBuilder clearEmail(String nameOrId) {
    return property(nameOrId, NotionProperties.email((String) null));
  }

  /**
   * Sets a phone-number property.
   *
   * @param nameOrId    schema property name or id
   * @param phoneNumber phone number
   * @return this builder
   */
  public NotionPropertiesBuilder phoneNumber(String nameOrId, String phoneNumber) {
    return property(nameOrId, NotionProperties.phoneNumber(phoneNumber));
  }

  public NotionPropertiesBuilder clearPhoneNumber(String nameOrId) {
    return property(nameOrId, NotionProperties.phoneNumber((String) null));
  }

  // People

  /**
   * Sets a people property from one or more user ids.
   *
   * @param nameOrId schema property name or id
   * @param userIds  Notion user ids
   * @return this builder
   */
  public NotionPropertiesBuilder people(String nameOrId, String... userIds) {
    return property(nameOrId, NotionProperties.people(userIds));
  }

  /**
   * Sets a people property from a list of user ids.
   *
   * @param nameOrId schema property name or id
   * @param userIds  Notion user ids
   * @return this builder
   */
  public NotionPropertiesBuilder people(String nameOrId, List<String> userIds) {
    return property(nameOrId, NotionProperties.people(userIds));
  }

  public NotionPropertiesBuilder clearPeople(String nameOrId) {
    return property(nameOrId, NotionProperties.people(Collections.emptyList()));
  }

  // Files

  /**
   * Sets a files property.
   *
   * @param nameOrId schema property name or id
   * @param files    file payloads
   * @return this builder
   */
  public NotionPropertiesBuilder files(String nameOrId, FileData... files) {
    return property(nameOrId, NotionProperties.files(files));
  }

  /**
   * Sets a files property.
   *
   * @param nameOrId schema property name or id
   * @param files    file payloads
   * @return this builder
   */
  public NotionPropertiesBuilder files(String nameOrId, List<FileData> files) {
    return property(nameOrId, NotionProperties.files(files));
  }

  public NotionPropertiesBuilder clearFiles(String nameOrId) {
    return property(nameOrId, NotionProperties.files(Collections.emptyList()));
  }

  // Relation

  /**
   * Sets a relation property from one or more related page ids.
   *
   * @param nameOrId schema property name or id
   * @param pageIds  related page ids
   * @return this builder
   */
  public NotionPropertiesBuilder relation(String nameOrId, String... pageIds) {
    return property(nameOrId, NotionProperties.relation(pageIds));
  }

  /**
   * Sets a relation property from a list of related page ids.
   *
   * @param nameOrId schema property name or id
   * @param pageIds  related page ids
   * @return this builder
   */
  public NotionPropertiesBuilder relation(String nameOrId, List<String> pageIds) {
    return property(nameOrId, NotionProperties.relation(pageIds));
  }

  public NotionPropertiesBuilder clearRelation(String nameOrId) {
    return property(nameOrId, NotionProperties.relation(Collections.emptyList()));
  }

  // Status

  // there is no clearStatus method as it is not supported. Status cannot be cleared via
  // the API — it can only be set to one of the existing options in your database.
  // This is consistent with how Notion's UI works (status always has a value).

  /**
   * Sets a status property by option name.
   *
   * @param nameOrId   schema property name or id
   * @param optionName option name in the schema
   * @return this builder
   */
  public NotionPropertiesBuilder status(String nameOrId, String optionName) {
    return property(nameOrId, NotionProperties.status(optionName));
  }

  // Place

  /**
   * Sets a place property with latitude and longitude only.
   *
   * @param nameOrId schema property name or id
   * @param lat      latitude
   * @param lon      longitude
   * @return this builder
   */
  public NotionPropertiesBuilder place(String nameOrId, double lat, double lon) {
    return property(nameOrId, NotionProperties.place(lat, lon));
  }

  /**
   * Sets a place property with latitude, longitude and a display name.
   *
   * @param nameOrId    schema property name or id
   * @param lat         latitude
   * @param lon         longitude
   * @param displayName display name for the location
   * @return this builder
   */
  public NotionPropertiesBuilder place(
          String nameOrId, double lat, double lon, String displayName) {
    return property(nameOrId, NotionProperties.place(lat, lon, displayName));
  }

  /**
   * Sets a place property by configuring its {@link PlaceProperty.Place} payload.
   *
   * @param nameOrId schema property name or id
   * @param consumer callback that mutates the place payload
   * @return this builder
   */
  public NotionPropertiesBuilder place(String nameOrId, Consumer<PlaceProperty.Place> consumer) {
    return property(nameOrId, NotionProperties.place(consumer));
  }

  public NotionPropertiesBuilder clearPlace(String nameOrId) {
    return property(nameOrId, NotionProperties.place((PlaceProperty.Place) null));
  }
}
