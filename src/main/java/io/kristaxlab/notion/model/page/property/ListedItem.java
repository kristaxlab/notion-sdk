package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public sealed class ListedItem permits ListedRichText, ListedPeople, ListedRelation {

  private String object;

  private String type;

  private String id;
}
