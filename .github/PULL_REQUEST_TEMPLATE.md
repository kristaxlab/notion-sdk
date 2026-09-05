## What changed

<!-- One or two sentences. Link the issue if there is one. -->

## Documentation checklist

Tick what applies, delete what does not. Each row names the *single* place that fact belongs — link
to it from elsewhere instead of restating it.

- [ ] **Public API changed** → Javadoc updated on the changed types (`./gradlew javadoc` passes)
- [ ] **New or changed usage pattern** → the matching page under `docs/cookbook/` updated
- [ ] **A design decision was made or reversed** → new ADR in `docs/adr/`, or an existing one marked `Superseded`
- [ ] **A new term entered the vocabulary** → defined once in `CONTEXT.md` and used consistently
- [ ] **A Notion API rule was discovered the hard way** → `docs/internals/notion-api-constraints.md`
- [ ] **Contributor-facing mechanics changed** → `docs/internals/`
- [ ] **A new doc was added** → linked from `README.md` and `llms.txt`, so it cannot rot unnoticed

No documentation change needed because: <!-- e.g. internal refactor, no behavior or vocabulary change -->

## Verification

<!-- What you ran. Integration tests need NOTION_TEST_AUTH_TOKEN; see docs/internals/testing-guide.md -->

- [ ] `./gradlew build`
- [ ] `./gradlew testIntegration`
