package io.kristaxlab.notion.model.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class DatabaseTypeTest {

  @Test
  @DisplayName("type returns api tokens")
  void type_returnsApiTokens() {
    assertEquals("tasks", DatabaseType.TASKS.type());
    assertEquals("projects", DatabaseType.PROJECTS.type());
    assertEquals("skills", DatabaseType.SKILLS.type());
  }

  @ParameterizedTest
  @EnumSource(DatabaseType.class)
  @DisplayName("from value round trips every constant")
  void fromValue_roundTripsEveryConstant(DatabaseType databaseType) {
    assertEquals(databaseType, DatabaseType.fromValue(databaseType.type()));
  }

  @Test
  @DisplayName("from value unknown token throws illegal argument")
  void fromValue_unknownToken_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> DatabaseType.fromValue("notes"));
  }

  @Test
  @DisplayName("from value null throws illegal argument")
  void fromValue_null_throwsIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> DatabaseType.fromValue(null));
  }

  @Test
  @DisplayName("enum values contains all database types")
  void enumValues_containsAllDatabaseTypes() {
    assertEquals(3, DatabaseType.values().length);
  }
}
