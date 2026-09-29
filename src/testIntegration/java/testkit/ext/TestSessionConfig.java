package testkit.ext;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testkit.util.TestConfigurationLookup;

/**
 * Immutable configuration for a test session: Test Session Parent Id, template, title, and cleanup.
 */
public class TestSessionConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSessionConfig.class);

  private static final String SESSION_PARENT_ID = "notion.tests.session.parent.id";
  private static final String SESSION_TEMPLATE_ID = "notion.tests.session.template.id";
  private static final String SESSION_TITLE = "notion.tests.session.title";
  private static final String CLEANUP_ENABLED = "notion.tests.session.cleanup";

  private final String parentId;
  private final String templateId;
  private final String sessionTitle;
  private final boolean cleanupEnabled;

  private TestSessionConfig(
      String parentId, String templateId, String sessionTitle, boolean cleanupEnabled) {
    this.parentId = parentId;
    this.templateId = templateId;
    this.sessionTitle = sessionTitle;
    this.cleanupEnabled = cleanupEnabled;
  }

  /**
   * Resolves session configuration through {@link TestConfigurationLookup}. Requires the Test
   * Session Parent Id.
   *
   * @param context the JUnit extension context
   * @return resolved configuration
   * @throws IllegalStateException if the Test Session Parent Id is missing
   */
  public static TestSessionConfig from(ExtensionContext context) {
    LOGGER.debug("Resolving session configuration from environment, system, and JUnit properties");

    String parentId = TestConfigurationLookup.lookupRequired(SESSION_PARENT_ID, context);
    String templateId = TestConfigurationLookup.lookupOptional(SESSION_TEMPLATE_ID, context);
    String sessionTitle = TestConfigurationLookup.lookupOptional(SESSION_TITLE, context);
    boolean cleanupEnabled = TestConfigurationLookup.lookupBoolean(CLEANUP_ENABLED, context);

    return builder()
        .parentId(parentId)
        .templateId(templateId)
        .sessionTitle(sessionTitle)
        .cleanupEnabled(cleanupEnabled)
        .build();
  }

  /**
   * Whether the test session page should be moved to trash when the run ends. Safe to call without
   * a Test Session Parent Id.
   */
  public static boolean cleanupEnabled(ExtensionContext context) {
    return TestConfigurationLookup.lookupBoolean(CLEANUP_ENABLED, context);
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

  public static class Builder {
    private String parentId;
    private String templateId;
    private String sessionTitle;
    private boolean cleanupEnabled;

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

    public TestSessionConfig build() {
      return new TestSessionConfig(parentId, templateId, sessionTitle, cleanupEnabled);
    }
  }
}
