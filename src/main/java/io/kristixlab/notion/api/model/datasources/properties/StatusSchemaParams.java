package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Schema params for a Notion {@code status} database property.
 *
 * <p>Use {@link #builder()} to define the desired set of options. The update request must always
 * carry the <em>full</em> desired options list: options present in the list are kept/created,
 * options omitted are deleted.
 *
 * <p><strong>Note on groups:</strong> Notion status groups ({@code To-do}, {@code In progress},
 * {@code Complete}) are <em>read-only</em> via the API. Every option added through the API is
 * automatically placed in the default group. Group membership cannot be changed programmatically.
 *
 * <p>Typical usage — create:
 *
 * <pre>{@code
 * DataSourceSchemaBuilder.builder()
 *     .status("Status", StatusSchemaParams.builder()
 *         .option("Idea",        Color.DEFAULT)
 *         .option("In Progress", Color.YELLOW)
 *         .option("Completed",   Color.GREEN)
 *         .build())
 *     .build();
 * }</pre>
 *
 * <p>Typical usage — update (send the full desired options list):
 *
 * <pre>{@code
 * DataSource ds = notion.dataSources().retrieve(dsId);
 * StatusSchema existing = (StatusSchema) ds.getProperties().get("Status");
 *
 * // Seed the builder from the existing options, then add/remove as needed.
 * StatusSchemaParams updated = StatusSchemaParams.builder(existing.getStatus().getOptions())
 *     .option("Cancelled", Color.RED)   // add new option
 *     .remove("Idea")                   // omit → Notion will delete it
 *     .build();
 * }</pre>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private StatusConfig status = new StatusConfig();

  @Data
  public static class StatusConfig {

    @JsonProperty("options")
    private List<StatusOption> options;
  }

  // ── Static factory ────────────────────────────────────────────────────

  /**
   * i * Returns a builder pre-seeded with the given list of existing options.
   *
   * <p>Each option is deep-copied so the builder is fully self-contained. Use this factory when
   * updating an existing status property: seed from the live options, then call {@link
   * Builder#option} to add new ones and {@link Builder#remove} to exclude ones that should be
   * deleted.
   *
   * @param existingOptions the current options retrieved from a {@link StatusSchema}
   * @return a new {@link Builder} pre-populated with copies of {@code existingOptions}
   */
  public static Builder builder(List<StatusOption> existingOptions) {
    return new Builder().options(existingOptions);
  }

  /**
   * Returns an empty builder.
   *
   * <p>Notion ignores group configuration; all options are placed in the built-in "To-do" group
   * automatically. On update, send the <em>full</em> desired options list — Notion treats omitted
   * options as deletions.
   *
   * @return a new {@link Builder}
   */
  public static Builder builder() {
    return new Builder();
  }

  // ── Builder ────────────────────────────────────────────────────────────

  /**
   * Fluent builder for a status property schema.
   *
   * <p>Each {@code option()} call appends one option to the list. When used for updates, include
   * every option that should remain after the operation (Notion deletes any option not present in
   * the list).
   */
  public static class Builder {

    private final List<StatusOption> options = new ArrayList<>();

    /**
     * Appends all options from the given list, deep-copying each entry to preserve Notion-assigned
     * IDs and colors.
     *
     * <p>Intended for seeding an update payload from the options of a retrieved {@link
     * StatusSchema}. Equivalent to calling {@link #option(String, String)} for every entry, but
     * without having to enumerate them manually.
     *
     * @param existingOptions the options to copy into this builder; {@code null} is treated as an
     *     empty list
     * @return this builder
     */
    public Builder options(List<StatusOption> existingOptions) {
      if (existingOptions != null) {
        for (StatusOption src : existingOptions) {
          StatusOption copy = new StatusOption();
          copy.setId(src.getId());
          copy.setName(src.getName());
          copy.setColor(src.getColor());
          options.add(copy);
        }
      }
      return this;
    }

    /**
     * Adds a status option with the default color.
     *
     * @param name the option name as it will appear in Notion
     * @return this builder
     */
    public Builder option(String name) {
      return option(name, (Color) null);
    }

    /**
     * Adds a status option with the given {@link Color}.
     *
     * @param name the option name
     * @param color the option color, or {@code null} for the Notion default
     * @return this builder
     */
    public Builder option(String name, Color color) {
      return option(name, color == null ? null : color.getValue());
    }

    /**
     * Adds a status option with the given raw color string (e.g. {@code "blue"}).
     *
     * <p>Prefer the {@link #option(String, Color)} overload where possible.
     *
     * @param name the option name
     * @param color the raw color string accepted by the Notion API, or {@code null} for the default
     * @return this builder
     */
    public Builder option(String name, String color) {
      StatusOption opt = new StatusOption();
      opt.setName(name);
      if (color != null) {
        opt.setColor(color);
      }
      options.add(opt);
      return this;
    }

    /**
     * Removes a previously added option from the list by name.
     *
     * <p>No-op if no option with the given name is present. Useful when building an update payload
     * from a pre-populated list and a specific option needs to be excluded.
     *
     * @param name the option name to remove (case-sensitive)
     * @return this builder
     */
    public Builder remove(String name) {

      options.removeIf(o -> name.equals(o.getName()));
      return this;
    }

    /**
     * Builds and returns the {@link StatusSchemaParams}.
     *
     * @return the configured params, ready to pass to the create or update-data-source request
     */
    public StatusSchemaParams build() {
      StatusSchemaParams params = new StatusSchemaParams();
      StatusConfig config = new StatusConfig();
      config.setOptions(options.isEmpty() ? null : new ArrayList<>(options));
      params.setStatus(config);
      return params;
    }
  }
}
