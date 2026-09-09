package testkit.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Marks a {@code String} parameter that receives the session user id of the integration token.
 * Resolved once per run via {@link SessionUserIdProvisioner}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(SessionUserIdProvisioner.class)
public @interface SessionUserId {}
