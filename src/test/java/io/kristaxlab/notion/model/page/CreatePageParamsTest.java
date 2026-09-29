package io.kristaxlab.notion.model.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.kristaxlab.notion.model.common.Parent;
import io.kristaxlab.notion.model.common.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CreatePageParams")
class CreatePageParamsTest {

  @Test
  @DisplayName("builder with position")
  void builder_withPosition() {
    Position position = Position.pageStart();

    CreatePageParams params =
        CreatePageParams.builder()
            .parent(Parent.pageParent("parent-1"))
            .title("Placed")
            .position(position)
            .build();

    assertSame(position, params.getPosition());
  }

  @Test
  @DisplayName("builder without position leaves position null")
  void builder_withoutPosition_leavesNull() {
    CreatePageParams params =
        CreatePageParams.builder().parent(Parent.pageParent("parent-1")).title("Unset").build();

    assertNull(params.getPosition());
  }

  @Test
  @DisplayName("builder position after block")
  void builder_positionAfterBlock() {
    CreatePageParams params =
        CreatePageParams.builder()
            .parent(Parent.pageParent("parent-1"))
            .title("After")
            .position(Position.afterBlock("block-9"))
            .build();

    assertEquals("after_block", params.getPosition().getType());
    assertEquals("block-9", params.getPosition().getAfterBlock().getId());
  }
}
