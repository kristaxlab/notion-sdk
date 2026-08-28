package testkit.util;

/**
 * If base url changed from "https://www.notion.so/", see TestSessionConfig for the configuration
 * parameter to change it.
 */
public class NotionPageUrlResolver {

  public static String resolveNotionPageUrl(String baseUrl, String pageId) {
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("baseUrl cannot be null or empty");
    }

    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }

    return baseUrl + pageId.replace("-", "");
  }
}
