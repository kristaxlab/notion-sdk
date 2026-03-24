package io.kristixlab.notion.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);
  private static final JsonConverter CONVERTER = new JsonConverter();
  private final ObjectMapper strictMappe;
  private final ObjectMapper regularMapper;

  private JsonConverter() {
    strictMappe = configureMapper(true);
    regularMapper = configureMapper(false);
  }

  private ObjectMapper configureMapper(boolean failOnUnknown) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknown);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public static JsonConverter getInstance() {
    return CONVERTER;
  }

  // TODO log unrecognized fields
  public <T> T toObject(String json, Class<T> type) {
    return toObject(json, type, true);
  }

  public <T> T toObject(String json, Class<T> type, boolean strict) {
    ObjectMapper mapper = strict ? strictMappe : regularMapper;
    try {
      return mapper.readValue(json, type);
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
        return strictMappe.writerWithDefaultPrettyPrinter().writeValueAsString(object);
      } else {
        return strictMappe.writeValueAsString(object);
      }
    } catch (JsonProcessingException e) {
      LOGGER.error("Error converting object to JSON: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Parses {@code json} into a plain Java structure ({@code Map}, {@code List}, or boxed primitive)
   * so it can be embedded as a real JSON value when re-serialized, rather than as a quoted string.
   * Falls back to returning the raw string when {@code json} is not valid JSON.
   *
   * <p>Returns {@code null} when {@code json} is {@code null} or blank.
   */
  public Object parseJson(String json) {
    if (json == null || json.isBlank()) {
      return null;
    }
    try {
      return regularMapper.readValue(json, Object.class);
    } catch (JsonProcessingException e) {
      return json;
    }
  }

  public void toFile(File file, Object object) {
    try {
      strictMappe.writerWithDefaultPrettyPrinter().writeValue(file, object);
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
      return strictMappe.readValue(is, targetClass);
    } catch (IOException e) {
      LOGGER.error("Error loading object from file {}: {}", filePath, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
