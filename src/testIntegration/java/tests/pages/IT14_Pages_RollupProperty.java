package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.database.CreateDatabaseParams;
import io.kristaxlab.notion.model.datasource.properties.RollupFunctionType;
import io.kristaxlab.notion.model.page.Page;
import io.kristaxlab.notion.model.page.property.ListedItem;
import io.kristaxlab.notion.model.page.property.ListedNumber;
import io.kristaxlab.notion.model.page.property.ListedPeople;
import io.kristaxlab.notion.model.page.property.ListedRelation;
import io.kristaxlab.notion.model.page.property.ListedRichText;
import io.kristaxlab.notion.model.page.property.ListedUnknown;
import io.kristaxlab.notion.model.page.property.PagePropertyValue;
import io.kristaxlab.notion.model.page.property.RollupPropertyList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;
import testkit.ext.SessionUserId;

@Tag("heavy")
public class IT14_Pages_RollupProperty extends WithEmptyTestPage {

  private static final String TITLE_PROP = "Name";
  private static final String SCORE_PROP = "Score";
  private static final String DATE_PROP = "Due";
  private static final String DONE_PROP = "Done";
  private static final String PEOPLE_PROP = "Assignee";
  private static final String RELATION_PROP = "Related";
  private static final int CHILD_COUNT = 5;

  /**
   * One column per {@link RollupFunctionType}. Flattened functions ({@code show_original}, {@code
   * unique}, {@code show_unique}, {@code median}) target different source types so every modelled
   * listed item appears in {@code results}.
   */
  private static final List<RollupSpec> SPECS =
      List.of(
          spec("Average", SCORE_PROP, RollupFunctionType.AVERAGE, ListedRelation.class),
          spec("Checked", DONE_PROP, RollupFunctionType.CHECKED, ListedRelation.class),
          spec("Count", TITLE_PROP, RollupFunctionType.COUNT, ListedRelation.class),
          spec("Count values", TITLE_PROP, RollupFunctionType.COUNT_VALUES, ListedRelation.class),
          spec("Date range", DATE_PROP, RollupFunctionType.DATE_RANGE, ListedRelation.class),
          spec("Earliest date", DATE_PROP, RollupFunctionType.EARLIEST_DATE, ListedRelation.class),
          spec("Empty", TITLE_PROP, RollupFunctionType.EMPTY, ListedRelation.class),
          spec("Latest date", DATE_PROP, RollupFunctionType.LATEST_DATE, ListedRelation.class),
          spec("Max", SCORE_PROP, RollupFunctionType.MAX, ListedRelation.class),
          spec("Median", SCORE_PROP, RollupFunctionType.MEDIAN, ListedNumber.class),
          spec("Min", SCORE_PROP, RollupFunctionType.MIN, ListedRelation.class),
          spec("Not empty", TITLE_PROP, RollupFunctionType.NOT_EMPTY, ListedRelation.class),
          spec(
              "Percent checked",
              DONE_PROP,
              RollupFunctionType.PERCENT_CHECKED,
              ListedRelation.class),
          spec("Percent empty", TITLE_PROP, RollupFunctionType.PERCENT_EMPTY, ListedRelation.class),
          spec(
              "Percent not empty",
              TITLE_PROP,
              RollupFunctionType.PERCENT_NOT_EMPTY,
              ListedRelation.class),
          spec(
              "Percent unchecked",
              DONE_PROP,
              RollupFunctionType.PERCENT_UNCHECKED,
              ListedRelation.class),
          spec("Range", SCORE_PROP, RollupFunctionType.RANGE, ListedRelation.class),
          spec("Show original", TITLE_PROP, RollupFunctionType.SHOW_ORIGINAL, ListedRichText.class),
          spec("Show unique", TITLE_PROP, RollupFunctionType.SHOW_UNIQUE, ListedRichText.class),
          spec("Sum", SCORE_PROP, RollupFunctionType.SUM, ListedRelation.class),
          spec("Unchecked", DONE_PROP, RollupFunctionType.UNCHECKED, ListedRelation.class),
          spec("Unique", PEOPLE_PROP, RollupFunctionType.UNIQUE, ListedPeople.class));

  private String dataSourceId;
  private String userId;

  @BeforeEach
  public void setup(@SessionUserId String sessionUserId) {
    userId = sessionUserId;
    assertEquals(
        EnumSet.allOf(RollupFunctionType.class),
        SPECS.stream()
            .map(RollupSpec::function)
            .collect(() -> EnumSet.noneOf(RollupFunctionType.class), Set::add, Set::addAll),
        "SPECS must cover every RollupFunctionType");

    dataSourceId =
        getSetupClient()
            .databases()
            .create(
                CreateDatabaseParams.builder()
                    .inPage(getTestPageId())
                    .title("Test Database")
                    .isInline(true)
                    .properties(
                        p ->
                            p.title(TITLE_PROP)
                                .number(SCORE_PROP)
                                .date(DATE_PROP)
                                .checkbox(DONE_PROP)
                                .people(PEOPLE_PROP))
                    .build())
            .getDataSources()
            .get(0)
            .getId();

    getSetupClient()
        .dataSources()
        .update(dataSourceId, ds -> ds.properties(p -> p.relation(RELATION_PROP, dataSourceId)));

    getSetupClient()
        .dataSources()
        .update(
            dataSourceId,
            ds ->
                ds.properties(
                    p -> {
                      for (RollupSpec spec : SPECS) {
                        p.rollup(spec.name, RELATION_PROP, spec.source, spec.function);
                      }
                    }));
  }

