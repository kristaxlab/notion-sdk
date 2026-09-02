# ADR 0001: Complex hierarchy and custom deserializers for `RetrievedProperty`, `PageProperty`, `PagePropertyList`

**Status:** Accepted

## Context

Notion exposes page property values through two endpoints that describe the *same* domain objects in
two different shapes.

### `GET /pages/{page_id}`

The page carries a property map, and every entry has the same uniform shape regardless of the
property kind: an `id`, a `type` holding the property kind, and a value field named after that kind.

```json
"Done":    { "id": "IuYw", "type": "checkbox", "checkbox": false },
"Notes":   { "id": "Zl%5B%3E", "type": "rich_text", "rich_text": [ { "type": "text", ... } ] },
"Parent":  { "id": "IV%7Dn", "type": "relation", "relation": [], "has_more": false }
```

For collection-valued properties the array may be truncated; `has_more: true` signals that the page
response does not contain the full value.

### `GET /pages/{page_id}/properties/{property_id}`

This endpoint returns one of two different shapes depending on the property kind:

- **Scalar properties** (`checkbox`, `select`, `number`, `email`, …) come back as a single property
  object that is nearly identical to the embedded form: `type` still holds the property kind.

  ```json
  { "object": "property_item", "type": "checkbox", "id": "Done", "checkbox": false }
  ```

- **Paginated properties** (`relation`, `title`, `rich_text`, `people`) come back as a *list*: a
  `results` array of per-item objects plus `has_more` / `next_cursor`. Here `type` is the literal
  string `"property_item"`, and the real property kind is nested under `property_item.type`. The
  individual values live in `results[]`, not in a field named after the property kind.

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

So `checkbox` and `select` look the same from both endpoints, while `relation`, `people`, `title`
and `rich_text` look fundamentally different depending on which endpoint produced them. Any Java
model has to reconcile that.

## Decision

Model the two shapes as two distinct types and unite them under an interface that serves as the
return type of the property-retrieve endpoint.

| Type | Represents | Hierarchy |
| --- | --- | --- |
| `PageProperty` | The uniform, single-object shape: entries of `Page.properties` **and** scalar results of the retrieve endpoint | `BaseNotionObject` → `PageProperty` |
| `PagePropertyList<I, L>` | The paginated list shape returned only by the retrieve endpoint | `BaseNotionObject` → `NotionList<L>` → `PagePropertyList` |
| `RetrievedProperty` | Union of the two; the declared return type of `retrieveProperty` | sealed interface, `permits PageProperty, PagePropertyList` |

- `Page.properties` is typed `Map<String, PageProperty>`. The page endpoint never yields a list
  shape, so callers reading a page are never exposed to `RetrievedProperty` at all.
- `retrieveProperty(pageId, propertyId)` returns `RetrievedProperty`, which resolves at runtime to a
  `PageProperty` subclass for scalar properties or a `PagePropertyList` subclass for paginated ones.
- `RetrievedProperty` is an **interface**, not a class, because `PageProperty` and `PagePropertyList`
  live in different hierarchies (`BaseNotionObject`-derived object vs. `NotionList`). An aggregating
  interface is the only way to pull them under a single nominal type.
- The list side of the hierarchy is **sealed**: `RetrievedProperty` permits exactly two subtypes,
  and `PagePropertyList`, `PropertyItem` and `ListedItem` each permit only their own
  implementations. Beyond exhaustive pattern matching, sealing keeps the hierarchy readable — the
  permits clause lists every implementation right next to the abstraction. `PageProperty` is
  declared `non-sealed` because its ~25 subclasses are registered through Jackson `@JsonSubTypes`
  instead, which would make a permits clause pure duplication.

### Convenience method for paginated properties

`retrievePaginatedProperty(pageId, propertyId[, startCursor, pageSize])` calls the same
`GET /pages/{page_id}/properties/{property_id}` endpoint but declares `PagePropertyList` as its
return type. When a caller already knows the property is paginated (`relation`, `title`,
`rich_text`, `people`), this avoids casting down from `RetrievedProperty`, and it is the only
overload that accepts `start_cursor` / `page_size`.

Narrowing helpers keep call sites free of explicit casts: `RetrievedProperty.asValue(Class)` /
`asList(Class)`, `PageProperty.as(Class)`, and `PagePropertyList.asRelationList()` /
`asRichTextList()` / `asTitleList()` / `asPeopleList()`.

### Type resolution and deserialization

