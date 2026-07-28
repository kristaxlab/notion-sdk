package integration.extension;

import integration.helper.NotionTestClientProvider;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class NotionTestContext implements ExtensionContext.Store.CloseableResource {

  private static final AtomicReference<NotionTestContext> INSTANCE = new AtomicReference<>();

  private final String rootTestPageId;
  private final Map<String, String> preAddedPages;

  public NotionTestContext(String rootTestPageId, Map<String, String> preAddedPages) {
    this.rootTestPageId = rootTestPageId;
    if (preAddedPages != null) {
      this.preAddedPages = Map.copyOf(preAddedPages);
    } else {
      this.preAddedPages = Map.of();
    }
  }

  public static void initialize(String pageId, Map<String, String> preAddedPages) {
    if (!INSTANCE.compareAndSet(null, new NotionTestContext(pageId, preAddedPages))) {
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

  @Override
  public void close() throws Throwable {
    // TODO make it configurable
    if (false) {
      NotionTestClientProvider.getInfraSetupClient().pages().moveToTrash(rootTestPageId);
    }
  }
}
