package testkit.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} parameter that receives the test page for this test. Depends on the test
 * session page.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(NotionPageIdProvisioner.class)
public @interface NotionPageId {}
