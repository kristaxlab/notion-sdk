package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DateDatabaseFilter extends DatabaseFilter {

    @JsonProperty("date")
    private DateFilterCondition date;
}
