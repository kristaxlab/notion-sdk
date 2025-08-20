package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristixlab.notion.api.model.databases.filter.condition.DateFilterCondition;
import io.kristixlab.notion.api.model.databases.filter.condition.NumberFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RollupDatabaseFilter extends DatabaseFilter {

  @JsonProperty("rollup")
  private RollupCondition rollup;

  @JsonTypeInfo(
          use = JsonTypeInfo.Id.DEDUCTION,
          defaultImpl = Object.class
  )
  @JsonSubTypes({
          @JsonSubTypes.Type(value = RollupAnyDatabaseFilter.class),
          @JsonSubTypes.Type(value = RollupEveryDatabaseFilter.class),
          @JsonSubTypes.Type(value = RollupNoneDatabaseFilter.class),
          @JsonSubTypes.Type(value = RollupDateDatabaseFilter.class),
          @JsonSubTypes.Type(value = RollupNumberDatabaseFilter.class)
  })
  @Data
  public static class RollupCondition {

  }

  // Specific rollup filter types
  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupAnyDatabaseFilter extends RollupCondition {
    @JsonProperty("any")
    private DatabaseFilter any;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupEveryDatabaseFilter extends RollupCondition {
    @JsonProperty("every")
    private DatabaseFilter every;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupNoneDatabaseFilter extends RollupCondition {
    @JsonProperty("none")
    private DatabaseFilter none;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupDateDatabaseFilter extends RollupCondition {
    @JsonProperty("date")
    private DateFilterCondition date;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupNumberDatabaseFilter extends RollupCondition {
    @JsonProperty("number")
    private NumberFilterCondition number;
  }
}
