package testkit.ext.client;

import io.kristaxlab.notion.NotionClient;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@link NotionClient} parameter that receives a client for the current test. Use {@link
 * #forSetup()} {@code true} for arrange-only calls.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(NotionTestClientProvisioner.class)
public @interface NotionTestClient {

  boolean forSetup() default false;
}
