# Data Sources — exploration notes

Knowledge gathered about the Data Sources area of the SDK (API version `2025-09-03`+). Used as a
reference for designing integration tests and understanding the endpoint surface.

## Concept

- A **data source** is the typed, schema-bearing entity that lives inside a **database** (parent).
  Since API `2025-09-03`, a single database can host **multiple data sources**.
- A data source owns the **property schema** (the typed columns) and is queried to return the
  **pages** (rows) that belong to it.
- The SDK exposes the endpoint via `DataSourcesEndpoint` / `DataSourcesEndpointImpl`. Note: at the
  time of exploration, `NotionClient` only wires up `users()`, `blocks()`, `pages()`,
  `fileUploads()` — a `dataSources()` accessor is referenced in javadoc/DSL examples
  (`client.dataSources()...`) but not yet present on `NotionClient`.

## Endpoint surface

`io.kristaxlab.notion.endpoints.DataSourcesEndpoint`
(impl: `io.kristaxlab.notion.endpoints.impl.DataSourcesEndpointImpl`)

| Method | HTTP | Path | Notes |
| --- | --- | --- | --- |
| `retrieve(String dataSourceId)` | GET | `/data_sources/{data_source_id}` | Returns `DataSource`. Validates id not null/empty. |
| `create(CreateDataSourceParams request)` | POST | `/data_sources` | Validates request not null. |
| `create(Consumer<CreateDataSourceParams.Builder> consumer)` | POST | `/data_sources` | Lambda overload; builds params then delegates. |
| `update(String id, UpdateDataSourceParams request)` | PATCH | `/data_sources/{data_source_id}` | Validates id and request. |
| `update(String id, Consumer<UpdateDataSourceParams.Builder> consumer)` | PATCH | `/data_sources/{data_source_id}` | Lambda overload. |
| `delete(String id)` | PATCH | `/data_sources/{data_source_id}` | Convenience: sends `inTrash = true`. Returns updated `DataSource`. |
| `restore(String id)` | PATCH | `/data_sources/{data_source_id}` | Convenience: sends `inTrash = false`. |
| `query(String id)` | POST | `/data_sources/{data_source_id}/query` | No filter/sort/pagination. |
| `query(String id, DataSourceQuery request)` | POST | `/data_sources/{data_source_id}/query` | Filter + sorts. |
| `query(String id, String startCursor, Integer pageSize)` | POST | `/data_sources/{data_source_id}/query` | Pagination only (new empty `DataSourceQuery`). |
| `query(String id, DataSourceQuery request, String startCursor, Integer pageSize)` | POST | `/data_sources/{data_source_id}/query` | Full control; applies `startCursor`/`pageSize` onto the request when non-null. |
| `retrieveTemplates(String id)` | GET | `/data_sources/{data_source_id}/templates` | Returns `Templates`. Validates id not null/empty. |

Validation is enforced via `io.kristaxlab.notion.endpoints.util.Validator`
(`checkNotNull`, `checkNotNullOrEmpty`) — these fail fast before any HTTP call.

## Models

### `DataSource` (response) — extends `NotionObject`

Fields: `title` (`List<RichText>`), `description` (`List<RichText>`), `databaseParent`
(`Parent`), `icon` (`Icon`), `cover` (`Cover`), `properties`
(`Map<String, DataSourcePropertySchema>`), `isInline` (`Boolean`), `url` (`String`),
`publicUrl` (`String`).

### `CreateDataSourceParams` (request)

- Fields: `parent` (`Parent`), `title` (`List<RichText>`), `properties`
  (`Map<String, DataSourcePropertySchema>`), `icon` (`Icon`).
- Builder highlights:
  - `inDatabase(parentDatabaseId)` → shortcut for `parent(Parent.databaseParent(id))`. Parent for
    a data source is a **database**, not a page/data source.
  - `title(String)` / `title(List<RichText>)`.
  - `properties(Map<...>)`, `properties(Consumer<NotionSchemaBuilder>)` (preferred fluent DSL),
    and `property(name, schema)` single-entry escape hatch.
  - `icon(Icon)`.

### `UpdateDataSourceParams` (request)

- Fields: `parent` (`Parent`), `title` (`List<RichText>`), `properties` (annotated
  `@JsonInclude(NON_NULL, content = ALWAYS)` so explicit `null` values are serialized — that is how
  a column is **deleted**), `icon` (`Icon`), `inTrash` (`Boolean`).
- Static helpers: `fromProperty(nameOrId, schema)` (single-column update), `builder()`.
- Builder highlights: `dataSourceTitle(String/List)`, `icon(Icon)`, `inTrash(Boolean)`,
  `properties(Map/Consumer)`, `property(name, schema)`.
- Caveat: the **builder does not expose `parent`** even though the model field exists, so "move a
  data source to another database" is only reachable by setting `parent` directly on the params
  object.

### `DataSourceQuery` (request)

- Fields: `filter` (`Filter`), `sorts` (`List<Sort>`), `startCursor` (`String`), `pageSize`
  (`Integer`, max 100).
