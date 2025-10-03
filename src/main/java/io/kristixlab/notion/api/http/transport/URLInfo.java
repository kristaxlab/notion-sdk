package io.kristixlab.notion.api.http.transport;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class URLInfo {

  private String url;
  private Map<String, List<String>> queryParams = new HashMap<>();
  private Map<String, String> pathParams = new HashMap<>();

  public static URLInfoBuilder builder() {
    return new URLInfoBuilder();
  }

  public static URLInfoBuilder builder(String url) {
    return new URLInfoBuilder().url(url);
  }

  public static URLInfo build(String url) {
    URLInfo urlInfo = new URLInfo();
    urlInfo.setUrl(url);
    return urlInfo;
  }
}
