package io.kristaxlab.notion.model.datasource.properties;

import io.kristaxlab.notion.model.page.property.PropertyType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Database property for status columns. Similar to select but designed for workflow states. */
@Getter
@Setter
public class StatusSchema extends DataSourcePropertySchema {

  private final String type = PropertyType.STATUS.type();

  private StatusConfig status = new StatusConfig();

  @Getter
  @Setter
  public static class StatusConfig {

    private List<StatusOption> options;

    private List<StatusGroup> groups;
  }
}
