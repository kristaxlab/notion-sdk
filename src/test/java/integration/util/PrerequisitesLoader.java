package integration.util;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.BlockList;

public class PrerequisitesLoader {

  public static Prerequisites load(NotionApiClient client, String prerequisitesPageId) {
    Prerequisites prerequisites = new Prerequisites();

    BlockList pagaContent = client.blocks().retrieveChildren(prerequisitesPageId);
    for (int i = 0; i < pagaContent.getResults().size(); i++) {
      if (matchTitle(pagaContent.getResults().get(i), "Cover")) {
        prerequisites.setCoverUrl(getCover(pagaContent, i + 1));
      } else if (matchTitle(pagaContent.getResults().get(i), "Icon")) {
        prerequisites.setEmojiIcon(getIcon(pagaContent, i + 1));
      }
    }
    if (prerequisites.getEmojiIcon() == null || prerequisites.getCoverUrl() == null) {
      throw new IllegalStateException(
          "Prerequisites page should contain both cover and icon blocks with the right titles");
    }
    return prerequisites;
  }

  private static boolean matchTitle(Block block, String title) {
    return block.getType() != null
        && block.getType().equals("heading_3")
        && block.asHeadingThree().getHeading3().getRichText().get(0).getPlainText().equals(title);
  }

  private static String getCover(BlockList block, int coverBlockIndex) {
    if (block.getResults().size() <= coverBlockIndex) {
      throw new IllegalStateException("Index points to missing block in the prerequisites page");
    }
    Block b = block.getResults().get(coverBlockIndex);
    if (!"image".equals(b.getType())) {
      throw new IllegalStateException(
          "Cover index points to a block with type different than image");
    }
    return b.asImage().getImage().getExternal().getUrl();
  }

  private static String getIcon(BlockList block, int coverBlockIndex) {
    if (block.getResults().size() <= coverBlockIndex) {
      throw new IllegalStateException("Index points to missing block in the prerequisites page");
    }
    Block b = block.getResults().get(coverBlockIndex);
    if (!"paragraph".equals(b.getType())) {
      throw new IllegalStateException(
          "Icon index points to a block with type different than paragraph");
    }
    return b.asParagraph().getParagraph().getRichText().get(0).getPlainText();
  }
}
