package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Schema params for a Notion {@code status} database property.
 *
 * <p>Because the Notion API handles status schema differently on creation vs. update, this class
 * exposes two distinct entry points:
 *
 * <ul>
 *   <li>{@link #builder()} — for <strong>creating</strong> a new status property. Notion silently
 *       ignores group configuration during creation and places every option in the built-in "To-do"
 *       group automatically. Only option names and colors are needed here.
 *   <li>{@link #editor(StatusSchema)} — for <strong>reorganising groups</strong> after the property
 *       has been created. Accepts the live {@link StatusSchema} (retrieved from Notion) so that
 *       real Notion-assigned IDs are resolved internally by name, mirroring the drag-and-drop
 *       experience in the Notion UI.
 * </ul>
 *
 * <p>Typical usage:
 *
 * <pre>{@code
 * // ── Step 1: create with options ────────────────────────────────────────
 * DataSourceSchemaBuilder.builder()
 *     .status("Status", StatusSchemaParams.builder()
 *         .option("Idea",        Color.DEFAULT)
 *         .option("In Progress", Color.YELLOW)
 *         .option("Completed",   Color.GREEN)
 *         .option("Cancelled",   Color.RED)
 *         .build())
 *     .build();
 *
 * // ── Step 2: retrieve live schema to get real Notion IDs ─────────────────
 * DataSource ds = notion.dataSources().retrieve(dsId);
 * StatusSchema existing = (StatusSchema) ds.getProperties().get("Status");
 *
 * // ── Step 3: reorganise options into groups ──────────────────────────────
 * StatusSchemaParams reorganised = StatusSchemaParams.editor(existing)
 *     .toDo()
 *         .option("Idea")
 *     .inProgress()
 *         .option("In Progress")
 *     .completed()
 *         .option("Completed")
 *         .option("Cancelled")
 *     .build();
 * }</pre>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusSchemaParams extends DataSourcePropertySchemaParams {

  @JsonProperty("status")
  private StatusConfig status = new StatusConfig();

  @Data
  public static class StatusConfig {

    @JsonProperty("options")
    private List<StatusOption> options;

    @JsonProperty("groups")
    private List<StatusGroup> groups;
  }

  // ── Static factories ────────────────────────────────────────────────────

  /**
   * Returns a builder for <em>creating</em> a new status property.
   *
   * <p>Notion ignores group configuration on creation; all options are placed in the built-in
   * "To-do" group automatically. Use {@link #editor(StatusSchema)} to reorganise options across
   * groups once the property has been created and Notion has assigned real IDs.
   *
   * @return a new {@link Builder}
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns an editor for <em>reorganising the group membership</em> of an existing status
   * property.
   *
   * <p>The {@code existing} schema — typically obtained by retrieving the data source and casting
   * the property value to {@link StatusSchema} — carries the real Notion-assigned IDs for every
   * option and group. The {@link Editor} lets you work by <em>name</em> (exactly as in the Notion
   * UI) and resolves those names to the correct IDs internally before sending the update.
   *
   * <pre>{@code
   * DataSource ds = notion.dataSources().retrieve(dsId);
   * StatusSchema existing = (StatusSchema) ds.getProperties().get("Status");
   *
   * StatusSchemaParams updated = StatusSchemaParams.editor(existing)
   *     .toDo()
   *         .option("Idea")
   *     .inProgress()
   *         .option("In Progress")
   *     .completed()
   *         .option("Completed")
   *         .option("Cancelled")
   *     .build();
   * }</pre>
   *
   * @param existing the live {@link StatusSchema} retrieved from Notion
   * @return a new {@link Editor}
   */
  public static Editor editor(StatusSchema existing) {
    return new Editor(existing);
  }

  // ── Creation Builder ────────────────────────────────────────────────────

  /**
   * Fluent builder for <em>creating</em> a status property schema.
   *
   * <p>Notion ignores group configuration on creation and places all options in the built-in
   * "To-do" group by default. After creation, use {@link StatusSchemaParams#editor(StatusSchema)}
   * to reorganise options across groups.
   */
  public static class Builder {

    private final List<StatusOption> options = new ArrayList<>();

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
     * @param color the raw color string accepted by the Notion API
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
     * Builds and returns the {@link StatusSchemaParams}.
     *
     * @return the configured params, ready to pass to the create-data-source request
     */
    public StatusSchemaParams build() {
      StatusSchemaParams params = new StatusSchemaParams();
      StatusConfig config = new StatusConfig();
      config.setOptions(options.isEmpty() ? null : new ArrayList<>(options));
      params.setStatus(config);
      return params;
    }
  }

  // ── Update Editor ────────────────────────────────────────────────────────

  /**
   * Fluent editor for <em>reorganising the group membership</em> of an existing status property.
   *
   * <p>The editor mirrors the Notion UI experience: call {@link #group(String)} to select a target
   * group by name, then call {@link #option(String)} one or more times to move options into that
   * group. All lookups are performed by name (case-sensitive) against the live schema supplied to
   * {@link StatusSchemaParams#editor(StatusSchema)}, with ID-based fallback.
   *
   * <p>Each option belongs to exactly one group. When an option is moved to a group via {@link
   * #option(String)}, it is automatically removed from whichever group currently holds it.
   *
   * <p>Groups that are not explicitly referenced by {@link #group(String)} retain their existing
   * option membership unchanged.
   */
  public static class Editor {

    /** The fixed name Notion always assigns to the first default group. */
    public static final String GROUP_TODO = "To-do";

    /** The fixed name Notion always assigns to the second default group. */
    public static final String GROUP_IN_PROGRESS = "In progress";

    /** The fixed name Notion always assigns to the third default group. */
    public static final String GROUP_COMPLETED = "Completed";

    private final List<StatusOption> options;
    private final List<StatusGroup> groups;

    /** Fast name-based lookup — the primary way SDK users reference options. */
    private final Map<String, StatusOption> optionsByName;

    /** Fast name-based lookup — the primary way SDK users reference groups. */
    private final Map<String, StatusGroup> groupsByName;

    private StatusGroup activeGroup;

    private Editor(StatusSchema existing) {
      StatusSchema.StatusConfig cfg =
          (existing.getStatus() != null) ? existing.getStatus() : new StatusSchema.StatusConfig();

      // Deep-copy options (preserve Notion-assigned IDs; no groups needed here)
      List<StatusOption> srcOptions =
          cfg.getOptions() != null ? cfg.getOptions() : Collections.emptyList();
      this.options =
          srcOptions.stream()
              .map(
                  o -> {
                    StatusOption copy = new StatusOption();
                    copy.setId(o.getId());
                    copy.setName(o.getName());
                    copy.setColor(o.getColor());
                    return copy;
                  })
              .collect(Collectors.toCollection(ArrayList::new));

      // Deep-copy groups (preserve Notion-assigned IDs; option_ids rebuilt per group() call)
      List<StatusGroup> srcGroups =
          cfg.getGroups() != null ? cfg.getGroups() : Collections.emptyList();
      this.groups =
          srcGroups.stream()
              .map(
                  g -> {
                    StatusGroup copy = new StatusGroup();
                    copy.setId(g.getId());
                    copy.setName(g.getName());
                    copy.setColor(g.getColor());
                    copy.setOptionIds(
                        g.getOptionIds() != null
                            ? new ArrayList<>(g.getOptionIds())
                            : new ArrayList<>());
                    return copy;
                  })
              .collect(Collectors.toCollection(ArrayList::new));

      this.optionsByName =
          this.options.stream()
              .filter(o -> o.getName() != null)
              .collect(
                  Collectors.toMap(
                      StatusOption::getName, Function.identity(), (a, b) -> a, LinkedHashMap::new));

      this.groupsByName =
          this.groups.stream()
              .filter(g -> g.getName() != null)
              .collect(
                  Collectors.toMap(
                      StatusGroup::getName, Function.identity(), (a, b) -> a, LinkedHashMap::new));
    }

    /**
     * Selects an existing group as the active target for subsequent {@link #option(String)} calls.
     *
     * <p>Because Notion always creates exactly three groups with fixed names — {@value
     * #GROUP_TODO}, {@value #GROUP_IN_PROGRESS}, and {@value #GROUP_COMPLETED} — prefer the named
     * convenience methods {@link #toDo()}, {@link #inProgress()}, and {@link #completed()} over
     * this method. Use {@code group(nameOrId)} only as an escape hatch when a group ID must be used
     * instead of a name.
     *
     * <p>Selecting a group <strong>resets</strong> its current option membership — only options
     * explicitly assigned via {@link #option(String)} after this call will belong to the group in
     * the resulting schema. Groups that are never selected keep their current membership intact.
     *
     * @param nameOrId the group name (preferred) or Notion-assigned group ID
     * @return this editor
     * @throws IllegalArgumentException if no group with the given name or ID exists in the
     *     underlying schema
     */
    public Editor group(String nameOrId) {
      StatusGroup g = groupsByName.get(nameOrId);
      if (g == null) {
        g =
            groups.stream()
                .filter(gr -> nameOrId.equals(gr.getId()))
                .findFirst()
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "No status group found with name or ID: \""
                                + nameOrId
                                + "\"."
                                + " Available groups: "
                                + groupsByName.keySet()));
      }
      g.setOptionIds(new ArrayList<>()); // reset; rebuilt by subsequent option() calls
      activeGroup = g;
      return this;
    }

    /**
     * Selects the <strong>"To-do"</strong> group as the active target for subsequent {@link
     * #option(String)} calls.
     *
     * <p>Equivalent to {@code group(}{@value #GROUP_TODO}{@code )}.
     *
     * @return this editor
     * @throws IllegalArgumentException if the "To-do" group is not present in the underlying schema
     */
    public Editor toDo() {
      return group(GROUP_TODO);
    }

    /**
     * Selects the <strong>"In progress"</strong> group as the active target for subsequent {@link
     * #option(String)} calls.
     *
     * <p>Equivalent to {@code group(}{@value #GROUP_IN_PROGRESS}{@code )}.
     *
     * @return this editor
     * @throws IllegalArgumentException if the "In progress" group is not present in the underlying
     *     schema
     */
    public Editor inProgress() {
      return group(GROUP_IN_PROGRESS);
    }

    /**
     * Selects the <strong>"Completed"</strong> group as the active target for subsequent {@link
     * #option(String)} calls.
     *
     * <p>Equivalent to {@code group(}{@value #GROUP_COMPLETED}{@code )}.
     *
     * @return this editor
     * @throws IllegalArgumentException if the "Completed" group is not present in the underlying
     *     schema
     */
    public Editor completed() {
      return group(GROUP_COMPLETED);
    }

    /**
     * Sets the color of the currently active group.
     *
     * <p>Must be called after {@link #toDo()}, {@link #inProgress()}, {@link #completed()}, or
     * {@link #group(String)} has been used to select a group.
     *
     * @param color the new group color
     * @return this editor
     * @throws IllegalStateException if no group has been selected yet
     */
    public Editor color(Color color) {
      return color(color == null ? null : color.getValue());
    }

    /**
     * Sets the color of the currently active group using a raw color string (e.g. {@code "blue"}).
     *
     * <p>Prefer the {@link #color(Color)} overload where possible.
     *
     * @param color the raw color string accepted by the Notion API, or {@code null} to clear
     * @return this editor
     * @throws IllegalStateException if no group has been selected yet
     */
    public Editor color(String color) {
      if (activeGroup == null) {
        throw new IllegalStateException(
            "Call toDo(), inProgress(), completed(), or group() before color().");
      }
      activeGroup.setColor(color);
      return this;
    }

    /**
     * Moves an existing option into the currently active group.
     *
     * <p>The option is looked up by name (case-sensitive) first, then by Notion-assigned ID,
     * against the live schema supplied to {@link StatusSchemaParams#editor(StatusSchema)}. If the
     * option currently belongs to another group it is removed from that group automatically,
     * enforcing the Notion constraint that each option belongs to exactly one group.
     *
     * @param nameOrId the option name (preferred) or Notion-assigned option ID
     * @return this editor
     * @throws IllegalStateException if {@link #group(String)} has not been called yet
     * @throws IllegalArgumentException if no option with the given name or ID exists in the
     *     underlying schema
     */
    public Editor option(String nameOrId) {
      if (activeGroup == null) {
        throw new IllegalStateException(
            "Call group() before option() to specify the target group.");
      }
      StatusOption opt = optionsByName.get(nameOrId);
      if (opt == null) {
        opt =
            options.stream()
                .filter(o -> nameOrId.equals(o.getId()))
                .findFirst()
                .orElseThrow(
                    () ->
                        new IllegalArgumentException(
                            "No status option found with name or ID: \""
                                + nameOrId
                                + "\"."
                                + " Available options: "
                                + optionsByName.keySet()));
      }
      final String optionId = opt.getId();
      // Enforce uniqueness: remove from every other group first
      groups.stream()
          .filter(g -> g != activeGroup && g.getOptionIds() != null)
          .forEach(g -> g.getOptionIds().remove(optionId));
      // Add to the active group (guard against duplicates)
      if (!activeGroup.getOptionIds().contains(optionId)) {
        activeGroup.getOptionIds().add(optionId);
      }
      return this;
    }

    /**
     * Adds a brand-new option to the currently active group.
     *
     * <p>Use this when you want to introduce an option that does not yet exist in Notion. A
     * provisional ID is generated locally; Notion replaces it with a canonical ID after the update
     * round-trip.
     *
     * @param name the new option name
     * @param color the option color, or {@code null} for the Notion default
     * @return this editor
     * @throws IllegalStateException if {@link #group(String)} has not been called yet
     */
    public Editor addOption(String name, Color color) {
      if (activeGroup == null) {
        throw new IllegalStateException(
            "Call group() before addOption() to specify the target group.");
      }
      StatusOption opt = new StatusOption();
      opt.setName(name);
      opt.setId(UUID.randomUUID().toString()); // provisional; Notion replaces on response
      if (color != null) {
        opt.setColor(color.getValue());
      }
      options.add(opt);
      optionsByName.put(name, opt);
      activeGroup.getOptionIds().add(opt.getId());
      return this;
    }

    /**
     * Builds and returns the {@link StatusSchemaParams} representing the reorganised status schema,
     * ready to be sent to the Notion update-data-source endpoint.
     *
     * @return the configured params
     */
    public StatusSchemaParams build() {
      StatusSchemaParams params = new StatusSchemaParams();
      StatusConfig config = new StatusConfig();
      config.setOptions(options.isEmpty() ? null : new ArrayList<>(options));
      config.setGroups(groups.isEmpty() ? null : new ArrayList<>(groups));
      params.setStatus(config);
      return params;
    }
  }
}
