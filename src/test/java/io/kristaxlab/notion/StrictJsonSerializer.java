package io.kristaxlab.notion;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class StrictJsonSerializer extends JacksonSerializer {

  public StrictJsonSerializer() {
    super();
    getMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  public void toFile(File file, Object object) {
    try {
      getMapper().writerWithDefaultPrettyPrinter().writeValue(file, object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void print(Object object, PrintStream out) {
    try {
      String json = getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
      out.println(json);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T fromFile(String filePath, Class<T> targetClass) {
    try (InputStream is =
        StrictJsonSerializer.class.getClassLoader().getResourceAsStream(filePath)) {
      if (is == null) {
        throw new IllegalArgumentException("Resource not found: " + filePath);
      }
      return getMapper().readValue(is, targetClass);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
