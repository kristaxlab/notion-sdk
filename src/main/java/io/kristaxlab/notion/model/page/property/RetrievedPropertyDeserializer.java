package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class RetrievedPropertyDeserializer extends JsonDeserializer<RetrievedProperty> {

  public RetrievedPropertyDeserializer() {}

  @Override
  public RetrievedProperty deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    boolean isList = node.has("results") || node.isArray();

    if (isList) {
      return mapper.treeToValue(node, PagePropertyList.class);
    } else {
      return mapper.treeToValue(node, PageProperty.class);
    }
  }
}
