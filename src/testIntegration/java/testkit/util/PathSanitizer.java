package testkit.util;

public class PathSanitizer {

  /**
   * Converts a candidate file or directory name into a filesystem-safe identifier.
   *
   * <p>Illegal path characters are replaced with underscores, whitespace and dot runs are
   * normalized to a single underscore, repeated underscores are collapsed, and leading/trailing
   * underscores are removed.
   *
   * @param name input value to sanitize
   * @return sanitized identifier, or an empty string when {@code name} is {@code null}
   */
  public static String sanitize(String name) {
    if (name == null) return "";
    return name.replaceAll("[/\\\\:*?\"<>|()\\[\\]]", "_")
        .replaceAll("[\\s.]+", "_")
        .replaceAll("_+", "_")
        .replaceAll("^_|_$", "");
  }
}
