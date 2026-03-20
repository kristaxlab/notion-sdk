package integration.util;

import lombok.Data;

@Data
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
