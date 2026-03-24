package io.kristixlab.notion.api.http.log;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExchangeContext {
  private static ThreadLocal<ExchangeContext> contexts = new ThreadLocal<>();

  private Map<String, Object> contextMap = new HashMap<>();
  private String requestBody;
  private String responseBody;

  public static ExchangeContext getCurrent() {
    if (contexts.get() == null) {
      contexts.set(new ExchangeContext());
    }
    return contexts.get();
  }

  public static void clearContext() {
    getCurrent().clear();
  }

  public Object get(String key) {
    return contextMap.get(key);
  }

  public Object getOrDefault(String key, Object defaultValue) {
    return contextMap.getOrDefault(key, defaultValue);
  }

  public String getString(String key) {
    return (String) contextMap.get(key);
  }

  public void put(String key, Object value) {
    contextMap.put(key, value);
  }

  public Map<String, Object> getContextMap() {
    return contextMap;
  }

  public Object computeIfAbsent(String key, Function<String, Object> mappingFunction) {
    if (contextMap.containsKey(key)) {
      return contextMap.get(key);
    }
    Object value = mappingFunction.apply(key);
    contextMap.put(key, value);
    return value;
  }

  public String computeStringIfAbsent(String key, Function<String, String> mappingFunction) {
    if (contextMap.containsKey(key)) {
      return getString(key);
    }
    String value = mappingFunction.apply(key);
    contextMap.put(key, value);
    return value;
  }

  public void setContextMap(Map<String, Object> metadata) {
    this.contextMap = metadata;
  }

  public void clear(String key) {
    contextMap.remove(key);
  }

  public void clear() {
    contextMap.clear();
  }
}
