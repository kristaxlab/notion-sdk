# Testing Guide

The SDK uses JUnit 5 for unit and integration testing. Tests are organized into two categories:

| Category          | Tag                   | Runs by default | Purpose                                     |
|-------------------|-----------------------|-----------------|---------------------------------------------|
| Unit tests        | *(none)*              | Yes             | Fast, isolated tests for individual classes |
| Integration tests | `@Tag("integration")` | No              | Tests against the real Notion API           |

## Unit Tests

```bash
./gradlew test
```

Unit tests run by default and do not require any environment setup.

## Integration Tests

### Prerequisites

Integration tests run against the live Notion API and require two things:

1. **A Notion page that contains the prerequisites** structure for the tests. Prerequisites include things like databases 
with specific properties, media content, etc. The tests will use the content of this page and also every test run will 
have a corresponding record added to the Integration Tests Database in this page. You can duplicate the prerequisites 
page into your own workspace from [this URL](https://sdk-integration.notion.site/Integration-tests-2f4cd6cf14068001ac57e261d1c18fda).
Id of this page should be added as `NOTION_TEST_PAGE_ID` environment variable.
2. **Auth token** — Notion auth token, create one at <https://www.notion.so/my-integrations>
(the prerequisites page mentioned above should be accessible with this token). Auth token should be provided as an
environment variable `NOTION_TEST_AUTH_TOKEN`.

### Environment Setup

Copy the sample env file and fill in your values:

```bash
cp .env.test.sample .env.test.local
```

```dotenv
# .env.test.local  (git-ignored)
NOTION_TEST_AUTH_TOKEN=secret_xxx
NOTION_TEST_PAGE_ID=<your-duplicated-page-id>
```

The Gradle `integrationTest` task loads `.env.test` and `.env.test.local` automatically
(later files override earlier ones). `.env.test.local` is listed in `.gitignore`
so your token is never committed.

Alternatively, export the variables directly:

```bash
export NOTION_TEST_AUTH_TOKEN=secret_xxx
export NOTION_TEST_PAGE_ID=<your-duplicated-page-id>
```

### Running Integration Tests

```bash
./gradlew integrationTest
```

# For contributors
### Writing a new Integration Test

Every integration test must extend `BaseIntegrationTest`, which:

- tags the class with `@Tag("integration")` so it is picked up by the `integrationTest` task
  and excluded from the regular `test` task,
- creates a `NotionClient` before each test with a strict JSON serializer that fails on
  unknown properties (keeping SDK models in sync with the live API),
- writes HTTP exchange logs to a per-test directory under `exchanges/exchange-logs/`.

Minimal example:

```java
package integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PagesIT extends BaseIntegrationTest {

  @Test
  @DisplayName("[INT-XX]: Pages - Retrieve a page by id")
  public void testRetrievePage() {
    // Use getNotion() to access the pre-configured client
    var page = getNotion().pages().retrieve("<page-id>");

    assertNotNull(page);
  }
}
```

Key points:

- Place the class in the `integration` package (`src/test/java/integration/`).
- Use `getNotion()` to obtain the client — do **not** create your own `NotionClient`.
- Give each test a descriptive `@DisplayName` with a ticket reference (e.g. `[INT-42]`).

## Test Reports

After running tests, Gradle generates HTML reports:

| Task              | Report location                                  |
|-------------------|--------------------------------------------------|
| `test`            | `build/reports/tests/test/index.html`            |
| `jacoco`          | `build/reports/jacoco/test/html/index.html`      |
| `integrationTest` | `build/reports/tests/integrationTest/index.html` |

### JaCoCo Coverage

JaCoCo generates coverage reports after unit tests run.
Lombok-generated code (`@Builder`, `@Getter`, etc.) is excluded from coverage metrics.

### CI Artifacts

In GitHub Actions, test reports are uploaded as a downloadable artifact
(`test-reports-<run_number>`) after every build — even when tests fail.
Look for the **Artifacts** section at the bottom of the workflow run summary page.

## See Also

- [Development Setup](development-setup.md) — build and run commands
- [Architecture](architecture.md) — understanding what to test
- [Exchange Recording](../advanced/exchange-recording.md) — HTTP exchange files written during integration tests
