package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmbedBlockTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();
  private static final String FILE_UPLOAD_ID = "43833259-72ae-404e-8441-b6577f3159b4";

  @Test
  @DisplayName("constructor sets type and initializes embed")
  void constructor_setsTypeAndInitializesEmbed() {
    EmbedBlock block = new EmbedBlock();

    assertEquals("embed", block.getType());
    assertNotNull(block.getEmbed());
  }

  @Test
  @DisplayName("builder with url")
  void builder_withUrl() {
    EmbedBlock block = EmbedBlock.builder().url("https://maps.google.com").build();

    assertEquals("https://maps.google.com", block.getEmbed().getUrl());
    assertNull(block.getEmbed().getType());
    assertNull(block.getEmbed().getFileUpload());
  }

  @Test
  @DisplayName("builder with url and caption string")
  void builder_withUrlAndCaptionString() {
    EmbedBlock block = EmbedBlock.builder().url("https://youtube.com").caption("Video").build();

    assertEquals("https://youtube.com", block.getEmbed().getUrl());
    assertEquals(1, block.getEmbed().getCaption().size());
    assertEquals("Video", block.getEmbed().getCaption().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder no caption set caption is null")
  void builder_noCaptionSet_captionIsNull() {
    EmbedBlock block = EmbedBlock.builder().url("https://example.com").build();

    assertNull(block.getEmbed().getCaption());
  }

  @Test
  @DisplayName("builder with file upload sets type and file upload id")
  void builder_withFileUpload() {
    EmbedBlock block = EmbedBlock.builder().fileUpload(FILE_UPLOAD_ID).build();

    assertEquals("file_upload", block.getEmbed().getType());
    assertEquals(FILE_UPLOAD_ID, block.getEmbed().getFileUpload().getId());
    assertNull(block.getEmbed().getUrl());
  }

  @Test
  @DisplayName("builder with file upload and caption")
  void builder_withFileUploadAndCaption() {
    EmbedBlock block =
        EmbedBlock.builder().fileUpload(FILE_UPLOAD_ID).caption("Interactive chart").build();

    assertEquals("file_upload", block.getEmbed().getType());
    assertEquals(FILE_UPLOAD_ID, block.getEmbed().getFileUpload().getId());
    assertEquals("Interactive chart", block.getEmbed().getCaption().get(0).getPlainText());
  }

  @Test
  @DisplayName("builder rejects both url and file upload")
  void builder_rejectsBothUrlAndFileUpload() {
    EmbedBlock.Builder builder =
        EmbedBlock.builder().url("https://example.com").fileUpload(FILE_UPLOAD_ID);

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("cannot both be set"));
  }

  @Test
  @DisplayName("builder rejects neither url nor file upload")
  void builder_rejectsNeitherUrlNorFileUpload() {
    EmbedBlock.Builder builder = EmbedBlock.builder().caption("orphan caption");

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("Either url or fileUpload"));
  }

  @Test
  @DisplayName("serialize url write shape has url only under embed")
  void serialize_urlWriteShape() {
    EmbedBlock block =
        EmbedBlock.builder().url("https://companywebsite.com").caption("Site").build();

    String json = JSON.toJson(block);
    JsonNode root = JSON.toObject(json, JsonNode.class);
    JsonNode embed = root.get("embed");

    assertEquals("embed", root.get("type").asText());
    assertEquals("https://companywebsite.com", embed.get("url").asText());
    assertFalse(embed.has("type"));
    assertFalse(embed.has("file_upload"));
    assertEquals("Site", embed.get("caption").get(0).get("plain_text").asText());
  }

  @Test
  @DisplayName("serialize file_upload write shape has type and file_upload")
  void serialize_fileUploadWriteShape() {
    EmbedBlock block =
        EmbedBlock.builder().fileUpload(FILE_UPLOAD_ID).caption("HTML widget").build();

    String json = JSON.toJson(block);
    JsonNode root = JSON.toObject(json, JsonNode.class);
    JsonNode embed = root.get("embed");

    assertEquals("embed", root.get("type").asText());
    assertEquals("file_upload", embed.get("type").asText());
    assertEquals(FILE_UPLOAD_ID, embed.get("file_upload").get("id").asText());
    assertFalse(embed.has("url"));
    assertEquals("HTML widget", embed.get("caption").get(0).get("plain_text").asText());
  }

  @Test
  @DisplayName("deserialize url-only response yields EmbedBlock with url and caption")
  void deserialize_urlOnlyResponse() {
    String json =
        """
        {
          "object": "block",
          "id": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
          "type": "embed",
          "has_children": false,
          "embed": {
            "url": "https://maps.app.goo.gl/Sr6odhLUhAEYaH4W7",
            "caption": [
              {
                "type": "text",
                "text": { "content": "stress relief ideas", "link": null },
                "annotations": {
                  "bold": false,
                  "italic": false,
                  "strikethrough": false,
                  "underline": false,
                  "code": false,
                  "color": "default"
                },
                "plain_text": "stress relief ideas",
                "href": null
              }
            ]
          }
        }
        """;

    Block block = JSON.toObject(json, Block.class);

    assertInstanceOf(EmbedBlock.class, block);
    EmbedBlock embed = block.asEmbed();
    assertEquals("https://maps.app.goo.gl/Sr6odhLUhAEYaH4W7", embed.getEmbed().getUrl());
    assertNull(embed.getEmbed().getType());
    assertNull(embed.getEmbed().getFileUpload());
    assertEquals("stress relief ideas", embed.getEmbed().getCaption().get(0).getPlainText());
  }
}
