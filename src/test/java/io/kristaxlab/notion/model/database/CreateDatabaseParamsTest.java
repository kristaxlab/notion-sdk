package io.kristaxlab.notion.model.database;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreateDatabaseParamsTest {

  @Nested
  class BuilderTests {

    @Test
    @DisplayName("build empty leaves database type unset")
    void buildEmpty_databaseTypeIsNull() {
      CreateDatabaseParams params = CreateDatabaseParams.builder().build();

      assertNull(params.getDatabaseType());
      assertNull(params.getInitialDataSource());
    }

    @Test
    @DisplayName("database type enum sets the api token")
    void databaseType_enum_setsApiToken() {
      CreateDatabaseParams params =
          CreateDatabaseParams.builder().databaseType(DatabaseType.TASKS).build();

      assertEquals("tasks", params.getDatabaseType());
    }

    @Test
    @DisplayName("database type string sets the token as given")
    void databaseType_string_setsTokenAsGiven() {
      CreateDatabaseParams params = CreateDatabaseParams.builder().databaseType("projects").build();

      assertEquals("projects", params.getDatabaseType());
    }

    @Test
    @DisplayName("database type null enum clears the field")
    void databaseType_nullEnum_clearsField() {
      CreateDatabaseParams params =
          CreateDatabaseParams.builder()
              .databaseType(DatabaseType.SKILLS)
              .databaseType((DatabaseType) null)
              .build();

      assertNull(params.getDatabaseType());
    }

    @Test
    @DisplayName("last database type call wins")
    void lastDatabaseTypeCallWins() {
      CreateDatabaseParams params =
          CreateDatabaseParams.builder()
              .databaseType(DatabaseType.TASKS)
              .databaseType("skills")
              .build();

      assertEquals("skills", params.getDatabaseType());
    }

    @Test
    @DisplayName("builder does not reject database type together with properties")
    void build_allowsDatabaseTypeTogetherWithProperties() {
      CreateDatabaseParams params =
          CreateDatabaseParams.builder()
              .databaseType(DatabaseType.TASKS)
              .properties(s -> s.title("Name"))
              .build();

      assertEquals("tasks", params.getDatabaseType());
      assertNotNull(params.getInitialDataSource());
    }

    @Test
    @DisplayName("builder method chaining returns same builder")
    void builderMethodChaining_returnsSameBuilder() {
      CreateDatabaseParams.Builder builder = CreateDatabaseParams.builder();

      assertSame(builder, builder.databaseType(DatabaseType.TASKS));
      assertSame(builder, builder.databaseType("projects"));
    }
  }

  @Nested
  class Serialization {

    @Test
    @DisplayName("database type serializes as database_type")
    void databaseType_serializesAsSnakeCaseField() {
      CreateDatabaseParams params =
          CreateDatabaseParams.builder().inPage("page-1").databaseType(DatabaseType.SKILLS).build();

      String json = JacksonSerializer.withDefaults().toJson(params);

      assertTrue(json.contains("\"database_type\":\"skills\""));
      assertFalse(json.contains("initial_data_source"));
    }

    @Test
    @DisplayName("unset database type is omitted")
    void unsetDatabaseType_isOmitted() {
      CreateDatabaseParams params = CreateDatabaseParams.builder().inPage("page-1").build();

      String json = JacksonSerializer.withDefaults().toJson(params);

      assertFalse(json.contains("database_type"));
    }
  }
}
