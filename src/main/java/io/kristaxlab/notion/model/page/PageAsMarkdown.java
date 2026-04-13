package io.kristaxlab.notion.model.page;

import io.kristaxlab.notion.model.BaseNotionObject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageAsMarkdown extends BaseNotionObject {

  private String id;

  private String markdown;

  private Boolean truncated;

  /*
   Block IDs that could not be loaded (appeared as tags in the markdown). Pass these IDs back to /pages/{id}/markdown
   endpoint to fetch their content separately.
  */
  private List<String> unknownBlockIds;
}
