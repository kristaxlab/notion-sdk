# Notion SDK for Java

Shared vocabulary for this project. Notion describes the same concept differently depending on the
endpoint, and several of the obvious words for these concepts are ambiguous or overloaded — this
file fixes one term per concept so the ADRs, the cookbook and the javadoc stay consistent.

This glossary is binding for code, Javadoc, docs, and agent prose. Use each listed term exactly.
Names on an `_Avoid_` line are rejected, not casual alternatives. If a concept you need is missing,
stop and ask — do not invent a term, and do not add a definition here until it has been agreed.

The vocabulary below covers API-wide conventions, page properties, data sources, the Markdown
endpoints, and the integration testkit; other areas are added as they get documented.

## Language

### API conventions

Terms that apply across the whole Notion API, not just to one area.

**Type-named value field**:
The JSON field of a polymorphic Notion object named after the value of its `type` field, holding
that variant's payload — `checkbox` in a checkbox property, `paragraph` in a paragraph block,
`number` in a number filter.
_Avoid_: value field, payload field, kind-named field.

### Page properties

**Property**:
A named value attached to a page, defined by the schema of the data source the page lives in.
_Avoid_: field, attribute, column (a column is the schema definition, not the value on the page),
page property (that names the union type below).

**Property type**:
The Notion classification of a property — `checkbox`, `relation`, `rich_text`, `title`, and so on.
Always qualified as "property type"; a bare "type" is ambiguous because it also means a Java type.
_Avoid_: property kind, kind.

**Property name**:
The human-readable label of a property, and the key under which its value appears in a page's
property map. Create and update property maps accept either this name or the property id as the
key. The property retrieve endpoint does not — it requires the property id.
_Avoid_: property label, property title, property key.

**Property id**:
The identifier Notion assigns to a property, used as the path parameter of the property retrieve
endpoint. Normally a short URL-encoded character sequence (`Zl%5B%3E`, `%5ExJ%60`). The one stable
exception is the `title` property, whose id is always literally `title` — Notion adds that property
automatically and it cannot be removed.
_Avoid_: property key, property name (the two are different things; only the id is valid on the
property retrieve endpoint).

**Paginated property**:
A property type whose value the property retrieve endpoint returns as a paginated list:
`relation`, `rich_text`, `title`, `people`, `rollup`.
Paginated properties are represented as a page property value in a page response, and as a page property
list in a property retrieve response.
_Avoid_: collection property, list property (`multi_select` and `files` are collections that never
paginate, so those terms mislead).

**Non-paginated property**:
Any other property type. Its value always arrives as a single object, even when that object contains
an array (`multi_select`, `files`).
_Avoid_: scalar property (inaccurate for the array-valued types), simple property.

**Truncated value**:
A paginated property value in a page response that Notion cut short, signalled by `has_more: true`.
The full value requires the property retrieve endpoint.
_Avoid_: partial value, incomplete value.

### Property representations

**Page property**:
Whatever the property retrieve endpoint returns, before the caller knows which representation
arrived: either a page property value or a page property list. Modelled by the sealed
`PageProperty` interface. Use this term only for that union — a property in general is just a
"property".
_Avoid_: retrieved property.

**Embedded property value**:
A property value as it appears in the property map of a page response — the entries of
`Page.getProperties()`, modelled by `PagePropertyValue`. Paginated properties may be truncated here.
To get the full list of values for paginated properties, property retrieve endpoint may be called to get such 
property as Page property list.
_Avoid_: page property (that names the union), inline property.

**Page property value**:
The single-object response of the property retrieve endpoint, returned for every non-paginated
property. Nearly identical to the embedded property value and modelled by the same
`PagePropertyValue` subclasses. 
_Avoid_: retrieved property value, property item (Notion's own word, but it collides with the SDK's
`PropertyItem` class), single-object shape.

**Page property list**:
The paginated response of the property retrieve endpoint, returned for every paginated property.
Modelled by `PagePropertyList` subclasses, which carry `results`, `has_more` and `next_cursor`.
For `rollup`, `results` are the related pages or values used to compute the aggregation; the
computed result lives in the property item metadata.
_Avoid_: retrieved property list, property item list, list shape.

**Property item metadata**:
The nested `property_item` object of a page property list, modelled by `PropertyItem`. It
carries the real property type, the property id, and `next_url`. Referred to by this full name only,
never as a bare "property item".
_Avoid_: property item, list header.

**Listed item**:
One element of the `results` array of a page property list, modelled by `ListedItem` subclasses
(`ListedRichText`, `ListedRelation`, `ListedPeople`, `ListedNumber`). Each listed item holds a
single value, not a list — one `RichText` run, one related page id, one user, one number.
_Avoid_: result item, property item, entry.

### Data sources

**Database**:
The container Notion exposes as `/databases`. It holds no schema of its own; since API version
`2025-09-03` it can host several data sources. It is the required parent when creating a data source.
_Avoid_: table, collection.

