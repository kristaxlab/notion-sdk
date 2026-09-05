# Page properties and pagination

Read property values from a retrieved `Page`, fetch a single property with `PagesEndpoint`
(`client.pages()`), and page through long `rich_text`, `relation`, `title`, `people` and `rollup`
values.

Notion returns property values in two different representations, and which one you get depends on
the endpoint **and** on the property type. **Paginated properties** are `rich_text`, `relation`,
`title`, `people` and `rollup`; every other property type is **non-paginated**.

| SDK method | HTTP endpoint | Returns | What comes back |
| --- | --- | --- | --- |
| `pages().retrieve(pageId)` | `GET /pages/{page_id}` | `Page` | `Map<String, PagePropertyValue>` under `getProperties()`; a paginated property may be truncated (`has_more`) |
| `pages().retrieveProperty(pageId, propertyId)` | `GET /pages/{page_id}/properties/{property_id}` | `PageProperty` | a `PagePropertyValue` subclass for a non-paginated property, a `PagePropertyList` subclass for a paginated one |
| `pages().retrievePaginatedProperty(pageId, propertyId)` | `GET /pages/{page_id}/properties/{property_id}` | `PagePropertyList` | the first page of `results`, plus `has_more` / `next_cursor` |
| `pages().retrievePaginatedProperty(pageId, propertyId, startCursor, pageSize)` | `GET /pages/{page_id}/properties/{property_id}` | `PagePropertyList` | one page of `results` starting at `startCursor`, at most `pageSize` listed items |

Only `retrieveProperty` returns the union type `PageProperty`, so it is the only method whose result
you have to narrow. `retrievePaginatedProperty` hits the same endpoint but commits to
`PagePropertyList`, and it is the only method that accepts a start cursor and a page size. See
[ADR 0001](../adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md) for why the
model is split this way, and [CONTEXT.md](../../CONTEXT.md) for the terms used on this page.

## Read properties from a retrieved page

```java
Page page = client.pages().retrieve("page-id");

Map<String, PagePropertyValue> properties = page.getProperties();
CheckboxProperty done = properties.get("Done").as(CheckboxProperty.class);
boolean isDone = Boolean.TRUE.equals(done.getCheckbox());
```

`NotionPageViewer` removes the casting and null-checking boilerplate:

```java
NotionPageViewer viewer = NotionPageViewer.of(client.pages().retrieve("page-id"));

String title = viewer.title();
boolean done = viewer.checkbox("Done");
String status = viewer.select("Status");
List<String> relatedIds = viewer.relation("Children Pages");
String notes = viewer.propertyAsPlainText("Notes");
```

## Detect a truncated property value

A page response caps paginated property values. When the value did not fit, Notion sets
`has_more: true` and you must go to the property retrieve endpoint for the rest.

```java
RelationProperty relation =
    client.pages().retrieve("page-id")
        .getProperties().get("Children Pages")
        .as(RelationProperty.class);

List<String> ids;
if (Boolean.TRUE.equals(relation.getHasMore())) {
  ids = collectAll(client, "page-id", relation.getId(), ListedRelation.class)
      .stream().map(item -> item.getRelation().getId()).toList();
} else {
  ids = relation.getRelation().stream().map(RelationValue::getId).toList();
}
```

`collectAll` is the reusable pagination helper defined further down this page. Only paginated
properties can be truncated; a non-paginated property value in a page response is always complete.

## Find the property id

`retrieveProperty` and `retrievePaginatedProperty` take a **property id**, which is normally a short
URL-encoded sequence such as `Zl%5B%3E`. Take it from the page response and pass `getId()` through
unchanged — the SDK decodes the argument and re-encodes the path segment for you.

```java
Page page = client.pages().retrieve("page-id");
String notesPropertyId = page.getProperties().get("Notes").getId();
```

Do **not** pass a property name. Notion's property retrieve endpoint does not treat names as ids the
way create/update property maps do. A name can return HTTP 200 with an empty `results` list instead
of an error. The one exception is `title`: that property's id is always literally `title`, because
Notion adds it automatically and it cannot be removed.

See [Notion API constraints](../internals/notion-api-constraints.md).

## Retrieve a non-paginated property

