package io.kristixlab.notion.api.exchange;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse<T> {

  private int status;

  private String errorCode;

  private Map<String, String> headers = new HashMap<>();

  private T body;
}
