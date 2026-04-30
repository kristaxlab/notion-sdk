package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.fluent.NotionText;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RichTextProperty extends PageProperty {
  private final String type = PagePropertyType.RICH_TEXT.type();

  private List<RichText> richText = new ArrayList<>();

  public static RichTextProperty of(String text) {
    RichTextProperty property = new RichTextProperty();
    List<RichText> richTexts = NotionText.plainText(text).asList();
    property.setRichText(richTexts);
    return property;
  }

  public static RichTextProperty of(List<RichText> richText) {
    RichTextProperty property = new RichTextProperty();
    property.getRichText().addAll(richText);
    return property;
  }
}
