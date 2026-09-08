# Agent notes

Find contributor conventions here, then read the document that owns them. Do not copy those rules
into this file.

## Vocabulary

Before writing code, Javadoc, docs, or commit/PR prose, read [CONTEXT.md](CONTEXT.md).

Use the listed term exactly. Never use a name from an `_Avoid_` line. If the concept is not in the
glossary, stop and ask. Do not coin a shorter name, and do not add a term to `CONTEXT.md` unless it
has been agreed.

## Notion API ambiguity

When two Notion sources disagree, stop and ask before writing code. This covers a field name, the
members of an enumerated set of token values, whether a field is required, and the shape of a
request body. Resolve anything else yourself and report it.

Do not choose a resolution, do not implement every variant, and do not defer the question to the
change summary. Report the field, what each source says, and the options.

If the work cannot wait for an answer, prefer OpenAPI spec over prose as the source of trust. If 
there is no OpenAPI spec, model the disputed field as a `String`and expose no enum
constant, factory, or builder method for the disputed token. The SDK then reads whatever arrives and
commits to nothing on the write side.

## Javadoc

When writing or changing Javadoc, follow [docs/internals/javadoc.md](docs/internals/javadoc.md).

## Endpoints

When adding a REST area, follow [docs/internals/adding-an-endpoint.md](docs/internals/adding-an-endpoint.md).

## Property types

When adding a property type, follow [docs/internals/adding-a-property-type.md](docs/internals/adding-a-property-type.md).

## Known API tokens

When a Notion field has a limited set of values, follow
[ADR 0002](docs/adr/0002-enums-for-known-api-tokens-stay-decoupled-from-models.md).
