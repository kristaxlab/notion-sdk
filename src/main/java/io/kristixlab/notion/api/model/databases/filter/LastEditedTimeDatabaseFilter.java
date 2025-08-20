package io.kristixlab.notion.api.model.databases.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.databases.filter.condition.DateFilterCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LastEditedTimeDatabaseFilter extends DatabaseFilter {

    @JsonProperty("timestamp")
    private String timestamp = "last_edited_time";

    @JsonProperty("last_edited_time")
    private DateFilterCondition lastEditedTime;
}
