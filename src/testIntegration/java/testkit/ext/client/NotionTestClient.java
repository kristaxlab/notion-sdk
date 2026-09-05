package testkit.ext.client;

import io.kristaxlab.notion.NotionClient;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@link NotionClient} parameter. Default is the Notion Test Http Client. Set {@link
 * #forSetup()} to {@code true} for the setup client.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(NotionTestClientProvisioner.class)
public @interface NotionTestClient {

  boolean forSetup() default false;
}
