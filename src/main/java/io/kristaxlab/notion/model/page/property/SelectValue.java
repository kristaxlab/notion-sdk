package io.kristaxlab.notion.model.page.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectValue {

  private String id;

  private String name;

  private String color;

  public static SelectValue of(String name) {
    return of(null, name);
  }

  public static SelectValue of(String id, String name) {
    SelectValue selectValue = new SelectValue();
    selectValue.setId(id);
    selectValue.setName(name);
    return selectValue;
  }
}
