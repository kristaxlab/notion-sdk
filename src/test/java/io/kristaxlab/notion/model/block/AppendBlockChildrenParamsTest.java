package io.kristaxlab.notion.model.block;

import static io.kristaxlab.notion.fluent.NotionBlocks.*;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.model.common.Position;
import java.util.ArrayList;
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

  @Test
  void builder_childrenVarargs() {
    Block a = paragraph("A");
    Block b = paragraph("B");

    AppendBlockChildrenParams params = AppendBlockChildrenParams.builder().children(a, b).build();

    assertEquals(2, params.getChildren().size());
    assertSame(a, params.getChildren().get(0));
    assertSame(b, params.getChildren().get(1));
  }

  @Test
  void builder_childrenMixedOverloads_preservesOrder() {
    Block a = paragraph("A");
    Block b = paragraph("B");
    Block c = paragraph("C");
    Block d = paragraph("D");

    AppendBlockChildrenParams params =
        AppendBlockChildrenParams.builder().children(a).children(List.of(b, c)).children(d).build();

    assertEquals(List.of(a, b, c, d), params.getChildren());
  }

  @Test
  void builder_childrenList_makesDefensiveCopyFromInputList() {
    List<Block> input = new ArrayList<>(List.of(paragraph("A"), paragraph("B")));

    AppendBlockChildrenParams params = AppendBlockChildrenParams.builder().children(input).build();
    input.clear();

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

  @Test
  void builder_emptyVarargs_throwsException() {
    AppendBlockChildrenParams.Builder builder = AppendBlockChildrenParams.builder().children();

    IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
    assertTrue(ex.getMessage().contains("At least one child"));
  }

  @Test
  void builder_buildCalledTwice_returnsDifferentParamsAndChildrenLists() {
    AppendBlockChildrenParams.Builder builder =
        AppendBlockChildrenParams.builder().children(paragraph("A"));

    AppendBlockChildrenParams first = builder.build();
    AppendBlockChildrenParams second = builder.build();

    assertNotSame(first, second);
    assertNotSame(first.getChildren(), second.getChildren());
    assertEquals(first.getChildren(), second.getChildren());
  }

  @Test
  void builder_mutatingBuiltChildren_doesNotAffectSubsequentBuild() {
    AppendBlockChildrenParams.Builder builder =
        AppendBlockChildrenParams.builder().children(paragraph("A"));

    AppendBlockChildrenParams first = builder.build();
    first.getChildren().add(paragraph("B"));

    AppendBlockChildrenParams second = builder.build();

    assertEquals(2, first.getChildren().size());
    assertEquals(1, second.getChildren().size());
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
