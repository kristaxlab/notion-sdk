package io.kristixlab.notion.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsonConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);
  private static final JsonConverter CONVERTER = new JsonConverter();
  private final ObjectMapper MAPPER;

  private JsonConverter() {
    MAPPER = new ObjectMapper();
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    MAPPER.registerModule(new JavaTimeModule());
  }

  public static JsonConverter getInstance() {
    return CONVERTER;
  }

  // TODO log unrecognized fields
  public <T> T toObject(String json, Class<T> type) {
    try {
      return MAPPER.readValue(json, type);
    } catch (Exception e) {
      LOGGER.error("Error converting JSON to {}: {}", type.toString(), e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public String toJson(Object object) {
    return toJson(object, false);
  }

  public String toJson(Object object, boolean pretty) {
    try {
      if (pretty) {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
      } else {
        return MAPPER.writeValueAsString(object);
      }
    } catch (JsonProcessingException e) {
      LOGGER.error("Error converting object to JSON: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void toFile(File file, Object object) {
    try {
      MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, object);
    } catch (IOException e) {
      LOGGER.error("Error saving object to file {}: {}", file.getAbsolutePath(), e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public void print(Object object) {
    try {
      String json = toJson(object, true);
      System.out.println(json);
    } catch (RuntimeException e) {
      LOGGER.error("Error printing object as JSON: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public <T> T fromFile(String filePath, Class<T> targetClass) {
    try (InputStream is = JsonConverter.class.getClassLoader().getResourceAsStream(filePath)) {
      if (is == null) {
        throw new IllegalArgumentException("Resource not found: " + filePath);
      }
      return MAPPER.readValue(is, targetClass);
    } catch (IOException e) {
      LOGGER.error("Error loading object from file {}: {}", filePath, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
