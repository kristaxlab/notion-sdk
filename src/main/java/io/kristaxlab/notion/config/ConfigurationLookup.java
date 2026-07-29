package io.kristaxlab.notion.config;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationLookup {

  public static boolean lookupBoolean(String key, boolean defaultValue) {
    return lookupBoolean(key).orElse(defaultValue);
  }

  public static Optional<Boolean> lookupBoolean(String key) {
    String value = lookup(key).orElse(null);
    return (value == null || value.trim().isEmpty()) ? Optional.empty() : Optional.of(Boolean.parseBoolean(value));
  }

  public static String lookup(String key, String defaultValue) {
    // TODO check
    return lookup(key).orElse(defaultValue);
  }

  public static Optional<String> lookup(String key) {

    List<String> keyModifications = getKeyModifications(key);
    for (String modifiedKey : keyModifications) {
      String value = System.getenv(modifiedKey);
      if (value != null && !value.trim().isEmpty()) {
        return Optional.of(value);
      }
    }

    for (String modifiedKey : keyModifications) {
      String value = System.getProperty(modifiedKey);
      if (value != null && !value.trim().isEmpty()) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  /**
   * Identifies key format and builds all the possible key variations: ENV_LIKE key, camelCase,
   * dot.separated
   *
   * @param key initialKey
   * @return list of key variations (including the key itself)
   */
  public static List<String> getKeyModifications(String key) {
    if (key == null || key.trim().isEmpty()) {
      return new ArrayList<>();
    }

    Set<String> variations = new LinkedHashSet<>();
    variations.add(key);

    List<String> words = tokenize(key);

    // 1. Adding ENV_LIKE (ex: USER_PROFILE_ID)
    variations.add(buildEnvLike(words));

    // 2. Adding camelCase (ex: userProfileId)
    variations.add(buildCamelCase(words));

    // 3. Adding dot.separated (ex: user.profile.id)
    variations.add(buildDotSeparated(words));

    return new ArrayList<>(variations);
  }

  /** Regex to separate string to words. Supports camelCase, dots, hyphens, underscores */
  private static List<String> tokenize(String key) {
    List<String> words = new ArrayList<>();
    Pattern pattern = Pattern.compile("[A-Z]+(?=[A-Z][a-z])|[A-Z]?[a-z]+|[A-Z]+|[0-9]+");
    Matcher matcher = pattern.matcher(key);

    while (matcher.find()) {
      words.add(matcher.group().toLowerCase());
    }
    return words;
  }

  private static String buildEnvLike(List<String> words) {
    return String.join("_", words).toUpperCase();
  }

  private static String buildCamelCase(List<String> words) {
    StringBuilder sb = new StringBuilder(words.get(0));
    for (int i = 1; i < words.size(); i++) {
      String word = words.get(i);
      sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
    }

    return sb.toString();
  }

  private static String buildDotSeparated(List<String> words) {
    return String.join(".", words);
  }
}
