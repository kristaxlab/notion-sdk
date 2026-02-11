package io.kristixlab.notion.api.http.transport.util;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpTransportConfig {

  private String apiName;
  private String baseUrl;
  private boolean jsonFailOnUnknownProperties;

  // prevent loading entire file into memory for files larger than this threshold (in bytes) if
  // presents
  private Long streamFileAfterBytes;
}
