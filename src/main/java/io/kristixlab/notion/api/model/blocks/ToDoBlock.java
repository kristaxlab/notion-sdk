package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ToDoBlock extends Block {
    @JsonProperty("to_do")
    private ToDo toDo;

    @Data
    public static class ToDo {
        @JsonProperty("rich_text")
        private List<RichText> richText;

        @JsonProperty("checked")
        private Boolean checked;

        @JsonProperty("color")
        private String color;

        @JsonProperty("children")
        private List<Block> children;
    }
}
