# Search by title

Search pages and data sources shared with the connection using `SearchEndpoint`
(`client.search()`). Matches are by **title** substring across the workspace — not inside one data
source. To filter rows in a specific data source, use
[Query a data source](https://developers.notion.com/reference/query-a-data-source) instead.

Official reference: [Search by title](https://developers.notion.com/reference/post-search).

## Search with a query

```java
SearchList results = client.search().search("meeting notes");
```

An empty body returns everything shared with the connection (subject to Notion's search limits):

```java
SearchList everything = client.search().search();
```

## Filter and sort

Limit to pages or data sources, optionally include trash, and choose a sort:

```java
SearchList pages = client.search().search(p -> p
    .query("roadmap")
    .filter(SearchFilter.pages())
    .sort(SearchSort.byLastEditedTimeDescending()));

SearchList dataSources = client.search().search(p -> p
    .query("tasks")
    .filter(SearchFilter.dataSources())
    .sort(SearchSort.byRelevance()));
```

Trash-only (or trash plus an object filter):

```java
SearchList trashed = client.search().search(p -> p
    .query("old draft")
    .filter(SearchFilter.pages().withInTrash(true))
    .sort(SearchSort.byLastEditedTimeDescending()));
```

`SearchFilter.inTrash(true)` is the trash-only shape with no object constraint.

## Partition pages and data sources

Results are interleaved `page` and `data_source` objects. `NotionSearchViewer` splits one response:

```java
NotionSearchViewer view = NotionSearchViewer.of(client.search().search("notes"));

List<Page> pages = view.pages();
List<DataSource> dataSources = view.dataSources();
```

When you paginate yourself, fold the next chunk into the viewer with `merge`:

```java
NotionSearchViewer view = NotionSearchViewer.of(firstPage);
view = view.merge(nextPage);           // SearchList
view = view.merge(nextPage.getResults()); // or List<NotionObject>
```

## Paginate

Start cursor and page size travel in the JSON body (not as query parameters):

```java
List<NotionObject> all = new ArrayList<>();
String cursor = null;

do {
  SearchList chunk = client.search().search(
      SearchParams.builder().query("notes").filter(SearchFilter.pages()).build(),
      cursor,
      50);
  if (chunk.getResults() != null) {
    all.addAll(chunk.getResults());
  }
  cursor = chunk.getNextCursor();
} while (cursor != null);
```

Check `RequestStatus` on each response when a very large result set may have been capped
(`type` = `incomplete`). That signal also appears on data-source query responses (`QueryList`).

## Indexing delay

Search is eventually consistent. A page created, shared, or trashed moments earlier may be missing
from results. For create-then-search flows, see [Notion API constraints](../internals/notion-api-constraints.md#search)
and `SearchPoller` in the Javadoc — most callers searching existing content do not need a poller.

## Related cookbook pages

- [Reading page content](reading-content.md)
- [Page properties and pagination](page-properties.md)
- [Comments](comments.md)
- [Back to README](../../README.md#cookbook)
