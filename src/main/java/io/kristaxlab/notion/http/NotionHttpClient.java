package io.kristaxlab.notion.http;

import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.http.error.NotionApiException;
import java.util.Map;

/**
 * Typed HTTP facade for the Notion API: same contract as {@link ApiClient}, but methods declare
 * {@link NotionApiException} for API failures. Prefer {@link io.kristaxlab.notion.NotionClient} for
 * typical use; use this when you need direct {@code call(...)} access with the configured pipeline.
 */
public interface NotionHttpClient extends ApiClient {

  @Override
  <T> T call(String method, ApiPath apiPath, Class<T> responseType) throws NotionApiException;

  @Override
  <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType)
      throws NotionApiException;

  @Override
  <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType)
      throws NotionApiException;
}
