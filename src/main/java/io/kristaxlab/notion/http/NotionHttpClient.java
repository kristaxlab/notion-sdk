package io.kristaxlab.notion.http;

import io.kristaxlab.notion.http.base.client.ApiClient;
import io.kristaxlab.notion.http.base.request.ApiPath;
import io.kristaxlab.notion.http.error.NotionApiException;
import java.util.Map;

/**
 * Notion-specific API client.
 *
 * <p>This interface adds NotionApiException to the method signatures.
 *
 * <p>This interface also serves to library readability improvement: advanced users who need direct
 * control over Notion API might choose to interacto with this interface instead of NotionClient. So
 * having Notion in its name will reduce the confusion and make a clear understanding what this
 * interface is for.
 */
public interface NotionHttpClient extends ApiClient {

  /** Adds NotionApiException to the method signature */
  <T> T call(String method, ApiPath apiPath, Class<T> responseType) throws NotionApiException;

  /** Adds NotionApiException to the method signature */
  <T> T call(String method, ApiPath apiPath, Object body, Class<T> responseType)
      throws NotionApiException;

  <T> T call(
      String method,
      ApiPath apiPath,
      Map<String, String> extraHeaders,
      Object body,
      Class<T> responseType)
      throws NotionApiException;
}
