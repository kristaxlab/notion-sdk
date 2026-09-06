# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Add notable changes here as they land, under **[Unreleased]**. At release, rename that heading to the version and date,
and open a new empty **[Unreleased]** section above it. Do not reconstruct the list from git history at the last minute.

## [Unreleased]

### Added

- File Uploads - all operations.
- Databases - all operations
- Data Sources - all operations
- Pages - added support for all the page properties types (including property retrieve endpoint
  support), markdown support for pages content.

- `NotionProperties` / `NotionPropertiesBuilder` fluent DSL for declaring properties
- `NotionSchema` / `NotionSchemaBuilder` fluent DSL for declaring data source columns.
- `NotionPageViewer` for typed reads of embedded property values on a retrieved page.
- `TemplatePoller` for blocking until a template is applied to a page.

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
