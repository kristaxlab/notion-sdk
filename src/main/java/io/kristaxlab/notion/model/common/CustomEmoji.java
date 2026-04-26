package io.kristaxlab.notion.model.common;

import lombok.Getter;
import lombok.Setter;

/** Represents a custom emoji asset available in a Notion workspace. */
@Getter
@Setter
public class CustomEmoji {

  private String id;

  private String name;

  private String url;
}
