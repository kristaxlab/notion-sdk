package io.kristixlab.notion.api.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotionIcons {

  public static final String PIZZA = "pizza";
  public static final String MEETING = "meeting";

  public static NotionIcon meeting() {
    return baseIcon(MEETING);
  }

  public static NotionIcon meeting(String color) {
    NotionIcon icon = meeting();
    icon.setColor(color);
    return icon;
  }

  public static NotionIcon pizza() {
    return baseIcon(PIZZA);
  }

  public static NotionIcon pizza(String color) {
    NotionIcon icon = pizza();
    icon.setColor(color);
    return icon;
  }

  private static NotionIcon baseIcon(String name) {
    NotionIcon icon = new NotionIcon();
    icon.setName(name);
    return icon;
  }
}
