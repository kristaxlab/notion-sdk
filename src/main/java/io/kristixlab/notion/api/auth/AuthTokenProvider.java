package io.kristixlab.notion.api.auth;

public interface AuthTokenProvider {

  /**
   * Retrieves the current access token.
   *
   * @return the current access token, or null if not available
   */
  String getAuthToken();
}
