package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BreadcrumbBlock extends Block {
  @JsonProperty("breadcrumb")
  private Object breadcrumb; // Notion returns an empty object

  public BreadcrumbBlock() {
    setType("breadcrumb");
    breadcrumb = new Object();
  }
}
