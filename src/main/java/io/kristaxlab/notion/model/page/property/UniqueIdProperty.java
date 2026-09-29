package io.kristaxlab.notion.model.page.property;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;

/* readonly */
@Getter
@Setter
public class UniqueIdProperty extends PagePropertyValue {
  private final String type = PropertyType.UNIQUE_ID.type();

  private UniqueIdValue uniqueId;

  @Getter
  @Setter
  public static class UniqueIdValue {
    private BigInteger number;
    private String prefix;
  }
}