The project's preferred mechanism is `@JsonTypeInfo` on an `EXISTING_PROPERTY` named `type`. That
works directly for `Page.properties` and for scalar retrieve responses, where `type` holds the
property kind. It does **not** work for paginated retrieve responses, where `type` is always
`"property_item"` and the discriminating value sits in the nested `property_item.type` —
`@JsonTypeInfo` cannot read a nested field. Hence two custom deserializers:

- **`RetrievedPropertyDeserializer`** (bound to `RetrievedProperty`) decides which of the two shapes
  arrived by checking for a `results` field (or a top-level array). It then delegates to
  `PagePropertyList` or to `PageProperty`, letting the latter's `@JsonTypeInfo` / `@JsonSubTypes`
  take over.
- **`PagePropertyListDeserializer`** (bound to `PagePropertyList`) reads `property_item.type` and
  maps it to `RelationPropertyList`, `RichTextPropertyList`, `TitlePropertyList`,
  `PeoplePropertyList`, or `UnknownPropertyList` as the fallback.

Two guards are needed to keep the delegation from looping back into the deserializer that started
it:

- `PageProperty` is annotated `@JsonDeserialize(using = JsonDeserializer.None.class)` so that
  deserializing a `PageProperty` directly (as in `Page.properties`, or after delegation from
  `RetrievedPropertyDeserializer`) uses annotation-driven subtype resolution rather than inheriting
  `RetrievedPropertyDeserializer` from the interface.
- Every concrete `PagePropertyList` implementation carries the same
  `@JsonDeserialize(using = JsonDeserializer.None.class)`. Without it, resolving the concrete class
  re-enters `PagePropertyListDeserializer` and overflows the stack.

Unknown property kinds degrade instead of failing: `UnknownProperty`, `UnknownPropertyItem` and
`UnknownPropertyList` act as `defaultImpl` / fallback targets, so a property type added by Notion
after this SDK release still deserializes.

## Alternatives considered

### 1. One model for both shapes

A single `PageProperty` extending `NotionList`, able to represent either a scalar value or a list of
items. Rejected because:

- the model accumulates fields that are meaningless for most property kinds;
- the hierarchy becomes hard to read — every scalar property inherits list machinery
  (`results`, `has_more`, `next_cursor`);
- worst of all, for the paginated kinds the actual content would live in a *different field
  depending on which endpoint produced the object*: in the kind-named field (e.g. `relation`) when
  read from a page, in `results` when read from the retrieve endpoint. Callers would have to keep
  that rule in mind on every access.

### 2. Fully separate models per endpoint

A distinct class tree for page-embedded properties and another for retrieved properties (e.g.
`CheckboxProperty` vs. `RetrievedCheckboxProperty`). Rejected because:

- it duplicates nearly identical classes for every scalar property, even though only the four
  paginated kinds genuinely differ between endpoints;
- callers would need to convert between the two representations of the same semantic value, which is
  both boilerplate and a fresh source of confusion.

The accepted design keeps a single class per property kind for everything that is genuinely
identical across endpoints, and introduces separate list types only for the four kinds that really
do come back in a different shape.

## Consequences

**Positive**

- One `Page.properties` type (`PageProperty`) with no list noise; the common read path stays simple.
- Each field holds exactly one thing: a scalar value lives in the kind-named field, a paginated
  value lives in `results`. There is no "it depends on the endpoint" rule.
- No conversion step between the page-embedded and retrieved representations of a scalar property.
- Sealed hierarchies document themselves and enable exhaustive `switch` / pattern matching.
- Unknown property kinds are tolerated rather than fatal.

**Negative / accepted trade-offs**

- `retrieveProperty` returns a union type, so callers of that method must narrow (via
  `instanceof` pattern matching or the `asValue` / `asList` helpers) unless they use
  `retrievePaginatedProperty`.
- Two hand-written deserializers replace pure annotation-driven resolution and must be kept in sync
  when Notion adds a paginated property kind: register the list class in
  `PagePropertyListDeserializer`, add it to the `permits` clause, and annotate it with
  `JsonDeserializer.None`.
- The `JsonDeserializer.None` annotations look redundant; they are load-bearing (stack-overflow
  guard) and must not be removed.
- Two endpoint methods map to one HTTP endpoint, which has to be explained in the docs.

## References

- Cookbook: [Page properties and pagination](../cookbook/06-page-properties.md)
- `io.kristaxlab.notion.model.page.property.RetrievedProperty`
- `io.kristaxlab.notion.model.page.property.PageProperty`
- `io.kristaxlab.notion.model.page.property.PagePropertyList`
- `io.kristaxlab.notion.model.page.property.RetrievedPropertyDeserializer`
- `io.kristaxlab.notion.model.page.property.PagePropertyListDeserializer`
- `io.kristaxlab.notion.endpoints.PagesEndpoint`
