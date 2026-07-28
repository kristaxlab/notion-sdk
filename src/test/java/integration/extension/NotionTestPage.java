package integration.extension;

import java.lang.annotation.*;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} field that receives the ID of a Notion page created for the test class.
 *
 * <p>{@link NotionTestPageExtension} creates the page under the page of the current test session
 * before any test of the class runs, so each test class works in its own subtree of the workspace:
 *
 * <pre>{@code
 * public class Pages_IT1_BasicCRUD extends BaseIntegrationTest {
 *
 *   @NotionTestPage
 *   private static String testPageId;
 * }
 * }</pre>
 *
 * <p>Static fields are populated before {@code @BeforeAll} methods, instance fields right after the
 * test instance is created.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@ExtendWith(NotionTestPageExtension.class)
public @interface NotionTestPage {

  /**
   * Title of the page to create. Defaults to the display name of the test class.
   *
   * @return the page title
   */
  String value() default "";
}
