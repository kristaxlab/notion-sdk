package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Icon {

    @JsonProperty("type")
    private String type;

    @JsonProperty("emoji")
    private String emoji;

    @JsonProperty("file")
    private FileData file;

}
