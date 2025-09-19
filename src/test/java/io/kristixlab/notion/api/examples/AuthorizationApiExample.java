package io.kristixlab.notion.api.examples;

import io.kristixlab.notion.api.AuthorizationApi;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenResponse;
import io.kristixlab.notion.api.model.authorization.RevokeTokenResponse;
import io.kristixlab.notion.api.model.authorization.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AuthorizationApiExample extends IntegrationTest {

  private AuthorizationApi authorizationApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    authorizationApi = new AuthorizationApi(null, getTransport());
  }

  @Test
  void exchangeAuthCode() throws IOException {
    TokenResponse rs =
            authorizationApi.exchangeCodeForToken(
                    "038cc797-c0e8-4f0b-b5b1-8a0bce18ab2d", "https://www.kkkkkkkk.com");

    saveToFile(rs, "authorization-by-code-rs.json");
  }

  @Test
  void refreshToken() throws IOException {
    TokenResponse rs =
            authorizationApi.refreshToken("nrt_496768403108R9BFLyuYSi97lAyGbu5qYvdFwCqRmYv4wC");

    saveToFile(rs, "authorization-refresh-token-rs.json");
  }

  @Test
  void introspectToken() throws IOException {
    IntrospectTokenResponse rs =
            authorizationApi.introspectToken("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");

    saveToFile(rs, "authorization-introspect-token-rs.json");
  }

  @Test
  void revokeToken() throws IOException {
    RevokeTokenResponse rs =
            authorizationApi.revokeToken("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");

    saveToFile(rs, "authorization-revoke-token-rs.json");
  }
}
