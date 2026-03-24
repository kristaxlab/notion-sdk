package io.kristixlab.notion.api.http;

import io.kristixlab.notion.api.http.legacy.HttpTransport;
import io.kristixlab.notion.api.http.request.ApiPath;
import io.kristixlab.notion.api.http.legacy.ApiResponse;
import java.util.Map;

/**
 * A test double for {@link HttpTransport} that records the last invocation and returns a
 * configurable fixed response. Intended to be shared across endpoint unit tests.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * TransportStub transport = new TransportStub();
 * transport.setResponse(someObject);
 * SomeEndpointImpl endpoint = new SomeEndpointImpl(transport);
 *
 * // after the call:
 * assertEquals("GET", transport.lastMethod);
 * assertEquals("/some/path", transport.lastUrlInfo.getUrl());
 * }</pre>
 */
public class TransportStub implements HttpTransport {

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
  public <T> T call(String method, ApiPath urlInfo, Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = urlInfo;
    this.lastBody = null;
    return (T) response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T call(String method, ApiPath urlInfo, Object body, Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = urlInfo;
    this.lastBody = body;
    return (T) response;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T call(
      String method,
      ApiPath urlInfo,
      Map<String, String> headerParams,
      Object body,
      Class<T> responseType) {
    this.lastMethod = method;
    this.lastUrlInfo = urlInfo;
    this.lastBody = body;
    return (T) response;
  }

  @Override
  public <T> ApiResponse<T> execute(
      String method,
      ApiPath urlInfo,
      Map<String, String> headerParams,
      Object body,
      Class<T> responseType) {
    throw new UnsupportedOperationException(
        "execute() is not stubbed — use call() variants instead");
  }

  @Override
  public String getBaseUrl() {
    return "";
  }

  @Override
  public String getApiName() {
    return "stub";
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
