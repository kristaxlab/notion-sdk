package io.kristixlab.notion.api.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageReference {
  @JsonProperty("id")
  private String id;

  public static PageReference of(String id) {
    PageReference pageReference = new PageReference();
    pageReference.setId(id);
    return pageReference;
  }
}
