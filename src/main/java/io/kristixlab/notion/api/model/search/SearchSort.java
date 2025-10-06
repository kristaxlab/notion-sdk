package io.kristixlab.notion.api.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Sort criteria for search requests. */
@Data
public class SearchSort {

  /** The direction to sort results. Can be "ascending" or "descending". */
  @JsonProperty("direction")
  private String direction;

  /** The name of the timestamp to sort by. For search, this is always "last_edited_time" */
  @JsonProperty("timestamp")
  private String timestamp = "last_edited_time";

  /** Creates a sort for ascending order by last edited time */
  public static SearchSort ascending() {
    SearchSort sort = new SearchSort();
    sort.setDirection("ascending");
    return sort;
  }

  /** Creates a sort for descending order by last edited time */
  public static SearchSort descending() {
    SearchSort sort = new SearchSort();
    sort.setDirection("descending");
    return sort;
  }
}
