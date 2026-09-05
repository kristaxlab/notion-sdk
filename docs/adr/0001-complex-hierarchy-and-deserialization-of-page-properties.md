# ADR 0001: Complex hierarchy and custom deserializers for `PageProperty`, `PagePropertyValue`, `PagePropertyList`

**Status:** Accepted

Terms used below — *page property*, *embedded property value*, *page property value*, *page
property list*, *paginated property*, *non-paginated property*, *property item metadata*, *listed
item* — are defined in [CONTEXT.md](../../CONTEXT.md).

## Context

Notion exposes page property values through two endpoints that describe the *same* domain objects in
two different representations. The divergence is not uniform across property types: a non-paginated
property such as `checkbox` or `select` comes back in the same shape from both endpoints, while the
five paginated properties — `relation`, `people`, `rich_text`, `title` and `rollup` — are represented
differently depending on which endpoint returned them. Those five are the entire reason this model
needs more than one class.

### Page retrieve endpoint — `GET /pages/{page_id}`

The page carries a property map, and every entry has the same uniform layout regardless of the
property type: an `id`, a `type` holding the property type, and a type-named value field.

```json
"Done":    { "id": "IuYw", "type": "checkbox", "checkbox": false },
"Notes":   { "id": "Zl%5B%3E", "type": "rich_text", "rich_text": [ { "type": "text", ... } ] },
"Parent":  { "id": "IV%7Dn", "type": "relation", "relation": [], "has_more": false }
```

These entries are *embedded property values*. For paginated properties the array may be truncated;
`has_more: true` signals that the page response does not contain the full value.

### Property retrieve endpoint — `GET /pages/{page_id}/properties/{property_id}`

This endpoint returns one of two different representations depending on the property type:

- **Non-paginated properties** (`checkbox`, `select`, `number`, `email`, …) come back as a *page
  property value*: a single object, nearly identical to the embedded property value, with `type`
  still holding the property type.

  ```json
  { "object": "property_item", "type": "checkbox", "id": "Done", "checkbox": false }
  ```

- **Paginated properties** (`relation`, `title`, `rich_text`, `people`, `rollup`) come back as a *page property
  list*: a `results` array of listed items plus `has_more` / `next_cursor`. Here `type` is the
  literal string `"property_item"`, and the real property type is nested inside the property item
  metadata under `property_item.type`. The individual values live in `results[]`, not in a
  type-named value field.

  ```json
  {
    "object": "list",
    "results": [ { "object": "property_item", "type": "rich_text", "id": "Ou%3Av", "rich_text": { ... } } ],
    "next_cursor": "HNuaMx",
    "has_more": true,
    "type": "property_item",
    "property_item": { "id": "Ou%3Av", "type": "rich_text", "next_url": "...", "rich_text": {} }
  }
  ```

A Java model therefore has to carry both representations without forcing callers to guess which one
they are holding, and without duplicating the property types that never diverge.

## Decision

Model the two representations as two distinct Java classes and unite them under an interface that
serves as the return type of the property retrieve endpoint.

| Java type | Represents | Base hierarchy |
| --- | --- | --- |
| `PagePropertyValue` | Embedded property values **and** page property values — the two are interchangeable | `BaseNotionObject` → `PagePropertyValue` |
| `PagePropertyList<I, L>` | Page property lists | `BaseNotionObject` → `NotionList<L>` → `PagePropertyList` |
| `PageProperty` | Union of the two; the declared return type of `retrieveProperty` | sealed interface, `permits PagePropertyValue, PagePropertyList` |

- `Page.properties` is typed `Map<String, PagePropertyValue>`. The page retrieve endpoint never
  yields a page property list, so callers reading a page are never exposed to the `PageProperty`
  union at all.
- `retrieveProperty(pageId, propertyId)` returns `PageProperty`, which resolves at runtime to a
  `PagePropertyValue` subclass for non-paginated properties and to a `PagePropertyList` subclass for
  paginated ones.
- `PageProperty` is an **interface**, not a class, because `PagePropertyValue` and
  `PagePropertyList` sit in different base hierarchies (a `BaseNotionObject` descendant vs. a
  `NotionList` descendant). An aggregating interface is the only way to pull them under a single
  nominal Java type.
- The list side of the hierarchy is **sealed**: `PageProperty` permits exactly two subtypes, and
  `PagePropertyList`, `PropertyItem` and `ListedItem` each permit only their own implementations.
  Beyond exhaustive pattern matching, sealing keeps the hierarchy readable — the permits clause
  lists every implementation right next to the abstraction. `PagePropertyValue` is declared
  `non-sealed` because its 25 subclasses are registered through Jackson `@JsonSubTypes` instead,
  which would make a permits clause pure duplication.

