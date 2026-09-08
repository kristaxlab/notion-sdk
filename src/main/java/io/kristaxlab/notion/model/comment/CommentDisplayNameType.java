package io.kristaxlab.notion.model.comment;

/**
 * Comment display name types the SDK can build today.
 *
 * <p>{@link CommentDisplayName} stores {@code type} as a string so a new Notion value still
 * deserializes. Use these constants only when creating a comment display name.
 *
 * @see CommentDisplayName
 * @see <a href="https://developers.notion.com/reference/create-a-comment">Create a comment</a>
 */
public enum CommentDisplayNameType {

  /** Name of the authenticated user or token owner. */
  USER("user"),

  /** Custom label sent as {@code custom.name}. */
  CUSTOM("custom"),

  /** Name of the integration that created the comment. */
  INTEGRATION("integration");

  private final String type;

  CommentDisplayNameType(String type) {
    this.type = type;
  }

  /**
   * Returns the API token for this comment display name type.
   *
   * @return {@code user}, {@code custom}, or {@code integration}
   */
  public String type() {
    return type;
  }

  /**
   * Returns the comment display name type for an API token.
   *
   * @param type {@code user}, {@code custom}, or {@code integration}
   * @return the matching constant
   * @throws IllegalArgumentException if {@code type} is not a known comment display name type
   */
  public static CommentDisplayNameType fromValue(String type) {
    for (CommentDisplayNameType displayNameType : CommentDisplayNameType.values()) {
      if (displayNameType.type.equals(type)) {
        return displayNameType;
      }
    }
    throw new IllegalArgumentException("Unknown comment display name type: " + type);
  }
}
