package io.kristixlab.notion.api.http.transport.util;

import io.kristixlab.notion.api.http.transport.rq.URLInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class URLInfoBuilder {

  private final URLInfo urlInfo = new URLInfo();

  public URLInfo build() {
    return urlInfo;
  }

  public URLInfoBuilder url(String url) {
    urlInfo.setUrl(url);
    return this;
  }

  public URLInfoBuilder pathParam(String key, String value) {
    urlInfo.getPathParams().put(key, value);
    return this;
  }

  public URLInfoBuilder pathParams(Map<String, String> pathParams) {
    urlInfo.getPathParams().putAll(pathParams);
    return this;
  }

  public URLInfoBuilder queryParam(String key, List<String> values) {
    if (urlInfo.getQueryParams().containsKey(key)) {
      urlInfo.getQueryParams().get(key).addAll(values);
    } else {
      urlInfo.getQueryParams().put(key, values);
    }
    return this;
  }

  public URLInfoBuilder queryParam(String key, String value) {
    queryParam(key, toList(value));
    return this;
  }

  public URLInfoBuilder queryParam(String key, Object value) {
    queryParam(key, toList(String.valueOf(value)));
    return this;
  }

  public URLInfoBuilder queryParams(Map<String, String> queryParams) {
    queryParams.forEach((key, value) -> urlInfo.getQueryParams().put(key, List.of(value)));
    return this;
  }

  public URLInfoBuilder queryParamsArrays(Map<String, String[]> queryParams) {
    queryParams.forEach((key, values) -> urlInfo.getQueryParams().put(key, toList(values)));
    return this;
  }

  public URLInfoBuilder queryParamsLists(Map<String, List<String>> queryParams) {
    urlInfo.getQueryParams().putAll(queryParams);
    return this;
  }

  private List<String> toList(String value) {
    List<String> values = new ArrayList<>();
    values.add(value);
    return values;
  }

  private List<String> toList(String[] array) {
    List<String> values = new ArrayList<>();
    Collections.addAll(values, array);
    return values;
  }
}
