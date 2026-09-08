# ADR 0002: Enums for known API tokens stay decoupled from models

**Status:** Accepted

Terms used below — *property type*, *database type*, *comment display name* — are defined in
[CONTEXT.md](../../CONTEXT.md).

## Context

Many Notion fields accept a closed set of tokens: property types (`checkbox`, `relation`, …), block
types (`paragraph`, `heading_1`, …), number formats (`dollar`, `euro`, …), database types
(`tasks`, `projects`, `skills`), comment display name types (`user`, `custom`, `integration`), and
similar.

The SDK needs two things from those sets that pull in opposite directions:

- Callers building a request want a single type that lists every value the SDK can write today.
- Responses must keep working when Notion adds a token the SDK has not modelled yet. A page that
  carries a new property type, or a comment whose display name uses a new type, must deserialize.

Typing the model field as the enum satisfies the first need and breaks the second: Jackson looks up
the enum constant, fails, and the whole retrieve fails.

## Decision

For every Notion field with a limited range of values:

1. Add a dedicated enum that lists the tokens the SDK can build today (`PropertyType`, `BlockType`,
   `NumberFormatType`, `DatabaseType`, `CommentDisplayNameType`, …).
2. Keep the corresponding field on the model as a `String`.
3. Factories and builders accept the enum and write `enum.type()` (or `getValue()`) onto that
   string field.

The enum is a catalogue of proven write values. It is not the Java type of the wire field.

This is separate from polymorphic `@JsonSubTypes` hierarchies (`Block`, `PagePropertyValue`), which
already tolerate unknown variants through a `defaultImpl`. Those hierarchies still keep `type` as a
`String` and still use the enum only when constructing a known variant.

## Alternatives considered

### 1. Type the model field as the enum

```java
private CommentDisplayNameType type;
```

Rejected. The next token Notion ships — a new property type on a retrieved page, a new comment
display name type — has no constant. Deserialization throws, and every caller of retrieve or list
fails even if they never touch that field.

### 2. No enum; strings only

Rejected. Callers cannot see the supported write values at a glance, and every factory repeats
string literals that drift.

### 3. Enum field with an `UNKNOWN` / default constant

Rejected. A new token would deserialize instead of crashing, but it would be indistinguishable from
a malformed value, and writing `UNKNOWN` is not a valid Notion request. The raw string on the model
already preserves what Notion sent.

## Consequences

**Positive**

- Factories build model instances with proven tokens.
- One enum lists every value the SDK can write today.
- Retrieve and list keep working when Notion adds a token that is not on the enum yet.

**Negative / accepted trade-offs**

- A token that exists only on a response stays a string. Callers who want the enum must map it
  themselves (`fromValue`) and handle the unknown case.
- The enum can lag the live API for writes until a new constant is added. That is intentional: add
  the constant when the SDK learns how to build that value, not when it first appears on a
  response.

## References

- [CONTEXT.md](../../CONTEXT.md) — vocabulary used in this ADR
- [Adding a page property type](../internals/adding-a-property-type.md)
- [Adding an endpoint](../internals/adding-an-endpoint.md)
- `io.kristaxlab.notion.model.page.property.PropertyType`
- `io.kristaxlab.notion.model.block.BlockType`
- `io.kristaxlab.notion.model.datasource.properties.NumberFormatType`
- `io.kristaxlab.notion.model.database.DatabaseType`
- `io.kristaxlab.notion.model.comment.CommentDisplayNameType`
