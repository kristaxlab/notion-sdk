package io.kristixlab.notion.api.model.comments;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Fallback attachment type for unknown or unsupported comment attachment types.
 * Used when the API returns an attachment type that is not yet supported by this SDK.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownCommentAttachment extends CommentAttachment {
    // Unknown attachments store only the basic fields from the base class
}
