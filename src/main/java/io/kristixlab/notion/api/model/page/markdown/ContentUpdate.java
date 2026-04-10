package io.kristixlab.notion.api.model.page.markdown;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentUpdate {

  private String oldStr;

  private String newStr;

  private Boolean replaceAllMatches;

  public ContentUpdate() {}

  ContentUpdate(ContentUpdate source) {
    this.oldStr = source.oldStr;
    this.newStr = source.newStr;
    this.replaceAllMatches = source.replaceAllMatches;
  }

  /**
   * Convenience method for creating a single ContentUpdate instance with replaceAllMatches set to
   * true.
   */
  public static ContentUpdate of(String oldStr, String newStr) {
    return builder().from(oldStr).to(newStr).build();
  }

  public static Builder builder() {
    return new Builder();
  }

  /** Builder for constructing one or more {@link ContentUpdate} instances. */
  public static class Builder {

    private final List<ContentUpdate> updates = new ArrayList<>();

    private Builder() {
      updates.add(createContentUpdate(true));
    }

    public Builder and() {
      ContentUpdate last = getLast();
      if (last.getOldStr() == null) {
        throw new IllegalStateException(
            "The last ContentUpdate entry is missing an oldStr value; call from() before and() to provide this value");
      }
      if (last.getNewStr() == null) {
        throw new IllegalStateException(
            "The last ContentUpdate entry is missing a newStr value; call to() before and() to provide this value");
      }
      updates.add(createContentUpdate(true));
      return this;
    }

    private ContentUpdate createContentUpdate(boolean replaceAllMatches) {
      ContentUpdate cu = new ContentUpdate();
      cu.setReplaceAllMatches(replaceAllMatches);
      return cu;
    }

    /**
     * Adds a new {@link ContentUpdate} entry with {@code replaceAllMatches} set to {@code true}.
     *
     * @param oldStr the text to search for
     * @return this builder
     */
    public Builder from(String oldStr) {
      ContentUpdate last = getLast();
      if (last.getOldStr() != null) {
        throw new IllegalStateException(
            "ContentUpdate entry already has an oldStr value; call and() to start a new entry");
      }

      last.setOldStr(oldStr);
      return this;
    }

    /**
     * Updates the last {@link ContentUpdate} entry with a newStr value.
     *
     * @param newStr the replacement text
     * @return this builder
     */
    public Builder to(String newStr) {
      ContentUpdate last = getLast();
      if (last.getNewStr() != null) {
        throw new IllegalStateException(
            "The last ContentUpdate entry already has a newStr value; call and() to start a new entry");
      }
      last.setNewStr(newStr);
      return this;
    }

    /**
     * Overrides the {@code replaceAllMatches} flag on the most recently added entry.
     *
     * @return this builder
     * @throws IllegalStateException if no entry has been added yet
     */
    public Builder replaceAll() {
      getLast().setReplaceAllMatches(true);
      return this;
    }

    /**
     * Overrides the {@code replaceAllMatches} flag on the most recently added entry.
     *
     * @return this builder
     * @throws IllegalStateException if no entry has been added yet
     */
    public Builder replaceFirst() {
      getLast().setReplaceAllMatches(false);
      return this;
    }

    /**
     * Builds a single {@link ContentUpdate}.
     *
     * @return the single entry
     * @throws IllegalStateException if no entries have been added, or more than one entry exists
     */
    public ContentUpdate build() {
      if (updates.isEmpty()) {
        throw new IllegalStateException("No ContentUpdate entry defined in the builder");
      }
      if (updates.size() > 1) {
        throw new IllegalStateException(
            "The builder contains more than one entry; use buildList() instead");
      }
      return new ContentUpdate(updates.get(0));
    }

    /**
     * Builds the list of all accumulated {@link ContentUpdate} instances.
     *
     * @return a new list containing all entries
     */
    public List<ContentUpdate> buildList() {
      List<ContentUpdate> result = new ArrayList<>(updates.size());
      for (ContentUpdate cu : updates) {
        result.add(new ContentUpdate(cu));
      }
      return result;
    }

    private ContentUpdate getLast() {
      if (updates.isEmpty()) {
        throw new IllegalStateException(
            "No ContentUpdate entry defined in the builder; call oldStr() to add a new update entry");
      }
      return updates.get(updates.size() - 1);
    }
  }
}
