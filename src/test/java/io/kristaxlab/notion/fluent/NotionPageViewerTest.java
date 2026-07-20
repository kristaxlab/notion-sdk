package io.kristaxlab.notion.fluent;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.*;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.*;
import io.kristaxlab.notion.model.user.User;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotionPageViewerTest {

  private static Page pageWith(String name, PageProperty property) {
    Page page = new Page();
    Map<String, PageProperty> props = new LinkedHashMap<>();
    props.put(name, property);
    page.setProperties(props);
    return page;
  }

  @Nested
  class Factory {

    @Test
    @DisplayName("of null page throws illegal argument")
    void ofNullPage_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> NotionPageViewer.of(null));
    }

    @Test
    @DisplayName("of page wraps the given instance")
    void ofPage_wrapsInstance() {
      Page page = new Page();
      page.setId("page-1");

      NotionPageViewer view = NotionPageViewer.of(page);

      assertSame(page, view.page());
      assertEquals("page-1", view.id());
    }
  }

  @Nested
  class SystemAttributes {

    @Test
    @DisplayName("url returns value")
    void url_returnsValue() {
      Page page = new Page();
      page.setUrl("https://www.notion.so/my-page");

      assertEquals("https://www.notion.so/my-page", NotionPageViewer.of(page).url());
    }

    @Test
    @DisplayName("url is null when missing")
    void url_nullWhenMissing() {
      assertNull(NotionPageViewer.of(new Page()).url());
    }

    @Test
    @DisplayName("public url returns value")
    void publicUrl_returnsValue() {
      Page page = new Page();
      page.setPublicUrl("https://share.notion.so/abc");

      assertEquals("https://share.notion.so/abc", NotionPageViewer.of(page).publicUrl());
    }

    @Test
    @DisplayName("public url is null when missing")
    void publicUrl_nullWhenMissing() {
      assertNull(NotionPageViewer.of(new Page()).publicUrl());
    }

    @Test
    @DisplayName("created and last edited times exposed")
    void timestamps_exposed() {
      Page page = new Page();
      page.setCreatedTime("2024-01-02T03:04:00.000Z");
      page.setLastEditedTime("2024-02-03T04:05:00.000Z");

      NotionPageViewer view = NotionPageViewer.of(page);
      assertEquals("2024-01-02T03:04:00.000Z", view.createdTime());
      assertEquals("2024-02-03T04:05:00.000Z", view.lastEditedTime());
    }

    @Test
    @DisplayName("created and last edited users exposed")
    void users_exposed() {
      User creator = new User();
      creator.setId("user-create");
      User editor = new User();
      editor.setId("user-edit");

      Page page = new Page();
      page.setCreatedBy(creator);
      page.setLastEditedBy(editor);

      NotionPageViewer view = NotionPageViewer.of(page);
      assertSame(creator, view.createdBy());
      assertSame(editor, view.lastEditedBy());
    }

    @Test
    @DisplayName("parent exposed")
    void parent_exposed() {
      Page page = new Page();
      page.setParent(Parent.databaseParent("db-1"));

      Parent parent = NotionPageViewer.of(page).parent();
      assertNotNull(parent);
      assertEquals("database_id", parent.getType());
      assertEquals("db-1", parent.getDatabaseId());
    }

    @Test
    @DisplayName("is locked treats null as false")
    void isLocked_nullAsFalse() {
      assertFalse(NotionPageViewer.of(new Page()).isLocked());
    }

    @Test
    @DisplayName("is locked true when set")
    void isLocked_trueWhenSet() {
      Page page = new Page();
      page.setIsLocked(true);

      assertTrue(NotionPageViewer.of(page).isLocked());
    }

    @Test
    @DisplayName("is in trash uses inTrash flag")
    void isInTrash_usesInTrashFlag() {
      Page page = new Page();
      page.setInTrash(true);

      assertTrue(NotionPageViewer.of(page).isInTrash());
    }

    @Test
    @DisplayName("is in trash falls back to deprecated archived flag")
    void isInTrash_fallsBackToArchivedFlag() {
      Page page = new Page();
      page.setIsArchived(true);

      assertTrue(NotionPageViewer.of(page).isInTrash());
    }

    @Test
    @DisplayName("is in trash false when both flags unset")
    void isInTrash_falseWhenUnset() {
      assertFalse(NotionPageViewer.of(new Page()).isInTrash());
    }
  }

  @Nested
  class IconAndCover {

    @Test
    @DisplayName("icon emoji extracted")
    void iconEmoji_extracted() {
      Page page = new Page();
      page.setIcon(Icon.emoji("🚀"));

      assertEquals("🚀", NotionPageViewer.of(page).iconEmoji());
    }

    @Test
    @DisplayName("icon emoji null for non emoji icon")
    void iconEmoji_nullForNonEmoji() {
      Page page = new Page();
      page.setIcon(Icon.external("https://cdn.example.com/icon.png"));

      assertNull(NotionPageViewer.of(page).iconEmoji());
    }

    @Test
    @DisplayName("icon url extracted from external icon")
    void iconUrl_fromExternal() {
      Page page = new Page();
      page.setIcon(Icon.external("https://cdn.example.com/icon.png"));

      assertEquals("https://cdn.example.com/icon.png", NotionPageViewer.of(page).iconUrl());
    }

    @Test
    @DisplayName("icon url extracted from hosted file")
    void iconUrl_fromHostedFile() {
      Icon icon = new Icon();
      icon.setType("file");
      NotionFile file = new NotionFile();
      file.setUrl("https://files.notion.so/icon.png");
      icon.setFile(file);

      Page page = new Page();
      page.setIcon(icon);

      assertEquals("https://files.notion.so/icon.png", NotionPageViewer.of(page).iconUrl());
    }

    @Test
    @DisplayName("icon url null for emoji icon")
    void iconUrl_nullForEmoji() {
      Page page = new Page();
      page.setIcon(Icon.emoji("✨"));

      assertNull(NotionPageViewer.of(page).iconUrl());
    }

    @Test
    @DisplayName("icon null when not set")
    void icon_nullWhenNotSet() {
      NotionPageViewer view = NotionPageViewer.of(new Page());
      assertNull(view.icon());
      assertNull(view.iconUrl());
      assertNull(view.iconEmoji());
    }

    @Test
    @DisplayName("cover url extracted from external cover")
    void coverUrl_fromExternal() {
      Page page = new Page();
      page.setCover(Cover.external("https://cdn.example.com/cover.jpg"));

      assertEquals("https://cdn.example.com/cover.jpg", NotionPageViewer.of(page).coverUrl());
    }

    @Test
    @DisplayName("cover url extracted from hosted file")
    void coverUrl_fromHostedFile() {
      Cover cover = new Cover();
      cover.setType("file");
      NotionFile file = new NotionFile();
      file.setUrl("https://files.notion.so/cover.jpg");
      cover.setFile(file);

      Page page = new Page();
      page.setCover(cover);

      assertEquals("https://files.notion.so/cover.jpg", NotionPageViewer.of(page).coverUrl());
    }

    @Test
    @DisplayName("cover null when not set")
    void cover_nullWhenNotSet() {
      NotionPageViewer view = NotionPageViewer.of(new Page());
      assertNull(view.cover());
      assertNull(view.coverUrl());
    }
  }

  @Nested
  class Title {

    @Test
    @DisplayName("title resolves from title property")
    void title_resolvesFromTitleProperty() {
      Page page = pageWith("title", NotionProperties.title("My Page"));

      assertEquals("My Page", NotionPageViewer.of(page).title());
    }

    @Test
    @DisplayName("title concatenates multiple rich text segments")
    void title_concatenatesSegments() {
      TitleProperty title =
          NotionProperties.title(NotionText.plainText("Hel"), NotionText.plainText("lo"));
      Page page = pageWith("title", title);

      assertEquals("Hello", NotionPageViewer.of(page).title());
    }

    @Test
    @DisplayName("title empty when no title property exists")
    void title_emptyWhenNoTitleProperty() {
      Page page = pageWith("Description", NotionProperties.richText("just text"));

      assertEquals("", NotionPageViewer.of(page).title());
    }

    @Test
    @DisplayName("title empty when title rich text is empty")
    void title_emptyWhenRichTextEmpty() {
      TitleProperty empty = new TitleProperty();
      empty.setTitle(List.of());
      Page page = pageWith("title", empty);

      assertEquals("", NotionPageViewer.of(page).title());
    }

    @Test
    @DisplayName("title found regardless of property name key")
    void title_foundRegardlessOfKey() {
      Page page = pageWith("Name", NotionProperties.title("Custom Key Title"));

      assertEquals("Custom Key Title", NotionPageViewer.of(page).title());
    }

    @Test
    @DisplayName("title property accessor returns the property instance")
    void titleProperty_returnsPropertyInstance() {
      TitleProperty title = NotionProperties.title("Hi");
      Page page = pageWith("title", title);

      assertSame(title, NotionPageViewer.of(page).titleProperty());
    }
  }

  @Nested
  class SinglePropertyShortcuts {

    @Test
    @DisplayName("unique id formatted with prefix")
    void uniqueId_formattedWithPrefix() {
      Page page = pageWith("ID", uniqueIdProp("TASK", 123));

      assertEquals("TASK-123", NotionPageViewer.of(page).uniqueId());
    }

    @Test
    @DisplayName("unique id without prefix returns plain number")
    void uniqueId_noPrefix_returnsNumber() {
      Page page = pageWith("ID", uniqueIdProp(null, 42));

      assertEquals("42", NotionPageViewer.of(page).uniqueId());
    }

    @Test
    @DisplayName("unique id empty prefix treated as no prefix")
    void uniqueId_emptyPrefix_returnsNumber() {
      Page page = pageWith("ID", uniqueIdProp("", 7));

      assertEquals("7", NotionPageViewer.of(page).uniqueId());
    }

    @Test
    @DisplayName("unique id null when number is null")
    void uniqueId_nullWhenNumberNull() {
      Page page = pageWith("ID", uniqueIdProp("TASK", null));

      assertNull(NotionPageViewer.of(page).uniqueId());
    }

    @Test
    @DisplayName("unique id null when value missing")
    void uniqueId_nullWhenValueMissing() {
      UniqueIdProperty prop = new UniqueIdProperty();
      prop.setUniqueId(null);
      Page page = pageWith("ID", prop);

      assertNull(NotionPageViewer.of(page).uniqueId());
    }

    @Test
    @DisplayName("unique id by name null when missing")
    void uniqueId_byName_nullWhenMissing() {
      assertNull(NotionPageViewer.of(new Page()).uniqueId());
    }

    private static UniqueIdProperty uniqueIdProp(String prefix, Integer number) {
      UniqueIdProperty.UniqueIdValue value = new UniqueIdProperty.UniqueIdValue();
      value.setPrefix(prefix);
      value.setNumber(number == null ? null : BigInteger.valueOf(number));
      UniqueIdProperty prop = new UniqueIdProperty();
      prop.setUniqueId(value);
      return prop;
    }
  }

  @Nested
  class GenericPropertyAccess {

    @Test
    @DisplayName("properties returns unmodifiable map")
    void properties_unmodifiable() {
      Page page = pageWith("Priority", NotionProperties.number(5));

      Map<String, PageProperty> props = NotionPageViewer.of(page).properties();
      assertThrows(
          UnsupportedOperationException.class, () -> props.put("Other", new UnknownProperty()));
    }

    @Test
    @DisplayName("properties empty map when none set")
    void properties_emptyWhenNoneSet() {
      Page page = new Page();
      page.setProperties(null);

      assertTrue(NotionPageViewer.of(page).properties().isEmpty());
    }

    @Test
    @DisplayName("property names exposes keys")
    void propertyNames_exposesKeys() {
      Page page = new Page();
      Map<String, PageProperty> props = new LinkedHashMap<>();
      props.put("Name", NotionProperties.title("X"));
      props.put("Priority", NotionProperties.number(1));
      page.setProperties(props);

      assertEquals(
          List.of("Name", "Priority"), List.copyOf(NotionPageViewer.of(page).propertyNames()));
    }

    @Test
    @DisplayName("has property true when present")
    void hasProperty_trueWhenPresent() {
      Page page = pageWith("Status", NotionProperties.select("Open"));

      assertTrue(NotionPageViewer.of(page).hasProperty("Status"));
    }

    @Test
    @DisplayName("has property false when absent")
    void hasProperty_falseWhenAbsent() {
      Page page = pageWith("Status", NotionProperties.select("Open"));

      assertFalse(NotionPageViewer.of(page).hasProperty("Other"));
    }

    @Test
    @DisplayName("property by name returns value")
    void property_byName_returnsValue() {
      NumberProperty number = NotionProperties.number(42);
      Page page = pageWith("Priority", number);

      assertSame(number, NotionPageViewer.of(page).property("Priority"));
    }

    @Test
    @DisplayName("property by name null when missing")
    void property_byName_nullWhenMissing() {
      assertNull(NotionPageViewer.of(new Page()).property("Anything"));
    }

    @Test
    @DisplayName("property by type returns cast value")
    void propertyByType_returnsCast() {
      NumberProperty number = NotionProperties.number(3);
      Page page = pageWith("Priority", number);

      assertSame(number, NotionPageViewer.of(page).property("Priority", NumberProperty.class));
    }

    @Test
    @DisplayName("property by type null for type mismatch")
    void propertyByType_nullForMismatch() {
      Page page = pageWith("Priority", NotionProperties.number(3));

      assertNull(NotionPageViewer.of(page).property("Priority", CheckboxProperty.class));
    }

    @Test
    @DisplayName("properties of type filters and preserves order")
    void propertiesOfType_filtersAndPreservesOrder() {
      Page page = new Page();
      Map<String, PageProperty> props = new LinkedHashMap<>();
      props.put("Title", NotionProperties.title("X"));
      props.put("A", NotionProperties.number(1));
      props.put("B", NotionProperties.number(2));
      props.put("Done", NotionProperties.checkbox(true));
      page.setProperties(props);

      Map<String, NumberProperty> numbers =
          NotionPageViewer.of(page).propertiesOfType(NumberProperty.class);

      assertEquals(2, numbers.size());
      assertEquals(List.of("A", "B"), List.copyOf(numbers.keySet()));
    }

    @Test
    @DisplayName("properties of type empty when no match")
    void propertiesOfType_emptyWhenNoMatch() {
      Page page = pageWith("Priority", NotionProperties.number(1));

      assertTrue(NotionPageViewer.of(page).propertiesOfType(CheckboxProperty.class).isEmpty());
    }
  }

  @Nested
  class TypedAccessors {

    @Test
    @DisplayName("plain text from rich text property")
    void plainText_fromRichTextProperty() {
      Page page = pageWith("Description", NotionProperties.richText("Hello world"));

      assertEquals("Hello world", NotionPageViewer.of(page).propertyAsPlainText("Description"));
    }

    @Test
    @DisplayName("plain text from title property")
    void plainText_fromTitleProperty() {
      Page page = pageWith("Name", NotionProperties.title("Doc Title"));

      assertEquals("Doc Title", NotionPageViewer.of(page).propertyAsPlainText("Name"));
    }

    @Test
    @DisplayName("plain text from number property")
    void plainText_fromNumberProperty() {
      Page page = pageWith("Priority", NotionProperties.number(1));

      assertEquals("1", NotionPageViewer.of(page).propertyAsPlainText("Priority"));
    }

    @Test
    @DisplayName("plain text from checkbox property")
    void plainText_fromCheckboxProperty() {
      Page page = pageWith("Done", NotionProperties.checkbox(true));

      assertEquals("true", NotionPageViewer.of(page).propertyAsPlainText("Done"));
    }

    @Test
    @DisplayName("plain text from relation property")
    void plainText_fromRelationProperty() {
      Page page = pageWith("Project", NotionProperties.relation("page-a", "page-b"));

      assertEquals("page-a, page-b", NotionPageViewer.of(page).propertyAsPlainText("Project"));
    }

    @Test
    @DisplayName("plain text from people property")
    void plainText_fromPeopleProperty() {
      Page page = pageWith("Owners", NotionProperties.people("user-1", "user-2"));

      assertEquals("user-1, user-2", NotionPageViewer.of(page).propertyAsPlainText("Owners"));
    }

    @Test
    @DisplayName("plain text empty when value is empty")
    void plainText_emptyWhenValueEmpty() {
      RichTextProperty empty = new RichTextProperty();
      Page page = pageWith("Description", empty);

      assertEquals("", NotionPageViewer.of(page).propertyAsPlainText("Description"));
    }

    @Test
    @DisplayName("plain text empty for unsupported property")
    void plainText_emptyForUnsupportedType() {
      Page page = pageWith("Unknown", new UnknownProperty());

      assertEquals("", NotionPageViewer.of(page).propertyAsPlainText("Unknown"));
    }

    @Test
    @DisplayName("plain text from select property")
    void plainText_fromSelectProperty() {
      Page page = pageWith("Status", NotionProperties.select("In progress"));

      assertEquals("In progress", NotionPageViewer.of(page).propertyAsPlainText("Status"));
    }

    @Test
    @DisplayName("plain text from status property")
    void plainText_fromStatusProperty() {
      Page page = pageWith("Stage", NotionProperties.status("Done"));

      assertEquals("Done", NotionPageViewer.of(page).propertyAsPlainText("Stage"));
    }

    @Test
    @DisplayName("plain text from multi select joins names")
    void plainText_fromMultiSelect_joinsNames() {
      Page page = pageWith("Tags", NotionProperties.multiSelect("urgent", "review", "draft"));

      assertEquals("urgent, review, draft", NotionPageViewer.of(page).propertyAsPlainText("Tags"));
    }

    @Test
    @DisplayName("plain text from date range includes both bounds")
    void plainText_fromDateRange_includesStartAndEnd() {
      Page page = pageWith("Sprint", NotionProperties.dateRange("2026-05-01", "2026-05-15"));

      assertEquals(
          "2026-05-01 2026-05-15", NotionPageViewer.of(page).propertyAsPlainText("Sprint"));
    }

    @Test
    @DisplayName("plain text from files joins extracted urls")
    void plainText_fromFiles_joinsUrls() {
      FileData external = FileData.builder().externalUrl("https://x.com/a.pdf").build();
      FileData hosted = new FileData();
      hosted.setType("file");
      NotionFile hostedFile = new NotionFile();
      hostedFile.setUrl("https://files.notion.so/b.pdf");
      hosted.setFile(hostedFile);

      Page page = pageWith("Files", NotionProperties.files(external, hosted));

      assertEquals(
          "https://x.com/a.pdf, https://files.notion.so/b.pdf",
          NotionPageViewer.of(page).propertyAsPlainText("Files"));
    }

    @Test
    @DisplayName("plain text from formula property")
    void plainText_fromFormulaProperty() {
      FormulaProperty.FormulaValue formulaValue = new FormulaProperty.FormulaValue();
      formulaValue.setType("number");
      formulaValue.setNumber(3.5);
      FormulaProperty formula = new FormulaProperty();
      formula.setFormula(formulaValue);

      Page page = pageWith("Result", formula);

      assertEquals("3.5", NotionPageViewer.of(page).propertyAsPlainText("Result"));
    }

    @Test
    @DisplayName("plain text from unique id property")
    void plainText_fromUniqueIdProperty() {
      UniqueIdProperty.UniqueIdValue uniqueIdValue = new UniqueIdProperty.UniqueIdValue();
      uniqueIdValue.setPrefix("TASK");
      uniqueIdValue.setNumber(BigInteger.valueOf(42));
      UniqueIdProperty uniqueId = new UniqueIdProperty();
      uniqueId.setUniqueId(uniqueIdValue);

      Page page = pageWith("ID", uniqueId);

      assertEquals("TASK-42", NotionPageViewer.of(page).propertyAsPlainText("ID"));
    }

    @Test
    @DisplayName("plain text from created by property")
    void plainText_fromCreatedByProperty() {
      User creator = new User();
      creator.setName("Alice");
      CreatedByProperty createdBy = new CreatedByProperty();
      createdBy.setCreatedBy(creator);

      Page page = pageWith("Created by", createdBy);

      assertEquals("Alice", NotionPageViewer.of(page).propertyAsPlainText("Created by"));
    }

    @Test
    @DisplayName("plain text from created time property")
    void plainText_fromCreatedTimeProperty() {
      CreatedTimeProperty createdTime = new CreatedTimeProperty();
      createdTime.setCreatedTime("2026-01-01T00:00:00.000Z");

      Page page = pageWith("Created time", createdTime);

      assertEquals(
          "2026-01-01T00:00:00.000Z",
          NotionPageViewer.of(page).propertyAsPlainText("Created time"));
    }

    @Test
    @DisplayName("plain text from place property")
    void plainText_fromPlaceProperty() {
      PlaceProperty.Place placeValue = new PlaceProperty.Place();
      placeValue.setName("Notion HQ");
      placeValue.setAddress("1 Main St");
      placeValue.setLat(30.12);
      placeValue.setLon(-60.72);
      PlaceProperty place = new PlaceProperty();
      place.setPlace(placeValue);

      assertEquals(
          "Notion HQ 1 Main St 30.12,-60.72",
          NotionPageViewer.of(pageWith("Place", place)).propertyAsPlainText("Place"));
    }

    @Test
    @DisplayName("plain text from verification property")
    void plainText_fromVerificationProperty() {
      User verifier = new User();
      verifier.setId("user-verify");
      DateData verificationDate = new DateData();
      verificationDate.setStart("2026-05-05");
      VerificationProperty.VerificationValue verificationValue =
          new VerificationProperty.VerificationValue();
      verificationValue.setState("verified");
      verificationValue.setVerifiedBy(verifier);
      verificationValue.setDate(verificationDate);
      VerificationProperty verification = new VerificationProperty();
      verification.setVerification(verificationValue);

      assertEquals(
          "verified user-verify 2026-05-05",
          NotionPageViewer.of(pageWith("Verification", verification))
              .propertyAsPlainText("Verification"));
    }

    @Test
    @DisplayName("plain text from rollup property")
    void plainText_fromRollupProperty() {
      RollupProperty.RollupValue rollupValue = new RollupProperty.RollupValue();
      rollupValue.setType("number");
      rollupValue.setNumber(11.0);
      RollupProperty rollup = new RollupProperty();
      rollup.setRollup(rollupValue);

      assertEquals(
          "11.0", NotionPageViewer.of(pageWith("Rollup", rollup)).propertyAsPlainText("Rollup"));
    }

    @Test
    @DisplayName("number returns numeric value")
    void number_returnsValue() {
      Page page = pageWith("Priority", NotionProperties.number(42));

      assertEquals(42, NotionPageViewer.of(page).number("Priority"));
    }

    @Test
    @DisplayName("number null when null value")
    void number_nullWhenNull() {
      NumberProperty empty = new NumberProperty();
      Page page = pageWith("Priority", empty);

      assertNull(NotionPageViewer.of(page).number("Priority"));
    }

    @Test
    @DisplayName("checkbox returns true")
    void checkbox_returnsTrue() {
      Page page = pageWith("Done", NotionProperties.checkbox(true));

      assertTrue(NotionPageViewer.of(page).checkbox("Done"));
    }

    @Test
    @DisplayName("checkbox false value preserved")
    void checkbox_falsePreserved() {
      Page page = pageWith("Done", NotionProperties.checkbox(false));

      assertFalse(NotionPageViewer.of(page).checkbox("Done"));
    }

    @Test
    @DisplayName("select returns option name")
    void select_returnsOptionName() {
      Page page = pageWith("Status", NotionProperties.select("In progress"));

      assertEquals("In progress", NotionPageViewer.of(page).select("Status"));
    }

    @Test
    @DisplayName("select null when no option chosen")
    void select_nullWhenNoOption() {
      SelectProperty empty = new SelectProperty();
      Page page = pageWith("Status", empty);

      assertNull(NotionPageViewer.of(page).select("Status"));
    }

    @Test
    @DisplayName("multi select returns option names in order")
    void multiSelect_returnsNamesInOrder() {
      Page page = pageWith("Tags", NotionProperties.multiSelect("urgent", "review", "draft"));

      assertEquals(
          List.of("urgent", "review", "draft"), NotionPageViewer.of(page).multiSelect("Tags"));
    }

    @Test
    @DisplayName("multi select empty list when absent")
    void multiSelect_emptyWhenAbsent() {
      assertTrue(NotionPageViewer.of(new Page()).multiSelect("Tags").isEmpty());
    }

    @Test
    @DisplayName("multi select empty list when null values")
    void multiSelect_emptyWhenNullValues() {
      MultiSelectProperty empty = new MultiSelectProperty();
      Page page = pageWith("Tags", empty);

      assertTrue(NotionPageViewer.of(page).multiSelect("Tags").isEmpty());
    }

    @Test
    @DisplayName("status returns name")
    void status_returnsName() {
      Page page = pageWith("Stage", NotionProperties.status("Done"));

      assertEquals("Done", NotionPageViewer.of(page).status("Stage"));
    }

    @Test
    @DisplayName("url accessor returns value")
    void url_returnsValue() {
      Page page = pageWith("Website", NotionProperties.url("https://example.com"));

      assertEquals("https://example.com", NotionPageViewer.of(page).url("Website"));
    }

    @Test
    @DisplayName("email returns value")
    void email_returnsValue() {
      Page page = pageWith("Contact", NotionProperties.email("a@b.com"));

      assertEquals("a@b.com", NotionPageViewer.of(page).email("Contact"));
    }

    @Test
    @DisplayName("phone number returns value")
    void phoneNumber_returnsValue() {
      Page page = pageWith("Phone", NotionProperties.phoneNumber("+1-555-1234"));

      assertEquals("+1-555-1234", NotionPageViewer.of(page).phoneNumber("Phone"));
    }

    @Test
    @DisplayName("date returns full payload")
    void date_returnsPayload() {
      Page page = pageWith("Due", NotionProperties.date("2026-05-05"));

      DateData data = NotionPageViewer.of(page).date("Due");
      assertNotNull(data);
      assertEquals("2026-05-05", data.getStart());
      assertNull(data.getEnd());
    }

    @Test
    @DisplayName("date start and end exposed for ranges")
    void dateRange_startAndEnd() {
      Page page = pageWith("Sprint", NotionProperties.dateRange("2026-05-01", "2026-05-15"));

      NotionPageViewer view = NotionPageViewer.of(page);
      assertEquals("2026-05-01", view.dateStart("Sprint"));
      assertEquals("2026-05-15", view.dateEnd("Sprint"));
    }

    @Test
    @DisplayName("date end null for single day")
    void dateEnd_nullForSingleDay() {
      Page page = pageWith("Due", NotionProperties.date("2026-05-05"));

      assertNull(NotionPageViewer.of(page).dateEnd("Due"));
    }

    @Test
    @DisplayName("people returns user ids in order")
    void people_returnsIds() {
      Page page = pageWith("Owners", NotionProperties.people("user-1", "user-2"));

      assertEquals(
          NotionProperties.people("user-1", "user-2").getPeople(),
          NotionPageViewer.of(page).people("Owners"));
    }

    @Test
    @DisplayName("people empty when absent")
    void people_emptyWhenAbsent() {
      assertTrue(NotionPageViewer.of(new Page()).people("Owners").isEmpty());
    }

    @Test
    @DisplayName("relation returns related ids")
    void relation_returnsIds() {
      Page page = pageWith("Project", NotionProperties.relation("page-a", "page-b"));

      assertEquals(List.of("page-a", "page-b"), NotionPageViewer.of(page).relation("Project"));
    }

    @Test
    @DisplayName("files returns file payloads")
    void files_returnsPayloads() {
      FileData a = FileData.builder().externalUrl("https://x.com/a.pdf").build();
      FileData b = FileData.builder().externalUrl("https://x.com/b.pdf").build();
      Page page = pageWith("Attachments", NotionProperties.files(a, b));

      List<FileData> files = NotionPageViewer.of(page).files("Attachments");
      assertEquals(2, files.size());
      assertSame(a, files.get(0));
      assertSame(b, files.get(1));
    }

    @Test
    @DisplayName("file urls extracted preferring external")
    void fileUrls_preferExternal() {
      FileData external = FileData.builder().externalUrl("https://x.com/a.pdf").build();

      FileData hosted = new FileData();
      hosted.setType("file");
      NotionFile nf = new NotionFile();
      nf.setUrl("https://files.notion.so/b.pdf");
      hosted.setFile(nf);

      Page page = pageWith("Files", NotionProperties.files(external, hosted));

      assertEquals(
          List.of("https://x.com/a.pdf", "https://files.notion.so/b.pdf"),
          NotionPageViewer.of(page).fileUrls("Files"));
    }

    @Test
    @DisplayName("file urls skip files with no url")
    void fileUrls_skipNoUrl() {
      FileData blank = new FileData();
      blank.setType("external");
      blank.setExternal(new ExternalFile());

      FileData ok = FileData.builder().externalUrl("https://x.com/a.pdf").build();

      Page page = pageWith("Files", NotionProperties.files(blank, ok));

      assertEquals(List.of("https://x.com/a.pdf"), NotionPageViewer.of(page).fileUrls("Files"));
    }

    @Test
    @DisplayName("system property accessors expose user and time payloads")
    void systemProperties_exposed() {
      User creator = new User();
      creator.setId("user-create");
      CreatedByProperty createdBy = new CreatedByProperty();
      createdBy.setCreatedBy(creator);

      CreatedTimeProperty createdTime = new CreatedTimeProperty();
      createdTime.setCreatedTime("2024-01-01T00:00:00.000Z");

      User editor = new User();
      editor.setId("user-edit");
      LastEditedByProperty lastEditedBy = new LastEditedByProperty();
      lastEditedBy.setLastEditedBy(editor);

      LastEditedTimeProperty lastEditedTime = new LastEditedTimeProperty();
      lastEditedTime.setLastEditedTime("2024-02-01T00:00:00.000Z");

      Page page = new Page();
      Map<String, PageProperty> props = new LinkedHashMap<>();
      props.put("Created by", createdBy);
      props.put("Created at", createdTime);
      props.put("Edited by", lastEditedBy);
      props.put("Edited at", lastEditedTime);
      page.setProperties(props);
    }
  }

  @Nested
  class TypedAccessorsMissing {

    @Test
    @DisplayName("typed accessors return empty or null when property name is unknown")
    void typedAccessors_nullWhenUnknown() {
      NotionPageViewer view = NotionPageViewer.of(new Page());

      assertEquals("", view.propertyAsPlainText("X"));
      assertNull(view.number("X"));
      assertFalse(view.checkbox("X"));
      assertNull(view.select("X"));
      assertTrue(view.multiSelect("X").isEmpty());
      assertNull(view.status("X"));
      assertNull(view.url("X"));
      assertNull(view.email("X"));
      assertNull(view.phoneNumber("X"));
      assertNull(view.date("X"));
      assertNull(view.dateStart("X"));
      assertNull(view.dateEnd("X"));
      assertTrue(view.people("X").isEmpty());
      assertTrue(view.relation("X").isEmpty());
      assertTrue(view.files("X").isEmpty());
      assertTrue(view.fileUrls("X").isEmpty());
      assertNull(view.formula("X"));
      assertNull(view.formulaNumber("X"));
      assertNull(view.formulaBoolean("X"));
      assertNull(view.formulaDate("X"));
    }

    @Test
    @DisplayName("typed accessors return empty or null when property has different type")
    void typedAccessors_nullForWrongType() {
      Page page = pageWith("Priority", NotionProperties.number(1));
      NotionPageViewer view = NotionPageViewer.of(page);

      assertFalse(view.checkbox("Priority"));
      assertNull(view.select("Priority"));
      assertNull(view.url("Priority"));
      assertTrue(view.multiSelect("Priority").isEmpty());
    }
  }

  @Nested
  class Formula {

    private static FormulaProperty formula(String type) {
      FormulaProperty.FormulaValue value = new FormulaProperty.FormulaValue();
      value.setType(type);
      FormulaProperty prop = new FormulaProperty();
      prop.setFormula(value);
      return prop;
    }

    @Test
    @DisplayName("string formula stringified")
    void stringFormula() {
      FormulaProperty f = formula("string");
      f.getFormula().setString("hello");

      assertEquals("hello", NotionPageViewer.of(pageWith("F", f)).formula("F"));
    }

    @Test
    @DisplayName("number formula stringified")
    void numberFormula_stringified() {
      FormulaProperty f = formula("number");
      f.getFormula().setNumber(3.14);

      assertEquals("3.14", NotionPageViewer.of(pageWith("F", f)).formula("F"));
    }

    @Test
    @DisplayName("boolean formula stringified")
    void booleanFormula_stringified() {
      FormulaProperty f = formula("boolean");
      f.getFormula().setBooleanValue(true);

      assertEquals("true", NotionPageViewer.of(pageWith("F", f)).formula("F"));
    }

    @Test
    @DisplayName("date formula returns start as string")
    void dateFormula_returnsStart() {
      FormulaProperty f = formula("date");
      DateData date = new DateData();
      date.setStart("2026-05-05");
      f.getFormula().setDate(date);

      assertEquals("2026-05-05", NotionPageViewer.of(pageWith("F", f)).formula("F"));
    }

    @Test
    @DisplayName("formula number typed accessor")
    void formulaNumber_typed() {
      FormulaProperty f = formula("number");
      f.getFormula().setNumber(7.0);

      assertEquals(7.0, NotionPageViewer.of(pageWith("F", f)).formulaNumber("F"));
    }

    @Test
    @DisplayName("formula number null when type mismatched")
    void formulaNumber_nullWhenMismatch() {
      FormulaProperty f = formula("string");
      f.getFormula().setString("not a number");

      assertNull(NotionPageViewer.of(pageWith("F", f)).formulaNumber("F"));
    }

    @Test
    @DisplayName("formula boolean typed accessor")
    void formulaBoolean_typed() {
      FormulaProperty f = formula("boolean");
      f.getFormula().setBooleanValue(false);

      assertEquals(Boolean.FALSE, NotionPageViewer.of(pageWith("F", f)).formulaBoolean("F"));
    }

    @Test
    @DisplayName("formula date typed accessor")
    void formulaDate_typed() {
      FormulaProperty f = formula("date");
      DateData date = new DateData();
      date.setStart("2026-05-05");
      f.getFormula().setDate(date);

      DateData out = NotionPageViewer.of(pageWith("F", f)).formulaDate("F");
      assertNotNull(out);
      assertEquals("2026-05-05", out.getStart());
    }

    @Test
    @DisplayName("formula null when value type unknown")
    void formula_nullWhenUnknownType() {
      FormulaProperty f = formula("mystery");

      assertNull(NotionPageViewer.of(pageWith("F", f)).formula("F"));
    }

    @Test
    @DisplayName("formula null when value null")
    void formula_nullWhenNull() {
      FormulaProperty empty = new FormulaProperty();
      empty.setFormula(null);

      assertNull(NotionPageViewer.of(pageWith("F", empty)).formula("F"));
    }
  }

  @Nested
  class Contains {

    @Test
    @DisplayName("matches title plain text case insensitively")
    void matchesTitleCaseInsensitive() {
      Page page = pageWith("title", NotionProperties.title("Project Atlas"));

      assertTrue(NotionPageViewer.of(page).contains("atlas"));
    }

    @Test
    @DisplayName("matches across multiple rich text segments in title")
    void matchesAcrossSegments() {
      TitleProperty title =
          NotionProperties.title(NotionText.plainText("Pro"), NotionText.plainText("ject Atlas"));
      Page page = pageWith("title", title);

      assertTrue(NotionPageViewer.of(page).contains("project"));
    }

    @Test
    @DisplayName("matches rich text property")
    void matchesRichText() {
      Page page = pageWith("Description", NotionProperties.richText("Build a SaaS"));

      assertTrue(NotionPageViewer.of(page).contains("saas"));
    }

    @Test
    @DisplayName("matches select option")
    void matchesSelect() {
      Page page = pageWith("Status", NotionProperties.select("In progress"));

      assertTrue(NotionPageViewer.of(page).contains("progress"));
    }

    @Test
    @DisplayName("matches status option")
    void matchesStatus() {
      Page page = pageWith("Stage", NotionProperties.status("Backlog"));

      assertTrue(NotionPageViewer.of(page).contains("backlog"));
    }

    @Test
    @DisplayName("matches multi select option")
    void matchesMultiSelect() {
      Page page = pageWith("Tags", NotionProperties.multiSelect("urgent", "review"));

      assertTrue(NotionPageViewer.of(page).contains("review"));
    }

    @Test
    @DisplayName("matches url property")
    void matchesUrl() {
      Page page = pageWith("Website", NotionProperties.url("https://notion.so/docs"));

      assertTrue(NotionPageViewer.of(page).contains("notion.so"));
    }

    @Test
    @DisplayName("matches email")
    void matchesEmail() {
      Page page = pageWith("Contact", NotionProperties.email("hello@example.com"));

      assertTrue(NotionPageViewer.of(page).contains("example.com"));
    }

    @Test
    @DisplayName("matches phone number")
    void matchesPhone() {
      Page page = pageWith("Phone", NotionProperties.phoneNumber("+1-555-9999"));

      assertTrue(NotionPageViewer.of(page).contains("555"));
    }

    @Test
    @DisplayName("matches date start")
    void matchesDateStart() {
      Page page = pageWith("Due", NotionProperties.date("2026-05-05"));

      assertTrue(NotionPageViewer.of(page).contains("2026-05"));
    }

    @Test
    @DisplayName("matches date end")
    void matchesDateEnd() {
      Page page = pageWith("Sprint", NotionProperties.dateRange("2026-05-01", "2026-05-31"));

      assertTrue(NotionPageViewer.of(page).contains("2026-05-31"));
    }

    @Test
    @DisplayName("matches file url")
    void matchesFileUrl() {
      FileData fd = FileData.builder().externalUrl("https://cdn.example.com/photo.png").build();
      Page page = pageWith("Files", NotionProperties.files(fd));

      assertTrue(NotionPageViewer.of(page).contains("cdn.example.com"));
    }

    @Test
    @DisplayName("matches formula stringified value")
    void matchesFormula() {
      FormulaProperty.FormulaValue value = new FormulaProperty.FormulaValue();
      value.setType("string");
      value.setString("Calculated answer");
      FormulaProperty f = new FormulaProperty();
      f.setFormula(value);
      Page page = pageWith("Result", f);

      assertTrue(NotionPageViewer.of(page).contains("calculated"));
    }

    @Test
    @DisplayName("matches unique id formatted display string")
    void matchesUniqueId() {
      UniqueIdProperty.UniqueIdValue value = new UniqueIdProperty.UniqueIdValue();
      value.setPrefix("TASK");
      value.setNumber(BigInteger.valueOf(123));
      UniqueIdProperty prop = new UniqueIdProperty();
      prop.setUniqueId(value);
      Page page = pageWith("ID", prop);

      NotionPageViewer view = NotionPageViewer.of(page);
      assertTrue(view.contains("TASK-123"));
      assertTrue(view.contains("task"));
    }

    @Test
    @DisplayName("returns false for no match")
    void noMatch() {
      Page page = pageWith("title", NotionProperties.title("Project Atlas"));

      assertFalse(NotionPageViewer.of(page).contains("zzz"));
    }

    @Test
    @DisplayName("false when no properties")
    void noProperties() {
      assertFalse(NotionPageViewer.of(new Page()).contains("anything"));
    }

    @Test
    @DisplayName("null keyword throws npe")
    void nullKeyword_throwsNpe() {
      Page page = pageWith("title", NotionProperties.title("X"));

      assertThrows(NullPointerException.class, () -> NotionPageViewer.of(page).contains(null));
    }

    @Test
    @DisplayName("ignores property types without searchable text")
    void ignoresUnsupportedTypes() {
      Page page = new Page();
      Map<String, PageProperty> props = new LinkedHashMap<>();
      props.put("Priority", NotionProperties.number(42));
      props.put("Done", NotionProperties.checkbox(true));
      props.put("People", NotionProperties.people("user-1"));
      page.setProperties(props);

      assertFalse(NotionPageViewer.of(page).contains("42"));
      assertFalse(NotionPageViewer.of(page).contains("true"));
      assertFalse(NotionPageViewer.of(page).contains("user"));
    }
  }
}
