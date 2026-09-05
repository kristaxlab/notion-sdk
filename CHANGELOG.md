# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Add notable changes here as they land, under **[Unreleased]**. At release, rename that heading to
the version and date, and open a new empty **[Unreleased]** section above it. Do not reconstruct the
list from git history at the last minute.

## [Unreleased]

### Added

- `UpdateDataSourceParams.Builder` exposes `parent` / `inDatabase` so a data source can be moved to
  another database through the fluent update API.

### Changed

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
