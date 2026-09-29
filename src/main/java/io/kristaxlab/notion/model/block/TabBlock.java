package io.kristaxlab.notion.model.block;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion tab block: a container whose strip entries are direct paragraph children.
 *
 * <p>On retrieve the type-named value field is empty ({@code tab: {}}). On create, nest paragraph
 * children under {@code tab.children}: each paragraph is a tab label ({@code rich_text}, optional
 * icon, color); its nested children are that tab's panel content.
 *
 * @see <a href="https://developers.notion.com/reference/block">Notion Block object</a>
 */
@Getter
@Setter
public class TabBlock extends Block {

  private Tab tab;

  /** Creates a tab block initialized with an empty container payload. */
  public TabBlock() {
    setType(BlockType.TAB.getValue());
    tab = new Tab();
  }

  /** The inner content object of a tab block. */
  @Getter
  @Setter
  public static class Tab {

    /**
     * Paragraph children that form the tab strip. Null on retrieve responses; set on create
     * requests.
     */
    private List<ParagraphBlock> children;
  }
}
