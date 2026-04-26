package io.kristaxlab.notion.http.base.client;

import io.kristaxlab.notion.http.base.request.ApiPath;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * A stub for {@link ApiClient} that records the last invocation and returns a configurable fixed
 * response. Intended to be used in endpoint unit tests.
 */
public class ApiClientStub implements ApiClient {

  @Getter private String lastMethod;

  @Getter private ApiPath lastUrlInfo;

  @Getter private Object lastBody;

  @Setter private Object response;

  @Override
  @SuppressWarnings("unchecked")
  public <T> T call(String method, ApiPath apiPath, Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = apiPath;
    this.lastBody = null;
    return (T) response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = apiPath;
    this.lastBody = body;
    return (T) response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = apiPath;
    this.lastBody = body;
    return (T) response;
  }
}
