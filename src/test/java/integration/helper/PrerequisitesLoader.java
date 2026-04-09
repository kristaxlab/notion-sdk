package integration.helper;

import io.kristixlab.notion.api.NotionClient;
import io.kristixlab.notion.api.model.block.Block;
import io.kristixlab.notion.api.model.block.BlockList;
import io.kristixlab.notion.api.model.file.FileUpload;
import io.kristixlab.notion.api.model.file.FileUploadCreateParams;
import io.kristixlab.notion.api.model.file.FileUploadList;
import io.kristixlab.notion.api.model.file.FileUploadSendParams;
import io.kristixlab.notion.api.model.user.User;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Loads integration-test prerequisites from a well-known Notion page.
 *
 * <p>The prerequisites page is expected to contain heading_3 blocks labelled "Cover", "Icon", and
 * "Image uploaded via UI", each immediately followed by the corresponding content block. A
 * child_database block provides the test database ID.
 */
public class PrerequisitesLoader {

  /**
   * Loads all prerequisites required by integration tests from the given Notion page.
   *
   * @param client the authenticated Notion client
   * @param prerequisitesPageId the ID of the prerequisites page
   * @return fully populated {@link Prerequisites}
   * @throws IllegalStateException if required blocks are missing or malformed
   */
  public static Prerequisites load(NotionClient client, String prerequisitesPageId) {
    Prerequisites prerequisites = new Prerequisites();
    prerequisites.setUserId(resolveUserId(client));

    BlockList pageContent = client.blocks().retrieveChildren(prerequisitesPageId);
    List<Block> blocks = pageContent.getResults();

    prerequisites.setImageFileUploadId(resolveImageFileUploadId(client, blocks));
    parsePageBlocks(prerequisites, blocks);

    if (prerequisites.getEmojiIcon() == null || prerequisites.getExternalImageUrl() == null) {
      throw new IllegalStateException(
          "Prerequisites page must contain both 'Cover' and 'Icon' heading_3 sections");
    }
    return prerequisites;
  }

  /**
   * Iterates over the prerequisites page blocks and populates the corresponding fields.
   *
   * <p>Each heading_3 block acts as a section header; the block immediately following it provides
   * the section value.
   */
  private static void parsePageBlocks(Prerequisites prerequisites, List<Block> blocks) {
    for (int i = 0; i < blocks.size(); i++) {
      Block block = blocks.get(i);

      if (matchHeading(block, "Cover")) {
        Block imageBlock = requireBlockAt(blocks, i + 1, "image");
        prerequisites.setExternalImageUrl(imageBlock.asImage().getImage().getExternal().getUrl());

      } else if (matchHeading(block, "Icon")) {
        Block paragraphBlock = requireBlockAt(blocks, i + 1, "paragraph");
        prerequisites.setEmojiIcon(
            paragraphBlock.asParagraph().getParagraph().getRichText().get(0).getPlainText());

      } else if (matchHeading(block, "Image uploaded via UI")) {
        Block imageBlock = requireBlockAt(blocks, i + 1, "image");
        prerequisites.setImageUploadedViaUI(imageBlock.asImage().getImage().getFile().getUrl());
        prerequisites.setImageUploadedViaUIExpiryTime(
            imageBlock.asImage().getImage().getFile().getExpiryTime());

      } else if ("child_database".equals(block.getType())) {
        prerequisites.setTestDatabaseId(block.asChildDatabase().getId());
      }
    }
  }

  /**
   * Returns the block at {@code index}, validating both bounds and the expected type.
   *
   * @param blocks the block list
   * @param index the index of the expected content block
   * @param expectedType the Notion block type (e.g. "image", "paragraph")
   * @return the validated block
   * @throws IllegalStateException if the index is out of bounds or the type does not match
   */
  private static Block requireBlockAt(List<Block> blocks, int index, String expectedType) {
    if (index >= blocks.size()) {
      throw new IllegalStateException(
          "Expected a '"
              + expectedType
              + "' block at index "
              + index
              + " but the prerequisites page has only "
              + blocks.size()
              + " blocks");
    }
    Block block = blocks.get(index);
    if (!expectedType.equals(block.getType())) {
      throw new IllegalStateException(
          "Expected block type '"
              + expectedType
              + "' at index "
              + index
              + " but found '"
              + block.getType()
              + "'");
    }
    return block;
  }

  private static boolean matchHeading(Block block, String title) {
    return "heading_3".equals(block.getType())
        && block.asHeadingThree().getHeading3().getRichText().get(0).getPlainText().equals(title);
  }

  /**
   * Finds an existing uploaded image file or creates one from a fallback external URL.
   *
   * @param client the authenticated Notion client
   * @param prerequisitePageBlocks the blocks of the prerequisites page, used to populate fallback
   * @return the file upload ID
   */
  private static String resolveImageFileUploadId(
      NotionClient client, List<Block> prerequisitePageBlocks) {
    FileUploadList uploads = client.fileUploads().listFileUploads("uploaded");
    return uploads.getResults().stream()
        .filter(f -> f.getContentType() != null && f.getContentType().contains("image"))
        .findFirst()
        .map(FileUpload::getId)
        .orElseGet(() -> createFallbackImageUpload(client, prerequisitePageBlocks));
  }

  private static String createFallbackImageUpload(
      NotionClient client, List<Block> prerequisitePageBlocks) {
    String headingName = "Fallback for file upload";

    for (int i = 0; i < prerequisitePageBlocks.size(); i++) {
      if (matchHeading(prerequisitePageBlocks.get(i), headingName)) {
        Block imageBlock = requireBlockAt(prerequisitePageBlocks, i + 1, "image");
        String url = imageBlock.asImage().getImage().getFile().getUrl();
        return uploadImageFromUrl(client, url, "test-image.jpg");
      }
    }

    throw new IllegalStateException(
        "Section '" + headingName + "' or fallback image is missing in the prerequisites page");
  }

  /**
   * Downloads an image from {@code url} into memory and uploads it to Notion as a single-part file
   * upload.
   *
   * <p>The content type is read from the HTTP response; {@code image/jpeg} is used as a fallback
   * when the server does not return one.
   *
   * @param client the authenticated Notion client
   * @param url the publicly accessible URL of the image to download
   * @param filename the filename to assign to the uploaded file
   * @return the ID of the completed file upload
   * @throws IllegalStateException if the download or upload fails
   */
  private static String uploadImageFromUrl(NotionClient client, String url, String filename) {
    HttpURLConnection connection;
    try {
      connection = (HttpURLConnection) new URL(url).openConnection();
      connection.connect();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to connect to image URL: " + url, e);
    }

    try {
      String contentType = connection.getContentType();
      if (contentType == null) {
        contentType = "image/jpeg";
      }
      byte[] imageBytes;
      try (InputStream in = connection.getInputStream()) {
        imageBytes = in.readAllBytes();
      }

      FileUpload created = client.fileUploads().create(FileUploadCreateParams.singlePart(filename));
      client
          .fileUploads()
          .upload(created.getId(), FileUploadSendParams.of(imageBytes, contentType));
      return created.getId();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to download image from: " + url, e);
    } finally {
      connection.disconnect();
    }
  }

  /**
   * Returns the ID of the first person-type user visible to the integration.
   *
   * @param client the authenticated Notion client
   * @return the user ID, or {@code null} if no person user is found
   */
  private static String resolveUserId(NotionClient client) {
    return client.users().listUsers().getResults().stream()
        .filter(u -> u.getPerson() != null)
        .findFirst()
        .orElse(new User())
        .getId();
  }
}
