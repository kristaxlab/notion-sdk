# Notion SDK for Java -- Documentation

Welcome to the Notion SDK for Java documentation. This SDK provides a typed Java client for the [Notion API](https://developers.notion.com/) with a composable interceptor pipeline, automatic error mapping, and support for every block type.

**Requirements:** Java 17+ | **Notion API version:** 2026-03-11

## Getting Started

New to the SDK? Start here:

1. [Getting Started](getting-started.md) -- install the dependency and make your first API call
2. [Authentication](authentication.md) -- using auth token and Notion OAuth flow
3. [Configuration](configuration.md) -- customize timeouts, logging, serialization, and more

## Guides

Learn how to work with each Notion API resource:

- [Pages](guides/pages.md) -- create, read, update, and archive pages
- [Databases](guides/databases.md) -- create and query databases, manage schemas and properties
- [Blocks](guides/blocks.md) -- append, read, update, and delete content blocks
- [Users](guides/users.md) -- list workspace users and retrieve bot info
- [Search](guides/search.md) -- full-text search across your workspace
- [Comments](guides/comments.md) -- create and read page and block comments
- [Files & Media](guides/files-and-media.md) -- upload and reference files, images, and videos
- [Rich Text](guides/rich-text.md) -- compose and parse rich text objects
- [Pagination](guides/pagination.md) -- cursor-based pagination patterns

## Advanced Topics

Deeper dives into the SDK internals and extension points:

- [Error Handling](advanced/error-handling.md) -- exception hierarchy, retry strategies, rate limits
- [Custom Interceptors](advanced/custom-interceptors.md) -- architecture, writing custom interceptors
- [Exchange Recording](advanced/exchange-recording.md) -- debug with request/response JSON files
- [Custom HTTP Client](advanced/custom-http-client.md) -- replace OkHttp with your own http client implementation
- [Notion Http Client](advanced/notion-http-client.md) -- use `NotionHttpClient` directly

## Recipes

End-to-end examples for common integration patterns:

- [Sync External Data to Notion](recipes/sync-external-to-notion.md) -- push CRM, Jira, or GitHub data into Notion
- [Notion as CMS](recipes/notion-as-cms.md) -- read pages and databases to power a website
- [Task Automation](recipes/task-automation.md) -- automate project management workflows
- [Bulk Operations](recipes/bulk-operations.md) -- import/export data, batch writes
- [Webhooks](recipes/webhooks.md) -- react to Notion changes in real-time

## Contributing

- [Development Setup](contributing/development-setup.md) -- clone, build, test, format
- [Architecture](contributing/architecture.md) -- package design, pipeline, extension points
- [Testing Guide](contributing/testing-guide.md) -- unit vs integration tests, test doubles
- [Release Process](contributing/release-process.md) -- versioning, publishing, changelog
