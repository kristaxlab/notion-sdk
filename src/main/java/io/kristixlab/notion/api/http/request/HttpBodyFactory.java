package io.kristixlab.notion.api.http.request;

import io.kristixlab.notion.api.http.client.HttpClient.Body;
import io.kristixlab.notion.api.http.client.HttpClient.StringBody;
import io.kristixlab.notion.api.json.JsonSerializer;

/**
 * Creates library-agnostic {@link Body} instances from SDK request objects.
 *
 * <p>Two cases:
 *
 * <ul>
 *   <li>If {@code body} is already a {@link Body} (e.g. {@link
 *       io.kristixlab.notion.api.http.client.HttpClient.MultipartBody}) it is returned as-is.
 *   <li>Any other non-{@code null} value is serialized to JSON via a {@link JsonSerializer} and
 *       wrapped in a {@link StringBody} with content-type {@code application/json}.
 * </ul>
 *
 * <p>A {@code null} body produces {@code null} (used by GET / DELETE without a body).
 */
public class HttpBodyFactory {

  private static final String APPLICATION_JSON = "application/json";

  private HttpBodyFactory() {}

  /**
   * Creates a {@link Body} for the given payload.
   *
   * @param body {@code null}, a {@link Body} subtype, or any JSON-serializable object
   * @param serializer the serializer to use for JSON conversion
   * @return a {@link Body}, or {@code null} when {@code body} is {@code null}
   */
  public static Body create(Object body, JsonSerializer serializer) {
    if (body == null) {
      return null;
    }
    if (body instanceof Body alreadyBody) {
      return alreadyBody;
    }
    String serialized = serializer.toJson(body);
    return new StringBody(serialized, APPLICATION_JSON);
  }

  /**
   * Returns {@code true} when the body will be JSON-serialized and the caller should set a {@code
   * Content-Type: application/json} request header.
   */
  public static boolean requiresJsonContentType(Object body) {
    return body != null && !(body instanceof Body);
  }
}
