package testkit.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} parameter that receives the prefilled fixture page named after the test id
 * ({@code IT-8}). Fixture pages are discovered once per run on the test session page.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(FixturePageIdProvisioner.class)
public @interface FixtureNotionPageId {}
