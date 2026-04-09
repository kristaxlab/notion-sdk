package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.FileData;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion PDF block that displays an embedded PDF document.
 *
 * <p>Simple construction via {@link #of(FileData)}.
 */
@Getter
@Setter
public class PdfBlock extends Block {

  private FileData pdf;

  public PdfBlock() {
    setType("pdf");
    pdf = new FileData();
  }

  /**
   * Creates a PDF block from the given file data.
   *
   * @param fileData the PDF file data (external URL or Notion-hosted file)
   * @return a new PdfBlock
   */
  public static PdfBlock of(FileData fileData) {
    PdfBlock pdfBlock = new PdfBlock();
    pdfBlock.setPdf(fileData);
    return pdfBlock;
  }
}
