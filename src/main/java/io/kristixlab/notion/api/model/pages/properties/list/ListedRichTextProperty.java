package io.kristixlab.notion.api.model.pages.properties.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.kristixlab.notion.api.model.common.RichText;
import io.kristixlab.notion.api.model.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListedRichTextProperty extends ListedPageProperty {

  @JsonProperty("rich_text")
  private RichText richText;

}
