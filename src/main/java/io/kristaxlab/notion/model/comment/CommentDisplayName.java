package io.kristaxlab.notion.model.comment;

import lombok.Getter;
import lombok.Setter;

/**
 * Author name shown for a comment.
 *
 * <p>Create requests set {@code type} and, when {@code type} is {@code custom}, a nested {@code
 * custom.name}. Responses return {@code type} and {@code resolved_name}. {@code type} is a string
 * so a value Notion adds later still deserializes. Factories use {@link CommentDisplayNameType} for
 * the types the SDK can build today.
 *
 * @see CommentDisplayNameType
 * @see <a href="https://developers.notion.com/reference/comment-display-name">Comment display
 *     name</a>
 */
@Getter
@Setter
public class CommentDisplayName {

  /** API token for how the author name is chosen. */
  private String type;

  /** Only in request */
  private Custom custom;

  /** Only in response */
  private String resolvedName;

  /**
   * Creates a comment display name that uses the authenticated user.
   *
   * @return comment display name with type {@link CommentDisplayNameType#USER}
   */
  public static CommentDisplayName user() {
    return ofType(CommentDisplayNameType.USER);
  }

  /**
   * Creates a comment display name that uses the integration name.
   *
   * @return comment display name with type {@link CommentDisplayNameType#INTEGRATION}
   */
  public static CommentDisplayName integration() {
    return ofType(CommentDisplayNameType.INTEGRATION);
  }

  /**
   * Creates a comment display name with a custom label.
   *
   * @param name custom name shown on the comment
   * @return comment display name with type {@link CommentDisplayNameType#CUSTOM}
   */
  public static CommentDisplayName custom(String name) {
    CommentDisplayName displayName = ofType(CommentDisplayNameType.CUSTOM);
    Custom custom = new Custom();
    custom.setName(name);
    displayName.setCustom(custom);
    return displayName;
  }

  private static CommentDisplayName ofType(CommentDisplayNameType type) {
    CommentDisplayName displayName = new CommentDisplayName();
    displayName.setType(type.type());
    return displayName;
  }

  /** Custom name object sent when the comment display name type is {@code custom}. */
  @Getter
  @Setter
  public static class Custom {

    /** Custom name shown on the comment. */
    private String name;
  }
}
