package io.kristixlab.notion.api.model.page;

import io.kristixlab.notion.api.model.common.CoverParams;
import io.kristixlab.notion.api.model.common.IconParams;
import io.kristixlab.notion.api.model.page.property.*;
import io.kristixlab.notion.api.model.page.templates.TemplateParams;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePageParams {

  private Map<String, PageProperty> properties;

  private IconParams icon;

  private CoverParams cover;

  private Boolean inTrash;

  private Boolean isLocked;

  private TemplateParams template;

  private Boolean eraseContent;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final Map<String, PageProperty> properties = new LinkedHashMap<>();
    private IconParams icon;
    private CoverParams cover;
    private Boolean inTrash;
    private Boolean isLocked;

    /** Sets the title property. */
    public Builder title(String text) {
      return property(TitleProperty.NAME, TitleProperty.of(text));
    }

    /**
     * Sets an arbitrary property. Use as an escape hatch for property types not covered by the
     * named convenience methods above.
     */
    public Builder property(String name, PageProperty property) {
      this.properties.put(name, property);
      return this;
    }

    /** Moves the page to the trash. */
    public Builder inTrash(boolean inTrash) {
      this.inTrash = inTrash;
      return this;
    }

    /** Locks or unlocks the page. */
    public Builder isLocked(boolean isLocked) {
      this.isLocked = isLocked;
      return this;
    }

    /** Sets the page icon. */
    public Builder icon(IconParams icon) {
      this.icon = icon;
      return this;
    }

    /** Sets the page cover. */
    public Builder cover(CoverParams cover) {
      this.cover = cover;
      return this;
    }

    public UpdatePageParams build() {
      UpdatePageParams params = new UpdatePageParams();
      params.setProperties(properties.isEmpty() ? null : new LinkedHashMap<>(properties));
      params.setIcon(icon);
      params.setCover(cover);
      params.setInTrash(inTrash);
      params.setIsLocked(isLocked);
      return params;
    }
  }
}
