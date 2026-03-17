package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.Color;
import io.kristixlab.notion.api.model.common.RichText;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class ParagraphBlock extends Block {

  @JsonProperty("paragraph")
  private Paragraph paragraph;

  public ParagraphBlock() {
    setType("paragraph");
    paragraph = new Paragraph();
  }

  public static ParagraphBlockBuilder builder() {
    return new ParagraphBlockBuilder();
  }

  public static ParagraphBlock of(String text) {
    return of(text, Color.DEFAULT);
  }

  public static ParagraphBlock of(String text, Color color) {
    ParagraphBlock paragraphBlock = new ParagraphBlock();
    Paragraph paragraph = paragraphBlock.getParagraph();
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
    return paragraphBlock;
  }

  @Data
  public static class Paragraph {
    @JsonProperty("rich_text")
    private List<RichText> richText = new ArrayList<>();

    @JsonProperty("color")
    private String color;

    @JsonProperty("children")
    private List<Block> children;
  }

  public static class ParagraphBlockBuilder {
    private ParagraphBlock block = new ParagraphBlock();
    private RichText.RichTextBuilder richTextBuilder = RichText.builder();
    private boolean built = false;

    public ParagraphBlock build() {
      if (built) {
        throw new IllegalStateException(
            "Builder is already done, create a new builder for another paragraph block");
      }
      if (getBlock().getParagraph() == null) {
        getBlock().setParagraph(new Paragraph());
      }
      if (getBlock().getParagraph().getRichText() == null) {
        getBlock().getParagraph().setRichText(new ArrayList<>());
      }
      getBlock().getParagraph().getRichText().add(getRichTextBuilder().build());
      ParagraphBlock readyBlock = getBlock();
      built = true;

      return readyBlock;
    }

    public ParagraphBlockBuilder addAnother() {
      if (getBlock().getParagraph() == null) {
        getBlock().setParagraph(new Paragraph());
      }
      if (getBlock().getParagraph().getRichText() == null) {
        getBlock().getParagraph().setRichText(new ArrayList<>());
      }
      getBlock().getParagraph().getRichText().add(getRichTextBuilder().build());
      richTextBuilder = RichText.builder();
      return this;
    }

    private ParagraphBlock getBlock() {
      if (built) {
        throw new IllegalStateException(
            "Builder is already done, create a new builder for another paragraph block");
      }
      return block;
    }

    private RichText.RichTextBuilder getRichTextBuilder() {
      if (built) {
        throw new IllegalStateException(
            "Builder is already done, create a new builder for another paragraph block");
      }
      return richTextBuilder;
    }

    public ParagraphBlockBuilder fromText(String text) {
      getRichTextBuilder().fromText(text);
      return this;
    }

    public ParagraphBlockBuilder fromBlockMention(String id) {
      getRichTextBuilder().fromBlockMention(id);
      return this;
    }

    public ParagraphBlockBuilder fromUserMention(String userId) {
      getRichTextBuilder().fromUserMention(userId);
      return this;
    }

    public ParagraphBlockBuilder fromUrl(String url) {
      getRichTextBuilder().fromUrl(url);
      return this;
    }

    public ParagraphBlockBuilder fromDateMention(String dateFrom) {
      getRichTextBuilder().fromDateMention(dateFrom, null, null);
      return this;
    }

    public ParagraphBlockBuilder fromDateMention(String dateFrom, String dateTo) {
      getRichTextBuilder().fromDateMention(dateFrom, dateTo, null);
      return this;
    }

    public ParagraphBlockBuilder fromDateMention(String dateFrom, String dateTo, String timeZone) {
      getRichTextBuilder().fromDateMention(dateFrom, dateTo, timeZone);
      return this;
    }

    public ParagraphBlockBuilder fromExpression(String expression) {
      getRichTextBuilder().fromExpression(expression);
      return this;
    }

    public ParagraphBlockBuilder fromCustomEmoji(String id) {
      getRichTextBuilder().fromCustomEmoji(id);
      return this;
    }

    /*
     * @notionapinotes TODO check with Notion team if it is a bug that linkMention in request is not supported
     */
    public ParagraphBlockBuilder fromLinkMention(String url) {
      getRichTextBuilder().fromLinkMention(url);
      return this;
    }

    public ParagraphBlockBuilder withColor(Color color) {
      getRichTextBuilder().withColor(color);
      return this;
    }

    public ParagraphBlockBuilder withBold(boolean bold) {
      getRichTextBuilder().withBold(bold);
      return this;
    }

    public ParagraphBlockBuilder withItalic(boolean italic) {
      getRichTextBuilder().withItalic(italic);
      return this;
    }

    public ParagraphBlockBuilder withUnderline(boolean underline) {
      getRichTextBuilder().withUnderline(underline);
      return this;
    }

    public ParagraphBlockBuilder withStrikethrough(boolean strikethrough) {
      getRichTextBuilder().withStrikethrough(strikethrough);
      return this;
    }

    public ParagraphBlockBuilder withCode(boolean code) {
      getRichTextBuilder().withCode(code);
      return this;
    }
  }
}
