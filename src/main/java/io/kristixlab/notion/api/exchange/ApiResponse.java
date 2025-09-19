package io.kristixlab.notion.api.exchange;

import java.util.Map;

public class ApiResponse<T> {

  private int code;

  private Map<String, String> headers;

  private T body;
}
