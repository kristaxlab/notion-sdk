package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;

class BaseBlockDeserializationTest {
  protected static Blocks response;

  @BeforeAll
  static void init() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    InputStream is =
        BaseBlockDeserializationTest.class
            .getClassLoader()
            .getResourceAsStream("notion-json-examples/blocks/blocks-retrieve-children-rs.json");
    assertNotNull(is, "Test JSON file not found");
    response = mapper.readValue(is, Blocks.class);
    assertNotNull(response);
    assertNotNull(response.getResults());
    assertFalse(response.getResults().isEmpty());
  }

  protected Block findBlockById(String id) {
    return response.getResults().stream()
        .filter(b -> id.equals(b.getId()))
        .findFirst()
        .orElse(null);
  }

  protected boolean assertParent(Block block, String expectedPageId) {
    assertNotNull(block.getParent());
    assertEquals("page_id", block.getParent().getType());
    assertEquals(expectedPageId, block.getParent().getPageId());
    return true;
  }
}
