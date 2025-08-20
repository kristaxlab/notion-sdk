package io.kristixlab.notion.api.model.pages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kristixlab.notion.api.model.common.FileData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CoverDeserializationTest {

  private static Map<String, FileData> covers;

  @BeforeAll
  static void init() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    InputStream is =
            CoverDeserializationTest.class
                    .getClassLoader()
                    .getResourceAsStream("notion-json-examples/cover-examples.json");
    assertNotNull(is, "Test JSON file not found");
    covers = mapper.readValue(is, new TypeReference<Map<String, FileData>>() {});
    assertNotNull(covers);
  }

  @Test
  void testNotionGalleryCover() {
    assertNotNull(covers.get("notionGalleryCover"));
    FileData cover = covers.get("notionGalleryCover");
    assertEquals("external", cover.getType());
    assertNotNull(cover.getExternal());
    assertEquals("https://www.notion.so/images/page-cover/met_william_morris_1877_willow.jpg", cover.getExternal().getUrl());
  }

  @Test
  void testUploadedCover() {
    assertNotNull(covers.get("uploadedCover"));
    FileData cover = covers.get("uploadedCover");
    assertEquals("file", cover.getType());
    assertNotNull(cover.getFile());
    assertEquals("https://prod-files...", cover.getFile().getUrl());
    assertEquals("2025-08-11T15:54:30.316Z", cover.getFile().getExpiryTime());
  }

  @Test
  void testLinkedUrlCover() {
    assertNotNull(covers.get("linkedUrlCover"));
    FileData cover = covers.get("linkedUrlCover");
    assertEquals("external", cover.getType());
    assertNotNull(cover.getExternal());
    assertEquals("https://www.heart.org/-/media/Images/Health-Topics/Congenital-Heart-Defects/50_1683_44a_ASD.jpg?h=551&w=572&sc_lang=en", cover.getExternal().getUrl());
  }

  @Test
  void testUnsplashCover() {
    assertNotNull(covers.get("unsplashCover"));
    FileData cover = covers.get("unsplashCover");
    assertEquals("external", cover.getType());
    assertNotNull(cover.getExternal());
    assertEquals("https://www.heart.org/-/media/Images/Health-Topics/Congenital-Heart-Defects/50_1683_44a_ASD.jpg?h=551&w=572&sc_lang=en", cover.getExternal().getUrl());
  }
}
