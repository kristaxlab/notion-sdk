package io.kristixlab.notion.api.model.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response object for OAuth token exchange containing access token and workspace information.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TokenResponse extends BaseNotionResponse {

  /**
   * The access token that can be used to make authenticated requests.
   */
  @JsonProperty("access_token")
  private String accessToken;

  /**
   * The type of token, typically "bearer".
   */
  @JsonProperty("token_type")
  private String tokenType;

  /**
   * The refresh token that can be used to obtain new access tokens.
   */
  @JsonProperty("refresh_token")
  private String refreshToken;

  /**
   * Information about the bot user created for this integration.
   */
  @JsonProperty("bot_id")
  private String botId;

  /**
   * The workspace that granted access to the integration.
   */
  @JsonProperty("workspace_name")
  private String workspaceName;

  /**
   * The ID of the workspace.
   */
  @JsonProperty("workspace_id")
  private String workspaceId;

  /**
   * The workspace icon URL.
   */
  @JsonProperty("workspace_icon")
  private String workspaceIcon;

  /**
   * The owner of the workspace who authorized the integration.
   */
  @JsonProperty("owner")
  private Owner owner;

  /**
   * The ID of a duplicated template, if applicable.
   */
  @JsonProperty("duplicated_template_id")
  private String duplicatedTemplateId;
}
