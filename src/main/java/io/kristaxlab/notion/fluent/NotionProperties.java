package io.kristaxlab.notion.fluent;

import static io.kristaxlab.notion.fluent.NotionText.plainText;
import static io.kristaxlab.notion.fluent.NotionText.textBuilder;

import io.kristaxlab.notion.model.common.DateData;
import io.kristaxlab.notion.model.common.FileData;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.page.property.*;
import io.kristaxlab.notion.model.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Static factory helpers for constructing common Notion page property values.
 *
 * <p>Designed for use with a static import so call sites read as a concise DSL:
 *
 * <pre>{@code
 * import static io.kristaxlab.notion.fluent.NotionProperties.*;
 *
 * client.pages().create(page -> page
 *     .underDataSource("data-source-id")
 *     .properties(p -> p
 *         .title("Build a SaaS")
 *         .richText("Description", "Build a SaaS for nerds")
 *         .number("Priority", 5)
 *         .select("Status", "In progress")
 *         .multiSelect("Tags", "urgent", "review")
 *         .date("Due Date", LocalDate.of(2026, 1, 15))
 *         .checkbox("Done", false)));
 * }</pre>
 *
 * <p>The static methods on this class return individual {@link PageProperty} instances; use {@link
 * NotionPropertiesBuilder} to assemble a {@code name -> property} map for create/update payloads.
 */
public class NotionProperties {

  /** The Notion API's special key for the page title property. */
  public static final String TITLE = PagePropertyType.TITLE.type();

  /**
   * Creates a fluent properties builder.
   *
   * @return new properties builder
   */
  public static NotionPropertiesBuilder propertiesBuilder() {
    return new NotionPropertiesBuilder();
  }

  // Title

  /**
   * Creates a title property from plain text.
   *
   * @param text title text
   * @return title property
   */
  public static TitleProperty title(String text) {
    return title(plainText(text));
  }

  /**
   * Creates a title property from rich text fragments.
   *
   * @param richTexts rich text fragments
   * @return title property
   */
  public static TitleProperty title(RichText... richTexts) {
    return title(Arrays.asList(richTexts));
  }

  /**
   * Creates a title property from a list of rich text fragments.
   *
   * @param richTexts rich text fragments
   * @return title property
   */
  public static TitleProperty title(List<RichText> richTexts) {
    TitleProperty property = new TitleProperty();
    property.setTitle(new ArrayList<>(richTexts));
    return property;
  }

  /**
   * Creates a title property by configuring a {@link NotionTextBuilder}.
   *
   * @param consumer callback that populates the rich text builder
   * @return title property
   */
  public static TitleProperty title(Consumer<NotionTextBuilder> consumer) {
    NotionTextBuilder builder = textBuilder();
    consumer.accept(builder);
    return title(builder.build());
  }

  // Rich text

  /**
   * Creates a rich-text property from plain text.
   *
   * @param text plain text content
   * @return rich-text property
   */
  public static RichTextProperty richText(String text) {
    return richText(plainText(text));
  }

  /**
   * Creates a rich-text property from rich text fragments.
   *
   * @param richTexts rich text fragments
   * @return rich-text property
   */
  public static RichTextProperty richText(RichText... richTexts) {
    return richText(Arrays.asList(richTexts));
  }

  /**
   * Creates a rich-text property from a list of rich text fragments.
   *
   * @param richTexts rich text fragments
   * @return rich-text property
   */
  public static RichTextProperty richText(List<RichText> richTexts) {
    RichTextProperty property = new RichTextProperty();
    property.setRichText(new ArrayList<>(richTexts));
    return property;
  }

  /**
   * Creates a rich-text property by configuring a {@link NotionTextBuilder}.
   *
   * @param consumer callback that populates the rich text builder
   * @return rich-text property
   */
  public static RichTextProperty richText(Consumer<NotionTextBuilder> consumer) {
    NotionTextBuilder builder = textBuilder();
    consumer.accept(builder);
    return richText(builder.build());
  }

  // Number

  /**
   * Creates a number property.
   *
   * @param value numeric value
   * @return number property
   */
  public static NumberProperty number(Number value) {
    NumberProperty property = new NumberProperty();
    property.setNumber(value);
    return property;
  }

  // Select

  /**
   * Creates a select property by option name.
   *
   * @param optionName the existing option's name in the schema
   * @return select property
   */
  public static SelectProperty select(String optionName) {
    SelectValue value = new SelectValue();
    value.setName(optionName);
    return select(value);
  }

  /**
   * Creates a select property by option id.
   *
   * @param optionId the existing option's id in the schema
   * @return select property
   */
  public static SelectProperty selectById(String optionId) {
    SelectValue value = new SelectValue();
    value.setId(optionId);
    return select(value);
  }

