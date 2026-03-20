package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.BaseNotionObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PageAsMarkdown extends BaseNotionObject {

  @JsonProperty("id")
  private String id;

  @JsonProperty("markdown")
  private String markdown;

  @JsonProperty("truncated")
  private Boolean truncated;

  /*
   Block IDs that could not be loaded (appeared as tags in the markdown). Pass these IDs back to /pages/{id}/markdown
   endpoint to fetch their content separately.
  */
  @JsonProperty("unknown_block_ids")
  private List<String> unknownBlockIds;
}