- Helpers: `setFilter(property, filter)` (sets the filter's property then assigns it),
  `addSort(Sort)`, `addSort(property, SortDirection)`, `addSort(Timestamp, SortDirection)`.

### `DataSourcePageList` (response) — extends `NotionList<Page>`

- Holds the matched `Page` rows plus a `pageOrDataSource` object. Standard `NotionList` pagination
  fields (`results`, `has_more`, `next_cursor`) apply.

## Schema DSL — `NotionSchemaBuilder` (via `NotionSchema.schemaBuilder()`)

Maps a `property name-or-id -> DataSourcePropertySchema`. Names are easier to author; ids survive
renames. Available column factories:

- Text/number: `title`, `richText`, `number`, `number(NumberFormatType)`, `number(String format)`.
- Choice: `select(name, options...)`, `multiSelect(name, options...)` (also `List<SelectOption>`
  overloads), `status`.
- Scalars: `date`, `checkbox`, `url`, `email`, `phoneNumber`, `people`, `files`.
- Computed/relational: `formula(expression)`, `relation(name, dataSourceId)`,
  `relationDual(name, dataSourceId, syncedPropertyName)`,
  `rollup(name, relationProp, rollupProp, RollupFunctionType)`.
- Read-only/system: `createdTime`, `createdBy`, `lastEditedTime`, `lastEditedBy`, `button`,
  `place`, `verification`, `uniqueId`, `uniqueId(prefix)`.
- Schema editing: `property(name, schema)`, `properties(Map)`, `remove(nameOrId)` (maps to `null`
  to delete a column), `rename(nameOrId, newName)` (re-keys while preserving order).

Constraint: every data source must have **exactly one title column**.

## Query filters — `io.kristaxlab.notion.model.datasource.filter`

`Filter` is an abstract base (`@JsonTypeInfo` DEDUCTION, default impl `UnknownFilter`) with fields
`or` (`List<Filter>`), `and` (`List<Filter>`), and `property` (name or id). Compound AND/OR is
expressed through the `and`/`or` lists.

Concrete filter types: `CheckboxFilter`, `DateFilter`, `CreatedTimeFilter`, `LastEditedTimeFilter`,
`FilesFilter`, `FormulaFilter`, `NumberFilter`, `PeopleFilter`, `CreatedByFilter`,
`LastEditedByFilter`, `RelationFilter`, `RichTextFilter`, `RollupFilter`, `SelectFilter`,
`MultiSelectFilter`, `StatusFilter`, `IdFilter`, `PhoneNumberFilter`, `UrlFilter`.

Example condition surface (`NumberFilter`): `isEmpty`, `isNotEmpty`, `equals`, `doesNotEqual`,
`greaterThan`, `lessThan`, `greaterThanOrEqualTo`, `lessThanOrEqualTo` — each returns a configured
filter via static factories. Other filter types follow the same static-factory-per-condition shape.

## Sorting — `io.kristaxlab.notion.model.datasource.sort`

- `Sort`: fields `property`, `direction` (`"ascending"`/`"descending"`), `timestamp`. Factories:
  `Sort.by(property, SortDirection)` and `Sort.by(Timestamp, SortDirection)`.
- `SortDirection`: enum providing `ascending`/`descending` values.
- `Timestamp`: enum for `created_time` / `last_edited_time` timestamp sorts.

## Related context

- Parent factories (`io.kristaxlab.notion.model.common.Parent`): `workspaceParent`, `pageParent`,
  `databaseParent`, `dataSourceParent`, `blockParent`. Data source create uses **`databaseParent`**;
  pages can be parented by **`dataSourceParent`**.
- `DatabasesEndpoint` (the parent of data sources) exposes `create`, `update`, `retrieve`,
  `delete`, `restore` (`CreateDatabaseParams` / `UpdateDatabaseParams` / `Database`). A database is
  the required container when creating a data source.

## Integration-test infrastructure (for writing `DataSourcesIT`)

- `BaseIntegrationTest` (`@Tag("integration")`): provides `getNotion()` and per-test HTTP exchange
  log directories derived from the test class/method (display) name.
- `IntegrationTestAssisstant`: bootstraps a shared session — discovers the root page
  (`IT_NOTION_ROOT_PAGE_ID` env or hardcoded default), loads the `Integration Test Prerequisites`
  page, finds the `Integration Test Sessions` database, and creates a per-run "Integration tests"
  page. Helpers: `createPageForTests(name[, parent])`, `getPrerequisites()`,
  `loadFileFailIfMissing(...)`.
- `Prerequisites`: `externalImageUrl`, `imageUploadedViaUI`, `imageUploadedViaUIExpiryTime`,
  `emojiIcon`, `pageWithBlocksId`, `imageFileUploadId`, `testDatabaseId`, `userId`.
- Convention from `PagesIT`: each test is self-contained (provisions its own data), uses
  `@DisplayName("[IT-N]: <Resource> - <scenario>")`, and tests are tracked in a shared Notion
  database (see Resources). Tests requiring SDK features not yet exposed are explicitly documented
  as "not covered".
- Notion data specifics: a database page may have only one `unique_id` property; its numeric part
  is auto-incremented by Notion and cannot be set manually (the prefix is customizable).

## Resources

Notion tracking database for integration test status (retrieve / add / update via Notion MCP):
https://www.notion.so/kristalamenweb/2e8c5b968ec4804d8b91c99c1e04b0ca
