//package integration.extension;
//
//import integration.helper.ConfigurationLookupHelper;
//import integration.helper.NotionTestClientProvider;
//import io.kristaxlab.notion.NotionClient;
//import org.junit.platform.launcher.TestExecutionListener;
//import org.junit.platform.launcher.TestPlan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class NotionTestInfraInitializer implements TestExecutionListener {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(NotionTestInfraInitializer.class);
//  private static final String PARENT_ID = "notion.test.datasource.id";
//  private static final String TEMPLATE_ID = "notion.test.template.id";
//  private static final String PAGE_NAME = "notion.test.page.name";
//  private static final String PAGE_CLEANUP = "notion.test.page.cleanup.enabled";
//
//  @Override
//  public void testPlanExecutionStarted(TestPlan testPlan) {
//    NotionClient client = NotionTestClientProvider.getInfraSetupClient();
//
//    LOGGER.debug("Reading test properties before preparing Notion workspace for running tests");
//    String testSessionParentId =
//        ConfigurationLookupHelper.lookup(PARENT_ID, testPlan.getConfigurationParameters())
//            .orElseThrow(
//                () -> new IllegalStateException("Notion page for running tests must be provided"));
//    LOGGER.debug("{}: {}", PARENT_ID, testSessionParentId);
//
//    String templateId =
//        ConfigurationLookupHelper.lookup(TEMPLATE_ID, testPlan.getConfigurationParameters())
//            .orElse(null);
//    LOGGER.debug("{}: {}", TEMPLATE_ID, templateId);
//
//    String pageName =
//        ConfigurationLookupHelper.lookup(PAGE_NAME, testPlan.getConfigurationParameters())
//            .orElse(null);
//    LOGGER.debug("{}: {}", PAGE_NAME, pageName);
//
//    LOGGER.info(
//        "Creating Notion page for tests in {}, template={}, name={}",
//        testSessionParentId,
//        templateId,
//        pageName);
//    String testSessionPageId = null;
//    //    String testSessionPageId = client.pages()
//    //            .create(page -> page
//    //                    .title(StringUtils.isBlank(pageName) ? "Integration tests" : pageName)
//    //                    .inDataSource(testSessionParentId)
//    //                    .template(templateId == null ? null :
//    // TemplateParams.templateId(templateId)))
//    //            .getId();
//
//    NotionTestContext.initialize(testSessionPageId, null);
//  }
//
//  @Override
//  public void testPlanExecutionFinished(TestPlan testPlan) {
//    boolean cleanUp =
//        ConfigurationLookupHelper.lookupBoolean(PAGE_CLEANUP, testPlan.getConfigurationParameters())
//            .orElse(false);
//
//    if (cleanUp) {
//      LOGGER.info("Deleting Notion page for tests");
//      NotionTestClientProvider.getInfraSetupClient()
//          .pages()
//          .moveToTrash(NotionTestContext.getInstance().getRootTestPageId());
//    } else {
//      LOGGER.info("Notion page cleanup is not enabled. Use {} property to enable it", PAGE_CLEANUP);
//    }
//
//    // reads config params
//    // if applicable - deletes the testing page
//  }
//}
