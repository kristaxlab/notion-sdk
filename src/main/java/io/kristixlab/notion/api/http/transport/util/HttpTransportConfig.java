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
}