## Hierarchy schema

The union, and which representation each side carries:

```
                               PageProperty                  «sealed interface»
                   return type of retrieveProperty(...)
                                     │
                ┌────────────────────┴─────────────────────┐
                │                                          │
        PagePropertyValue                       PagePropertyList<I, L>
      «abstract non-sealed»                        «abstract sealed»
                │                                          │
   embedded property value                     page property list
   page property value                         (paginated properties only)
   (non-paginated properties)
```

Both sides in the context of the SDK's base model classes:

```
BaseNotionObject                                    object, requestId
│
├── NotionObject                                    id, parent, createdTime, …
│   └── Page                                        ← holds embedded property values
│         properties : Map<String, PagePropertyValue>
│
├── PagePropertyValue                               «non-sealed» @JsonTypeInfo on "type"
│   │                                               id + type-named value field
│   ├── CheckboxProperty          checkbox          Boolean
│   ├── RichTextProperty          rich_text         List<RichText>
│   ├── TitleProperty             title             List<RichText>
│   ├── RelationProperty          relation          List<RelationValue> + hasMore
│   ├── PeopleProperty            people            List<User>
│   ├── … 19 further property types
│   └── UnknownProperty           (no value field)  «defaultImpl»
│
└── NotionList<T>                                   results, type, hasMore, nextCursor
    └── PagePropertyList<I extends PropertyItem,    «sealed» custom deserializer
                         L extends ListedItem>       propertyItem : I
        ├── RelationPropertyList  <RelationPropertyItem, ListedRelation>
        ├── RichTextPropertyList  <RichTextPropertyItem, ListedRichText>
        ├── TitlePropertyList     <TitlePropertyItem,    ListedRichText>
        ├── PeoplePropertyList    <PeoplePropertyItem,   ListedPeople>
        ├── RollupPropertyList    <RollupPropertyItem,   ListedItem>
        └── UnknownPropertyList   <PropertyItem,         ListedItem>      «fallback»
```

The two type parameters of `PagePropertyList` are the two sealed supporting hierarchies:

```
PropertyItem            «sealed» @JsonTypeInfo on "type"    ← property item metadata
│                        id, type, nextUrl
├── RelationPropertyItem
├── RichTextPropertyItem
├── TitlePropertyItem
├── PeoplePropertyItem
├── RollupPropertyItem
└── UnknownPropertyItem                                     «defaultImpl»

ListedItem              «sealed» @JsonTypeInfo on "type"    ← listed items, results[]
│                        object, type, id
├── ListedRichText       richText : RichText                 one run, not a list
├── ListedRelation       relation : RelationValue            related page id
├── ListedPeople         people : User
├── ListedNumber         number : Double
└── ListedUnknown                                            «defaultImpl»
```

Which JSON node each Java type covers:

| JSON node | Concept | Java type |
| --- | --- | --- |
| An entry of the page's `properties` map | Embedded property value | `PagePropertyValue` subclass |
| A single-object property retrieve response | Page property value | `PagePropertyValue` subclass |
| A list-shaped property retrieve response | Page property list | `PagePropertyList` subclass |
| Its nested `property_item` | Property item metadata | `PropertyItem` subclass |
| An element of its `results` | Listed item | `ListedItem` subclass |

### Convenience method for paginated properties

`retrievePaginatedProperty(pageId, propertyId[, startCursor, pageSize])` calls the same
`GET /pages/{page_id}/properties/{property_id}` endpoint but declares `PagePropertyList` as its
return type. When a caller already knows the property is paginated, this avoids narrowing down from
`PageProperty`, and it is the only overload that accepts a start cursor and a page size.

Narrowing helpers keep call sites free of explicit casts: `PageProperty.asValue(Class)` /
`asList(Class)`, `PagePropertyValue.as(Class)`, `ListedItem.as(Class)`, and
`PagePropertyList.asRelationList()` / `asRichTextList()` / `asTitleList()` / `asPeopleList()` /
`asRollupList()`.

### Type resolution and deserialization

The project's preferred mechanism is `@JsonTypeInfo` on an `EXISTING_PROPERTY` named `type`. That
works directly for embedded property values and for page property values, where `type` holds the
property type. It does **not** work for page property lists, where `type` is always
`"property_item"` and the discriminating value sits in the nested `property_item.type` —
`@JsonTypeInfo` cannot read a nested field. Hence two custom deserializers:

