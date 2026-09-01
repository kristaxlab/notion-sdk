package testkit.ext;

import java.lang.annotation.*;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} parameter that receives the ID of a Notion page created for the test
 * class.
 *
 * <p>{@link TestSessionBeforeAll} provisions the test session and {@link TestPagesProvisioner}
 * resolves the page for the test within it (a prefilled prerequisite page, the shared session page,
 * or a dedicated page created under the session page).
 *
 * <p>Parameters are populated right after the test instance is created.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@ExtendWith({TestSessionBeforeAll.class, TestPagesProvisioner.class, TestPageAfterEach.class})
public @interface TestPage {

  boolean fixture() default false;
}
