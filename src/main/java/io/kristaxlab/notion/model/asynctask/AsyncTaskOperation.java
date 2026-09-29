package io.kristaxlab.notion.model.asynctask;

import lombok.Getter;
import lombok.Setter;

/** Identifies which Notion operation produced an async task. */
@Getter
@Setter
public class AsyncTaskOperation {

  private String surface;

  private String name;
}
