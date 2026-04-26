package io.kristaxlab.notion.model.common.richtext;

import io.kristaxlab.notion.model.common.BlockReference;
import io.kristaxlab.notion.model.common.DateData;
import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

/** Rich-text mention payload for users, pages, databases, dates, and related variants. */
@Getter
@Setter
public class Mention {

  private String type;

  private User user;

  private BlockReference page;

  private BlockReference database;

  private DateData date;

  private CustomEmojiRef customEmoji;

  /**
   * @deprecated outdated (only may return in response for blocks created with older Notion
   *     versions)
   */
  @Deprecated private LinkMention linkMention;

  /*
   * Read only, the link_preview block can only be returned as part of a response.
   * The API does not support creating or appending link_preview blocks.
   * more details: https://developers.notion.com/reference/block#link-preview
   */
  private LinkPreview linkPreview;

  /*
   * @deprecated after March 27, 2023
   * Read only, may return in response, but can not be created/updated with Notion API
   * more details: https://developers.notion.com/reference/block#template and
   * https://developers.notion.com/reference/rich-text#template-mention-type-object
   */
  private TemplateMention templateMention;

  /*
   * @deprecated after March 27, 2023
   * Read only, may return in response, but can not be created/updated with Notion API
   * more details: https://developers.notion.com/reference/block#template and
   * https://developers.notion.com/reference/rich-text#template-mention-type-object
   */
  /** Legacy template mention payload. */
  @Getter
  @Setter
  public static class TemplateMention {

    private String templateMentionDate;

    private String templateMentionUser;
  }

  /** Reference to a custom emoji mention. */
  @Getter
  @Setter
  public static class CustomEmojiRef {

    private String id;
  }

  /*
   * Read only, the link_preview block can only be returned as part of a response.
   * The API does not support creating or appending link_preview blocks.
   * more details: https://developers.notion.com/reference/block#link-preview
   */
  /** Read-only link preview mention payload. */
  @Getter
  @Setter
  public static class LinkPreview {

    private String url;
  }

  /*
   * Not documented but returns for url mention blocks
   */
  /** Legacy URL mention payload. */
  @Getter
  @Setter
  public static class LinkMention {

    private String href;

    private String title;

    private String iconUrl;

    private String description;

    private String linkProvider;

    private String thumbnailUrl;
  }
}
