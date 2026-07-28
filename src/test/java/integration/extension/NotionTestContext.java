package integration.extension;

import integration.helper.NotionTestClientProvider;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.extension.ExtensionContext;

public class NotionTestContext implements ExtensionContext.Store.CloseableResource {

  private static final AtomicReference<NotionTestContext> INSTANCE = new AtomicReference<>();

  private final String rootTestPageId;
  private final String testBotUserId;
  private final Map<String, String> preAddedPages;

  public NotionTestContext(String rootTestPageId, Map<String, String> preAddedPages, String testBotUserId) {
    this.rootTestPageId = rootTestPageId;
    this.testBotUserId = testBotUserId;
    if (preAddedPages != null) {
      this.preAddedPages = Map.copyOf(preAddedPages);
    } else {
      this.preAddedPages = Map.of();
    }
  }

  public static void initialize(String pageId, Map<String, String> preAddedPages, String testBotUserId) {
    if (!INSTANCE.compareAndSet(null, new NotionTestContext(pageId, preAddedPages, testBotUserId))) {
      throw new IllegalStateException("Notion context was already initialized!");
    }
  }

  public static NotionTestContext getInstance() {
    NotionTestContext context = INSTANCE.get();
    if (context == null) {
      throw new IllegalStateException("Notion context has not been initialized yet!");
    }
    return context;
  }

  public String getRootTestPageId() {
    return rootTestPageId;
  }

  public Map<String, String> getPrefilledPages() {
    return preAddedPages;
  }

  public String getTestBotUserId() {
    return testBotUserId;
  }

  @Override
  public void close() throws Throwable {
    // TODO make it configurable
    if (false) {
      NotionTestClientProvider.getInfraSetupClient().pages().moveToTrash(rootTestPageId);
    }
  }
}
