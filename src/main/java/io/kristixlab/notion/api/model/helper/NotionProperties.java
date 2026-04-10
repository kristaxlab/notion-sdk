package io.kristixlab.notion.api.model.helper;

import static io.kristixlab.notion.api.model.helper.NotionText.plainText;
import static io.kristixlab.notion.api.model.helper.NotionText.textBuilder;

import io.kristixlab.notion.api.model.common.richtext.RichText;
import io.kristixlab.notion.api.model.page.property.PagePropertyType;
import io.kristixlab.notion.api.model.page.property.TitleProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class NotionProperties {

  public static final String TITLE = PagePropertyType.TITLE.toString();

  public static TitleProperty title(String text) {
    return title(plainText(text));
  }

  public static TitleProperty title(RichText... richTexts) {
    return title(Arrays.asList(richTexts));
  }

  public static TitleProperty title(Consumer<NotionTextBuilder> consumer) {
    NotionTextBuilder builder = textBuilder();
    consumer.accept(builder);
    return title(builder.build());
  }

  public static TitleProperty title(List<RichText> richTexts) {
    TitleProperty property = new TitleProperty();
    property.setTitle(new ArrayList<>(richTexts));
    return property;
  }
}
