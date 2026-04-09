package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import org.junit.jupiter.api.Test;

class ToDoBlockTest {

  @Test
  void constructor_setsTypeAndInitializesToDo() {
    ToDoBlock block = new ToDoBlock();

    assertEquals("to_do", block.getType());
    assertNotNull(block.getToDo());
  }

  @Test
  void of_setsRichText() {
    ToDoBlock block = ToDoBlock.of("Buy groceries");

    assertEquals("to_do", block.getType());
    assertEquals(1, block.getToDo().getRichText().size());
    assertEquals("Buy groceries", block.getToDo().getRichText().get(0).getPlainText());
    assertNull(block.getToDo().getChecked());
  }

  @Test
  void builder_withText() {
    ToDoBlock block = ToDoBlock.builder().text("Task").build();

    assertEquals("to_do", block.getType());
    assertEquals("Task", block.getToDo().getRichText().get(0).getPlainText());
  }

  @Test
  void builder_withCheckedTrue() {
    ToDoBlock block = ToDoBlock.builder().text("Done task").checked(true).build();

    assertTrue(block.getToDo().getChecked());
  }

  @Test
  void builder_withCheckedFalse() {
    ToDoBlock block = ToDoBlock.builder().text("Pending task").checked(false).build();

    assertFalse(block.getToDo().getChecked());
  }

  @Test
  void builder_withCheckedNoArg() {
    ToDoBlock block = ToDoBlock.builder().text("Done").checked().build();

    assertTrue(block.getToDo().getChecked());
  }

  @Test
  void builder_checkedNotSet_isNull() {
    ToDoBlock block = ToDoBlock.builder().text("No status").build();

    assertNull(block.getToDo().getChecked());
  }

  @Test
  void builder_withColor() {
    ToDoBlock block = ToDoBlock.builder().text("Colored").color(Color.YELLOW).build();

    assertEquals("yellow", block.getToDo().getColor());
  }

  @Test
  void builder_withChildren() {
    ToDoBlock block =
        ToDoBlock.builder().text("Parent task").children(c -> c.todo("Sub-task")).build();

    assertNotNull(block.getToDo().getChildren());
    assertEquals(1, block.getToDo().getChildren().size());
  }

  @Test
  void builder_withAnnotations() {
    ToDoBlock block = ToDoBlock.builder().text("Important").bold().underline().build();

    assertTrue(block.getToDo().getRichText().get(0).getAnnotations().getBold());
    assertTrue(block.getToDo().getRichText().get(0).getAnnotations().getUnderline());
  }

  @Test
  void toDo_checkedGetterSetter() {
    ToDoBlock.ToDo toDo = new ToDoBlock.ToDo();

    assertNull(toDo.getChecked());
    toDo.setChecked(true);
    assertTrue(toDo.getChecked());
    toDo.setChecked(false);
    assertFalse(toDo.getChecked());
  }
}
