package io.kristixlab.notion.api.model.datasources.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Fallback filter for unknown or unsupported filter types. Used when the API returns a filter type
 * that is not yet supported by this SDK.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownFilter extends Filter {}
