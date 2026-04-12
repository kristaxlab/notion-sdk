# Release Process

This document describes how the Notion SDK is versioned, released, and published.

> **Status: Planned** -- The release pipeline is not yet automated. This document describes the intended process.

## Versioning

The SDK follows [Semantic Versioning](https://semver.org/):

- **MAJOR** (1.x.x → 2.0.0) -- breaking changes to the public API
- **MINOR** (1.0.x → 1.1.0) -- new features, new Notion API support, backward-compatible
- **PATCH** (1.0.0 → 1.0.1) -- bug fixes, documentation, dependency updates

### Pre-release versions

- `1.0.0-SNAPSHOT` -- development version (current)
- `1.0.0-RC1` -- release candidate
- `1.0.0` -- stable release

## What Constitutes a Breaking Change

| Change | Breaking? |
|---|---|
| Removing a public method or class | Yes |
| Changing a method signature | Yes |
| Changing default behavior (e.g. default timeout) | Yes |
| Adding a new method to an interface | Yes (for implementors) |
| Adding a new class or method | No |
| Adding a new optional builder parameter | No |
| Adding a new exception subclass | No |
| Adding new fields to model classes | No |
| Upgrading a transitive dependency | Depends |

## Release Checklist

1. **Verify tests pass**

   ```bash
   ./gradlew clean build
   ```

2. **Verify formatting**

   ```bash
   ./gradlew spotlessCheck
   ```

3. **Update version in `build.gradle`**

   ```groovy
   version = '1.0.0'  // remove -SNAPSHOT
   ```

4. **Update documentation**
   - Update version numbers in `README.md` and `docs/getting-started.md`
   - Update changelog (see below)

5. **Tag the release**

   ```bash
   git tag -a v1.0.0 -m "Release 1.0.0"
   git push origin v1.0.0
   ```

6. **Publish to Maven Central**

   > **Status: Planned** -- Gradle publishing plugin is not yet configured.

   ```bash
   ./gradlew publish
   ```

7. **Bump to next snapshot**

   ```groovy
   version = '1.1.0-SNAPSHOT'
   ```

8. **Create a GitHub Release**
   - Use the tag
   - Include the changelog entry
   - Attach the JAR artifact

## Changelog

Maintain a `CHANGELOG.md` in the project root following [Keep a Changelog](https://keepachangelog.com/) format:

```markdown
## [1.0.0] - 2026-04-01

### Added
- `NotionClient` entry point with `forToken()` and `builder()`
- Full block type model (30+ types)
- Interceptor pipeline: auth, versioning, logging, exchange recording
- Typed exception hierarchy for all Notion API errors
- `ApiPath` for URL template building

### Changed
- (nothing yet)

### Fixed
- (nothing yet)
```

## Branch Strategy

- **`main`** -- stable, release-ready code
- **Feature branches** -- `feature/description` or issue-number-based
- **Release branches** -- `release/1.0.0` for release preparation (if needed)

## See Also

- [Development Setup](development-setup.md) -- build and test commands
- [Architecture](architecture.md) -- understanding the codebase
- [Testing Guide](testing-guide.md) -- ensuring quality before release
