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
public class QuoteBlock extends Block {

    @JsonProperty("quote")
    private Quote quote;

    @Data
    public static class Quote {
        @JsonProperty("rich_text")
        private List<RichText> richText;

        @JsonProperty("color")
        private String color;

        @JsonProperty("children")
        private List<Block> children;
    }
}
