package io.kristixlab.notion.api.http.base.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonSerializer implements JsonSerializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonSerializer.class);
  private final ObjectMapper objectMapper;

  public JacksonSerializer() {
    this.objectMapper = defaultMapper();
  }

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

  public static JacksonSerializer withDefaults() {
    return new JacksonSerializer(defaultMapper());
  }

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

  protected ObjectMapper getMapper() {
    return objectMapper;
  }

  protected Logger getLogger() {
    return LOGGER;
  }
}
