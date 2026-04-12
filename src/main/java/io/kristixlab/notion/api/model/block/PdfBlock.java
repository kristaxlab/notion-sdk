package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/** A Notion PDF block that displays an embedded PDF document. */
@Getter
@Setter
public class PdfBlock extends Block {

  private FileData pdf;

  public PdfBlock() {
    setType(BlockType.PDF.getValue());
    pdf = new FileData();
  }
}
