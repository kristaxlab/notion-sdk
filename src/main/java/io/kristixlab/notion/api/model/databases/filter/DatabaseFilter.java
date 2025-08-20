package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = UnknownDatabaseFilter.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CheckboxDatabaseFilter.class),
  @JsonSubTypes.Type(value = DateDatabaseFilter.class),
  @JsonSubTypes.Type(value = CreatedTimeDatabaseFilter.class),
  @JsonSubTypes.Type(value = LastEditedTimeDatabaseFilter.class),
  @JsonSubTypes.Type(value = FilesDatabaseFilter.class),
  @JsonSubTypes.Type(value = FormulaDatabaseFilter.class),
  @JsonSubTypes.Type(value = NumberDatabaseFilter.class),
  @JsonSubTypes.Type(value = PeopleDatabaseFilter.class),
  @JsonSubTypes.Type(value = CreatedByDatabaseFilter.class),
  @JsonSubTypes.Type(value = LastEditedByDatabaseFilter.class),
  @JsonSubTypes.Type(value = RelationDatabaseFilter.class),
  @JsonSubTypes.Type(value = RichTextDatabaseFilter.class),
  @JsonSubTypes.Type(value = RollupDatabaseFilter.class),
  @JsonSubTypes.Type(value = SelectDatabaseFilter.class),
  @JsonSubTypes.Type(value = MultiSelectDatabaseFilter.class),
  @JsonSubTypes.Type(value = StatusDatabaseFilter.class),
  @JsonSubTypes.Type(value = IdDatabaseFilter.class),
  @JsonSubTypes.Type(value = PhoneNumberFilter.class),
  @JsonSubTypes.Type(value = UrlFilter.class),
})
@Data
public abstract class DatabaseFilter {

  @JsonProperty("or")
  private List<DatabaseFilter> or;

  @JsonProperty("and")
  private List<DatabaseFilter> and;

  /* property name or id */
  @JsonProperty("property")
  private String property;
}
