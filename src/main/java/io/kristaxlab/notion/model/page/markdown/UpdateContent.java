package io.kristaxlab.notion.model.page.markdown;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContent {

  private List<ContentUpdate> contentUpdates;

  private Boolean allowDeletingContent;
}
