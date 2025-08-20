package io.kristixlab.notion.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;

public class BaseTest {

  protected static ObjectMapper MAPPER = new ObjectMapper();
  private static final String TEST_INPUT_DIR = "notion-json-examples/";

  @BeforeAll
  protected static void init() throws IOException {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  }

  protected static <T> T loadFromFile(String filename, Class<T> classType) throws IOException {
    String filePath = getTestInputDir() + filename;
    InputStream is = BaseTest.class.getClassLoader().getResourceAsStream(filePath);
    if (is == null) {
      throw new IllegalArgumentException("File not found " + filePath);
    }
    return MAPPER.readValue(is, classType);
  }

  protected static String getTestInputDir() {
    return TEST_INPUT_DIR;
  }

  protected static ObjectMapper getObjectMapper() {
    return MAPPER;
  }
}
