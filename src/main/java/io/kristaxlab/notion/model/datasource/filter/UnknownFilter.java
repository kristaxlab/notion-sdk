package io.kristaxlab.notion.model.datasource.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * Fallback filter for unknown or unsupported filter types. Used when the API returns a filter type
 * that is not yet supported by this SDK.
 */
@Getter
@Setter
public class UnknownFilter extends Filter {}
