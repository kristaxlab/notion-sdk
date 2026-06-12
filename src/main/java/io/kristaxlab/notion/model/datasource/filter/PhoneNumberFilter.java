package io.kristaxlab.notion.model.datasource.filter;

import io.kristixlab.notion.api.model.datasources.filter.condition.TextFilterCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberFilter extends Filter {

  private TextFilterCondition phoneNumber;
}
