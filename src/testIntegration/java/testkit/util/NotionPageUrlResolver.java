package testkit.util;

import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Builds inspectable Notion web URLs. The base URL is {@code notion.links.base.url} (default {@code
 * https://www.notion.so/}).
 */
public class NotionPageUrlResolver {

  private static final String NOTION_LINKS_BASE_URL = "notion.links.base.url";
  private static final String DEFAULT_BASE_URL = "https://www.notion.so/";

  public static String resolveNotionPageUrl(String baseUrl, String pageId) {
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("baseUrl cannot be null or empty");
    }

    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }

    return baseUrl + pageId.replace("-", "");
  }

  public static String resolveNotionPageUrl(ExtensionContext context, String pageId) {
    String baseUrl = getNotionBaseUrl(context);
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("baseUrl cannot be null or empty");
    }

    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }

    return baseUrl + pageId.replace("-", "");
  }

  public static String getNotionBaseUrl(ExtensionContext context) {
    return TestConfigurationLookup.lookup(NOTION_LINKS_BASE_URL, context).orElse(DEFAULT_BASE_URL);
  }
}
