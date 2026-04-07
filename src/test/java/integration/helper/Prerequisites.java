package integration.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prerequisites {

  private String externalImageUrl;
  private String imageUploadedViaUI;
  private String imageUploadedViaUIExpiryTime;
  private String emojiIcon;
  private String pageWithBlocksId;
  private String imageFileUploadId;
  private String testDatabaseId;
  private String userId;
}
