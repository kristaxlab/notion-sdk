package integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class NotionTestClientProviderApiTest {

  @Test
  void exposesReadableClientFactoryNamesInIntegrationPackage() throws Exception {
    assertFactoryMethodsPresent(NotionTestClientProvider.class);
  }

  @Test
  void exposesReadableClientFactoryNamesInHelperPackage() throws Exception {
    assertFactoryMethodsPresent(Class.forName("integration.helper.NotionTestClientProvider"));
  }

  private static void assertFactoryMethodsPresent(Class<?> providerClass) throws Exception {
    Method setupClientFactory = providerClass.getMethod("prerequisiteSetupClient");
    Method testRunClientFactory = providerClass.getMethod("testRunClient", Path.class);

    assertTrue(Modifier.isPublic(setupClientFactory.getModifiers()));
    assertTrue(Modifier.isStatic(setupClientFactory.getModifiers()));
    assertTrue(Modifier.isPublic(testRunClientFactory.getModifiers()));
    assertTrue(Modifier.isStatic(testRunClientFactory.getModifiers()));
  }
}
