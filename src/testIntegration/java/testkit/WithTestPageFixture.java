package testkit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import testkit.ext.FixtureNotionPageId;

/**
 * Injects the fixture page named after the test id via {@link FixtureNotionPageId}. Tagged {@code
 * fixture}.
 */
@Tag("fixture")
public abstract class WithTestPageFixture extends BaseIntegrationTest {

  private String testPageId;

  @BeforeEach
  protected void beforeEach(@FixtureNotionPageId String testPageId) {
    setTestPageId(testPageId);
  }

  protected String getTestPageId() {
    return testPageId;
  }

  protected void setTestPageId(String testPageId) {
    this.testPageId = testPageId;
  }
}
