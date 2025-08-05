package io.kristixlab.notion.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BaseNotionResponse {

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("developer_survey")
    private String developerSurvey;


}
