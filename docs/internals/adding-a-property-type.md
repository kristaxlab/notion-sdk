# Adding a page property type

Notion adds property types over time (`place` and `verification` are recent examples). This runbook lists every place a new type has to be registered. Read [ADR-0001](../adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md) first if you are unsure why the hierarchy is shaped this way; terminology used below is defined in [CONTEXT.md](../../CONTEXT.md).

## Step 0 — decide whether the property is paginated

This is the only decision that changes the amount of work. Notion paginates exactly five property types today: `relation`, `people`, `rich_text`, `title` and `rollup`. Everything else is non-paginated.

Check the property retrieve endpoint response for the new type:

- `"type": "<property_type>"` at the top level → **non-paginated**, do Step 1 only
- `"object": "list"` with `"type": "property_item"` and a `results` array → **paginated**, do Steps 1 and 2

Do not guess from the property's semantics. A property that *looks* list-like (`multi_select`, `files`) can still be non-paginated.

## Step 1 — the page property value

Every property type needs a `PagePropertyValue` subclass, because the page retrieve endpoint returns all types in the uniform embedded form.

1. Create `<Name>Property.java` in `model.page.property`. Model the type-named value field, and pin `type` to a constant:

   ```java
   @Getter
   @Setter
   public class CheckboxProperty extends PagePropertyValue {
     private final String type = PropertyType.CHECKBOX.type();

     private Boolean checkbox;
   }
   ```

2. Add the constant to `PropertyType`.
3. Register the subtype in the `@JsonSubTypes` list on `PagePropertyValue`, keeping the list alphabetical. Mark read-only types with a `// read-only` comment as the neighbouring entries do.

If you skip registration, the value silently deserializes to `UnknownProperty` instead of failing — so always cover the new type with a test.

## Step 2 — paginated types only

A paginated type needs three more classes plus two registrations:

1. `<Name>PropertyItem.java` — the property item metadata; register it in the `@JsonSubTypes` list on `PropertyItem`.
2. `Listed<Name>.java` — one entry of the `results` array, implementing `ListedItem`.
3. `<Name>PropertyList.java` — the page property list itself:

   ```java
   /** Page property list of a {@code relation} property. */
   @JsonDeserialize(using = JsonDeserializer.None.class)
   public final class RelationPropertyList
       extends PagePropertyList<RelationPropertyItem, ListedRelation> {}
   ```

   `@JsonDeserialize(using = JsonDeserializer.None.class)` is **required**. `PagePropertyList` declares a class-level custom deserializer, and without this annotation the subclass inherits it and recurses into itself, producing a `StackOverflowError`.

4. Add the class to the `permits` clause of the sealed `PagePropertyList`, and add an `as<Name>List()` accessor next to the existing ones.
5. Add a `case` to the `switch` in `PagePropertyListDeserializer`, which resolves the target class from the nested `property_item.type` field. Unregistered types fall through to `UnknownPropertyList`.

## Step 3 — writing support and schemas

These are separate hierarchies; a new property type does not automatically appear in either.

- **`fluent/NotionProperties`** — add factory methods only if the property is writable. Read-only types (`created_by`, `created_time`, `last_edited_by`, `last_edited_time`, `unique_id`, `verification`, `formula`, `rollup`) must not get one — none of them currently has a factory, and that is deliberate.
- **`model/datasource/properties/<Name>Schema.java`** — needed if the type can appear in a data source's property schema; register it in the `@JsonSubTypes` list on `DataSourcePropertySchema`.
- **`fluent/NotionSchema` and `fluent/NotionSchemaBuilder`** — add a static factory on `NotionSchema` and a matching `nameOrId` method on `NotionSchemaBuilder` that delegates to it. Without both, callers cannot declare the column through `properties(s -> s....)`. Schema factories are required even for read-only types if the column can be added to a data source; skip the pair only if Notion rejects the type on schema create/update.
- **`fluent/NotionPageViewer`** — add a shortcut accessor only if the type warrants one. Generic access via `property(name, Class)` already works for every registered type.

## Step 4 — tests

1. Add a fixture under `src/test/resources/json/`: `retrieve-property-value-<type>.json` for a page property value, `retrieve-property-list-<type>.json` for a page property list. Use a real recorded response — see [Exchange Recording](exchange-recording.md).
2. Extend `PagePropertyDeserializeTest` to assert the JSON resolves to the concrete class, not to `UnknownProperty` / `UnknownPropertyList`.
3. For paginated types, also assert `propertyItem` is populated and `results` holds the expected listed item type.

## Step 5 — documentation

Update only what actually changed:

| Change | Update |
| --- | --- |
| Any new type | Javadoc on the new classes |
| A new paginated type | The paginated-property list in [ADR-0001](../adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md), [CONTEXT.md](../../CONTEXT.md) and [Page properties](../cookbook/page-properties.md) — those enumerate `relation`, `people`, `rich_text`, `title` and `rollup` |
| A response shape the model cannot express | A new ADR superseding 0001 — do not quietly rewrite 0001 |

A non-paginated type needs no prose changes. It is one more entry in a list the docs deliberately do not enumerate.

## See also

- [ADR-0001](../adr/0001-complex-hierarchy-and-deserialization-of-page-properties.md) — why the hierarchy has three types
- [Architecture](architecture.md#polymorphic-type-resolution) — the project-wide type-resolution convention
- [Page properties cookbook](../cookbook/page-properties.md) — the client-facing view
