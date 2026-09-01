package testkit.ext;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestBeforeEach implements BeforeEachCallback {
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {}
}
