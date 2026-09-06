package io.kristaxlab.notion.model.database;

/**
 * Enumerates the database type values Notion accepts when creating a typed database.
 *
 * <p>Each constant is Notion's canonical schema for that type. Property names follow the language
 * preference of the user who owns the integration token; internal integrations receive English
 * names. Read the data source after create before writing pages.
 *
 * @see CreateDatabaseParams
 */
public enum DatabaseType {

  /**
   * Tasks schema: Task name ({@code title}), Assignee ({@code people}), Status ({@code status}),
   * Due ({@code date}).
   */
  TASKS("tasks"),

  /**
   * Projects schema: Project name ({@code title}), Owner ({@code people}), Dates ({@code date}),
   * Status ({@code status}).
   */
  PROJECTS("projects"),

  /**
   * Skills schema: Skill name ({@code title}), Description ({@code rich_text}), Files ({@code
   * files}), Tags ({@code multi_select}), Created by ({@code created_by}).
   */
  SKILLS("skills");

  private final String type;

  DatabaseType(String type) {
    this.type = type;
  }

  /**
   * Returns the API token for this database type.
   *
   * @return {@code tasks}, {@code projects}, or {@code skills}
   */
  public String type() {
    return type;
  }

  /**
   * Returns the database type for an API token.
   *
   * @param type {@code tasks}, {@code projects}, or {@code skills}
   * @return the matching constant
   * @throws IllegalArgumentException if {@code type} is not a known database type
   */
  public static DatabaseType fromValue(String type) {
    for (DatabaseType databaseType : DatabaseType.values()) {
      if (databaseType.type.equals(type)) {
        return databaseType;
      }
    }
    throw new IllegalArgumentException("Unknown database type: " + type);
  }
}
