package io.kristixlab.notion.api.http.transport.rq;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FileRequest {

  private String contentType;
  private byte[] fileContent;
  private String fileName;
  private Map<String, String> additionalInfo = new HashMap<>();
}
