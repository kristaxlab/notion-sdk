package io.kristixlab.notion.api.model.helper;

import io.kristixlab.notion.api.model.block.*;
import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.common.richtext.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts a Markdown string into a list of Notion {@link Block} objects.
 *
 * <p>Supports the following Markdown elements:
 *
 * <ul>
 *   <li>Headings ({@code #} through {@code ####})
 *   <li>Paragraphs (plain text lines)
 *   <li>Bulleted lists ({@code -} or {@code *} prefixed lines)
 *   <li>Numbered lists ({@code 1.} prefixed lines)
 *   <li>To-do items ({@code - [ ]} and {@code - [x]})
 *   <li>Fenced code blocks (triple backticks with optional language)
 *   <li>Blockquotes ({@code >} prefixed lines)
 *   <li>Horizontal rules ({@code ---}, {@code ***}, {@code ___})
 *   <li>Images ({@code ![alt](url)})
 * </ul>
 *
 * <p>Inline formatting within text content is preserved as Notion rich text annotations:
 *
 * <ul>
 *   <li><strong>Bold</strong> ({@code **text**})
 *   <li><em>Italic</em> ({@code *text*} or {@code _text_})
 *   <li><s>Strikethrough</s> ({@code ~~text~~})
 *   <li><code>Inline code</code> ({@code `text`})
 *   <li>Links ({@code [text](url)})
 * </ul>
 *
 * <p>Usage:
 *
 * <pre>{@code
 * List<Block> blocks = MarkdownToBlocks.of("# Hello\nSome paragraph text");
 * }</pre>
 */
public final class MarkdownToBlocks {

  private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,4})\\s+(.+)$");
  private static final Pattern BULLET_PATTERN = Pattern.compile("^[-*]\\s+(.+)$");
  private static final Pattern NUMBERED_PATTERN = Pattern.compile("^\\d+\\.\\s+(.+)$");
  private static final Pattern TODO_UNCHECKED_PATTERN = Pattern.compile("^-\\s+\\[ ]\\s+(.+)$");
  private static final Pattern TODO_CHECKED_PATTERN =
      Pattern.compile("^-\\s+\\[x]\\s+(.+)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern CODE_FENCE_OPEN_PATTERN = Pattern.compile("^```(.*)$");
  private static final Pattern CODE_FENCE_CLOSE_PATTERN = Pattern.compile("^```\\s*$");
  private static final Pattern BLOCKQUOTE_PATTERN = Pattern.compile("^>\\s?(.*)$");
  private static final Pattern DIVIDER_PATTERN = Pattern.compile("^([-*_])\\1{2,}\\s*$");
  private static final Pattern IMAGE_PATTERN =
      Pattern.compile("^!\\[([^\\]]*)\\]\\(([^)]+)\\)\\s*$");

  private static final Pattern INLINE_CODE_PATTERN = Pattern.compile("`([^`]+)`");
  private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.+?)\\*\\*");
  private static final Pattern ITALIC_ASTERISK_PATTERN =
      Pattern.compile("(?<!\\*)\\*(?!\\*)(.+?)(?<!\\*)\\*(?!\\*)");
  private static final Pattern ITALIC_UNDERSCORE_PATTERN =
      Pattern.compile("(?<![\\w_])_(?!_)(.+?)(?<!_)_(?![\\w_])");
  private static final Pattern STRIKETHROUGH_PATTERN = Pattern.compile("~~(.+?)~~");
  private static final Pattern LINK_PATTERN = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");

  private MarkdownToBlocks() {}

  /**
   * Converts a Markdown string into a list of Notion blocks.
   *
   * @param markdown the Markdown content to convert (must not be {@code null})
   * @return a list of Notion blocks representing the Markdown content
   * @throws IllegalArgumentException if {@code markdown} is {@code null}
   */
  public static List<Block> of(String markdown) {
    if (markdown == null) {
      throw new IllegalArgumentException("Markdown content must not be null");
    }

    List<Block> blocks = new ArrayList<>();
    String[] lines = markdown.split("\n", -1);
    int i = 0;

    while (i < lines.length) {
      String line = lines[i];

      // Blank line — skip
      if (line.trim().isEmpty()) {
        i++;
        continue;
      }

      // Fenced code block
      Matcher codeFenceMatcher = CODE_FENCE_OPEN_PATTERN.matcher(line.trim());
      if (codeFenceMatcher.matches()) {
        String language = codeFenceMatcher.group(1).trim();
        StringBuilder codeContent = new StringBuilder();
        i++;
        while (i < lines.length && !CODE_FENCE_CLOSE_PATTERN.matcher(lines[i].trim()).matches()) {
          if (codeContent.length() > 0) {
            codeContent.append("\n");
          }
          codeContent.append(lines[i]);
          i++;
        }
        // Skip closing fence
        if (i < lines.length) {
          i++;
        }
        CodeBlock.Builder builder = CodeBlock.builder().code(codeContent.toString());
        if (!language.isEmpty()) {
          builder.language(language);
        }
        blocks.add(builder.build());
        continue;
      }

      // Image
      Matcher imageMatcher = IMAGE_PATTERN.matcher(line.trim());
      if (imageMatcher.matches()) {
        String url = imageMatcher.group(2);
        blocks.add(NotionBlocks.image(url));
        i++;
        continue;
      }

      // Divider
      if (DIVIDER_PATTERN.matcher(line.trim()).matches()) {
        blocks.add(new DividerBlock());
        i++;
        continue;
      }

      // Heading
      Matcher headingMatcher = HEADING_PATTERN.matcher(line.trim());
      if (headingMatcher.matches()) {
        int level = headingMatcher.group(1).length();
        String text = headingMatcher.group(2);
        List<RichText> richTexts = parseInlineFormatting(text);
        switch (level) {
          case 1:
            blocks.add(NotionBlocks.heading1(richTexts));
            break;
          case 2:
            blocks.add(NotionBlocks.heading2(richTexts));
            break;
          case 3:
            blocks.add(NotionBlocks.heading3(richTexts));
            break;
          default:
            blocks.add(NotionBlocks.heading4(richTexts));
            break;
        }
        i++;
        continue;
      }

      // To-do items (must be checked before bullet pattern)
      Matcher todoCheckedMatcher = TODO_CHECKED_PATTERN.matcher(line.trim());
      if (todoCheckedMatcher.matches()) {
        String text = todoCheckedMatcher.group(1);
        blocks.add(ToDoBlock.builder().text(parseInlineFormatting(text)).checked(true).build());
        i++;
        continue;
      }

      Matcher todoUncheckedMatcher = TODO_UNCHECKED_PATTERN.matcher(line.trim());
      if (todoUncheckedMatcher.matches()) {
        String text = todoUncheckedMatcher.group(1);
        blocks.add(ToDoBlock.builder().text(parseInlineFormatting(text)).checked(false).build());
        i++;
        continue;
      }

      // Bulleted list
      Matcher bulletMatcher = BULLET_PATTERN.matcher(line.trim());
      if (bulletMatcher.matches()) {
        String text = bulletMatcher.group(1);
        blocks.add(NotionBlocks.bullet(parseInlineFormatting(text)));
        i++;
        continue;
      }

      // Numbered list
      Matcher numberedMatcher = NUMBERED_PATTERN.matcher(line.trim());
      if (numberedMatcher.matches()) {
        String text = numberedMatcher.group(1);
        blocks.add(NotionBlocks.numbered(parseInlineFormatting(text)));
        i++;
        continue;
      }

      // Blockquote
      Matcher quoteMatcher = BLOCKQUOTE_PATTERN.matcher(line.trim());
      if (quoteMatcher.matches()) {
        String text = quoteMatcher.group(1);
        blocks.add(NotionBlocks.quote(parseInlineFormatting(text)));
        i++;
        continue;
      }

      // Default: paragraph
      blocks.add(NotionBlocks.paragraph(parseInlineFormatting(line.trim())));
      i++;
    }

    return blocks;
  }

  /**
   * Parses inline Markdown formatting into a list of Notion {@link RichText} objects.
   *
   * <p>Handles bold ({@code **}), italic ({@code *} or {@code _}), strikethrough ({@code ~~}),
   * inline code ({@code `}), and links ({@code [text](url)}).
   *
   * @param text the text to parse for inline formatting
   * @return a list of rich text segments with appropriate annotations
   */
  static List<RichText> parseInlineFormatting(String text) {
    List<RichText> result = new ArrayList<>();
    if (text == null || text.isEmpty()) {
      result.add(NotionText.plainText(""));
      return result;
    }

    List<InlineToken> tokens = tokenize(text);
    for (InlineToken token : tokens) {
      RichText rt;
      if (token.href != null) {
        rt = createLinkRichText(token.text, token.href);
      } else {
        rt = NotionText.plainText(token.text);
      }
      if (token.bold) {
        rt.bold();
      }
      if (token.italic) {
        rt.italic();
      }
      if (token.strikethrough) {
        rt.strikethrough();
      }
      if (token.code) {
        rt.code();
      }
      result.add(rt);
    }

    if (result.isEmpty()) {
      result.add(NotionText.plainText(""));
    }
    return result;
  }

  /**
   * Tokenizes a text string into inline segments, each with formatting flags.
   *
   * <p>Processing order: inline code first (to prevent nested parsing), then bold, italic,
   * strikethrough, and links.
   */
  private static List<InlineToken> tokenize(String text) {
    List<InlineSegment> segments = new ArrayList<>();
    segments.add(new InlineSegment(text, 0));

    // Process inline code first — content inside backticks is literal
    segments = splitByPattern(segments, INLINE_CODE_PATTERN, Flag.CODE);

    // Process bold before italic to handle ** vs *
    segments = splitByPattern(segments, BOLD_PATTERN, Flag.BOLD);

    // Links before italic to avoid conflicts with [text](url) patterns
    segments = splitByPattern(segments, LINK_PATTERN, Flag.LINK);

    // Italic with asterisks
    segments = splitByPattern(segments, ITALIC_ASTERISK_PATTERN, Flag.ITALIC);

    // Italic with underscores
    segments = splitByPattern(segments, ITALIC_UNDERSCORE_PATTERN, Flag.ITALIC);

    // Strikethrough
    segments = splitByPattern(segments, STRIKETHROUGH_PATTERN, Flag.STRIKETHROUGH);

    List<InlineToken> tokens = new ArrayList<>();
    for (InlineSegment seg : segments) {
      if (!seg.text.isEmpty()) {
        tokens.add(seg.toToken());
      }
    }
    return tokens;
  }

  private static List<InlineSegment> splitByPattern(
      List<InlineSegment> segments, Pattern pattern, Flag flag) {
    List<InlineSegment> result = new ArrayList<>();
    for (InlineSegment seg : segments) {
      if (seg.hasFlag(Flag.CODE) && flag != Flag.CODE) {
        // Don't process formatting inside code spans
        result.add(seg);
        continue;
      }
      Matcher m = pattern.matcher(seg.text);
      int lastEnd = 0;
      while (m.find()) {
        // Add text before the match
        if (m.start() > lastEnd) {
          result.add(seg.derive(seg.text.substring(lastEnd, m.start())));
        }
        // Add the matched content with the flag
        if (flag == Flag.LINK) {
          String linkText = m.group(1);
          String linkUrl = m.group(2);
          InlineSegment linkSeg = seg.derive(linkText);
          linkSeg.addFlag(flag);
          linkSeg.href = linkUrl;
          result.add(linkSeg);
        } else {
          InlineSegment matchSeg = seg.derive(m.group(1));
          matchSeg.addFlag(flag);
          result.add(matchSeg);
        }
        lastEnd = m.end();
      }
      // Add remaining text after last match
      if (lastEnd < seg.text.length()) {
        result.add(seg.derive(seg.text.substring(lastEnd)));
      } else if (lastEnd == 0) {
        result.add(seg);
      }
    }
    return result;
  }

  private static RichText createLinkRichText(String displayText, String url) {
    RichText rt = new RichText();
    rt.setPlainText(displayText);
    rt.setHref(url);
    rt.setType("text");

    Text textObj = new Text();
    textObj.setContent(displayText);
    Text.Link link = new Text.Link();
    link.setUrl(url);
    textObj.setLink(link);
    rt.setText(textObj);

    return rt;
  }

  private enum Flag {
    BOLD,
    ITALIC,
    CODE,
    STRIKETHROUGH,
    LINK
  }

  private static class InlineSegment {
    String text;
    int flags;
    String href;

    InlineSegment(String text, int flags) {
      this.text = text;
      this.flags = flags;
    }

    boolean hasFlag(Flag flag) {
      return (flags & (1 << flag.ordinal())) != 0;
    }

    void addFlag(Flag flag) {
      flags |= (1 << flag.ordinal());
    }

    InlineSegment derive(String newText) {
      InlineSegment seg = new InlineSegment(newText, this.flags);
      seg.href = this.href;
      return seg;
    }

    InlineToken toToken() {
      InlineToken token = new InlineToken();
      token.text = this.text;
      token.bold = hasFlag(Flag.BOLD);
      token.italic = hasFlag(Flag.ITALIC);
      token.code = hasFlag(Flag.CODE);
      token.strikethrough = hasFlag(Flag.STRIKETHROUGH);
      token.href = this.href;
      return token;
    }
  }

  private static class InlineToken {
    String text;
    boolean bold;
    boolean italic;
    boolean code;
    boolean strikethrough;
    String href;
  }
}