```java
String doneId = client.pages().retrieve("page-id").getProperties().get("Done").getId();
PageProperty property = client.pages().retrieveProperty("page-id", doneId);

CheckboxProperty checkbox = property.asValue(CheckboxProperty.class);
boolean done = Boolean.TRUE.equals(checkbox.getCheckbox());
```

## Narrow `PageProperty` when the type is unknown

Use `instanceof` pattern matching when the same code path handles both representations:

```java
PageProperty property = client.pages().retrieveProperty("page-id", propertyId);

if (property instanceof CheckboxProperty checkbox) {
  boolean done = Boolean.TRUE.equals(checkbox.getCheckbox());
} else if (property instanceof RichTextProperty richText) {
  // page property value: a List<RichText> under getRichText()
  String text = richText.getRichText().stream()
      .map(RichText::getPlainText)
      .collect(Collectors.joining());
} else if (property instanceof RichTextPropertyList richTextList) {
  // page property list: one RichText per listed item, possibly paginated
  String firstPageText = richTextList.getResults().stream()
      .map(item -> item.getRichText().getPlainText())
      .collect(Collectors.joining());
}
```

`asValue(Class)` and `asList(Class)` are the shorthand when you already know which side of the union
you expect:

```java
NumberProperty number = client.pages()
    .retrieveProperty("page-id", estimatePropertyId)
    .asValue(NumberProperty.class);

RelationPropertyList relations = client.pages()
    .retrieveProperty("page-id", relationPropertyId)
    .asList(RelationPropertyList.class);
```

## Paginate a long `rich_text` property

`retrievePaginatedProperty` calls the same endpoint but returns `PagePropertyList` directly, so no
narrowing is needed, and it is the only overload that accepts a cursor and a page size. Loop while
`has_more` is set, feeding `next_cursor` back in as the start cursor.

```java
String propertyId = client.pages().retrieve("page-id").getProperties().get("Notes").getId();

List<RichText> allNotes = new ArrayList<>();
String cursor = null;

do {
  RichTextPropertyList chunk = client.pages()
      .retrievePaginatedProperty("page-id", propertyId, cursor, 100)
      .asRichTextList();

  for (ListedRichText item : chunk.getResults()) {
    allNotes.add(item.getRichText());
  }

  cursor = Boolean.TRUE.equals(chunk.getHasMore()) ? chunk.getNextCursor() : null;
} while (cursor != null);
```

Each listed item is a `ListedRichText` wrapping a **single** `RichText` run — unlike
`RichTextProperty.getRichText()` on a page, which is a `List<RichText>`.

Omit the last two arguments to fetch the first page with the API default page size:

```java
RichTextPropertyList firstPage = client.pages()
    .retrievePaginatedProperty("page-id", propertyId)
    .asRichTextList();
```

## Paginate a long `relation` property

A `ListedRelation` carries the related page id in `getRelation().getId()`.

```java
String relationPropertyId =
    client.pages().retrieve("page-id").getProperties().get("Children Pages").getId();

List<String> relatedPageIds = new ArrayList<>();
String cursor = null;

do {
  RelationPropertyList chunk = client.pages()
      .retrievePaginatedProperty("page-id", relationPropertyId, cursor, 100)
      .asRelationList();

  for (ListedRelation item : chunk.getResults()) {
    relatedPageIds.add(item.getRelation().getId());
  }

  cursor = Boolean.TRUE.equals(chunk.getHasMore()) ? chunk.getNextCursor() : null;
} while (cursor != null);
```

Follow up with a page retrieve per id when you need the related pages themselves:

```java
List<String> relatedTitles = relatedPageIds.stream()
    .map(id -> NotionPageViewer.of(client.pages().retrieve(id)).title())
    .toList();
```

## Paginate `title` and `people`

`title` and `people` behave identically — the property retrieve endpoint always returns a page
property list for them, even when the value is short or empty.

```java
TitlePropertyList titleList = client.pages()
    .retrievePaginatedProperty("page-id", "title")
    .asTitleList();

String title = titleList.getResults().stream()
    .map(item -> item.getRichText().getPlainText())
    .collect(Collectors.joining());
```

```java
String assigneesPropertyId =
    client.pages().retrieve("page-id").getProperties().get("Assignees").getId();

PeoplePropertyList peopleList = client.pages()
    .retrievePaginatedProperty("page-id", assigneesPropertyId)
    .asPeopleList();

List<User> assignees = peopleList.getResults().stream()
    .map(ListedPeople::getPeople)
    .toList();
```

