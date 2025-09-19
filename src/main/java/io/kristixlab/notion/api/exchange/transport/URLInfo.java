package io.kristixlab.notion.api.exchange.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class URLInfo {

  private String url;
  private Map<String, List<String>> queryParams = new HashMap<>();
  private Map<String, String> pathParams = new HashMap<>();

  public static URLInfoBuilder builder() {
    return new URLInfoBuilder();
  }
}
