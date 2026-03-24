package io.kristixlab.notion.api.http.config;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// TODO make config extendable for other api clients
public class ApiClientConfig {

  private boolean jsonFailOnUnknownProperties;
}
