package integration.util;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.blocks.BlockList;
import io.kristixlab.notion.api.model.files.FileUpload;
import io.kristixlab.notion.api.model.files.FileUploadCreateParams;
import io.kristixlab.notion.api.model.files.FileUploadList;
import io.kristixlab.notion.api.model.users.User;

public class PrerequisitesLoader {

  public static Prerequisites load(NotionApiClient client, String prerequisitesPageId) {
    Prerequisites prerequisites = new Prerequisites();

    BlockList pagaContent = client.blocks().retrieveChildren(prerequisitesPageId);
    for (int i = 0; i < pagaContent.getResults().size(); i++) {
      if (matchHeading(pagaContent.getResults().get(i), "Cover")) {
        prerequisites.setExternalImageUrl(getExternalUrl(pagaContent, i + 1));
      } else if (matchHeading(pagaContent.getResults().get(i), "Icon")) {
        prerequisites.setEmojiIcon(getIcon(pagaContent, i + 1));
      } else if (matchHeading(pagaContent.getResults().get(i), "Image uploaded via UI")) {
        prerequisites.setImageUploadedViaUI(getImageUploadedViaUI(pagaContent, i + 1));
        prerequisites.setImageUploadedViaUIExpiryTime(
            getImageUploadedViaUIExpiryTime(pagaContent, i + 1));
      } else if ("child_database".equals(pagaContent.getResults().get(i).getType())) {
        prerequisites.setTestDatabaseId(pagaContent.getResults().get(i).asChildDatabase().getId());
      }
    }

    loadFileUploadId(
        client,
        prerequisites,
        prerequisitesPageId,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/PNG_transparency_demonstration_1.png/640px-PNG_transparency_demonstration_1.png");

    if (prerequisites.getEmojiIcon() == null || prerequisites.getExternalImageUrl() == null) {
      throw new IllegalStateException(
          "Prerequisites page should contain both cover and icon blocks with the right titles");
    }

    String userId =
        client.users().listUsers().getResults().stream()
            .filter(u -> u.getPerson() != null)
            .findFirst()
            .orElse(new User())
            .getId();
    prerequisites.setUserId(userId);
    return prerequisites;
  }

  private static void loadFileUploadId(
      NotionApiClient client,
      Prerequisites prerequisites,
      String prerequisitesPageId,
      String backupImageUrl) {
    FileUploadList rs = client.fileUploads().listFileUploads("uploaded");
    if (rs.getResults().isEmpty()) {
      // TODO check if it works and update the url
      FileUploadCreateParams request = new FileUploadCreateParams();
      request.setMode("external");
      request.setExternalUrl(backupImageUrl);
      FileUpload response = client.fileUploads().createFileUpload(request);
      prerequisites.setImageFileUploadId(response.getId());
    } else {
      for (FileUpload fileUpload : rs.getResults()) {
        if (fileUpload.getContentType().contains("image")) {
          prerequisites.setImageFileUploadId(fileUpload.getId());
          break;
        }
      }
    }
  }

  private static boolean matchHeading(Block block, String title) {
    return block.getType() != null
        && block.getType().equals("heading_3")
        && block.asHeadingThree().getHeading3().getRichText().get(0).getPlainText().equals(title);
  }

  private static String getExternalUrl(BlockList block, int blockIndex) {
    if (block.getResults().size() <= blockIndex) {
      throw new IllegalStateException("Index points to missing block in the prerequisites page");
    }
    Block b = block.getResults().get(blockIndex);
    if (!"image".equals(b.getType())) {
      throw new IllegalStateException("Index points to a block with type different than image");
    }
    return b.asImage().getImage().getExternal().getUrl();
  }

  private static String getImageUploadedViaUI(BlockList block, int blockIndex) {
    if (block.getResults().size() <= blockIndex) {
      throw new IllegalStateException("Index points to missing block in the prerequisites page");
    }
    Block b = block.getResults().get(blockIndex);
    if (!"image".equals(b.getType())) {
      throw new IllegalStateException("Index points to a block with type different than image");
    }
    return b.asImage().getImage().getFile().getUrl();
  }

  private static String getImageUploadedViaUIExpiryTime(BlockList block, int blockIndex) {
    if (block.getResults().size() <= blockIndex) {
      throw new IllegalStateException("Index points to missing block in the prerequisites page");
    }
    Block b = block.getResults().get(blockIndex);
    if (!"image".equals(b.getType())) {
      throw new IllegalStateException("Index points to a block with type different than image");
    }
    return b.asImage().getImage().getFile().getExpiryTime();
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
