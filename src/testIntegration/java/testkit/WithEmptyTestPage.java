package testkit;

import org.junit.jupiter.api.BeforeEach;
import testkit.ext.NotionPageId;

/** Injects a test page under the test session page via {@link NotionPageId}. */
public abstract class WithEmptyTestPage extends BaseIntegrationTest {

  private String testPageId;

  @BeforeEach
  protected void beforeEach(@NotionPageId String testPageId) {
    setTestPageId(testPageId);
  }

  protected String getTestPageId() {
    return testPageId;
  }

  protected void setTestPageId(String testPageId) {
    this.testPageId = testPageId;
  }
}
