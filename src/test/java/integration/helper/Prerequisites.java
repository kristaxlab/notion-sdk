package integration.helper;

import io.kristaxlab.notion.model.common.NotionFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Prerequisites {

  private String externalImageUrl;
  private NotionFile imageUploadedViaUI;
  private String imageUploadedViaUIExpiryTime;
  private String emojiIcon;
  private String pageWithBlocksId;
  private String imageFileUploadId;
  private String testDatabaseId;
  private String userId;
}