For a single-run title, reading `NotionPageViewer.title()` off the page is cheaper than a dedicated
property call.

## Paginate a `rollup` property

`RollupPropertyList.getResults()` is a list of `ListedItem`, not one fixed subclass. The concrete
type depends on the rollup function: aggregations typically yield `ListedRelation`, while functions
that flatten the rolled-up property (`show_original`, `show_unique`, `unique`, `median`) yield that
property's listed item — `ListedRichText`, `ListedNumber`, `ListedPeople`, and so on. Narrow with
`ListedItem.as(Class)` (or `instanceof`) the same way you narrow a `PagePropertyValue`.

```java
String countPropertyId =
    client.pages().retrieve("page-id").getProperties().get("Count").getId();

RollupPropertyList chunk = client.pages()
    .retrievePaginatedProperty("page-id", countPropertyId)
    .asRollupList();

for (ListedItem item : chunk.getResults()) {
  if (item instanceof ListedRelation) {
    String relatedId = item.as(ListedRelation.class).getRelation().getId();
  } else if (item instanceof ListedRichText) {
    String text = item.as(ListedRichText.class).getRichText().getPlainText();
  }
}
```

## A reusable "collect all pages" helper

The loop above is the same for every paginated property, so it is worth extracting once. `ListedItem`
is the common supertype of all listed items.

```java
private static final int MAX_PAGE_SIZE = 100;

static <L extends ListedItem> List<L> collectAll(
    NotionClient client, String pageId, String propertyId, Class<L> itemType) {

  List<L> all = new ArrayList<>();
  String cursor = null;

  do {
    PagePropertyList<?, ?> chunk =
        client.pages().retrievePaginatedProperty(pageId, propertyId, cursor, MAX_PAGE_SIZE);

    for (ListedItem item : chunk.getResults()) {
      all.add(item.as(itemType));
    }

    cursor = Boolean.TRUE.equals(chunk.getHasMore()) ? chunk.getNextCursor() : null;
  } while (cursor != null);

  return all;
}
```

Call sites stay one-liners:

```java
List<RichText> notes = collectAll(client, pageId, notesPropertyId, ListedRichText.class)
    .stream().map(ListedRichText::getRichText).toList();

List<String> childIds = collectAll(client, pageId, relationPropertyId, ListedRelation.class)
    .stream().map(item -> item.getRelation().getId()).toList();
```

`ListedItem.as` throws `ClassCastException` if the property turns out to be a different paginated
property than expected, which surfaces a wrong property id immediately instead of silently returning
nothing. For a rollup, pass the listed-item class that function actually returns.

## Inspect the property item metadata

Every `PagePropertyList` exposes the nested `property_item` object, which tells you the real
property type — the top-level `type` of a page property list is always `"property_item"`.

```java
PagePropertyList<?, ?> list = client.pages().retrievePaginatedProperty("page-id", propertyId);

list.getType();                      // always "property_item"
list.getPropertyItem().getType();    // "rich_text", "relation", "title", "people" or "rollup"
list.getPropertyItem().getId();      // the property id
list.getPropertyItem().getNextUrl(); // API-provided next-page URL, informational
list.getHasMore();
list.getNextCursor();                // null on the last page
```

Pass `next_cursor` to `retrievePaginatedProperty` rather than calling `next_url` directly; the SDK
builds the request for you.

## Handle property types the SDK does not know

Unknown property types deserialize into fallback classes instead of failing, so a property type
introduced by Notion after this SDK release will not break a read.

```java
PageProperty property = client.pages().retrieveProperty("page-id", propertyId);

if (property instanceof UnknownProperty unknown) {
  // unsupported non-paginated property; unknown.getType() reports what Notion sent
} else if (property instanceof UnknownPropertyList unknownList) {
  // unsupported paginated property; listed items are bare ListedItem without a typed payload
}
```

## Related cookbook pages

- [Creating pages](creating-pages.md)
- [Reading page content](reading-content.md)
- [Updating pages](updating-pages.md)
- [CONTEXT.md](../../CONTEXT.md) — glossary
- [Back to README](../../README.md#cookbook)
