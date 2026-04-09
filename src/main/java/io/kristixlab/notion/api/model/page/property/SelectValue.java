package io.kristixlab.notion.api.model.page.property;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectValue {

  private String id;

  private String name;

  private String color;

  public static SelectValue ofId(String id) {
    SelectValue selectValue = new SelectValue();
    selectValue.setId(id);
    return selectValue;
  }

  public static SelectValue ofName(String name) {
    SelectValue selectValue = new SelectValue();
    selectValue.setName(name);
    return selectValue;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final List<SelectValue> values = new ArrayList<>();

    private Builder() {}

    /**
     * Sets the {@code id} of the current entry.
     *
     * @param id the option ID
     * @return this builder
     */
    public Builder id(String id) {
      values.add(SelectValue.ofId(id));
      return this;
    }

    /**
     * Sets the {@code name} of the current entry.
     *
     * @param name the option name
     * @return this builder
     */
    public Builder name(String name) {
      values.add(SelectValue.ofName(name));
      return this;
    }

    /**
     * Builds a single {@link SelectValue}.
     *
     * @return a new {@link SelectValue} instance
     * @throws IllegalStateException if more than one entry has been added; use {@link #buildList()}
     *     instead
     */
    public SelectValue build() {
      if (values.isEmpty()) {
        throw new IllegalStateException("No entries have been added to the builder");
      }
      if (values.size() > 1) {
        throw new IllegalStateException(
            "The builder contains more than one entry; use buildList() instead");
      }
      return copy(values.get(0));
    }

    /**
     * Builds the list of all accumulated {@link SelectValue} instances.
     *
     * @return a new list containing all entries
     */
    public List<SelectValue> buildList() {
      List<SelectValue> result = new ArrayList<>(values.size());
      for (SelectValue sv : values) {
        result.add(copy(sv));
      }
      return result;
    }

    private SelectValue copy(SelectValue source) {
      SelectValue sv = new SelectValue();
      sv.setId(source.getId());
      sv.setName(source.getName());
      sv.setColor(source.getColor());
      return sv;
    }
  }
}