  @Test
  @DisplayName("IT-14: Pages - Retrieve all rollup functions")
  public void testPaginatedPropertyRetrieve() {
    List<String> childIds = new ArrayList<>(CHILD_COUNT);
    for (int i = 0; i < CHILD_COUNT; i++) {
      childIds.add(createChild(i));
    }

    Page parent =
        getNotionClient()
            .pages()
            .create(
                page ->
                    page.inDataSource(dataSourceId)
                        .properties(
                            p -> p.title(TITLE_PROP, "Parent").relation(RELATION_PROP, childIds)));

    Set<Class<? extends ListedItem>> seen = new LinkedHashSet<>();
    for (RollupSpec spec : SPECS) {
      PagePropertyValue property = parent.getProperties().get(spec.name);
      assertNotNull(property, "Missing rollup property " + spec.name);
      seen.addAll(assertRollup(parent.getId(), property, spec));
    }

    assertTrue(seen.contains(ListedRelation.class), "Expected ListedRelation from aggregations");
    assertTrue(seen.contains(ListedRichText.class), "Expected ListedRichText from title flatten");
    assertTrue(seen.contains(ListedNumber.class), "Expected ListedNumber from median");
    assertTrue(seen.contains(ListedPeople.class), "Expected ListedPeople from unique people");
    assertFalse(seen.contains(ListedUnknown.class), "Unmodelled listed item: " + seen);
  }

  private Set<Class<? extends ListedItem>> assertRollup(
      String pageId, PagePropertyValue property, RollupSpec spec) {
    RollupPropertyList chunk =
        getNotionClient()
            .pages()
            .retrievePaginatedProperty(pageId, property.getId())
            .asRollupList();

    assertNotNull(chunk.getPropertyItem(), spec.name);
    assertEquals("rollup", chunk.getPropertyItem().getType(), spec.name);
    assertNotNull(chunk.getPropertyItem().getRollup(), spec.name);
    assertEquals(spec.function.getValue(), chunk.getPropertyItem().getRollup().getFunction());
    assertNotNull(chunk.getResults(), spec.name);
    assertFalse(chunk.getResults().isEmpty(), spec.name + " results");

    Set<Class<? extends ListedItem>> types = new LinkedHashSet<>();
    for (ListedItem item : chunk.getResults()) {
      assertInstanceOf(spec.resultType, item, spec.name + " item type=" + item.getType());
      assertEquals(expectedItemType(spec.resultType), item.getType(), spec.name);
      assertListedPayload(item, spec);
      types.add(item.getClass());
    }
    return types;
  }

  private void assertListedPayload(ListedItem item, RollupSpec spec) {
    if (item instanceof ListedRelation relation) {
      assertNotNull(relation.getRelation(), spec.name);
    } else if (item instanceof ListedRichText richText) {
      assertNotNull(richText.getRichText(), spec.name);
    } else if (item instanceof ListedNumber number) {
      assertNotNull(number.getNumber(), spec.name);
    } else if (item instanceof ListedPeople people) {
      assertNotNull(people.getPeople(), spec.name);
      assertEquals(userId, people.getPeople().getId(), spec.name);
    } else {
      fail(spec.name + " unexpected listed item " + item.getClass().getSimpleName());
    }
  }

  private static String expectedItemType(Class<? extends ListedItem> resultType) {
    if (resultType == ListedRelation.class) {
      return "relation";
    }
    if (resultType == ListedRichText.class) {
      return "title";
    }
    if (resultType == ListedNumber.class) {
      return "number";
    }
    if (resultType == ListedPeople.class) {
      return "people";
    }
    throw new IllegalArgumentException(resultType.getName());
  }

  private String createChild(int index) {
    return getSetupClient()
        .pages()
        .create(
            page ->
                page.inDataSource(dataSourceId)
                    .properties(
                        p ->
                            p.title(TITLE_PROP, "Child " + index)
                                .number(SCORE_PROP, index * 10)
                                .date(DATE_PROP, LocalDate.of(2026, 1, 1).plusDays(index))
                                .checkbox(DONE_PROP, index % 2 == 0)
                                .people(PEOPLE_PROP, userId)))
        .getId();
  }

  private static RollupSpec spec(
      String name,
      String source,
      RollupFunctionType function,
      Class<? extends ListedItem> resultType) {
    return new RollupSpec(name, source, function, resultType);
  }

  private record RollupSpec(
      String name,
      String source,
      RollupFunctionType function,
      Class<? extends ListedItem> resultType) {}
}
