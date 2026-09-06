---
agent: 'agent'
description: 'Write or extend a Notion SDK integration test.'
---

Write a new integration test (or finish ${input:classOrMethod}) against the live Notion API.

Ask if the scenario, endpoint, or test id is unclear. Do not invent a test id — use `IT-?` until
one is assigned.

# Required reading

Read these before writing code or prose:

- `docs/internals/testing-guide.md` — how the suite is run and how a test is written
- `CONTEXT.md` — binding vocabulary (see `AGENTS.md`)
- `docs/internals/notion-api-constraints.md` — Notion rules that produce a 400 if you ignore them

Tracking board for existing tests (ids and status):
https://www.notion.so/kristalamenweb/2e8c5b968ec4804d8b91c99c1e04b0ca

# Evaluation

Run the new class, not the whole suite, until it is green:

```bash
./gradlew testIntegration --tests "tests.pages.IT1_Pages_CRUD"
```
