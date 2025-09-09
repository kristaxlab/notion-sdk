package io.kristixlab.notion.api.model.authorization;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import org.junit.jupiter.api.Test;

public class AuthorizationDeserializationTest extends BaseTest {

  @Test
  void testTokenResponse() throws Exception {
    TokenResponse tokenResponse =
        loadFromFile("authorization/authorization-token-rs.json", TokenResponse.class);

    // Validate basic token fields
    assertNotNull(tokenResponse);
    assertNotNull(tokenResponse.getAccessToken());
    assertEquals("bearer", tokenResponse.getTokenType());
    assertNotNull(tokenResponse.getRefreshToken());
    assertNotNull(tokenResponse.getBotId());

    // Validate workspace information
    assertNotNull(tokenResponse.getWorkspaceName());
    assertNotNull(tokenResponse.getWorkspaceId());
    assertNotNull(tokenResponse.getWorkspaceIcon());

    // Validate request metadata
    assertNotNull(tokenResponse.getRequestId());
    assertNotNull(tokenResponse.getDuplicatedTemplateId());

    // Validate owner structure
    assertNotNull(tokenResponse.getOwner());
    assertEquals("user", tokenResponse.getOwner().getType());

    // Validate nested user object in owner
    assertNotNull(tokenResponse.getOwner().getUser());
    assertEquals("user", tokenResponse.getOwner().getUser().getObject());
    assertNotNull(tokenResponse.getOwner().getUser().getId());
    assertNotNull(tokenResponse.getOwner().getUser().getName());
    assertNull(tokenResponse.getOwner().getUser().getAvatarUrl());
    assertEquals("person", tokenResponse.getOwner().getUser().getType());

    // Validate person information in nested user
    assertNotNull(tokenResponse.getOwner().getUser().getPerson());
    assertEquals("test@example.com", tokenResponse.getOwner().getUser().getPerson().getEmail());

    // Bot should be null for person type user
    assertNull(tokenResponse.getOwner().getUser().getBot());
  }

  @Test
  void testIntrospectTokenResponse() throws Exception {
    IntrospectTokenResponse introspectResponse =
        loadFromFile(
            "authorization/authorization-introspect-token-rs.json", IntrospectTokenResponse.class);

    // Validate token activity and basic fields
    assertNotNull(introspectResponse);
    assertTrue(introspectResponse.getActive());
    assertNotNull(introspectResponse.getScope());
    assertNotNull(introspectResponse.getIat());
    assertNotNull(introspectResponse.getRequestId());
  }

  @Test
  void testRevokeTokenResponse() throws Exception {
    RevokeTokenResponse revokeResponse =
        loadFromFile("authorization/authorization-revoke-token-rs.json", RevokeTokenResponse.class);

    // Validate revocation response
    assertNotNull(revokeResponse);
    assertTrue(revokeResponse.getRevoked());
    assertEquals("Token successfully revoked", revokeResponse.getMessage());
    assertEquals("revoke-req-abcd-1234-5678-9876543210ab", revokeResponse.getRequestId());
  }
}
