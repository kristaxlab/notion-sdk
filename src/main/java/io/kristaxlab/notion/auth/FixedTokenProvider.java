package io.kristaxlab.notion.auth;

/** Token provider that always returns the same preconfigured token. */
public class FixedTokenProvider implements AuthTokenProvider {

  private String authToken;

  /**
   * Creates a fixed token provider.
   *
   * @param authToken token to return from {@link #getAuthToken()}
   */
  public FixedTokenProvider(String authToken) {
    this.authToken = authToken;
  }

  @Override
  public String getAuthToken() {
    return authToken;
  }
}
