package io.kristixlab.notion.api.model.builders.blocks;

import io.kristixlab.notion.api.model.common.RichText;

import java.util.List;

public class RichTextListBuilder {
  List<RichText> richTexts;

  public RichTextListBuilder() {
    this.richTexts = new java.util.ArrayList<>();
  }

  public List<RichText> build() {
    return richTexts;
  }

}
