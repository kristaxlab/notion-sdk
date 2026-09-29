# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Add notable changes here as they land, under **[Unreleased]**. At release, rename that heading to the version and date,
and open a new empty **[Unreleased]** section above it. Do not reconstruct the list from git history at the last minute.

## [Unreleased]

### Added

- Comments - all operations
- File Uploads - all operations.
- Databases - all operations
- Data Sources - all operations
- Pages - added support for all the page properties types (including property retrieve endpoint
  support), markdown support for pages content.
- Search - search by title (`client.search()`), with filter, sort, and pagination; `NotionSearchViewer`
  to partition pages and data sources; `SearchPoller` for create-then-search indexing delay;
  `RequestStatus` (result completeness) on `SearchList` and `QueryList`.

- `NotionProperties` / `NotionPropertiesBuilder` fluent DSL for declaring properties
- `NotionSchema` / `NotionSchemaBuilder` fluent DSL for declaring data source columns.
- `NotionPageViewer` for typed reads of embedded property values on a retrieved page.
- `TemplatePoller` for blocking until a template is applied to a page.

- Tab block — model, fluent `tab(...)` / builder children as raw paragraph tab labels, and retrieve
  that no longer falls through to `UnknownBlock`.
- HTML embed — embed create/update via `file_upload` of `.html` / `.htm` (url xor file_upload on
  write); responses still expose a temporary `url` plus caption.
- Meeting notes block — model with dual-read of legacy `transcription`; create/query on
  `BlocksEndpoint`; typed query filter/sort/limit; public `MeetingNotesPoller`.
- Blocks append — public `Position` overloads for single block, list, fluent `Consumer`, and
  `Supplier`; update helpers via fluent `Consumer` payloads (no `UpdateBlockParams`).

### Changed

- environment variable name for integration tests auth token changed from NOTION_TEST_AUTH_TOKEN to
  NOTION_TESTS_AUTH_TOKEN

### Fixed


## [0.1.0]

- Authorization with token is added.

### Endpoints

Added support for the endpoints below

- Users - all operations
- Blocks - all operations
- Pages - all operations for page content management, limited datasource properties values management

### Other

- Initial 'Cookbook' documentation is added to demonstrate Notion SDK capabilities.
