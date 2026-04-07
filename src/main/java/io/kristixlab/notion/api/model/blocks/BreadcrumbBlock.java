package io.kristixlab.notion.api.model.blocks;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreadcrumbBlock extends Block {

  private Object breadcrumb; // Notion returns an empty object

  public BreadcrumbBlock() {
    setType("breadcrumb");
    breadcrumb = new Object();
  }
}
