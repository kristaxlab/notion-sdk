package io.kristixlab.notion.api.http.transport.rs;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ApiResponse<T> {

  private int status;

  private String errorCode;

  private Map<String, String> headers = new HashMap<>();

  private T body;
}
