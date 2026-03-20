package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for status columns. Similar to select but designed for workflow states. */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusSchema extends DataSourcePropertySchema {

  @JsonProperty("status")
  private StatusConfig status = new StatusConfig();

  @Data
  public static class StatusConfig {
    @JsonProperty("options")
    private List<StatusOption> options;

    @JsonProperty("groups")
    private List<StatusGroup> groups;
  }
}
