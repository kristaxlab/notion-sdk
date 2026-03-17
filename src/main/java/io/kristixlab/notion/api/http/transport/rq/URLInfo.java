package io.kristixlab.notion.api.http.transport.rq;

import java.util.*;
import lombok.Data;

@Data
public class URLInfo {

  private String url;
  private Map<String, List<String>> queryParams = new HashMap<>();
  private Map<String, String> pathParams = new HashMap<>();

  public static Builder builder() {
    return new Builder();
  }

  public static URLInfo.Builder builder(String url) {
    return new URLInfo.Builder().url(url);
  }

  public static URLInfo.Builder builder(String url, String startCursor, Integer pageSize) {
    URLInfo.Builder urlInfo = new URLInfo.Builder().url(url);
    if (startCursor != null) {
      urlInfo.queryParam("start_cursor", startCursor);
    }
    if (pageSize != null) {
      urlInfo.queryParam("page_size", pageSize);
    }
    return urlInfo;
  }

  public static URLInfo from(String url) {
    return new URLInfo.Builder().url(url).build();
  }

  public static class Builder {

    private final URLInfo urlInfo = new URLInfo();

    public URLInfo build() {
      return urlInfo;
    }

    public Builder url(String url) {
      urlInfo.setUrl(url);
      return this;
    }

    public Builder pathParam(String key, String value) {
      urlInfo.getPathParams().put(key, value);
      return this;
    }

    public Builder pathParams(Map<String, String> pathParams) {
      urlInfo.getPathParams().putAll(pathParams);
      return this;
    }

    public Builder queryParam(String key, List<String> values) {
      if (urlInfo.getQueryParams().containsKey(key)) {
        urlInfo.getQueryParams().get(key).addAll(values);
      } else {
        urlInfo.getQueryParams().put(key, new ArrayList<>(values));
      }
      return this;
    }

    public Builder queryParam(String key, String value) {
      queryParam(key, toList(value));
      return this;
    }

    public Builder queryParam(String key, Object value) {
      queryParam(key, toList(String.valueOf(value)));
      return this;
    }

    public Builder queryParams(Map<String, String> queryParams) {
      queryParams.forEach((key, value) -> urlInfo.getQueryParams().put(key, List.of(value)));
      return this;
    }

    public Builder queryParamsArrays(Map<String, String[]> queryParams) {
      queryParams.forEach((key, values) -> urlInfo.getQueryParams().put(key, toList(values)));
      return this;
    }

    public Builder queryParamsLists(Map<String, List<String>> queryParams) {
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
}
