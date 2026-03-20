package integration.util;

import io.kristixlab.notion.api.http.transport.log.ExchangeContext;
import io.kristixlab.notion.api.http.transport.log.SequentialExchangeLogger;
import java.nio.file.Path;

public class TestExchangeLogger extends SequentialExchangeLogger {

  public TestExchangeLogger(String baseDir, boolean bodyOnly) {
    super(baseDir, bodyOnly);
  }

  @Override
  public void logRequest(ExchangeContext context) {
    if (context.get("testClass") == null || context.get("testMethod") == null) {
      return;
    }
    super.logRequest(context);
  }

  @Override
  public void logResponse(ExchangeContext context) {
    if (context.get("testClass") == null || context.get("testMethod") == null) {
      return;
    }
    super.logResponse(context);
  }

  @Override
  protected Path getBaseDir() {
    String testClass = ExchangeContext.getCurrent().getString("testClass");
    String testMethod = ExchangeContext.getCurrent().getString("testMethod");
    if (testClass != null && testMethod != null) {
      Path baseDir = super.getBaseDir().resolve(testClass).resolve(testMethod);
      if (ExchangeContext.getCurrent().get("testLogsPath") == null) {
        ExchangeContext.getCurrent().put("testLogsPath", baseDir);
      }
      return super.getBaseDir().resolve(testClass).resolve(testMethod);
    }
    return super.getBaseDir();
  }

  @Override
  protected String generateFileName(ExchangeContext context, String postfix) {
    // TODO adopt
    return super.generateFileName(context, postfix);
  }
}
