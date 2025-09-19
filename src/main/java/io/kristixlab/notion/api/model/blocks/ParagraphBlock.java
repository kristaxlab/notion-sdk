package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ParagraphBlock extends Block {

  @JsonProperty("paragraph")
  private Paragraph paragraph;

  @Data
  public static class Paragraph {
    @JsonProperty("rich_text")
    private List<RichText> richText;

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }

  public static ParagraphBlock of(String text) {
    return of(text, Color.DEFAULT);
  }

  public static ParagraphBlock of(String text, Color color) {
    ParagraphBlock paragraphBlock = new ParagraphBlock();
    paragraphBlock.setType("paragraph");
    Paragraph paragraph = new Paragraph();
    RichText richText = new RichText();
    RichText.Text textObj = new RichText.Text();
    textObj.setContent(text);
    richText.setType("text");
    richText.setText(textObj);
    richText.setPlainText(text);
    paragraph.setRichText(List.of(richText));
    if (color != null) {
      paragraph.setColor(color.getValue());
    }
    paragraphBlock.setParagraph(paragraph);
    return paragraphBlock;
  }
}
