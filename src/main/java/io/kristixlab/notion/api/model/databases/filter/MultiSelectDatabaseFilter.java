package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.ContainsEmptyFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiSelectDatabaseFilter extends DatabaseFilter {

    @JsonProperty("multi_select")
    private ContainsEmptyFilterCondition multiSelect;
}
