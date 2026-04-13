package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.common.DateData;
import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

/** readonly */
@Getter
@Setter
public class VerificationProperty extends PageProperty {
  private final String type = PagePropertyType.VERIFICATION.type();

  private VerificationValue verification;

  @Getter
  @Setter
  public static class VerificationValue {
    private String state;

    private User verifiedBy;

    private DateData date;
  }
}
