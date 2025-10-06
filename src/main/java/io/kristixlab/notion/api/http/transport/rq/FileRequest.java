package io.kristixlab.notion.api.http.transport.rq;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class FileRequest {

  private String contentType;
  private byte[] fileContent;
  private String fileName;
  private Map<String, String> additionalInfo = new HashMap<>();
}
