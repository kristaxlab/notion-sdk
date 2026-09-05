package io.kristaxlab.notion.model.datasource;

import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.model.common.Icon;
import io.kristaxlab.notion.model.common.Parent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateDataSourceParamsTest {

  @Nested
  class BuilderTests {

    @Test
    @DisplayName("build empty leaves parent unset")
    void buildEmpty_parentIsNull() {
      UpdateDataSourceParams params = UpdateDataSourceParams.builder().build();

      assertNull(params.getParent());
      assertNull(params.getTitle());
      assertNull(params.getIcon());
      assertNull(params.getInTrash());
    }

    @Test
    @DisplayName("parent sets a fully constructed parent")
    void parent_setsParent() {
      Parent parent = Parent.databaseParent("db-1");

      UpdateDataSourceParams params = UpdateDataSourceParams.builder().parent(parent).build();

      assertSame(parent, params.getParent());
      assertEquals("database_id", params.getParent().getType());
      assertEquals("db-1", params.getParent().getDatabaseId());
    }

    @Test
    @DisplayName("inDatabase sets a database parent")
    void inDatabase_setsDatabaseParent() {
      UpdateDataSourceParams params = UpdateDataSourceParams.builder().inDatabase("db-2").build();

      assertNotNull(params.getParent());
      assertEquals("database_id", params.getParent().getType());
      assertEquals("db-2", params.getParent().getDatabaseId());
    }

    @Test
    @DisplayName("last parent call wins")
    void lastParentCallWins() {
      UpdateDataSourceParams params =
          UpdateDataSourceParams.builder()
              .inDatabase("db-first")
              .parent(Parent.databaseParent("db-second"))
              .build();

      assertEquals("db-second", params.getParent().getDatabaseId());
    }

    @Test
    @DisplayName("builder method chaining returns same builder")
    void builderMethodChaining_returnsSameBuilder() {
      UpdateDataSourceParams.Builder builder = UpdateDataSourceParams.builder();

      assertSame(builder, builder.inDatabase("db-1"));
      assertSame(builder, builder.parent(Parent.databaseParent("db-2")));
      assertSame(builder, builder.title("Tasks"));
      assertSame(builder, builder.icon("📁"));
      assertSame(builder, builder.inTrash(true));
    }

    @Test
    @DisplayName("build copies remaining fields")
    void build_copiesRemainingFields() {
      UpdateDataSourceParams params =
          UpdateDataSourceParams.builder()
              .inDatabase("db-3")
              .title("Moved")
              .icon(Icon.emoji("📦"))
              .inTrash(false)
              .build();

      assertEquals("db-3", params.getParent().getDatabaseId());
      assertEquals("Moved", params.getTitle().get(0).getPlainText());
      assertEquals("📦", params.getIcon().getEmoji());
      assertEquals(false, params.getInTrash());
    }
  }

  @Nested
  class BuilderIndependence {

    @Test
    @DisplayName("mutating params after build does not affect subsequent build")
    void mutatingParamsAfterBuild_doesNotAffectSubsequentBuild() {
      UpdateDataSourceParams.Builder builder =
          UpdateDataSourceParams.builder().inDatabase("db-original");

      UpdateDataSourceParams first = builder.build();
      first.setParent(Parent.databaseParent("db-modified"));

      UpdateDataSourceParams second = builder.build();
      assertEquals("db-modified", first.getParent().getDatabaseId());
      assertEquals("db-original", second.getParent().getDatabaseId());
    }
  }

  @Nested
  class Serialization {

    @Test
    @DisplayName("parent serializes as a database_id parent object")
    void parent_serializesAsDatabaseParent() {
      UpdateDataSourceParams params =
          UpdateDataSourceParams.builder().inDatabase("db-serialize").build();

      String json = JacksonSerializer.withDefaults().toJson(params);

      assertTrue(json.contains("\"parent\""));
      assertTrue(json.contains("\"type\":\"database_id\""));
      assertTrue(json.contains("\"database_id\":\"db-serialize\""));
    }
  }
}