  /**
   * Creates a select property from a fully prepared {@link SelectValue}.
   *
   * @param value option payload
   * @return select property
   */
  public static SelectProperty select(SelectValue value) {
    SelectProperty property = new SelectProperty();
    property.setSelect(value);
    return property;
  }

  // Multi-select

  /**
   * Creates a multi-select property from option names.
   *
   * @param optionNames existing option names in the schema
   * @return multi-select property
   */
  public static MultiSelectProperty multiSelect(String... optionNames) {
    return multiSelect(Arrays.asList(optionNames));
  }

  /**
   * Creates a multi-select property from a list of option names.
   *
   * @param optionNames existing option names in the schema
   * @return multi-select property
   */
  public static MultiSelectProperty multiSelect(List<String> optionNames) {
    List<SelectValue> values = new ArrayList<>();
    for (String name : optionNames) {
      SelectValue value = new SelectValue();
      value.setName(name);
      values.add(value);
    }
    return multiSelectFromValues(values);
  }

  /**
   * Creates a multi-select property from option ids.
   *
   * @param optionIds existing option ids in the schema
   * @return multi-select property
   */
  public static MultiSelectProperty multiSelectById(String... optionIds) {
    List<SelectValue> values = new ArrayList<>();
    for (String id : optionIds) {
      SelectValue value = new SelectValue();
      value.setId(id);
      values.add(value);
    }
    return multiSelectFromValues(values);
  }

  /**
   * Creates a multi-select property from prepared {@link SelectValue}s.
   *
   * @param values option payloads
   * @return multi-select property
   */
  public static MultiSelectProperty multiSelectFromValues(List<SelectValue> values) {
    MultiSelectProperty property = new MultiSelectProperty();
    property.setMultiSelect(new ArrayList<>(values));
    return property;
  }

  // Date

  /**
   * Creates a single-day date property from a {@link LocalDate}.
   *
   * @param date date value
   * @return date property
   */
  public static DateProperty date(LocalDate date) {
    return date(date.toString());
  }

