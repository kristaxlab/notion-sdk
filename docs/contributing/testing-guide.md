# Testing Guide

The SDK uses JUnit 5 with a layered testing strategy.

## Test Categories

| Category          | Tag                   | Runs by default | Purpose                                     |
|-------------------|-----------------------|-----------------|---------------------------------------------|
| Unit tests        | *(none)*              | Yes             | Fast, isolated tests for individual classes |
| Integration tests | `@Tag("integration")` | No              | Tests against the real Notion API           |

Run specific categories:

```bash
# Unit tests only (default)
./gradlew test

# Integration tests (2 options)
NOTION_TOKEN=secret_xxx ./gradlew test -PincludeTags=integration
NOTION_TOKEN=secret_xxx ./gradlew integrationTest

```

## Integration Test Prerequisites

> **Status: To be added** -- Integration tests require a valid Notion API token and a special predefined Notion page
> duplicated into the testing workspace

## Coverage

JaCoCo generates coverage reports after unit tests:

```bash
./gradlew test jacocoTestReport
# HTML report: build/reports/jacoco/test/html/index.html
# XML report: build/reports/jacoco/test/jacocoTestReport.xml
```

Lombok-generated code (`@Builder`, `@Getter`, etc.) is excluded from coverage metrics.
## Test reports
<!-- TODO: choose license -->
TBD
## See Also

- [Development Setup](development-setup.md) -- build and run commands
- [Architecture](architecture.md) -- understanding what to test
- [Exchange Logging](../advanced/exchange-logging.md) -- using exchange files as test fixtures
