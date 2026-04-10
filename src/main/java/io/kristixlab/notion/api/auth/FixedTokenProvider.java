package io.kristixlab.notion.api.auth;

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
