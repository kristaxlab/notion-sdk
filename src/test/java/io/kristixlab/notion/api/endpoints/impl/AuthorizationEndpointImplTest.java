package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.NotionAuthSettings;
import io.kristixlab.notion.api.http.ApiClientStub;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenRequest;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenResponse;
import io.kristixlab.notion.api.model.authorization.RefreshTokenRequest;
import io.kristixlab.notion.api.model.authorization.RevokeTokenRequest;
import io.kristixlab.notion.api.model.authorization.RevokeTokenResponse;
import io.kristixlab.notion.api.model.authorization.TokenRequest;
import io.kristixlab.notion.api.model.authorization.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AuthorizationEndpointImplTest {

  private ApiClientStub client;
  private NotionAuthSettings authSettings;
  private AuthorizationEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    client.setResponse(new TokenResponse());
    authSettings = new NotionAuthSettings();
    endpoint = new AuthorizationEndpointImpl(authSettings, client);
  }

  // ── createToken ───────────────────────────────────────────────────────────

  @Test
  void createToken() {
    endpoint.createToken("auth-code");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertEquals("auth-code", ((TokenRequest) client.getLastBody()).getCode());
  }

  @Test
  void createToken_withRedirectUri() {
    endpoint.createToken("auth-code", "https://example.com/callback");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    TokenRequest body = (TokenRequest) client.getLastBody();
    assertEquals("auth-code", body.getCode());
    assertEquals("https://example.com/callback", body.getRedirectUri());
  }

  @Test
  void createToken_withRequest() {
    TokenRequest request = TokenRequest.of("auth-code", null);

    endpoint.createToken(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void createToken_withClientCredentials() {
    TokenRequest request = TokenRequest.of("auth-code", null);

    endpoint.createToken(request, "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void createToken_rejectsNullRequest() {
    assertThrows(IllegalArgumentException.class, () -> endpoint.createToken((TokenRequest) null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void createToken_rejectsBlankOrNullCode(String code) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.createToken(code));
  }

  // ── refreshToken ──────────────────────────────────────────────────────────

  @Test
  void refreshToken() {
    authSettings.setRefreshToken("stored-refresh-token");

    endpoint.refreshToken();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertEquals(
        "stored-refresh-token", ((RefreshTokenRequest) client.getLastBody()).getRefreshToken());
  }

  @Test
  void refreshToken_withToken() {
    endpoint.refreshToken("my-refresh-token");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertEquals(
        "my-refresh-token", ((RefreshTokenRequest) client.getLastBody()).getRefreshToken());
  }

  @Test
  void refreshToken_withRequest() {
    RefreshTokenRequest request = RefreshTokenRequest.create("my-refresh-token");

    endpoint.refreshToken(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void refreshToken_withClientCredentials() {
    endpoint.refreshToken("my-refresh-token", "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertEquals(
        "my-refresh-token", ((RefreshTokenRequest) client.getLastBody()).getRefreshToken());
  }

  @Test
  void refreshToken_withRequestAndClientCredentials() {
    RefreshTokenRequest request = RefreshTokenRequest.create("my-refresh-token");

    endpoint.refreshToken(request, "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/token", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void refreshToken_rejectsNullRequest() {
    assertThrows(
        IllegalArgumentException.class, () -> endpoint.refreshToken((RefreshTokenRequest) null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void refreshToken_rejectsBlankOrNullToken(String token) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.refreshToken(token));
  }

  // ── introspectToken ───────────────────────────────────────────────────────

  @Test
  void introspectToken() {
    client.setResponse(new IntrospectTokenResponse());

    endpoint.introspectToken("some-token");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertEquals("some-token", ((IntrospectTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void introspectToken_withRequest() {
    client.setResponse(new IntrospectTokenResponse());
    IntrospectTokenRequest request = IntrospectTokenRequest.of("some-token");

    endpoint.introspectToken(request);

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void introspectToken_withClientCredentials() {
    client.setResponse(new IntrospectTokenResponse());

    endpoint.introspectToken("some-token", "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertEquals("some-token", ((IntrospectTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void introspectToken_withRequestAndClientCredentials() {
    client.setResponse(new IntrospectTokenResponse());
    IntrospectTokenRequest request = IntrospectTokenRequest.of("some-token");

    endpoint.introspectToken(request, "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertSame(request, client.getLastBody());
  }

  @Test
  void introspectAccessToken() {
    client.setResponse(new IntrospectTokenResponse());
    authSettings.setAccessToken("stored-access-token");

    endpoint.introspectAccessToken();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertEquals("stored-access-token", ((IntrospectTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void introspectRefreshToken() {
    client.setResponse(new IntrospectTokenResponse());
    authSettings.setRefreshToken("stored-refresh-token");

    endpoint.introspectRefreshToken();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/introspect", client.getLastUrlInfo().getUrl());
    assertEquals(
        "stored-refresh-token", ((IntrospectTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void introspectToken_rejectsNullRequest() {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.introspectToken((IntrospectTokenRequest) null));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void introspectToken_rejectsBlankOrNullToken(String token) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.introspectToken(token));
  }

  // ── revokeToken ───────────────────────────────────────────────────────────

  @Test
  void revokeToken() {
    client.setResponse(new RevokeTokenResponse());

    endpoint.revokeToken("some-token");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/revoke", client.getLastUrlInfo().getUrl());
    assertEquals("some-token", ((RevokeTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void revokeToken_fromAuthSettings() {
    client.setResponse(new RevokeTokenResponse());
    authSettings.setAccessToken("stored-access-token");

    endpoint.revokeToken();

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/revoke", client.getLastUrlInfo().getUrl());
    assertEquals("stored-access-token", ((RevokeTokenRequest) client.getLastBody()).getToken());
  }

  @Test
  void revokeToken_withClientCredentials() {
    client.setResponse(new RevokeTokenResponse());

    endpoint.revokeToken("some-token", "client-id", "client-secret");

    assertEquals("POST", client.getLastMethod());
    assertEquals("/oauth/revoke", client.getLastUrlInfo().getUrl());
    assertEquals("some-token", ((RevokeTokenRequest) client.getLastBody()).getToken());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void revokeToken_rejectsBlankOrNullToken(String token) {
    assertThrows(IllegalArgumentException.class, () -> endpoint.revokeToken(token));
  }
}
