package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for token revocation indicating successful revocation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RevokeTokenResponse extends BaseNotionResponse {

  /**
   * Indicates whether the token was successfully revoked. This field may not be present in all
   * responses.
   */
  @JsonProperty("revoked")
  private Boolean revoked;

  /**
   * Optional message about the revocation status.
   */
  @JsonProperty("message")
  private String message;
}
