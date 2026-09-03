package io.kristaxlab.notion.model.page.property;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** Retrieved property list of a {@code rich_text} property. */
@JsonDeserialize(using = JsonDeserializer.None.class)
public final class RichTextPropertyList
    extends PagePropertyList<RichTextPropertyItem, ListedRichText> {}