**Data source**:
The schema-bearing entity inside a database, exposed as `/data_sources`. It owns the property schema
and is the thing you query to get pages. Pages live in a data source, not directly in a database.
_Avoid_: database (the two are distinct since `2025-09-03`), table, view.

**Column**:
One entry of a data source's property schema — the *definition* of a property, modelled by
`DataSourcePropertySchema`. A property is the value that a page carries for a column.
_Avoid_: property (that is the value on the page), field, schema property.

### Markdown

**Enhanced Markdown**:
Notion-flavored Markdown covering all block types, including databases, embeds and advanced blocks.
The dialect the Markdown endpoints read and write.
_Avoid_: markdown (unqualified, when the Notion dialect specifically is meant), notion markdown.

**Replace content mode**:
The `updateAsMarkdown` mode that overwrites a page's entire content, sent as
`type: "replace_content"`.
_Avoid_: overwrite mode, full update.

**Update content mode**:
The `updateAsMarkdown` mode that applies a batch of targeted search-and-replace operations, sent as
`type: "update_content"`. Mutually exclusive with replace content mode.
_Avoid_: patch mode, partial update, edit mode.

### Endpoints

**Page retrieve endpoint**:
`GET /pages/{page_id}`, exposed as `pages().retrieve(...)`. Returns the page including a map of all
its property values.
_Avoid_: page endpoint, retrieve endpoint.

**Property retrieve endpoint**:
`GET /pages/{page_id}/properties/{property_id}`, exposed as `pages().retrieveProperty(...)` and
`pages().retrievePaginatedProperty(...)`. Returns one property.
_Avoid_: property endpoint, property item endpoint, retrieve endpoint.

### Pagination

**Start cursor**:
The opaque position passed as `start_cursor` to request the page of results following a previous
response. In the SDK it is the `startCursor` argument of `retrievePaginatedProperty`.
_Avoid_: cursor, offset, page token.

**Next cursor**:
The `next_cursor` value of a response, to be passed back as the start cursor of the following
request. `null` on the last page.
_Avoid_: cursor, continuation token.

**Page size**:
The `page_size` request parameter capping how many listed items one response may contain. Distinct
from a Notion page — always spelled "page size" in full.
_Avoid_: limit, batch size.

### Integration testkit

Terms for the live-API suite under `src/testIntegration`. How a test is written is in
[Testing Guide](docs/internals/testing-guide.md); how the kit is structured is in
[Testkit](docs/internals/testkit.md).

**Test session**:
The per-run store of integration-test prerequisites. Provisioners read it through
`TestSession.get(context)`; tests do not.
_Avoid_: test run, suite context.

**Prerequisite**:
A value a test needs that a provisioner injects — session user id, test page, or fixture page.
_Avoid_: fixture (that is one kind of prerequisite), dependency.

**Provisioner**:
The JUnit `ParameterResolver` that materializes one prerequisite and injects it. Tests never call
`TestSession`; provisioners do.
_Avoid_: helper, factory, setup method.

**Session user id**:
The user id of the integration token running the suite, resolved once via `users().me()`. In code
the identifier is `sessionUserId` (`ensureSessionUserId`).
_Avoid_: bot user id, bot id.

**Test id**:
The `IT-*` identifier taken from `@DisplayName` (`IT-8`, `IT-?`). Fixture pages are titled as this
value.
_Avoid_: test name, display name (the display name may contain more than the id).

**Test session parent id**:
The configured data source or database id under which the test session page is created. In code
the identifier is `parentId` (`notion.tests.session.parent.id`).
_Avoid_: Test Sessions Home, Test Session Home, test session parent (unqualified), prerequisites
page, test root.

**Test session page**:
The page created for a run under the test session parent id, under which test pages and fixture
pages live. When the parent is a database, its default template is applied. In code the identifier
is `testSessionPage` (`testSessionPageId`, `ensureTestSessionPage`).
_Avoid_: scratch page, session page, fixtures page.

**Fixture page**:
A prefilled page titled as a test id (`IT-8`), copied onto the test session page from the session
template.
_Avoid_: prerequisite page, test page (that is the empty page created for one test).

**Test page**:
A dedicated empty page created for one test, as a child of the test session page.
_Avoid_: dedicated page, empty page, scratch page.

**Notion Test Http Client**:
The `NotionClient` used for the call the test is checking, injected as `@NotionTestClient`.
_Avoid_: assertion client, test client (unqualified).

**Setup client**:
The `NotionClient` used for arrange-only calls, injected as `@NotionTestClient(forSetup = true)`.
_Avoid_: infra client, env client.

## Related documentation

- [ADR 0001: Complex hierarchy and custom deserializers for page properties](docs/adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md)
- [Cookbook: Page properties and pagination](docs/cookbook/page-properties.md)
- [Notion API constraints](docs/internals/notion-api-constraints.md) — rules the API enforces that the types cannot
- [Testing Guide](docs/internals/testing-guide.md) — how to run the suite and write a test
- [Testkit](docs/internals/testkit.md) — integration testkit internals
