package io.kristaxlab.notion.http;

import io.kristaxlab.notion.http.base.client.ApiClientImpl;
import io.kristaxlab.notion.http.base.client.HttpClient;
import io.kristaxlab.notion.http.base.config.ApiClientConfig;
import io.kristaxlab.notion.http.base.json.JsonSerializer;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.http.error.NotionApiException;
import java.util.Map;

public class NotionHttpClientImpl extends ApiClientImpl implements NotionHttpClient {

  public NotionHttpClientImpl(
      HttpClient httpClient, ApiClientConfig config, JsonSerializer serializer) {
    super(httpClient, config, serializer);
  }

  /** Adds NotionApiException to the method signature */
  public <T> T call(String method, ApiPath apiPath, Class<T> responseType)
      throws NotionApiException {
    return super.call(method, apiPath, responseType);
  }

  /** Adds NotionApiException to the method signature */
  public <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType)
      throws NotionApiException {
    return super.call(method, apiPath, body, responseType);
  }

  /** Adds NotionApiException to the method signature */
  public <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType)
      throws NotionApiException {
    return super.call(method, apiPath, extraHeaders, body, responseType);
  }
}