- **`PagePropertyDeserializer`** (bound to `PageProperty`) decides which of the two representations
  arrived by checking for a `results` field (or a top-level array). It then delegates to
  `PagePropertyList` or to `PagePropertyValue`, letting the latter's `@JsonTypeInfo` /
  `@JsonSubTypes` take over.
- **`PagePropertyListDeserializer`** (bound to `PagePropertyList`) reads `property_item.type` and
  maps it to `RelationPropertyList`, `RichTextPropertyList`, `TitlePropertyList`,
  `PeoplePropertyList`, `RollupPropertyList`, or `UnknownPropertyList` as the fallback.

Two guards are needed to keep the delegation from looping back into the deserializer that started
it:

- `PagePropertyValue` is annotated `@JsonDeserialize(using = JsonDeserializer.None.class)` so that
  deserializing a `PagePropertyValue` directly (as in `Page.properties`, or after delegation from
  `PagePropertyDeserializer`) uses annotation-driven subtype resolution rather than inheriting
  `PagePropertyDeserializer` from the interface.
- Every concrete `PagePropertyList` implementation carries the same
  `@JsonDeserialize(using = JsonDeserializer.None.class)`. Without it, resolving the concrete class
  re-enters `PagePropertyListDeserializer` and overflows the stack.

Unknown property types degrade instead of failing: `UnknownProperty`, `UnknownPropertyItem` and
`UnknownPropertyList` act as `defaultImpl` / fallback targets, so a property type added by Notion
after this SDK release still deserializes.

## Alternatives considered

### 1. One Java class for both representations

A single `PagePropertyValue` extending `NotionList`, able to represent either a single value or a
list of listed items. Rejected because:

- the class accumulates fields that are meaningless for most property types;
- the hierarchy becomes hard to read — every non-paginated property inherits list machinery
  (`results`, `has_more`, `next_cursor`);
- worst of all, for the paginated properties the actual content would live in a *different field
  depending on which endpoint produced the object*: in the type-named value field (e.g. `relation`)
  when read from a page, in `results` when read from the property retrieve endpoint. Callers would
  have to keep that rule in mind on every access.

### 2. Fully separate models per endpoint

A distinct class tree for embedded property values and another for page property values — a
`CheckboxProperty` for what a page carries, plus a second near-identical class for what the property
retrieve endpoint returns. Rejected because:

- it duplicates nearly identical classes for every non-paginated property, even though only the five
  paginated properties genuinely differ between endpoints;
- callers would need to convert between the two representations of the same semantic value, which is
  both boilerplate and a fresh source of confusion.

The accepted design keeps a single class per property type for everything that is genuinely
identical across endpoints, and introduces separate list classes only for the five paginated
properties that really do arrive in a different representation.

## Consequences

**Positive**

- One Java type for `Page.properties` (`PagePropertyValue`) with no list machinery; the common read
  path stays simple.
- Each field holds exactly one thing: a non-paginated value lives in the type-named value field, a
  paginated value lives in `results`. There is no "it depends on the endpoint" rule.
- No conversion step between a value read from a page and the same value read from the property
  retrieve endpoint — both are the same `PagePropertyValue` subclass.
- Sealed hierarchies document themselves and enable exhaustive `switch` / pattern matching.
- Unknown property types are tolerated rather than fatal.

**Negative / accepted trade-offs**

- `retrieveProperty` returns a union type, so callers of that method must narrow (via `instanceof`
  pattern matching or the `asValue` / `asList` helpers) unless they use `retrievePaginatedProperty`.
- Two hand-written deserializers replace pure annotation-driven resolution and must be kept in sync
  when Notion adds a paginated property type: register the list class in
  `PagePropertyListDeserializer`, add it to the `permits` clause, and annotate it with
  `JsonDeserializer.None`.
- The `JsonDeserializer.None` annotations look redundant; they are load-bearing (stack-overflow
  guard) and must not be removed.
- Two endpoint methods map to one HTTP endpoint, which has to be explained in the docs.

## References

- [CONTEXT.md](../../CONTEXT.md) — vocabulary used in this ADR
- Cookbook: [Page properties and pagination](../cookbook/page-properties.md)
- `io.kristaxlab.notion.model.page.property.PageProperty`
- `io.kristaxlab.notion.model.page.property.PagePropertyValue`
- `io.kristaxlab.notion.model.page.property.PagePropertyList`
- `io.kristaxlab.notion.model.page.property.PagePropertyDeserializer`
- `io.kristaxlab.notion.model.page.property.PagePropertyListDeserializer`
- `io.kristaxlab.notion.endpoints.PagesEndpoint`
