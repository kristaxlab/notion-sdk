# Testing Guide

The SDK uses JUnit 5 for unit and tests testing. Tests live in two Gradle source sets:

| Category          | Source set        | Gradle task        | Runs by default | Purpose                                     |
|-------------------|-------------------|--------------------|-----------------|---------------------------------------------|
| Unit tests        | `test`            | `test`             | Yes             | Fast, isolated tests for individual classes |
| Integration tests | `testIntegration` | `testIntegration`  | No              | Tests against the real Notion API           |

## Unit Tests

```bash
./gradlew test
```

Unit tests live under `src/test/java` and do not require any environment setup.

## Integration Tests

### Prerequisites

Integration tests run against the live Notion API and require two things:

1. **Test Session Parent Id** — the data source or database that holds the structures tests need
(databases with specific properties, media content, and so on). Every run also adds a record to the
Integration Tests Database there. Duplicate the workspace location into your own workspace from
[this URL](https://sdk-integration.notion.site/Integration-tests-2f4cd6cf14068001ac57e261d1c18fda)
and set its id in `src/testIntegration/resources/junit-platform.properties` as
`notion.tests.session.parent.id` (the committed default lives there). The same value is also
accepted as the environment variable `NOTION_TESTS_SESSION_PARENT_ID`, which overrides the
properties file when both are set.
2. **Auth token** — Notion auth token, create one at <https://www.notion.so/my-integrations>
(the Test Session Parent Id should be accessible with this token). Auth token should be provided as
an environment variable `NOTION_TESTS_AUTH_TOKEN`.

### Environment Setup

Copy the sample env file and fill in your values:

```bash
cp .env.test.sample .env.test.local
```

```dotenv
# .env.test.local  (git-ignored)
NOTION_TESTS_AUTH_TOKEN=secret_xxx
# optional — overrides notion.tests.session.parent.id from junit-platform.properties
NOTION_TESTS_SESSION_PARENT_ID=<your-duplicated-page-id>
```

The Gradle `testIntegration` task loads `.env.test` and `.env.test.local` automatically
(later files override earlier ones). `.env.test.local` is listed in `.gitignore`
so your token is never committed.

Alternatively, export the variables directly. The Test Session Parent Id is optional for tests that
do not create pages (users, most file uploads) and is otherwise taken from
`junit-platform.properties` when unset:

```bash
export NOTION_TESTS_AUTH_TOKEN=secret_xxx
export NOTION_TESTS_SESSION_PARENT_ID=<your-duplicated-page-id>
```

### Configuration reference

Every setting except the auth token is resolved by `TestConfigurationLookup`, which accepts the same
key as an environment variable, a system property or a JUnit platform parameter — in that order of
precedence. Key spelling is normalized, so `notion.tests.session.parent.id`,
`NOTION_TESTS_SESSION_PARENT_ID` and `notionTestsSessionParentId` all resolve to the same setting.
Defaults for the suite live in `src/testIntegration/resources/junit-platform.properties`.

| Setting | Required | Purpose                                                                                                                                              |
| --- | --- |------------------------------------------------------------------------------------------------------------------------------------------------------|
| `NOTION_TESTS_AUTH_TOKEN` | yes | Integration token; env var only                                                                                                                      |
| `notion.tests.session.parent.id` | for tests that create pages | Test Session Parent Id — the data source or database the test session page is created under. Not required for tests that only need a client or `@SessionUserId`. Set in `junit-platform.properties`, or as `NOTION_TESTS_SESSION_PARENT_ID` (env var wins) |
| `notion.tests.session.title` | no | Title given to the test session page |
| `notion.tests.session.template.id` | no | Template used for the test session page |
| `notion.tests.session.cleanup` | no | Moves the test session page to trash when the run finishes; off by default so failed runs stay inspectable |
| `notion.links.base.url` | no | Base URL used when logging test page links (default `https://www.notion.so/`) |
| `notion.tests.json.strict` | no | When `true`, the Notion Test Http Client fails on unknown JSON properties. Off by default so the suite reports whether the SDK still works against the live API, not whether every new Notion field is modelled |

Parallel execution is intentionally disabled: most tests write to the same parent page, and
concurrent writes make Notion return `409 Conflict`.

### Running Integration Tests

The whole suite (`paid_plan` tests are excluded):

```bash
./gradlew testIntegration
```

A single class, useful while writing:

```bash
./gradlew testIntegration --tests "tests.pages.IT1_Pages_CRUD"
```

The short smoke subset registered in `suites.QuickCheck`:

```bash
./gradlew testIntegrationQuick
```

### Writing a new integration test

To have an agent write or finish a test, start
[`.github/prompts/integration_tests.prompt.md`](../../.github/prompts/integration_tests.prompt.md).
That file is a task launcher — goal, required reading, how to evaluate. The facts below are the
specification.

Pick a base class according to what the test needs. All three provide a Notion Test Http Client
(`getNotionClient()`) that writes HTTP exchange logs to a per-test directory. Do not construct a
`NotionClient` yourself. That client uses the same JSON defaults as production unless
`notion.tests.json.strict` is on — see the configuration table. The JUnit extensions behind these
bases are described in [Testkit](testkit.md).

| Base class | Use when | Gives you |
| --- | --- | --- |
| `BaseIntegrationTest` | the test needs no page of its own (users, most file uploads) | `getNotionClient()`; add `@SessionUserId` if you need the session user id |
| `WithEmptyTestPage` | the test provisions its own content — most tests | `getTestPageId()` (`@NotionPageId`) |
| `WithTestPageFixture` | the test needs a fixture page built by hand in the UI | `getTestPageId()` (`@FixtureNotionPageId`); tagged `fixture` |

Minimal example:

```java
package tests.pages;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithEmptyTestPage;

public class IT5_Pages_Retrieve extends WithEmptyTestPage {

  @Test
  @DisplayName("IT-5: Pages - Retrieve a page by id")
  public void testRetrievePage() {
    var page = getNotionClient().pages().retrieve(getTestPageId());

    assertNotNull(page);
  }
}
```

- One test method per class.
- Place the class under `src/testIntegration/java/tests/<endpoint>/` (`tests.pages`, `tests.blocks`,
  `tests.datasources`, `tests.fileuploads`, `tests.users`).
- Class name: `IT<id>_<Endpoint>_<Details>` (e.g. `IT1_Pages_CRUD`). With a placeholder id:
  `IT_Pages_RelationProperty`.
- `@DisplayName` on the test method: `IT-<id>: <Endpoint> - <description>`. Placeholder:
  `IT-?: Pages - Create and retrieve 'people' property`. The provisioner extracts the `IT-8` /
  `IT-?` prefix from this display name to resolve a fixture page, so keep that prefix exact.
- Use `getNotionClient()` for the call the test is asserting. Use `getSetupClient()` for
  arrange-only calls (create a database, upload a cover) so their exchanges land in a separate
  log directory.
- `testIntegration` already picks up every class under `tests.*`. Register a representative test
  in `src/testIntegration/java/suites/QuickCheck.java` only if it is a short, reliable smoke case.
  Do not add long, fixture-heavy, or `paid_plan` tests there.
- Tag paid-only coverage `@Tag("paid_plan")`. Other useful tags: `fixture` (already on
  `WithTestPageFixture`), `advanced`, `long`.
- Prefer the fluent builders the production API already exposes (`CreatePageParams`,
  `NotionSchema.schemaBuilder()`, `NotionProperties`).
- Upload files with `testkit.util.FileLoader.uploadFile(path, name, getSetupClient())`. Fixture
  files live under `src/testIntegration/resources/`.

An unknown property in a live response does not fail the test unless `notion.tests.json.strict` is
on. That is intentional: new backward-compatible Notion fields must not break the suite. Turn strict
mode on only when you want this run to fail on unmodelled fields.

### Where prerequisites belong

Create everything a test needs in its own `@BeforeEach`, so the test is self-contained. There are two
exceptions:

1. **The state cannot be created through the API** (named data-source templates, hand-built media
   layouts). Build it in the UI on a child page of the session template, titled exactly as the test
   id (`IT-8`). Database rows in a child database on that template are discovered the same way.
   Extend `WithTestPageFixture`. If that page is missing, the provisioner fails — do not fall back
   to an empty page.
2. **A shared, rarely changing entity that costs an API read.** Inject it with its own annotation
   (the session user id is `@SessionUserId`). The provisioner stores it once on `TestSession`. Do not call
   `TestSession` from a test. Do not add a new session field unless several tests need the same
   extra read — add an annotation and provisioner instead.

Constraints Notion imposes on test data — server-owned `unique_id` values, one title column per data
source, template polling, and similar — are listed in [Notion API constraints](notion-api-constraints.md).

## Test Reports

After running tests, Gradle generates HTML reports:

| Task | Report location |
| --- | --- |
| `test` | `build/reports/tests/test/index.html` |
| `jacoco` | `build/reports/jacoco/test/html/index.html` |
| `testIntegration` | `build/reports/tests/testIntegration/index.html` |
| `testIntegrationQuick` | `build/reports/tests/testIntegrationQuick/index.html` |

### JaCoCo Coverage

JaCoCo generates coverage reports after unit tests run.
Lombok-generated code (`@Builder`, `@Getter`, etc.) is excluded from coverage metrics.

### CI Artifacts

In GitHub Actions, test reports are uploaded as a downloadable artifact
(`test-reports-<run_number>`) after every build — even when tests fail.
Look for the **Artifacts** section at the bottom of the workflow run summary page.

## See Also

- [Installation](../../README.md#installation) — dependency setup and basics
- [Architecture](architecture.md) — understanding what to test
- [Testkit](testkit.md) — responsibilities, data shapes and extension points of the integration testkit
- [Exchange Recording](exchange-recording.md) — HTTP exchange files written during tests
- [Integration tests prompt](../../.github/prompts/integration_tests.prompt.md) — agent launcher for writing a test
