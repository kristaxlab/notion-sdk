package io.kristaxlab.notion.model.datasource.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.kristaxlab.notion.model.datasource.filter.condition.DateFilterCondition;
import io.kristaxlab.notion.model.datasource.filter.condition.NumberFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollupFilter extends Filter {

  private RollupCondition rollup;

  @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = Object.class)
  @JsonSubTypes({
    @JsonSubTypes.Type(value = RollupAnyDatabaseFilter.class),
    @JsonSubTypes.Type(value = RollupEveryDatabaseFilter.class),
    @JsonSubTypes.Type(value = RollupNoneDatabaseFilter.class),
    @JsonSubTypes.Type(value = RollupDateDatabaseFilter.class),
    @JsonSubTypes.Type(value = RollupNumberDatabaseFilter.class)
  })
  @Data
  public static class RollupCondition {}

  // Specific rollup filter types
  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupAnyDatabaseFilter extends RollupCondition {
    @JsonProperty("any")
    private Filter any;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupEveryDatabaseFilter extends RollupCondition {
    @JsonProperty("every")
    private Filter every;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class RollupNoneDatabaseFilter extends RollupCondition {
    @JsonProperty("none")
    private Filter none;
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
