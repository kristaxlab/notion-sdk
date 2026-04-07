package io.kristixlab.notion.api.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.http.base.client.ApiClientStub;
import io.kristixlab.notion.api.model.blocks.AppendBlockChildrenParams;
import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.common.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BlocksEndpointImplTest {

  private ApiClientStub client;
  private BlocksEndpointImpl endpoint;

  @BeforeEach
  void setUp() {
    client = new ApiClientStub();
    endpoint = new BlocksEndpointImpl(client);
  }

  @Test
  void appendChildren_withList_buildsPatchRequest() {
    List<Block> children = List.of(new Block(), new Block());

    endpoint.appendChildren("block-id-42", children);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

    AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
    assertEquals(children, body.getChildren());
    assertNull(body.getPosition());
  }

  @Test
  void appendChildren_withListAndPosition_buildsPatchRequest() {
    List<Block> children = List.of(new Block(), new Block());
    Position position = Position.afterBlock("after-block-id");

    endpoint.appendChildren("block-id-42", children, position);

    assertEquals("PATCH", client.getLastMethod());
    assertEquals("/blocks/{block_id}/children", client.getLastUrlInfo().getUrl());
    assertEquals("block-id-42", client.getLastUrlInfo().getPathParams().get("block_id"));

    AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
    assertEquals(children, body.getChildren());
    assertEquals(position, body.getPosition());
  }

  @Test
  void appendChildren_withSingleChildAndPosition_buildsPatchRequest() {
    Block child = new Block();
    Position position = Position.pageEnd();

    endpoint.appendChildren("block-id-42", child, position);

    AppendBlockChildrenParams body = (AppendBlockChildrenParams) client.getLastBody();
    assertEquals(List.of(child), body.getChildren());
    assertEquals(position, body.getPosition());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void appendChildren_rejectsBlankOrNullParentBlockId(String parentBlockId) {
    assertThrows(
        IllegalArgumentException.class,
        () -> endpoint.appendChildren(parentBlockId, List.of(new Block())));
  }
}
