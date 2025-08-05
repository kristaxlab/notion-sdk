package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class EmbedBlock extends Block {
    @JsonProperty("embed")
    private Embed embed;

    @Data
    public static class Embed {
        @JsonProperty("url")
        private String url;

        @JsonProperty("caption")
        private List<RichText> caption;
    }
}
