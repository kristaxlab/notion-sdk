package testkit.ext;

import io.kristaxlab.notion.config.ConfigurationLookup;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Immutable configuration for a test session.
 *
 * <p>Encapsulates all parameters needed to provision a test session including parent container,
 * template, naming, and lifecycle settings.
 */
public class TestSessionConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionConfig.class);

  private static final String SESSION_PARENT_ID = "notion.tests.session.parent.id";
  private static final String SESSION_TEMPLATE_ID = "notion.tests.session.template.id";
  private static final String SESSION_TITLE = "notion.tests.session.title";
  private static final String CLEANUP_ENABLED = "notion.tests.session.cleanup";
  private static final String NOTION_BASE_URL = "notion.tests.base.url";

  private final String parentId;
  private final String templateId;
  private final String sessionTitle;
  private final boolean cleanupEnabled;
  private final String notionBaseUrl;

  private TestSessionConfig(
      String parentId,
      String templateId,
      String sessionTitle,
      boolean cleanupEnabled,
      String notionBaseUrl) {
    this.parentId = parentId;
    this.templateId = templateId;
    this.sessionTitle = sessionTitle;
    this.cleanupEnabled = cleanupEnabled;
    this.notionBaseUrl = notionBaseUrl;
  }

  /**
   * Resolves session configuration from the JUnit extension context.
   *
   * <p>Reads configuration from environment variables, system properties, and JUnit properties in
   * that order of precedence.
   *
   * @param context the JUnit extension context
   * @return resolved configuration
   * @throws IllegalStateException if required configuration is missing
   */
  public static TestSessionConfig from(ExtensionContext context) {
    LOGGER.debug("Resolving session configuration from environment, system, and JUnit properties");

    String parentId = lookupRequired(SESSION_PARENT_ID, context);
    String templateId = lookupOptional(SESSION_TEMPLATE_ID, context);
    String sessionTitle = lookupOptional(SESSION_TITLE, context);
    boolean cleanupEnabled = lookupBoolean(CLEANUP_ENABLED, context);
    String basePageUrl = lookup(NOTION_BASE_URL, context).orElse("https://www.notion.so/");

    return builder()
        .parentId(parentId)
        .templateId(templateId)
        .sessionTitle(sessionTitle)
        .cleanupEnabled(cleanupEnabled)
        .basePageUrl(basePageUrl)
        .build();
  }

  /**
   * Creates a builder for constructing session configuration.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  public String getParentId() {
    return parentId;
  }

  public String getTemplateId() {
    return templateId;
  }

  public String getSessionTitle() {
    return sessionTitle;
  }

  public boolean isCleanupEnabled() {
    return cleanupEnabled;
  }

  public String getNotionBaseUrl() {
    return notionBaseUrl;
  }

  private static String lookupRequired(String key, ExtensionContext context) {
    Optional<String> value = lookup(key, context);
    LOGGER.debug("{}: {}", key, value.orElse(null));

    if (value.isEmpty()) {
      throw new IllegalStateException("Required property " + key + " is missing");
    }
    return value.get();
  }

  private static String lookupOptional(String key, ExtensionContext context) {
    Optional<String> value = lookup(key, context);
    LOGGER.debug("{}: {}", key, value.orElse(null));
    return value.orElse(null);
  }

  private static boolean lookupBoolean(String key, ExtensionContext context) {
    String value = lookup(key, context).orElse(null);
    LOGGER.debug("{}: {}", key, value);
    return value == null || value.trim().isEmpty() ? false : (Boolean.parseBoolean(value));
  }

  private static Optional<String> lookup(String key, ExtensionContext testExtensionContext) {
    Optional<String> value = ConfigurationLookup.lookup(key);
    if (value.isPresent() && !value.get().trim().isEmpty()) {
      return value;
    }

    List<String> keyModifications = ConfigurationLookup.getKeyModifications(key);
    for (String modifiedKey : keyModifications) {
      value = testExtensionContext.getConfigurationParameter(modifiedKey);
      if (value.isPresent() && !value.get().trim().isEmpty()) {
        return value;
      }
    }

    return Optional.empty();
  }

  public static class Builder {
    private String parentId;
    private String templateId;
    private String sessionTitle;
    private boolean cleanupEnabled;
    private String basePageUrl;

    public Builder parentId(String parentId) {
      this.parentId = parentId;
      return this;
    }

    public Builder templateId(String templateId) {
      this.templateId = templateId;
      return this;
    }

    public Builder sessionTitle(String sessionTitle) {
      this.sessionTitle = sessionTitle;
      return this;
    }

    public Builder cleanupEnabled(boolean cleanupEnabled) {
      this.cleanupEnabled = cleanupEnabled;
      return this;
    }

    public Builder basePageUrl(String basePageUrl) {
      this.basePageUrl = basePageUrl;
      return this;
    }

    public TestSessionConfig build() {
      return new TestSessionConfig(parentId, templateId, sessionTitle, cleanupEnabled, basePageUrl);
    }
  }
}
