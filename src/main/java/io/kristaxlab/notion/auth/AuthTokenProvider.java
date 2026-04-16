package io.kristaxlab.notion.auth;

/** Supplies the bearer token used to authenticate Notion API requests. */
public interface AuthTokenProvider {

  /**
   * Retrieves the current access token.
   *
   * @return the current access token, or null if not available
   */
  String getAuthToken();
}
