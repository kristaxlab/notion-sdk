# Adding an endpoint

When Notion exposes a new REST area, this runbook lists every place the SDK has to grow.
Read [Architecture](architecture.md) for the HTTP pipeline. Terminology used below is defined
in [CONTEXT.md](../../CONTEXT.md).

The complete example is `CommentsEndpoint` / `CommentsEndpointImpl`.

## Step 0 — read the API, then stop if a term is missing

Fetch the current Notion reference for the object and each operation. Decide:

- which operations the SDK will expose (`create`, `retrieve`, `update`, `delete`, list)
- which existing models to reuse
- whether list responses paginate ([start cursor](../../CONTEXT.md), [page size](../../CONTEXT.md),
  [next cursor](../../CONTEXT.md))

If you need a concept that is not in the glossary, stop and propose a term. Do not coin a shorter name, and do not
append [CONTEXT.md](../../CONTEXT.md) yourself.

If you see any misalignment between the API and the glossary, propose a correction. The glossary is the source of truth
for the SDK. For example, if Notion calls a resource "file upload" but the glossary calls it "file",
the SDK must use "file upload" in the public API.

## Step 1 — models

1. New types live under `io.kristaxlab.notion.model.<area>` (for example `model.comment`).
2. Reuse existing types (`RichText`, `User`, `Parent`, `NotionFile`). Do not duplicate them.
3. A paginated list extends `NotionList<T>`. Neighbouring lists (`UserList`, `CommentList`,
   `FileUploadList`) also declare the type-named empty object Notion puts on the list payload (`user`, `comment`,
   `fileUpload`).
4. Request bodies are params classes (`CommentCreateParams`) with builders, matching
   `FileUploadCreateParams` and `CreatePageParams`.
5. A resource that Notion tags with `"object": "<name>"` extends `NotionObject` and must be registered on `NotionObject`
   's `@JsonSubTypes` list. Unregistered types deserialize as
   `NotionObject` and drop unknown fields.
6. Add a constant to `ObjectType` only if other code switches on that `object` value.
7. A field with a limited set of tokens gets a dedicated enum for the values the SDK can build today. The model field
   stays a `String`. See
   [ADR 0002](../adr/0002-enums-for-known-api-tokens-stay-decoupled-from-models.md).

Prefer `@JsonTypeInfo` / `@JsonSubTypes` for polymorphic fields. A custom deserializer is allowed only when the
discriminator is not a top-level field — see
[Architecture](architecture.md#polymorphic-type-resolution).

## Step 2 — interface and implementation

1. `io.kristaxlab.notion.endpoints.<Name>Endpoint` — the public API.
2. `io.kristaxlab.notion.endpoints.impl.<Name>EndpointImpl` — extends `BaseEndpointImpl`.
3. Validate with `Validator.checkNotNull` / `checkNotNullOrEmpty` before calling the client. Reject blank path ids the
   same way `UsersEndpointImpl` does.
4. Build paths with `ApiPath.from("/resource")` or
   `ApiPath.builder("/resource/{id}").pathParam("id", value).build()`.
5. Paginated GETs use `paginatedPath(url, startCursor, pageSize)` and add extra query parameters on the builder. Expose
   a no-pagination overload that delegates with `null, null`.
6. Method names follow existing endpoints: `create`, `retrieve`, `update`, `delete`, `listX`. Do not invent REST-ish
   aliases (`get`, `patch`, `remove`).
7. JSON bodies go through `getClient().call(METHOD, path, body, Response.class)` using the
   `GET` / `POST` / `PATCH` / `DELETE` constants on `BaseEndpointImpl`.

**Exception:** multipart upload is not JSON. `FileUploadsEndpointImpl.upload` builds an
`HttpClient.MultipartBody`. Copy that only when Notion requires multipart.

Optional fluent `Consumer<...Builder>` overloads exist on pages and data sources. Add them when the params builder is
the primary way callers assemble the body.

## Step 3 — wire NotionClient

In `NotionClient`:

- a field, constructed as `new XxxEndpointImpl(httpClient)`
- a public accessor (`comments()`, `fileUploads()`, …)
- the accessor listed in the class Javadoc

That is the only public entry point. Do not construct endpoint implementations in user-facing examples.

## Step 4 — unit tests

`src/test/java/io/kristaxlab/notion/endpoints/impl/<Name>EndpointImplTest.java`, patterned on
`UsersEndpointImplTest`:

- `ApiClientStub` — assert HTTP method, `ApiPath` template, path params, query params, and body
- `@NullAndEmptySource` / blank rejection for every id
- deserialize official JSON (a recorded exchange or a Notion example) to the concrete model, not a fallback type

See [Testing Guide](testing-guide.md)

## Step 5 — integration tests

Facts for writing a test live in
[Testing Guide](testing-guide.md#writing-a-new-integration-test). Do not invent testkit types.

- Package `tests.<area>` (`tests.comments`, `tests.pages`, …)
- Class `IT<n>_<Endpoint>` or `IT<n>_<Endpoint>_<NegativeScenarioDesignator>`,
  `@DisplayName("IT-<n>: <Endpoint> - <description>")` with an unused [test id](../../CONTEXT.md)
- `WithEmptyTestPage` when the test needs a [test page](../../CONTEXT.md);
  `BaseIntegrationTest` when it does not
- If the test needs prerequisites that are not possible with the public API, extend from `WithTestPageFixture` and give
  instructions on what prereuisites should be created (the test will fail until they are created).
  See [Testing Guide](testing-guide.md#where-prerequisites-belong) for more details.
- [Notion Test Http Client](../../CONTEXT.md) (`getNotionClient()`) for the call under test;
  [setup client](../../CONTEXT.md) (`getSetupClient()`) for arrange-only calls
- Register in `QuickCheck` only if the case is a short, reliable smoke and does not need extra Notion capabilities that
  are off by default

## Step 6 — documentation

| Change                           | Update                                                                                                                                                      |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Public API                       | Javadoc on the new types and methods — [Javadoc conventions](javadoc.md). Run `./gradlew javadoc`. Never `{@link}` a Lombok-generated member.               |
| Usage                            | A cookbook recipe; a row in [docs/cookbook/README.md](../cookbook/README.md); a Related cookbook pages footer. [Documentation guide](documentation-guide.md). |
| A term was agreed                | [CONTEXT.md](../../CONTEXT.md) — agents propose, they do not decide                                                                                         |
| A `400` the types cannot express | [Notion API constraints](notion-api-constraints.md)                                                                                                         |
| User-visible API                 | `[Unreleased]` in `CHANGELOG.md`                                                                                                                            |
| New page                         | `llms.txt`                                                                                                                   |

Do not restate Javadoc method tables in Markdown.

## See also

- [Architecture](architecture.md) — request lifecycle and polymorphic type resolution
- [Adding a page property type](adding-a-property-type.md) — when the new area also introduces a property type
- [Testing Guide](testing-guide.md)
- [Documentation guide](documentation-guide.md)
