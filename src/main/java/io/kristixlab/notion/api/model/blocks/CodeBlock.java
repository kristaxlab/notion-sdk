package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeBlock extends Block {

  private Code code;

  public CodeBlock() {
    setType("code");
    code = new Code();
  }

  /**
   * Creates a code block with the given content and language.
   *
   * @param content the code content
   * @param language the programming language (e.g., "java", "python", "javascript")
   * @return a new CodeBlock
   */
  public static CodeBlock of(String content, String language) {
    CodeBlock block = new CodeBlock();
    block.getCode().setRichText(RichText.of(content));
    block.getCode().setLanguage(language);
    return block;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Getter
  @Setter
  public static class Code {

    private List<RichText> richText;

    private String language;

    private List<RichText> caption;
  }

  public static class Builder {
    private List<RichText> richText;

    private String language;

    private List<RichText> caption;

    private Builder() {}

    public Builder text(String text) {
      this.richText = RichText.of(text);
      return this;
    }

    public Builder richText(List<RichText> richText) {
      this.richText = richText;
      return this;
    }

    public Builder richText(Consumer<RichText.Builder> consumer) {
      RichText.Builder builder = RichText.builder();
      consumer.accept(builder);
      this.richText = builder.buildList();
      return this;
    }

    public Builder language(String language) {
      this.language = language;
      return this;
    }

    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    public Builder caption(Consumer<RichText.Builder> consumer) {
      RichText.Builder builder = RichText.builder();
      consumer.accept(builder);
      this.caption = builder.buildList();
      return this;
    }

    public CodeBlock build() {
      CodeBlock block = new CodeBlock();
      block.getCode().setRichText(richText);
      block.getCode().setLanguage(language);
      block.getCode().setCaption(caption);
      return block;
    }
  }
}
