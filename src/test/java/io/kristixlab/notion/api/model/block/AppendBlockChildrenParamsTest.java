package io.kristixlab.notion.api.model.block;

import static io.kristixlab.notion.api.model.helper.NotionBlocks.*;
import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Position;
import java.util.List;
import org.junit.jupiter.api.Test;

class AppendBlockChildrenParamsTest {

  // Builder - children(Block)

  @Test
  void builder_childSingleBlock() {
    AppendBlockChildrenParams params =
        AppendBlockChildrenParams.builder().children(paragraph("Single")).build();

    assertEquals(1, params.getChildren().size());
  }

  // Builder - children(List<Block>)

  @Test
  void builder_childrenList() {
    List<Block> blocks = paragraphList("A", "B");

    AppendBlockChildrenParams params = AppendBlockChildrenParams.builder().children(blocks).build();

    assertEquals(2, params.getChildren().size());
  }

  // Builder - position

  @Test
  void builder_withPosition() {
    Position position = Position.afterBlock("some-block-id");

    AppendBlockChildrenParams params =
        AppendBlockChildrenParams.builder()
            .children(paragraph("Inserted"))
            .position(position)
            .build();

    assertNotNull(params.getPosition());
    assertSame(position, params.getPosition());
    assertEquals(1, params.getChildren().size());
  }

  @Test
  void builder_noPosition_isNull() {
    AppendBlockChildrenParams params =
        AppendBlockChildrenParams.builder().children(paragraph("No position")).build();

    assertNull(params.getPosition());
  }

  // Builder - validation

  @Test
  void builder_noChildren_throwsException() {
    AppendBlockChildrenParams.Builder builder = AppendBlockChildrenParams.builder();

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("At least one child"));
  }

  // No-arg constructor

  @Test
  void noArgConstructor_fieldsNull() {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();

    assertNull(params.getChildren());
    assertNull(params.getPosition());
  }

  // Getter/setter

  @Test
  void getterSetter_children() {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();
    List<Block> blocks = List.of(paragraph("Test"));

    params.setChildren(blocks);

    assertSame(blocks, params.getChildren());
  }

  @Test
  void getterSetter_position() {
    AppendBlockChildrenParams params = new AppendBlockChildrenParams();
    Position pos = Position.pageStart();

    params.setPosition(pos);

    assertSame(pos, params.getPosition());
  }
}
