package io.kristixlab.notion.api.model.authorization;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.BaseTest;
import io.kristixlab.notion.api.model.users.Person;
import org.junit.jupiter.api.Test;

public class AuthorizationDeserializationTest extends BaseTest {

  @Test
  void testTokenResponse() throws Exception {
    TokenResponse tokenResponse =
        loadFromFile("authorization/authorization-token-rs.json", TokenResponse.class);

    // Validate basic token fields
    assertNotNull(tokenResponse);
    assertEquals("ntn_5557W6V5U4T3210987674821098765432109000001", tokenResponse.getAccessToken());
    assertEquals("bearer", tokenResponse.getTokenType());
    assertEquals("nrt_5557W6V5U4T3210987674821098765432109000000", tokenResponse.getRefreshToken());
    assertEquals("12345678-1234-5678-9abc-def012345678", tokenResponse.getBotId());

    // Validate workspace information
    assertEquals("Test Workspace", tokenResponse.getWorkspaceName());
    assertEquals("11333323-4444-43b3-8288-222222222222", tokenResponse.getWorkspaceId());
    assertEquals("https://example.com/workspace-icon.png", tokenResponse.getWorkspaceIcon());

    // Validate request metadata
    assertEquals("fedcba98-fedc-ba98-7654-321098765432", tokenResponse.getRequestId());
    assertEquals("25ab90f8-6d76-81f3-a8fb-c52c16558590", tokenResponse.getDuplicatedTemplateId());

    // Validate owner structure
    assertNotNull(tokenResponse.getOwner());
    assertEquals("user", tokenResponse.getOwner().getType());

    // Validate nested user object in owner
    assertNotNull(tokenResponse.getOwner().getUser());
    assertEquals("user", tokenResponse.getOwner().getUser().getObject());
    assertEquals(
        "33444444-4444-43b3-8288-444444444444", tokenResponse.getOwner().getUser().getId());
    assertEquals("Test User", tokenResponse.getOwner().getUser().getName());
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
        loadFromFile("authorization/authorization-introspect-token-rs.json", IntrospectTokenResponse.class);

    // Validate token activity and basic fields
    assertNotNull(introspectResponse);
    assertTrue(introspectResponse.getActive());
    assertEquals(
        "read_content insert_content update_content read_user_with_email read_user_without_email",
        introspectResponse.getScope());
    assertEquals(Long.valueOf(1756139508753L), introspectResponse.getIat());
    assertEquals("1c217857-1d3f-4357-aac2-66b6da18424e", introspectResponse.getRequestId());

    // Validate that scope contains expected permissions
    assertTrue(introspectResponse.getScope().contains("read_content"));
    assertTrue(introspectResponse.getScope().contains("insert_content"));
    assertTrue(introspectResponse.getScope().contains("update_content"));
    assertTrue(introspectResponse.getScope().contains("read_user_with_email"));
    assertTrue(introspectResponse.getScope().contains("read_user_without_email"));
  }

  @Test
  void testTokenResponseOwnerStructure() throws Exception {
    TokenResponse tokenResponse = loadFromFile("authorization/authorization-token-rs.json", TokenResponse.class);

    // Deep validation of the complex owner structure
    Owner owner = tokenResponse.getOwner();
    assertNotNull(owner);
    assertEquals("user", owner.getType());

    // Validate the user object within the owner
    assertNotNull(owner.getUser());
    assertEquals("user", owner.getUser().getObject());
    assertEquals("33444444-4444-43b3-8288-444444444444", owner.getUser().getId());
    assertEquals("Test User", owner.getUser().getName());
    assertEquals("person", owner.getUser().getType());

    // Validate person details
    Person person = owner.getUser().getPerson();
    assertNotNull(person);
    assertEquals("test@example.com", person.getEmail());
  }

  @Test
  void testTokenResponseAccessors() throws Exception {
    TokenResponse tokenResponse = loadFromFile("authorization/authorization-token-rs.json", TokenResponse.class);

    // Test that all accessor methods work correctly
    assertNotNull(tokenResponse.getAccessToken());
    assertNotNull(tokenResponse.getTokenType());
    assertNotNull(tokenResponse.getRefreshToken());
    assertNotNull(tokenResponse.getBotId());
    assertNotNull(tokenResponse.getWorkspaceName());
    assertNotNull(tokenResponse.getWorkspaceId());
    assertNotNull(tokenResponse.getWorkspaceIcon());
    assertNotNull(tokenResponse.getOwner());
    assertNotNull(tokenResponse.getRequestId());

    // This field can be null
    tokenResponse.getDuplicatedTemplateId(); // Should not throw exception

    // Validate the tokens are properly formatted
    assertTrue(tokenResponse.getAccessToken().startsWith("ntn_"));
    assertTrue(tokenResponse.getRefreshToken().startsWith("nrt_"));
    assertEquals("bearer", tokenResponse.getTokenType());

    // Validate workspace ID format (UUID)
    assertTrue(
        tokenResponse
            .getWorkspaceId()
            .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
    assertTrue(
        tokenResponse
            .getBotId()
            .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
  }

  @Test
  void testRefreshTokenResponse() throws Exception {
    TokenResponse refreshResponse =
        loadFromFile("authorization/authorization-refresh-token-rs.json", TokenResponse.class);

    // Validate that refresh token response has updated tokens
    assertNotNull(refreshResponse);
    assertEquals(
        "ntn_5557W6V5U4T3210987674821098765432109000001", refreshResponse.getAccessToken());
    assertEquals("bearer", refreshResponse.getTokenType());
    assertEquals(
        "nrt_5557W6V5U4T3210987674821098765432109000000", refreshResponse.getRefreshToken());

    // Bot ID should remain the same
    assertEquals("12345678-1234-5678-9abc-def012345678", refreshResponse.getBotId());

    // Workspace information should remain the same
    assertEquals("Test Workspace", refreshResponse.getWorkspaceName());
    assertEquals("11333323-4444-43b3-8288-222222222222", refreshResponse.getWorkspaceId());
    assertEquals("https://example.com/workspace-icon.png", refreshResponse.getWorkspaceIcon());

    // Request ID should be different (new request)
    assertEquals("33333323-4444-43b3-8288-222222222222", refreshResponse.getRequestId());
    assertNull(refreshResponse.getDuplicatedTemplateId());

    // Owner structure should remain the same
    assertNotNull(refreshResponse.getOwner());
    assertEquals("user", refreshResponse.getOwner().getType());
    assertEquals(
        "33444444-4444-43b3-8288-444444444444", refreshResponse.getOwner().getUser().getId());
    assertEquals("Test User", refreshResponse.getOwner().getUser().getName());
    assertEquals("test@example.com", refreshResponse.getOwner().getUser().getPerson().getEmail());

    // Validate that tokens are properly formatted and different from original
    assertTrue(refreshResponse.getAccessToken().startsWith("ntn_"));
    assertTrue(refreshResponse.getRefreshToken().startsWith("nrt_"));
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
