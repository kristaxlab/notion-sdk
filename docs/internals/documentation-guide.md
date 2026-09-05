# Documentation guide

How this project's documentation is organized and maintained. The goal is that every fact has exactly
one home, and that a reader who needs it can find that home.

## Which document owns what

Documents are grouped by *what change forces an edit*. If you know why you are writing something
down, this table tells you where it goes.

| Document | Edit trigger | Contains |
| --- | --- | --- |
| Javadoc | public API changed | signatures, contracts, nullability, thrown exceptions |
| `docs/cookbook/` | API surface or usage changed | runnable task recipes, minimal prose |
| `docs/adr/` | a decision was made or reversed | context, decision, rejected alternatives, consequences |
| `CONTEXT.md` | a term was coined or disputed | one definition per concept, plus the synonyms to avoid |
| `docs/internals/` | contributor-facing mechanics changed | how to extend, invariants, gotchas |
| `docs/internals/notion-api-constraints.md` | a Notion rule was discovered the hard way | constraints the types cannot express |
| `llms.txt` | the documentation map changed | paths only, for machine readers |

Do not restate a fact that another document owns — link to it. Duplication is not redundancy here; it
is two copies that will disagree within a few months.

## How much detail

The test is whether a reader could re-derive the information from the code in under a minute.

Write it down when the answer is no: Notion's cross-endpoint asymmetries, why a workaround exists,
which alternatives were rejected, constraints that only surface as a `400` at runtime.

Leave it out when the answer is yes. Field lists, subclass enumerations and method tables belong in
Javadoc, which is generated from the code and cannot drift. A Markdown table mirroring a class's
methods is the first thing to rot in any documentation set.

## The cookbook

[`docs/cookbook/README.md`](../cookbook/README.md) is the table of contents and the **only** place
where reading order and grouping are defined. GitHub renders it as the landing page for the
directory, and the root `README.md` links to it once.

Cookbook filenames describe content and carry no ordering — there are deliberately no numeric
prefixes. A filename is an identifier and a published URL, both of which should be stable, whereas a
document's position in a sequence changes every time something is inserted before it. Keeping order
in the index means a new recipe is slotted wherever it belongs by editing one list, and nothing is
ever renamed to make room.

When you add a recipe: create the page, add a row to the appropriate group in the cookbook index, and
add a "Related cookbook pages" footer linking to neighbouring recipes.

## Moving or removing a page

**Before 1.0.0** — delete or rename freely. Fix every inbound link in the same commit; the link check
described below will catch what you miss.

**From 1.0.0 onwards** — published paths are treated as stable, because most links to these files are
repository links that no web-server redirect can rewrite. A page that moves therefore leaves a
tombstone behind rather than a 404: the original file stays where it is, and its entire content is
replaced by a pointer to the new location.

```markdown
# Moved

This page now lives at [Page properties and pagination](page-properties.md).
```

Three rules keep a tombstone from decaying into the kind of invisible, half-live page this
convention exists to prevent:

1. A tombstone contains nothing but that heading and that single link. It never regains content.
2. Its target must exist, and the target must not itself be a tombstone.
3. Nothing inside the repository links to a tombstone. Internal links point at the real page; the
   tombstone exists only for links that were published outside the repository.

These rules are machine-checkable and the intention is to enforce them in
`.github/scripts/check_doc_links.py` when the convention takes effect. They are documented now so the
decision is settled before 1.0.0, not improvised during it.

## Automated checks

| Check | Runs | Catches |
| --- | --- | --- |
| `python3 .github/scripts/check_doc_links.py` | every push and PR | relative links to missing files, and `#anchors` with no matching heading |
| `./gradlew javadoc` | every push and PR | malformed Javadoc and unresolvable `{@link}` targets |
| `.github/PULL_REQUEST_TEMPLATE.md` | every PR, by hand | the documentation a change should have brought with it |

The link checker is offline and deterministic: it never fetches external URLs, so it cannot fail
because a third-party site is slow. It reproduces GitHub's heading-to-anchor rules, including the
doubled hyphen that a stripped em dash leaves behind.

Prose using the vocabulary from `CONTEXT.md` is not automated and relies on review.

## Adding a new document

Link it from the index it belongs to — the cookbook table of contents, the ADR index, or the root
`README.md` documentation table — and add it to `llms.txt`. An unlinked document is invisible: it
will not be found when it is needed, it will not be updated when the code changes, and it will still
be published to GitHub Pages while quietly going stale.
