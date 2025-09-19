package io.kristixlab.notion.api.exchange.transport;

import java.util.Map;

public interface Transport {

  <T> T call(
      String method,
      String path,
      Map<String, String> queryParams,
      Map<String, String> pathParams,
      Map<String, String> headerParams,
      Class<T> responseType);

  <T> T call(
          String method, URLInfo urlInfo, Map<String, String> headerParams, Class<T> responseType);

  <T> T call(String method, URLInfo urlInfo, Class<T> responseType);

  String getBaseUrl();

  String getApiName();
}
