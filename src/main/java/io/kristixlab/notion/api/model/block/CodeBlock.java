package io.kristixlab.notion.api.model.block;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.helper.NotionText;
import io.kristixlab.notion.api.model.helper.NotionTextBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;

/** A Notion code block. */
@Getter
@Setter
public class CodeBlock extends Block {

  private Code code;

  public CodeBlock() {
    setType("code");
    code = new Code();
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
    private List<RichText> richText = new ArrayList<>();

    private String language;

    private List<RichText> caption = new ArrayList<>();

    private Builder() {}

    /**
     * Sets the code content from a plain text string.
     *
     * @param text the code content
     * @return this builder
     */
    public Builder code(String text) {
      this.richText.add(NotionText.plainText(text));
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

    public Builder caption(String caption) {
      this.caption.add(NotionText.plainText(caption));
      return this;
    }

    public Builder caption(RichText... caption) {
      return caption(Arrays.asList(caption));
    }

    public Builder caption(List<RichText> caption) {
      this.caption.addAll(caption);
      return this;
    }

    public Builder caption(Consumer<NotionTextBuilder> consumer) {
      NotionTextBuilder builder = new NotionTextBuilder();
      consumer.accept(builder);
      this.caption.addAll(builder.build());
      return this;
    }

    /**
     * Builds the {@link CodeBlock}.
     *
     * @return a new CodeBlock
     */
    public CodeBlock build() {
      CodeBlock block = new CodeBlock();
      if (richText.isEmpty()) {
        richText.add(NotionText.plainText(""));
      }
      block.getCode().setRichText(new ArrayList<>(richText));
      block.getCode().setLanguage(language);

      if (caption != null && !caption.isEmpty()) {
        block.getCode().setCaption(new ArrayList<>(caption));
      }

      return block;
    }
  }
}
