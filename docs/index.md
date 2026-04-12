# Notion SDK for Java — Documentation

A typed Java client for the [Notion API](https://developers.notion.com/) with a composable interceptor pipeline, automatic error mapping, and support for every block type.

**Requirements:** Java 17+ | **Notion API version:** 2026-03-11

## Getting Started

1. [Getting Started](getting-started.md) — install the dependency and make your first API call
2. [Authentication](authentication.md) — integration token and OAuth flow
3. [Configuration](configuration.md) — timeouts, logging, serialization, and more

## Cookbook

Practical recipes for common operations:

- [Writing content](cookbook/writing-content.md) — append blocks to a page
- [Creating pages](cookbook/creating-pages.md) — create pages with title, icon, cover, and content
- [Reading page content](cookbook/reading-page-content.md) — retrieve a page and its blocks
- [Updating & managing blocks](cookbook/updating-blocks.md) — retrieve, update, delete, restore
- [Rich text & formatting](cookbook/rich-text.md) — bold, italic, colors, links, mentions
- [Structured layouts](cookbook/structured-layouts.md) — columns, tables, callouts, code blocks
- [Page lifecycle](cookbook/page-lifecycle.md) — update, archive, restore, move pages

## Advanced Topics

- [Error Handling](advanced/error-handling.md) — exception hierarchy, retry strategies, rate limits
- [Custom Interceptors](advanced/custom-interceptors.md) — architecture, writing custom interceptors
- [Exchange Recording](advanced/exchange-recording.md) — debug with request/response JSON files
- [Custom HTTP Client](advanced/custom-http-client.md) — replace OkHttp with your own implementation
- [Notion HTTP Client](advanced/notion-http-client.md) — use `NotionHttpClient` directly

## Contributing

- [Development Setup](contributing/development-setup.md) — clone, build, test, format
- [Architecture](contributing/architecture.md) — package design, pipeline, extension points
- [Testing Guide](contributing/testing-guide.md) — unit vs integration tests, test doubles
- [Release Process](contributing/release-process.md) — versioning, publishing, changelog
