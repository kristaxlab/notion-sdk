package io.kristixlab.notion.api.http.request;

import java.util.*;
import lombok.Data;

@Data
public class ApiPath {

  private String url;
  private Map<String, List<String>> queryParams = new HashMap<>();
  private Map<String, String> pathParams = new HashMap<>();

  public static Builder builder() {
    return new Builder();
  }

  public static ApiPath.Builder builder(String url) {
    return new ApiPath.Builder().url(url);
  }

  public static ApiPath.Builder builder(String url, String startCursor, Integer pageSize) {
    ApiPath.Builder urlInfo = new ApiPath.Builder().url(url);
    if (startCursor != null) {
      urlInfo.queryParam("start_cursor", startCursor);
    }
    if (pageSize != null) {
      urlInfo.queryParam("page_size", pageSize);
    }
    return urlInfo;
  }

  public static ApiPath from(String url) {
    return new ApiPath.Builder().url(url).build();
  }

  public static class Builder {

    private final ApiPath apiPath = new ApiPath();

    public ApiPath build() {
      return apiPath;
    }

    public Builder url(String url) {
      apiPath.setUrl(url);
      return this;
    }

    public Builder pathParam(String key, String value) {
      apiPath.getPathParams().put(key, value);
      return this;
    }

    public Builder pathParams(Map<String, String> pathParams) {
      apiPath.getPathParams().putAll(pathParams);
      return this;
    }

    public Builder queryParam(String key, List<String> values) {
      if (apiPath.getQueryParams().containsKey(key)) {
        apiPath.getQueryParams().get(key).addAll(values);
      } else {
        apiPath.getQueryParams().put(key, new ArrayList<>(values));
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
      queryParams.forEach((key, value) -> apiPath.getQueryParams().put(key, List.of(value)));
      return this;
    }

    public Builder queryParamsArrays(Map<String, String[]> queryParams) {
      queryParams.forEach((key, values) -> apiPath.getQueryParams().put(key, toList(values)));
      return this;
    }

    public Builder queryParamsLists(Map<String, List<String>> queryParams) {
      apiPath.getQueryParams().putAll(queryParams);
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
