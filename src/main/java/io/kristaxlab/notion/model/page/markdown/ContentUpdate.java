package io.kristaxlab.notion.model.page.markdown;

import lombok.Getter;
import lombok.Setter;

/** Defines a string replacement operation used by Markdown page update helpers. */
@Getter
@Setter
public class ContentUpdate {

  private String oldStr;

  private String newStr;

  private Boolean replaceAllMatches;
}
