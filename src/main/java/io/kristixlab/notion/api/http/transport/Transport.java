package io.kristixlab.notion.api.http.transport;

import io.kristixlab.notion.api.http.ApiResponse;

import java.util.Map;

public interface Transport {

  <T> ApiResponse<T> execute(String method, URLInfo urlInfo, Map<String, String> headerParams, Object body, Class<T> responseType);

  <T> T call(String method, URLInfo urlInfo, Map<String, String> headerParams, Object body, Class<T> responseType);

  <T> T call(String method, URLInfo urlInfo, Class<T> responseType);

  <T> T call(String method, URLInfo urlInfo, Object body, Class<T> responseType);

  String getBaseUrl();

  String getApiName();
}
