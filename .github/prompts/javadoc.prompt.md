---
agent: 'agent'
description: 'Generate Javadoc comments and update existing ones if needed.'
---

Generate Javadoc comments and update existing ones if needed for  ${input:classOrMethod}.

Ask me about ${input:classOrMethod} if it is not clear to you.

# Required reading

Read these before writing any prose:

- `docs/internals/javadoc.md` — Javadoc conventions for this repository
- `CONTEXT.md` — binding vocabulary (see `AGENTS.md`)

# Evaluation

```bash
./gradlew javadoc
```
