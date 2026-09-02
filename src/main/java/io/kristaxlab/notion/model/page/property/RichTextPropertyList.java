package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kristaxlab.notion.model.common.richtext.RichText;

/** Typed marker for paginated rich text property retrieve. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RichTextPropertyList extends PagePropertyList<RichTextPropertyItem, RichText> {}
