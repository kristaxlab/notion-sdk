package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfBlock extends Block {

  private FileData pdf;

  public PdfBlock() {
    setType("pdf");
    pdf = new FileData();
  }
}
