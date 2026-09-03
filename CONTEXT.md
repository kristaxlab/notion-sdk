# Notion SDK for Java

Shared vocabulary for this project. Notion describes the same concept differently depending on the
endpoint, and several of the obvious words for these concepts are ambiguous or overloaded — this
file fixes one term per concept so the ADRs, the cookbook and the javadoc stay consistent.

The vocabulary below covers API-wide conventions and page properties; other areas are added as they
get documented.

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
property map. A URL-encoded property name is usually accepted wherever a property id is expected.
_Avoid_: property label, property title, property key.

**Property id**:
The identifier Notion assigns to a property, used as the path parameter of the property retrieve
endpoint. Normally a short URL-encoded character sequence (`Zl%5B%3E`, `%5ExJ%60`). The one stable
exception is the `title` property, whose id is always literally `title` — Notion adds that property
automatically and it cannot be removed.
_Avoid_: property key, property name (a name may be accepted in place of an id, but the two are
different things).

**Paginated property**:
A property type whose value the property retrieve endpoint returns as a paginated list:
`relation`, `rich_text`, `title`, `people`.
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
_Avoid_: retrieved property list, property item list, list shape.

**Property item metadata**:
The nested `property_item` object of a page property list, modelled by `PropertyItem`. It
carries the real property type, the property id, and `next_url`. Referred to by this full name only,
never as a bare "property item".
_Avoid_: property item, list header.

**Listed item**:
One element of the `results` array of a page property list, modelled by `ListedItem` subclasses
(`ListedRichText`, `ListedRelation`, `ListedPeople`). Each listed item holds a single value, not a
list — one `RichText` run, one related page id, one user.
_Avoid_: result item, property item, entry.

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

## Related documentation

- [ADR 0001: Complex hierarchy and custom deserializers for page properties](docs/adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md)
- [Cookbook: Page properties and pagination](docs/cookbook/06-page-properties.md)
