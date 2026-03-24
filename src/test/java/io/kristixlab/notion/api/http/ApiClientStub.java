package io.kristixlab.notion.api.http;

import io.kristixlab.notion.api.http.client.ApiClient;
import io.kristixlab.notion.api.http.request.ApiPath;
import java.util.Map;

/**
 * A test double for {@link ApiClient} that records the last invocation and returns a configurable
 * fixed response. Intended to be used in endpoint unit tests.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * ApiClientStub client = new ApiClientStub();
 * client.setResponse(someObject);
 * SomeEndpointImpl endpoint = new SomeEndpointImpl(client);
 *
 * // after the call:
 * assertEquals("GET", client.getLastMethod());
 * assertEquals("/some/path", client.getLastUrlInfo().getUrl());
 * }</pre>
 */
public class ApiClientStub implements ApiClient {

  /** The HTTP method string of the most recent call (e.g. {@code "GET"}, {@code "POST"}). */
  private String lastMethod;

  /** The {@link ApiPath} passed with the most recent call. */
  private ApiPath lastUrlInfo;

  /** The request body passed with the most recent call, or {@code null} for bodyless calls. */
  private Object lastBody;

  private Object response;

  /** Sets the object that will be returned (cast to the requested type) on the next call. */
  public void setResponse(Object response) {
    this.response = response;
  }

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

  public String getLastMethod() {
    return lastMethod;
  }

  public ApiPath getLastUrlInfo() {
    return lastUrlInfo;
  }

  public Object getLastBody() {
    return lastBody;
  }
}
