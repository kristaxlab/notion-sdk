package integration.extension;

import java.lang.annotation.*;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} parameter that receives the ID of a Notion page created for the test class.
 *
 * <p>{@link NotionTestPagesProvisioner} creates the page under the page of the current test session
 * before any test of the class runs, so each test class works in its own subtree of the workspace:
 *
 * <p>Parameters are populated right after the test instance is created.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@ExtendWith(NotionTestPagesProvisioner.class)
public @interface NotionTestPage {

}
