package io.kristixlab.notion.api.model.blocks;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
  This class represents an unknown block type in the Notion API. In opposite to the UnsupportedBlock,
  it is used when the block type is not recognized in the current version of the SDK while
  the UnsupportedBlock is used when the block type is explicitly unsupported by the API.
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownBlock extends Block {
}
