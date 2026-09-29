package testkit.ext;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/** Injects the run-scoped session user id. The value is a singleton on {@link TestSession}. */
public class SessionUserIdProvisioner implements ParameterResolver {

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.isAnnotated(SessionUserId.class)
        && parameterContext.getParameter().getType() == String.class;
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context)
      throws ParameterResolutionException {
    return TestSession.get(context).ensureSessionUserId();
  }
}
