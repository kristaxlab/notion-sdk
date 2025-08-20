package io.kristixlab.notion.api.model.databases.filter.condition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContainsEmptyFilterCondition {

    @JsonProperty("contains")
    private String contains;

    @JsonProperty("does_not_contain")
    private String doesNotContain;

    @JsonProperty("is_empty")
    private Boolean isEmpty;

    @JsonProperty("is_not_empty")
    private Boolean isNotEmpty;
}
