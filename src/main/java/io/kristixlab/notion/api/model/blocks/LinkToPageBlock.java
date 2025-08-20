package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkToPageBlock extends Block {

  @JsonProperty("link_to_page")
  private LinkToPage linkToPage;

  @Data
  public static class LinkToPage {
    @JsonProperty("type")
    private String type;

    @JsonProperty("page_id")
    private String pageId;

    @JsonProperty("database_id")
    private String databaseId;

    @JsonProperty("workspace")
    private Boolean workspace;
  }
}
