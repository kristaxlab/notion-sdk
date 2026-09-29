package testkit.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Extracts the test id ({@code IT-8}, {@code IT-?}) from a method {@code @DisplayName}. */
public class NotionTestIdRetriever {

  public static Optional<String> retrieveTestId(String displayName) {
    // Extract the test id from the display name using regex
    Pattern pattern = Pattern.compile("(?i)\\bIT-(?:\\d+|\\?+)");
    Matcher matcher = pattern.matcher(displayName);
    if (matcher.find()) {
      return Optional.of(matcher.group().toUpperCase());
    }
    return Optional.empty();
  }
}
