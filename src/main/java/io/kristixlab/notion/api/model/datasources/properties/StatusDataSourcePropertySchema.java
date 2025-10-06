package io.kristixlab.notion.api.model.datasources.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Database property for status columns. Similar to select but designed for workflow states. */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatusDataSourcePropertySchema extends DataSourcePropertySchema {

  @JsonProperty("status")
  private StatusConfig status = new StatusConfig();

  @Data
  public static class StatusConfig {
    @JsonProperty("options")
    private List<StatusOption> options;

    @JsonProperty("groups")
    private List<StatusGroup> groups;
  }

  @Data
  public static class StatusOption {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("color")
    private String
        color; // "default", "gray", "brown", "orange", "yellow", "green", "blue", "purple", "pink",

    // "red"

    @JsonProperty("description")
    private String description;
  }

  @Data
  public static class StatusGroup {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("color")
    private String color;

    @JsonProperty("option_ids")
    private List<String> optionIds;
  }
}
