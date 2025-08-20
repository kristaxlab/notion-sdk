package io.kristixlab.notion.api.model.databases.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UserFilterCondition {

    @JsonProperty("contains")
    private UUID contains;

    @JsonProperty("does_not_contain")
    private UUID doesNotContain;

    @JsonProperty("is_empty")
    private Boolean isEmpty;

    @JsonProperty("is_not_empty")
    private Boolean isNotEmpty;
}
