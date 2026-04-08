package io.kristixlab.notion.api.model.blocks;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion code block.
 *
 * <p>Simple construction via {@link #of(String, String)}. For captions or rich text formatting use
 * {@link #builder()}.
 */
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
   * @param language the programming language (e.g., {@code "java"}, {@code "python"})
   * @return a new CodeBlock
   */
  public static CodeBlock of(String content, String language) {
    CodeBlock block = new CodeBlock();
    block.getCode().setRichText(RichText.of(content));
    block.getCode().setLanguage(language);
    return block;
  }

  /**
   * Returns a new builder for constructing a {@link CodeBlock} with language, caption, and/or rich
   * text formatting.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** The inner content object of a code block. */
  @Getter
  @Setter
  public static class Code {

    private List<RichText> richText;

    private String language;

    private List<RichText> caption;
  }

  /** Builder for {@link CodeBlock}. */
  public static class Builder {
    private List<RichText> richText;

    private String language;

    private List<RichText> caption;

    private Builder() {}

    /**
     * Sets the code content from a plain text string.
     *
     * @param text the code content
     * @return this builder
     */
    public Builder text(String text) {
      this.richText = RichText.of(text);
      return this;
    }

    /**
     * Sets the code content from a pre-built rich text list.
     *
     * @param richText the rich text content
     * @return this builder
     */
    public Builder richText(List<RichText> richText) {
      this.richText = richText;
      return this;
    }

    /**
     * Sets the code content using a {@link RichText.Builder} consumer.
     *
     * @param consumer a consumer that configures the rich text builder
     * @return this builder
     */
    public Builder richText(Consumer<RichText.Builder> consumer) {
      RichText.Builder builder = RichText.builder();
      consumer.accept(builder);
      this.richText = builder.buildList();
      return this;
    }

    /**
     * Sets the programming language for syntax highlighting.
     *
     * @param language the language identifier (e.g., {@code "java"}, {@code "python"})
     * @return this builder
     */
    public Builder language(String language) {
      this.language = language;
      return this;
    }

    /**
     * Sets the caption from a pre-built rich text list.
     *
     * @param caption the caption rich text elements
     * @return this builder
     */
    public Builder caption(List<RichText> caption) {
      this.caption = caption;
      return this;
    }

    /**
     * Sets the caption using a {@link RichText.Builder} consumer.
     *
     * @param consumer a consumer that configures the caption rich text builder
     * @return this builder
     */
    public Builder caption(Consumer<RichText.Builder> consumer) {
      RichText.Builder builder = RichText.builder();
      consumer.accept(builder);
      this.caption = builder.buildList();
      return this;
    }

    /**
     * Builds the {@link CodeBlock}.
     *
     * @return a new CodeBlock
     */
    public CodeBlock build() {
      CodeBlock block = new CodeBlock();
      block.getCode().setRichText(richText);
      block.getCode().setLanguage(language);
      block.getCode().setCaption(caption);
      return block;
    }
  }
}
