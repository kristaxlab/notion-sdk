package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;

public class PagePropertyListDeserializer extends JsonDeserializer<PagePropertyList> {

  public PagePropertyListDeserializer() {}

  @Override
  public PagePropertyList deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    String nestedType = node.path("property_item").path("type").asText();

    Class<? extends PagePropertyList> targetClass =
        switch (nestedType) {
          case "relation" -> RelationPropertyList.class;
          case "rich_text" -> RichTextPropertyList.class;
          case "title" -> TitlePropertyList.class;
          case "people" -> PeoplePropertyList.class;
          case "rollup" -> RollupPropertyList.class;
          default -> UnknownPropertyList.class;
        };

    ObjectReader listReader = mapper.readerFor(targetClass);
    try (JsonParser treeParser = mapper.treeAsTokens(node)) {
      return listReader.readValue(treeParser);
    }
  }
}
