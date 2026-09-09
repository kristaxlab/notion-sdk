package io.kristaxlab.notion.model.page.property;

import io.kristaxlab.notion.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ListedPeople extends ListedItem {

  private User people;
}
