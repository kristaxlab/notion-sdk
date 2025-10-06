package io.kristixlab.notion.api.model.datasources.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = UnknownFilter.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CheckboxFilter.class),
  @JsonSubTypes.Type(value = DateFilter.class),
  @JsonSubTypes.Type(value = CreatedTimeFilter.class),
  @JsonSubTypes.Type(value = LastEditedTimeFilter.class),
  @JsonSubTypes.Type(value = FilesFilter.class),
  @JsonSubTypes.Type(value = FormulaFilter.class),
  @JsonSubTypes.Type(value = NumberFilter.class),
  @JsonSubTypes.Type(value = PeopleFilter.class),
  @JsonSubTypes.Type(value = CreatedByFilter.class),
  @JsonSubTypes.Type(value = LastEditedByFilter.class),
  @JsonSubTypes.Type(value = RelationFilter.class),
  @JsonSubTypes.Type(value = RichTextFilter.class),
  @JsonSubTypes.Type(value = RollupFilter.class),
  @JsonSubTypes.Type(value = SelectFilter.class),
  @JsonSubTypes.Type(value = MultiSelectFilter.class),
  @JsonSubTypes.Type(value = StatusFilter.class),
  @JsonSubTypes.Type(value = IdFilter.class),
  @JsonSubTypes.Type(value = PhoneNumberFilter.class),
  @JsonSubTypes.Type(value = UrlFilter.class),
})
@Data
public abstract class Filter {

  @JsonProperty("or")
  private List<Filter> or;

  @JsonProperty("and")
  private List<Filter> and;

  /* property name or id */
  @JsonProperty("property")
  private String property;
}
