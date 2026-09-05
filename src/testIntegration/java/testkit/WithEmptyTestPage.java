package testkit;

import org.junit.jupiter.api.BeforeEach;
import testkit.ext.TestPageId;

public abstract class WithEmptyTestPage extends BaseIntegrationTest {

  private String testPageId;

  @BeforeEach
  protected void beforeEach(@TestPageId String testPageId) {
    setTestPageId(testPageId);
  }

  protected String getTestPageId() {
    return testPageId;
  }

  protected void setTestPageId(String testPageId) {
    this.testPageId = testPageId;
  }
}
