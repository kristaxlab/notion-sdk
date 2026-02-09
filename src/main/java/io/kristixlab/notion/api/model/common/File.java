package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class File {

    @JsonProperty("url")
    private String url;

    @JsonProperty("expiry_time")
    private String expiryTime;
}
