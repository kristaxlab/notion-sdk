package io.kristixlab.notion.api.model.blocks;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BlockRetrieveDeserializationTest {
  protected static Block response;

  @BeforeAll
  static void init() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    InputStream is =
        BlockRetrieveDeserializationTest.class
            .getClassLoader()
            .getResourceAsStream("notion-json-examples/blocks/blocks-retrieve-rs.json");
    assertNotNull(is, "Test JSON file not found");
    response = mapper.readValue(is, Block.class);
  }

  @Test
  void test() {
    assertNotNull(response);
    assertEquals("block", response.getObject());
    assertEquals("paragraph", response.getType());
    ParagraphBlock p = response.asParagraph();
    assertNotNull(p.getParagraph());
  }
}
