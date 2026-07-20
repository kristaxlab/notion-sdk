# Notion data structures specifics.

Here we save the knowledge about Notion data specifics to make the SDK testing scenarios to better mirror the real
Notion use cases.

A page within a database may only have one property of type "unique_id" (represented as
io.kristaxlab.notion.model.page.property.UniqueIdProperty). While the prefix of the unique_id value can be customized
for existing pages, its "number" part is auto-incremented by Notion and cannot be set manually. 