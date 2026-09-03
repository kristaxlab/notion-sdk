package io.kristaxlab.notion.fluent;

import io.kristaxlab.notion.model.common.*;
import io.kristaxlab.notion.model.common.richtext.RichText;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.*;
import io.kristaxlab.notion.model.user.User;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A read-only lens over a single {@link Page} that provides convenient access to its system
 * attributes and typed property values.
 *
 * <p>{@code NotionPageViewer} eliminates the boilerplate of null-checking, casting, and digging
 * into nested property payloads:
 *
 * <pre>{@code
 * NotionPageViewer page = NotionPageViewer.of(notion.pages().retrieve(pageId));
 *
 * String title = page.title();
 * String emoji = page.iconEmoji();
 * String status = page.select("Status");
 * Number priority = page.number("Priority");
 * List<String> tags = page.multiSelect("Tags");
 * boolean done = page.checkbox("Done");
 *
 * if (page.contains("urgent")) { ... }
 * }</pre>
 *
 * <p>Return conventions mirror {@link NotionBlocksViewer}:
 *
 * <ul>
 *   <li>Text accessors return {@code ""} when the value is absent or empty.
 *   <li>Collections return an empty list, never {@code null}.
 *   <li>Booleans return a primitive ({@code false} when absent or unset).
 *   <li>Numbers, dates, and object references return {@code null} when absent — callers must
 *       distinguish a missing property from a legitimate zero or empty value.
 *   <li>Generic property lookup ({@link #property(String)}, {@link #property(String, Class)})
 *       returns {@code null} when the property is missing or of the wrong type.
 * </ul>
 *
 * @see Page
 * @see PagePropertyValue
 */
public final class NotionPageViewer {

  private final Page page;

  private NotionPageViewer(Page page) {
    this.page = page;
  }

  /**
   * Creates a view over the given page.
   *
   * @param page the page to wrap
   * @return a new view
   * @throws IllegalArgumentException if {@code page} is {@code null}
   */
  public static NotionPageViewer of(Page page) {
    if (page == null) {
      throw new IllegalArgumentException("page cannot be null");
    }
    return new NotionPageViewer(page);
  }

  public static NotionPageViewer of(Map<String, PagePropertyValue> properties) {
    // TODO create a separate viewer for properties (and standalone and as delegate here)
    if (properties == null) {
      throw new IllegalArgumentException("properties cannot be null");
    }
    Page page = new Page();
    page.setProperties(properties);
    return new NotionPageViewer(page);
  }

  // ────────────────────────────────────────────────────────────────────────
  // Underlying page
  // ────────────────────────────────────────────────────────────────────────

  /**
   * Returns the wrapped {@link Page}. The returned object is the same instance the view was
   * constructed with — mutations are reflected in the view.
   *
   * @return the wrapped page
   */
  public Page page() {
    return page;
  }

  // ────────────────────────────────────────────────────────────────────────
  // System attributes
  // ────────────────────────────────────────────────────────────────────────

  /** Returns the page id, or {@code null} if not set. */
  public String id() {
    return page.getId();
  }

  /** Returns the canonical Notion URL, or {@code null} if not set. */
  public String url() {
    return page.getUrl();
  }

  /** Returns the public sharing URL, or {@code null} if the page is not shared publicly. */
  public String publicUrl() {
    return page.getPublicUrl();
  }

  /** Returns the ISO-8601 created timestamp, or {@code null} if not set. */
  public String createdTime() {
    return page.getCreatedTime();
  }

  /** Returns the ISO-8601 last-edited timestamp, or {@code null} if not set. */
  public String lastEditedTime() {
    return page.getLastEditedTime();
  }

  /** Returns the user who created the page, or {@code null} if not set. */
  public User createdBy() {
    return page.getCreatedBy();
  }

  /** Returns the user who last edited the page, or {@code null} if not set. */
  public User lastEditedBy() {
    return page.getLastEditedBy();
  }

  /** Returns the page parent, or {@code null} if not set. */
  public Parent parent() {
    return page.getParent();
  }

  /** Returns whether the page is locked. A {@code null} value is treated as {@code false}. */
  public boolean isLocked() {
    return Boolean.TRUE.equals(page.getIsLocked());
  }

  /**
   * Returns whether the page is in the trash. Falls back to the deprecated {@code isArchived} flag
   * when {@code inTrash} is unset.
   */
  public boolean isInTrash() {
    if (Boolean.TRUE.equals(page.getInTrash())) {
      return true;
    }
    return Boolean.TRUE.equals(page.getIsArchived());
  }

  // ────────────────────────────────────────────────────────────────────────
  // Icon & cover
  // ────────────────────────────────────────────────────────────────────────

  /** Returns the page icon payload, or {@code null} if no icon is set. */
  public Icon icon() {
    return page.getIcon();
  }

  /** Returns the emoji character when the icon is an emoji, otherwise {@code null}. */
  public String iconEmoji() {
    Icon icon = page.getIcon();
    return icon == null ? null : icon.getEmoji();
  }

  /** Returns the icon URL when the icon is external or Notion-hosted, otherwise {@code null}. */
  public String iconUrl() {
    Icon icon = page.getIcon();
    if (icon == null) {
      return null;
    }
    if (icon.getExternal() != null && icon.getExternal().getUrl() != null) {
      return icon.getExternal().getUrl();
    }
    if (icon.getFile() != null && icon.getFile().getUrl() != null) {
      return icon.getFile().getUrl();
    }
    return null;
  }

  /** Returns the page cover payload, or {@code null} if no cover is set. */
  public Cover cover() {
    return page.getCover();
  }

  /** Returns the cover URL when external or Notion-hosted, otherwise {@code null}. */
  public String coverUrl() {
    Cover cover = page.getCover();
    if (cover == null) {
      return null;
    }
    if (cover.getExternal() != null && cover.getExternal().getUrl() != null) {
      return cover.getExternal().getUrl();
    }
    if (cover.getFile() != null && cover.getFile().getUrl() != null) {
      return cover.getFile().getUrl();
    }
    return null;
  }

  // ────────────────────────────────────────────────────────────────────────
  // Single-instance property shortcuts
  // ────────────────────────────────────────────────────────────────────────

  /** Returns the first {@link TitleProperty}, or {@code null} if the page has none. */
  public TitleProperty titleProperty() {
    return first(TitleProperty.class);
  }

  /** Returns the page title as plain text, or {@code ""} if absent or empty. */
  public String title() {
    TitleProperty title = titleProperty();
    if (title == null) {
      return "";
    }
    return joinPlainText(title.getTitle());
  }

  /**
   * Returns the formatted unique id ({@code "PREFIX-123"}), or {@code null} if no such property
   * exists or its number is unset.
   */
  public String uniqueId() {
    UniqueIdProperty prop = first(UniqueIdProperty.class);
    if (prop == null || prop.getUniqueId() == null || prop.getUniqueId().getNumber() == null) {
      return null;
    }
    return formatUniqueId(prop.getUniqueId());
  }

  /**
   * Returns the formatted unique id ({@code "PREFIX-123"}), or {@code null} if no such property
   * exists or its number is unset.
   */
  public BigInteger uniqueIdNoPrefix() {
    UniqueIdProperty prop = first(UniqueIdProperty.class);
    if (prop == null || prop.getUniqueId() == null || prop.getUniqueId().getNumber() == null) {
      return null;
    }
    return prop.getUniqueId().getNumber();
  }

  // ────────────────────────────────────────────────────────────────────────
  // Generic property access
  // ────────────────────────────────────────────────────────────────────────

  /** Returns an unmodifiable view of the property map. */
  public Map<String, PagePropertyValue> properties() {
    Map<String, PagePropertyValue> props = page.getProperties();
    return props == null ? Collections.emptyMap() : Collections.unmodifiableMap(props);
  }

  /** Returns the set of property names defined on the page. */
  public Set<String> propertyNames() {
    return properties().keySet();
  }

  /** Returns whether the page defines a property with the given name. */
  public boolean hasProperty(String name) {
    Map<String, PagePropertyValue> props = page.getProperties();
    return props != null && props.containsKey(name);
  }

  /** Returns the raw property under the given name, or {@code null} if not present. */
  public PagePropertyValue property(String name) {
    Map<String, PagePropertyValue> props = page.getProperties();
    if (props == null) {
      return null;
    }
    return props.get(name);
  }

  /**
   * Returns the property under the given name cast to the requested type, or {@code null} if absent
   * or of the wrong type.
   */
  public <T extends PagePropertyValue> T property(String name, Class<T> type) {
    PagePropertyValue prop = property(name);
    return type.isInstance(prop) ? type.cast(prop) : null;
  }

  /** Returns all properties of the given type, preserving insertion order. */
  public <T extends PagePropertyValue> Map<String, T> propertiesOfType(Class<T> type) {
    Map<String, PagePropertyValue> props = page.getProperties();
    if (props == null || props.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, T> result = new LinkedHashMap<>();
    for (Map.Entry<String, PagePropertyValue> entry : props.entrySet()) {
      if (type.isInstance(entry.getValue())) {
        result.put(entry.getKey(), type.cast(entry.getValue()));
      }
    }
    return Collections.unmodifiableMap(result);
  }

  // ────────────────────────────────────────────────────────────────────────
  // Typed property accessors
  // ────────────────────────────────────────────────────────────────────────

  /**
   * Returns a text representation for any supported property type, or {@code ""} when absent,
   * unset, or unsupported.
   */
  public String propertyAsPlainText(String propertyName) {
    PagePropertyValue prop = property(propertyName);
    if (prop == null) {
      return "";
    }
    if (prop instanceof TitleProperty title) {
      return joinPlainText(title.getTitle());
    }
    if (prop instanceof RichTextProperty rt) {
      return joinPlainText(rt.getRichText());
    }
    if (prop instanceof NumberProperty number) {
      return number.getNumber() == null ? "" : String.valueOf(number.getNumber());
    }
    if (prop instanceof CheckboxProperty checkbox) {
      return checkbox.getCheckbox() == null ? "" : String.valueOf(checkbox.getCheckbox());
    }
    if (prop instanceof SelectProperty select) {
      return select.getSelect() == null || select.getSelect().getName() == null
          ? ""
          : select.getSelect().getName();
    }
    if (prop instanceof MultiSelectProperty multiSelect) {
      return joinNonBlank(
          multiSelect.getMultiSelect() == null
              ? Collections.emptyList()
              : multiSelect.getMultiSelect().stream()
                  .map(SelectValue::getName)
                  .collect(Collectors.toList()),
          ", ");
    }
    if (prop instanceof StatusProperty status) {
      return status.getStatus() == null || status.getStatus().getName() == null
          ? ""
          : status.getStatus().getName();
    }
    if (prop instanceof UrlProperty url) {
      return url.getUrl() == null ? "" : url.getUrl();
    }
    if (prop instanceof EmailProperty email) {
      return email.getEmail() == null ? "" : email.getEmail();
    }
    if (prop instanceof PhoneNumberProperty phoneNumber) {
      return phoneNumber.getPhoneNumber() == null ? "" : phoneNumber.getPhoneNumber();
    }
    if (prop instanceof DateProperty date) {
      return stringifyDate(date.getDate());
    }
    if (prop instanceof PeopleProperty people) {
      return joinNonBlank(
          people.getPeople() == null
              ? Collections.emptyList()
              : people.getPeople().stream()
                  .map(NotionPageViewer::stringifyUser)
                  .collect(Collectors.toList()),
          ", ");
    }
    if (prop instanceof RelationProperty relation) {
      return joinNonBlank(
          relation.getRelation() == null
              ? Collections.emptyList()
              : relation.getRelation().stream()
                  .map(RelationProperty.RelationValue::getId)
                  .collect(Collectors.toList()),
          ", ");
    }
    if (prop instanceof FilesProperty files) {
      return joinNonBlank(
          files.getFiles() == null
              ? Collections.emptyList()
              : files.getFiles().stream()
                  .map(NotionPageViewer::extractFileDataUrl)
                  .collect(Collectors.toList()),
          ", ");
    }
    if (prop instanceof FormulaProperty formula) {
      String value = stringifyFormula(formula.getFormula());
      return value == null ? "" : value;
    }
    if (prop instanceof UniqueIdProperty uniqueId) {
      UniqueIdProperty.UniqueIdValue value = uniqueId.getUniqueId();
      return value == null || value.getNumber() == null ? "" : formatUniqueId(value);
    }
    if (prop instanceof CreatedByProperty createdBy) {
      return stringifyUser(createdBy.getCreatedBy());
    }
    if (prop instanceof LastEditedByProperty lastEditedBy) {
      return stringifyUser(lastEditedBy.getLastEditedBy());
    }
    if (prop instanceof CreatedTimeProperty createdTime) {
      return createdTime.getCreatedTime() == null ? "" : createdTime.getCreatedTime();
    }
    if (prop instanceof LastEditedTimeProperty lastEditedTime) {
      return lastEditedTime.getLastEditedTime() == null ? "" : lastEditedTime.getLastEditedTime();
    }
    if (prop instanceof PlaceProperty place) {
      return stringifyPlace(place.getPlace());
    }
    if (prop instanceof VerificationProperty verification) {
      return stringifyVerification(verification.getVerification());
    }
    if (prop instanceof RollupProperty rollup) {
      return stringifyRollup(rollup.getRollup());
    }
    return "";
  }

  /** Returns the number value, or {@code null} if absent, wrong type, or unset. */
  public Number number(String name) {
    NumberProperty prop = property(name, NumberProperty.class);
    return prop == null ? null : prop.getNumber();
  }

  /** Returns the checkbox value; {@code false} when absent, wrong type, or unset. */
  public boolean checkbox(String name) {
    CheckboxProperty prop = property(name, CheckboxProperty.class);
    return prop != null && Boolean.TRUE.equals(prop.getCheckbox());
  }

  /** Returns the selected option name, or {@code null} if absent or unset. */
  public String select(String name) {
    SelectProperty prop = property(name, SelectProperty.class);
    if (prop == null || prop.getSelect() == null) {
      return null;
    }
    return prop.getSelect().getName();
  }

  /** Returns multi-select option names in order, or an empty list. */
  public List<String> multiSelect(String name) {
    MultiSelectProperty prop = property(name, MultiSelectProperty.class);
    if (prop == null || prop.getMultiSelect() == null) {
      return Collections.emptyList();
    }
    return prop.getMultiSelect().stream().map(SelectValue::getName).collect(Collectors.toList());
  }

  /** Returns the status name, or {@code null} if absent or unset. */
  public String status(String name) {
    StatusProperty prop = property(name, StatusProperty.class);
    if (prop == null || prop.getStatus() == null) {
      return null;
    }
    return prop.getStatus().getName();
  }

  /** Returns the URL property value, or {@code null} if absent or unset. */
  public String url(String name) {
    UrlProperty prop = property(name, UrlProperty.class);
    return prop == null ? null : prop.getUrl();
  }

  /** Returns the email property value, or {@code null} if absent or unset. */
  public String email(String name) {
    EmailProperty prop = property(name, EmailProperty.class);
    return prop == null ? null : prop.getEmail();
  }

  /** Returns the phone number property value, or {@code null} if absent or unset. */
  public String phoneNumber(String name) {
    PhoneNumberProperty prop = property(name, PhoneNumberProperty.class);
    return prop == null ? null : prop.getPhoneNumber();
  }

  /** Returns the date payload, or {@code null} if absent or unset. */
  public DateData date(String name) {
    DateProperty prop = property(name, DateProperty.class);
    return prop == null ? null : prop.getDate();
  }

  /** Returns the date start ISO string, or {@code null} if absent. */
  public String dateStart(String name) {
    DateData data = date(name);
    return data == null ? null : data.getStart();
  }

  /** Returns the date end ISO string, or {@code null} if absent or not a range. */
  public String dateEnd(String name) {
    DateData data = date(name);
    return data == null ? null : data.getEnd();
  }

  /** Returns people user ids in order, or an empty list. */
  public List<User> people(String name) {
    PeopleProperty prop = property(name, PeopleProperty.class);
    if (prop == null || prop.getPeople() == null) {
      return Collections.emptyList();
    }
    return prop.getPeople();
  }

  /** Returns related page ids in order, or an empty list. */
  public List<String> relation(String name) {
    RelationProperty prop = property(name, RelationProperty.class);
    if (prop == null || prop.getRelation() == null) {
      return Collections.emptyList();
    }
    return prop.getRelation().stream()
        .map(RelationProperty.RelationValue::getId)
        .collect(Collectors.toList());
  }

  /** Returns file payloads, or an empty list. */
  public List<FileData> files(String name) {
    FilesProperty prop = property(name, FilesProperty.class);
    if (prop == null || prop.getFiles() == null) {
      return Collections.emptyList();
    }
    return new ArrayList<>(prop.getFiles());
  }

  /** Returns file URLs, preferring external URLs over Notion-hosted ones. */
  public List<String> fileUrls(String name) {
    List<String> urls = new ArrayList<>();
    for (FileData fd : files(name)) {
      String url = extractFileDataUrl(fd);
      if (url != null) {
        urls.add(url);
      }
    }
    return urls;
  }

  /** Returns the formula result as a string, or {@code null} if absent or unset. */
  public String formula(String name) {
    FormulaProperty prop = property(name, FormulaProperty.class);
    return prop == null ? null : stringifyFormula(prop.getFormula());
  }

  /** Returns the formula number, or {@code null} if absent or not numeric. */
  public Number formulaNumber(String name) {
    FormulaProperty prop = property(name, FormulaProperty.class);
    if (prop == null
        || prop.getFormula() == null
        || !"number".equals(prop.getFormula().getType())) {
      return null;
    }
    return prop.getFormula().getNumber();
  }

  /** Returns the formula boolean, or {@code null} if absent or not boolean. */
  public Boolean formulaBoolean(String name) {
    FormulaProperty prop = property(name, FormulaProperty.class);
    if (prop == null
        || prop.getFormula() == null
        || !"boolean".equals(prop.getFormula().getType())) {
      return null;
    }
    return prop.getFormula().getBooleanValue();
  }

  /** Returns the formula date payload, or {@code null} if absent or not a date. */
  public DateData formulaDate(String name) {
    FormulaProperty prop = property(name, FormulaProperty.class);
    if (prop == null || prop.getFormula() == null || !"date".equals(prop.getFormula().getType())) {
      return null;
    }
    return prop.getFormula().getDate();
  }

  // ────────────────────────────────────────────────────────────────────────
  // Search
  // ────────────────────────────────────────────────────────────────────────

  /**
   * Tests whether any searchable property value contains the keyword (case-insensitive).
   *
   * @param keyword substring to search for (must not be {@code null})
   * @throws NullPointerException if {@code keyword} is {@code null}
   */
  public boolean contains(String keyword) {
    Objects.requireNonNull(keyword, "keyword must not be null");
    String lower = keyword.toLowerCase(Locale.ROOT);
    Map<String, PagePropertyValue> props = page.getProperties();
    if (props == null || props.isEmpty()) {
      return false;
    }
    for (PagePropertyValue prop : props.values()) {
      if (propertyContainsKeyword(prop, lower)) {
        return true;
      }
    }
    return false;
  }

  // ────────────────────────────────────────────────────────────────────────
  // Helpers
  // ────────────────────────────────────────────────────────────────────────

  private <T extends PagePropertyValue> T first(Class<T> type) {
    Map<String, PagePropertyValue> props = page.getProperties();
    if (props == null || props.isEmpty()) {
      return null;
    }
    for (PagePropertyValue prop : props.values()) {
      if (type.isInstance(prop)) {
        return type.cast(prop);
      }
    }
    return null;
  }

  private static String formatUniqueId(UniqueIdProperty.UniqueIdValue value) {
    if (value.getPrefix() == null || value.getPrefix().isEmpty()) {
      return String.valueOf(value.getNumber());
    }
    return value.getPrefix() + "-" + value.getNumber();
  }

  private static String joinPlainText(List<RichText> richTexts) {
    if (richTexts == null || richTexts.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (RichText rt : richTexts) {
      if (rt.getPlainText() != null) {
        sb.append(rt.getPlainText());
      }
    }
    return sb.toString();
  }

  private static String stringifyDate(DateData data) {
    if (data == null) {
      return "";
    }
    List<String> parts = new ArrayList<>();
    parts.add(data.getStart());
    parts.add(data.getEnd());
    parts.add(data.getTimeZone());
    return joinNonBlank(parts, " ");
  }

  private static String stringifyUser(User user) {
    if (user == null) {
      return "";
    }
    if (user.getName() != null && !user.getName().isBlank()) {
      return user.getName();
    }
    return user.getId() == null ? "" : user.getId();
  }

  private static String stringifyPlace(PlaceProperty.Place place) {
    if (place == null) {
      return "";
    }
    String coordinates = "";
    if (place.getLat() != null && place.getLon() != null) {
      coordinates = place.getLat() + "," + place.getLon();
    }
    List<String> parts = new ArrayList<>();
    parts.add(place.getName());
    parts.add(place.getAddress());
    parts.add(coordinates);
    return joinNonBlank(parts, " ");
  }

  private static String stringifyVerification(VerificationProperty.VerificationValue verification) {
    if (verification == null) {
      return "";
    }
    List<String> parts = new ArrayList<>();
    parts.add(verification.getState());
    parts.add(stringifyUser(verification.getVerifiedBy()));
    parts.add(stringifyDate(verification.getDate()));
    return joinNonBlank(parts, " ");
  }

  private static String stringifyRollup(RollupProperty.RollupValue rollup) {
    if (rollup == null) {
      return "";
    }
    if ("number".equals(rollup.getType())) {
      return rollup.getNumber() == null ? "" : String.valueOf(rollup.getNumber());
    }
    if ("date".equals(rollup.getType())) {
      return rollup.getDate() == null ? "" : rollup.getDate();
    }
    if ("array".equals(rollup.getType())) {
      return rollup.getArray() == null ? "" : String.valueOf(rollup.getArray());
    }
    return "";
  }

  private static String joinNonBlank(List<String> values, String delimiter) {
    if (values == null || values.isEmpty()) {
      return "";
    }
    return values.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(v -> !v.isEmpty())
        .collect(Collectors.joining(delimiter));
  }

  private static String extractFileDataUrl(FileData fd) {
    if (fd == null) {
      return null;
    }
    if (fd.getExternal() != null && fd.getExternal().getUrl() != null) {
      return fd.getExternal().getUrl();
    }
    if (fd.getFile() != null && fd.getFile().getUrl() != null) {
      return fd.getFile().getUrl();
    }
    return null;
  }

  private static String stringifyFormula(FormulaProperty.FormulaValue f) {
    if (f == null || f.getType() == null) {
      return null;
    }
    return switch (f.getType()) {
      case "string" -> f.getString();
      case "number" -> f.getNumber() == null ? null : String.valueOf(f.getNumber());
      case "boolean" -> f.getBooleanValue() == null ? null : String.valueOf(f.getBooleanValue());
      case "date" -> f.getDate() == null ? null : f.getDate().getStart();
      default -> null;
    };
  }

  private static boolean propertyContainsKeyword(PagePropertyValue prop, String lower) {
    if (prop instanceof TitleProperty title) {
      return containsLower(joinPlainText(title.getTitle()), lower);
    }
    if (prop instanceof RichTextProperty rt) {
      return containsLower(joinPlainText(rt.getRichText()), lower);
    }
    if (prop instanceof SelectProperty sel) {
      SelectValue v = sel.getSelect();
      return v != null && containsLower(v.getName(), lower);
    }
    if (prop instanceof StatusProperty st) {
      StatusProperty.StatusValue v = st.getStatus();
      return v != null && containsLower(v.getName(), lower);
    }
    if (prop instanceof MultiSelectProperty ms) {
      List<SelectValue> values = ms.getMultiSelect();
      if (values != null) {
        for (SelectValue v : values) {
          if (containsLower(v.getName(), lower)) {
            return true;
          }
        }
      }
      return false;
    }
    if (prop instanceof UrlProperty u) {
      return containsLower(u.getUrl(), lower);
    }
    if (prop instanceof EmailProperty e) {
      return containsLower(e.getEmail(), lower);
    }
    if (prop instanceof PhoneNumberProperty ph) {
      return containsLower(ph.getPhoneNumber(), lower);
    }
    if (prop instanceof DateProperty d) {
      DateData v = d.getDate();
      return v != null && (containsLower(v.getStart(), lower) || containsLower(v.getEnd(), lower));
    }
    if (prop instanceof FilesProperty fp) {
      List<FileData> files = fp.getFiles();
      if (files != null) {
        for (FileData fd : files) {
          if (containsLower(extractFileDataUrl(fd), lower)) {
            return true;
          }
        }
      }
      return false;
    }
    if (prop instanceof FormulaProperty f) {
      return containsLower(stringifyFormula(f.getFormula()), lower);
    }
    if (prop instanceof UniqueIdProperty u) {
      UniqueIdProperty.UniqueIdValue v = u.getUniqueId();
      return v != null && v.getNumber() != null && containsLower(formatUniqueId(v), lower);
    }
    return false;
  }

  private static boolean containsLower(String value, String lowerKeyword) {
    return value != null && value.toLowerCase(Locale.ROOT).contains(lowerKeyword);
  }
}
