package io.kristixlab.notion.api.model.blocks;

/**
 * Fallback block type for block types not yet mapped to a concrete class.
 *
 * <p>Used as the Jackson {@code defaultImpl} during deserialization when the {@code type} field
 * does not match any known block subtype.
 */
public class UnknownBlock extends Block {}
