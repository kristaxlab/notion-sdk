package io.kristaxlab.notion.model.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CommentDisplayNameTypeTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  @Test
  @DisplayName("type returns api tokens")
  void type_returnsApiTokens() {
    assertEquals("user", CommentDisplayNameType.USER.type());
    assertEquals("custom", CommentDisplayNameType.CUSTOM.type());
    assertEquals("integration", CommentDisplayNameType.INTEGRATION.type());
  }

  @ParameterizedTest
  @EnumSource(CommentDisplayNameType.class)
  @DisplayName("from value round trips every constant")
  void fromValue_roundTripsEveryConstant(CommentDisplayNameType displayNameType) {
    assertEquals(displayNameType, CommentDisplayNameType.fromValue(displayNameType.type()));
  }

  @Test
  @DisplayName("from value unknown token throws illegal argument")
  void fromValue_unknownToken_throwsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class, () -> CommentDisplayNameType.fromValue("connection"));
  }

  @Test
  @DisplayName("from value null throws illegal argument")
  void fromValue_null_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> CommentDisplayNameType.fromValue(null));
  }

  @Test
  @DisplayName("enum values contains all comment display name types")
  void enumValues_containsAllCommentDisplayNameTypes() {
    assertEquals(3, CommentDisplayNameType.values().length);
  }

  @Test
  @DisplayName("serialize display name writes api tokens")
  void serialize_displayNameWritesApiTokens() {
    String json = JSON.toJson(CommentDisplayName.integration());

    assertTrue(json.contains("\"type\":\"integration\""));
    assertTrue(json.contains("INTEGRATION") == false);
  }

  @Test
  @DisplayName("unknown response type remains a string")
  void unknownResponseType_remainsAString() {
    CommentDisplayName displayName =
        JSON.toObject(
            "{\"type\":\"future_type\",\"resolved_name\":\"X\"}", CommentDisplayName.class);

    assertEquals("future_type", displayName.getType());
    assertEquals("X", displayName.getResolvedName());
  }
}
