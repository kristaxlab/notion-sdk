# Cookbook

Task-oriented recipes for the Notion SDK for Java. Each page is self-contained and shows runnable
code for one area of the API. Terminology used across these pages is defined in
[CONTEXT.md](../../CONTEXT.md).

**This page is the table of contents.** Filenames carry no ordering — the arrangement below is the
only place reading order and grouping are defined, so a new recipe can be slotted wherever it makes
sense without renaming anything. See the
[documentation guide](../internals/documentation-guide.md) for the conventions.

## Creating content

| Recipe | What it covers |
| --- | --- |
| [Creating pages](creating-pages.md) | Page creation, parents, icons, covers, initial content |
| [Creating pages from templates](templates.md) | Template instantiation and polling for asynchronously applied content |
| [Adding blocks](adding-blocks.md) | Appending children with the fluent builder, positional inserts |
| [Rich text and inline formatting](rich-text.md) | Text runs, styles, colors, mentions |
| [Structured layouts](structured-layouts.md) | Columns, tables, callouts, code blocks |

## Reading and updating

| Recipe | What it covers |
| --- | --- |
| [Reading page content](reading-content.md) | Page metadata, blocks, pagination, Markdown export |
| [Page properties and pagination](page-properties.md) | Reading property values and paging through paginated properties |
| [Updating pages](updating-pages.md) | Title, icon, cover, lock, move, archive, Markdown updates |
| [Updating blocks](updating-blocks.md) | Editing, deleting and restoring blocks |

## Files and workflows

| Recipe | What it covers |
| --- | --- |
| [Files and media uploads](files-and-media.md) | Single-part and multi-part uploads, external imports |
| [End-to-end recipes](end-to-end-recipes.md) | Complete workflows combining several endpoints |

## Related documentation

- [README](../../README.md) — installation and quickstart
- [Authentication](../authentication.md) and [Error handling](../error-handling.md)
- [Architecture Decision Records](../adr/README.md) — why the models look the way they do
- [Internals](../internals/architecture.md) — for contributors
