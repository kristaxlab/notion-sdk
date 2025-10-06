package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for token introspection containing information about token validity and
 * properties.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IntrospectTokenResponse extends BaseNotionObject {

  /** Whether the token is active (valid and not expired). */
  @JsonProperty("active")
  private Boolean active;

  /** The scope associated with the token - space-separated list of permissions. */
  @JsonProperty("scope")
  private String scope;

  /** Issued at time of the token (Unix timestamp in milliseconds). */
  @JsonProperty("iat")
  private Long iat;
}
