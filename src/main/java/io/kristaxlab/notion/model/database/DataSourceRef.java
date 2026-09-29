package io.kristaxlab.notion.model.database;

import lombok.Getter;
import lombok.Setter;

/** Represents a data source reference with ID and name. */
@Getter
@Setter
public class DataSourceRef {

  private String id;

  private String name;
}
