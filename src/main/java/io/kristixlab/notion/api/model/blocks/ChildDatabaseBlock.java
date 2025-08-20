package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ChildDatabaseBlock extends Block {
  @JsonProperty("child_database")
  private ChildDatabase childDatabase;

  @Data
  public static class ChildDatabase {
    @JsonProperty("title")
    private String title;
  }
}
