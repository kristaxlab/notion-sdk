package io.kristaxlab.notion.auth;

public class FixedTokenProvider implements AuthTokenProvider {

  private String authToken;

  public FixedTokenProvider(String authToken) {
    this.authToken = authToken;
  }

  @Override
  public String getAuthToken() {
    return authToken;
  }
}
