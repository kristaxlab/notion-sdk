package testkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import testkit.ext.TestPage;

@Tag("fixture")
public abstract class WithTestPageFixture extends BaseIntegrationTest {

  private String testPageId;

  @BeforeEach
  protected void beforeEach(@TestPage(fixture = true) String testPageId) {
    setTestPageId(testPageId);
  }

  protected String getTestPageId() {
    return testPageId;
  }

  protected void setTestPageId(String testPageId) {
    this.testPageId = testPageId;
  }
}