  /**
   * Creates a date-time property from a {@link LocalDateTime}.
   *
   * @param dateTime date-time value
   * @return date property
   */
  public static DateProperty date(LocalDateTime dateTime) {
    return date(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  /**
   * Creates a date property from an ISO 8601 string.
   *
   * @param iso8601 ISO 8601 date or date-time
   * @return date property
   */
  public static DateProperty date(String iso8601) {
    DateData dateData = new DateData();
    dateData.setStart(iso8601);
    return date(dateData);
  }

  /**
   * Creates a date-range property from two {@link LocalDate}s.
   *
   * @param start range start
   * @param end range end
   * @return date property
   */
  public static DateProperty dateRange(LocalDate start, LocalDate end) {
    return dateRange(start.toString(), end.toString(), null);
  }

  /**
   * Creates a date-range property from two {@link LocalDateTime}s.
   *
   * @param start range start
   * @param end range end
   * @return date property
   */
  public static DateProperty dateRange(LocalDateTime start, LocalDateTime end) {
    return dateRange(
        start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        null);
  }

  /**
   * Creates a date-range property from two ISO 8601 strings.
   *
   * @param start ISO 8601 start
   * @param end ISO 8601 end
   * @return date property
   */
  public static DateProperty dateRange(String start, String end) {
    return dateRange(start, end, null);
  }

  /**
   * Creates a date-range property from two ISO 8601 strings and a time zone.
   *
   * @param start ISO 8601 start
   * @param end ISO 8601 end
   * @param timeZone IANA time zone (e.g. {@code "America/New_York"})
   * @return date property
   */
  public static DateProperty dateRange(String start, String end, String timeZone) {
    DateData dateData = new DateData();
    dateData.setStart(start);
    dateData.setEnd(end);
    dateData.setTimeZone(timeZone);
    return date(dateData);
  }

  /**
   * Creates a date property from a fully prepared {@link DateData} payload.
   *
   * @param dateData date payload
   * @return date property
   */
  public static DateProperty date(DateData dateData) {
    DateProperty property = new DateProperty();
    property.setDate(dateData);
    return property;
  }

  // Checkbox

  /**
   * Creates a checkbox property.
   *
   * @param checked whether the checkbox is checked
   * @return checkbox property
   */
  public static CheckboxProperty checkbox(boolean checked) {
    CheckboxProperty property = new CheckboxProperty();
    property.setCheckbox(checked);
    return property;
  }

  /**
   * Creates a checked checkbox property.
   *
   * @return checkbox property set to {@code true}
   */
  public static CheckboxProperty checked() {
    return checkbox(true);
  }

  /**
   * Creates an unchecked checkbox property.
   *
   * @return checkbox property set to {@code false}
   */
  public static CheckboxProperty unchecked() {
    return checkbox(false);
  }

  // Url, email, phone

  /**
   * Creates a URL property.
   *
   * @param url the URL value
   * @return URL property
   */
  public static UrlProperty url(String url) {
    UrlProperty property = new UrlProperty();
    property.setUrl(url);
    return property;
  }

  /**
   * Creates an email property.
   *
   * @param email the email address
   * @return email property
   */
  public static EmailProperty email(String email) {
    EmailProperty property = new EmailProperty();
    property.setEmail(email);
    return property;
  }

  /**
   * Creates a phone-number property.
   *
   * @param phoneNumber the phone number
   * @return phone-number property
   */
  public static PhoneNumberProperty phoneNumber(String phoneNumber) {
    PhoneNumberProperty property = new PhoneNumberProperty();
    property.setPhoneNumber(phoneNumber);
    return property;
  }

  // People

  /**
   * Creates a people property from one or more user ids.
   *
   * @param userIds Notion user ids
   * @return people property
   */
  public static PeopleProperty people(String... userIds) {
    return people(Arrays.asList(userIds));
  }

  /**
   * Creates a people property from a list of user ids.
   *
   * @param userIds Notion user ids
   * @return people property
   */
  public static PeopleProperty people(List<String> userIds) {
    List<User> users = new ArrayList<>();
    for (String id : userIds) {
      User user = new User();
      user.setId(id);
      users.add(user);
    }
    PeopleProperty property = new PeopleProperty();
    property.setPeople(users);
    return property;
  }

  // Files

  /**
   * Creates a files property.
   *
   * @param files file payloads
   * @return files property
   */
  public static FilesProperty files(FileData... files) {
    return files(Arrays.asList(files));
  }

  /**
   * Creates a files property.
   *
   * @param files file payloads
   * @return files property
   */
  public static FilesProperty files(List<FileData> files) {
    FilesProperty property = new FilesProperty();
    property.setFiles(new ArrayList<>(files));
    return property;
  }

  // Relation

  /**
   * Creates a relation property from one or more related page ids.
   *
   * @param pageIds related page ids
   * @return relation property
   */
  public static RelationProperty relation(String... pageIds) {
    return relation(Arrays.asList(pageIds));
  }

  /**
   * Creates a relation property from a list of related page ids.
   *
   * @param pageIds related page ids
   * @return relation property
   */
  public static RelationProperty relation(List<String> pageIds) {
    List<RelationProperty.RelationValue> values = new ArrayList<>();
    for (String id : pageIds) {
      RelationProperty.RelationValue value = new RelationProperty.RelationValue();
      value.setId(id);
      values.add(value);
    }
    RelationProperty property = new RelationProperty();
    property.setRelation(values);
    return property;
  }

  // Status

  /**
   * Creates a status property by option name.
   *
   * @param optionName the existing status option's name in the schema
   * @return status property
   */
  public static StatusProperty status(String optionName) {
    StatusProperty.StatusValue value = new StatusProperty.StatusValue();
    value.setName(optionName);
    StatusProperty property = new StatusProperty();
    property.setStatus(value);
    return property;
  }

  /**
   * Creates a status property by option id.
   *
   * @param optionId the existing status option's id in the schema
   * @return status property
   */
  public static StatusProperty statusById(String optionId) {
    StatusProperty.StatusValue value = new StatusProperty.StatusValue();
    value.setId(optionId);
    StatusProperty property = new StatusProperty();
    property.setStatus(value);
    return property;
  }

  // Place

  /**
   * Creates a place property with latitude and longitude only.
   *
   * @param lat latitude
   * @param lon longitude
   * @return place property
   */
  public static PlaceProperty place(double lat, double lon) {
    return place(
        p -> {
          p.setLat(lat);
          p.setLon(lon);
        });
  }

  /**
   * Creates a place property with latitude, longitude and a display name.
   *
   * @param lat latitude
   * @param lon longitude
   * @param name display name
   * @return place property
   */
  public static PlaceProperty place(double lat, double lon, String name) {
    return place(
        p -> {
          p.setLat(lat);
          p.setLon(lon);
          p.setName(name);
        });
  }

  /**
   * Creates a place property by configuring its {@link PlaceProperty.Place} payload.
   *
   * @param consumer callback that mutates the place payload
   * @return place property
   */
  public static PlaceProperty place(Consumer<PlaceProperty.Place> consumer) {
    PlaceProperty property = new PlaceProperty();
    consumer.accept(property.getPlace());
    return property;
  }
}
