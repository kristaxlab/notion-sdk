package testkit.util;

import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * If base url changed from "https://www.notion.so/", see TestSessionConfig for the configuration
 * parameter to change it.
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

  public static String getNotionBaseUrl(ExtensionContext context) {
    return TestConfigurationLookup.lookup(NOTION_LINKS_BASE_URL, context).orElse(DEFAULT_BASE_URL);
  }
}
