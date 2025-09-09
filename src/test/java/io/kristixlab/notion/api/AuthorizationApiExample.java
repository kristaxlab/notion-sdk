package io.kristixlab.notion.api;

import io.kristixlab.notion.Notion;
import io.kristixlab.notion.NotionClient;
import io.kristixlab.notion.api.model.IntegrationTest;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenResponse;
import io.kristixlab.notion.api.model.authorization.RevokeTokenResponse;
import io.kristixlab.notion.api.model.authorization.TokenResponse;
import java.io.IOException;

import io.kristixlab.notion.api.model.common.Parent;
import io.kristixlab.notion.api.model.pages.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthorizationApiExample extends IntegrationTest {

  private AuthorizationApi authorizationApi;
  private BlocksApi blocksApi;
  private DatabasesApi databasesApi;
  private PagesApi pagesApi;

  @BeforeEach
  protected void setUp() throws Exception {
    super.setUp();
    authorizationApi = new AuthorizationApi(null, getTransport());
    blocksApi = new BlocksApi(getTransport());
    pagesApi = new PagesApi(getTransport());

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

  @Test
  void experiment1() throws IOException {
    TokenResponse rs =
        authorizationApi.exchangeCodeForToken(
            "036e8679-9d92-4e09-b9ee-3e26ee47f18f", "https://www.kkkkkkkk.com");

    saveToFile(rs, "auth-experiment.json");
  }

  @Test
  void experiment2() throws IOException {
    String token = "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";
    String db = "156b90f86d768067a7f7e8f56640deaa";
    Page page = new Page();
    page.setParent(new Parent());
    page.getParent().setDatabaseId(db);
    page = pagesApi.create(page);
    // https: // www.notion.so/kkkkkkkk-lll/156b90f86d768067a7f7e8f56640deaa?v=156b90f86d7681808858000cb2c31e52&source=copy_link
    saveToFile(page, "auth-experiment-2.json");
  }

  //  "access_token": "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",
  //          "token_type": "bearer",
  //          "refresh_token": "nrt_496768403105vDWVbnretWRLTwM5sMPicgakZuUpDo63Gk",

}
