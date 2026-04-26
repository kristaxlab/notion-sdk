package io.kristaxlab.notion.http.base.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Jackson-based implementation of {@link JsonSerializer}. */
public class JacksonSerializer implements JsonSerializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonSerializer.class);
  private final ObjectMapper objectMapper;

  /** Creates a serializer with default mapper configuration. */
  public JacksonSerializer() {
    this.objectMapper = defaultMapper();
  }

  /**
   * Creates a serializer with a custom mapper.
   *
   * @param objectMapper object mapper to use
   */
  public JacksonSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> T toObject(String json, Class<T> type) {
    try {
      return getMapper().readValue(json, type);
    } catch (Exception e) {
      getLogger().error("Error converting JSON to {}: {}", type.toString(), e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toJson(Object object) {
    try {
      return getMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      getLogger().error("Error converting object to JSON: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a serializer with default mapper configuration.
   *
   * @return serializer with defaults
   */
  public static JacksonSerializer withDefaults() {
    return new JacksonSerializer(defaultMapper());
  }

  /**
   * Creates a serializer that pretty-prints JSON output.
   *
   * @return serializer configured with indentation
   */
  public static JacksonSerializer pretty() {
    return new JacksonSerializer(prettyMapper());
  }

  private static ObjectMapper prettyMapper() {
    ObjectMapper mapper = defaultMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    return mapper;
  }

  private static ObjectMapper defaultMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  /**
   * Returns the mapper used for serialization/deserialization.
   *
   * @return object mapper
   */
  protected ObjectMapper getMapper() {
    return objectMapper;
  }

  /**
   * Returns the logger used by this serializer.
   *
   * @return logger
   */
  protected Logger getLogger() {
    return LOGGER;
  }
}
