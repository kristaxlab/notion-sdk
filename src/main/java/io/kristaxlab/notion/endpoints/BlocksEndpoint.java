package io.kristaxlab.notion.endpoints;

import io.kristaxlab.notion.fluent.NotionBlocksBuilder;
import io.kristaxlab.notion.model.block.AppendBlockChildrenParams;
import io.kristaxlab.notion.model.block.Block;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.CreateMeetingNotesParams;
import io.kristaxlab.notion.model.block.MeetingNotesBlock;
import io.kristaxlab.notion.model.block.MeetingNotesList;
import io.kristaxlab.notion.model.block.QueryMeetingNotesParams;
import io.kristaxlab.notion.model.common.Position;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Block CRUD, child listing, appending children (with optional {@link Position}), and meeting-notes
 * create/query.
 *
 * @see <a href="https://developers.notion.com/reference/blocks">Notion Blocks API</a>
 */
public interface BlocksEndpoint {
  /**
   * Appends a single child block under a parent block.
   *
   * @param parentBlockId parent block identifier
   * @param child block to append
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, Block child);

  /**
   * Appends a single child block at the given position.
   *
   * @param parentBlockId parent block identifier
   * @param child block to append
   * @param position optional insert position relative to existing children; {@code null} appends at
   *     the end
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, Block child, Position position);

  /**
   * Appends multiple child blocks under a parent block.
   *
   * @param parentBlockId parent block identifier
   * @param children blocks to append
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, List<? extends Block> children);

  /**
   * Appends multiple child blocks at the given position.
   *
   * @param parentBlockId parent block identifier
   * @param children blocks to append
   * @param position optional insert position relative to existing children; {@code null} appends at
   *     the end
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, List<? extends Block> children, Position position);

  /**
   * Appends blocks built from the {@link NotionBlocksBuilder} DSL.
   *
   * @param parentBlockId parent block identifier
   * @param consumer builder consumer that produces child blocks
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, Consumer<NotionBlocksBuilder> consumer);

  /**
   * Appends blocks built from the {@link NotionBlocksBuilder} DSL at the given position.
   *
   * @param parentBlockId parent block identifier
   * @param consumer builder consumer that produces child blocks
   * @param position optional insert position relative to existing children; {@code null} appends at
   *     the end
   * @return API response containing appended blocks
   * @throws IllegalArgumentException if {@code consumer} is {@code null}
   */
  BlockList appendChildren(
      String parentBlockId, Consumer<NotionBlocksBuilder> consumer, Position position);

  /**
   * Appends lazily built blocks at the given position.
   *
   * @param parentBlockId parent block identifier
   * @param supplier supplier of child blocks
   * @param position optional insert position relative to existing children; {@code null} appends at
   *     the end
   * @return API response containing appended blocks
   */
  BlockList appendChildren(
      String parentBlockId, Supplier<List<? extends Block>> supplier, Position position);

  /**
   * Appends children using a fully prepared request payload.
   *
   * @param parentBlockId parent block identifier
   * @param request append payload including children and optional position
   * @return API response containing appended blocks
   */
  BlockList appendChildren(String parentBlockId, AppendBlockChildrenParams request);

  /**
   * Retrieves a single block by id.
   *
   * @param blockId block identifier
   * @return retrieved block
   */
  Block retrieve(String blockId);

  /**
   * Retrieves child blocks for a parent block using default pagination.
   *
   * @param blockId parent block identifier
   * @return paginated child block response
   */
  BlockList retrieveChildren(String blockId);

  /**
   * Retrieves child blocks for a parent block with explicit pagination.
   *
   * @param blockId parent block identifier
   * @param startCursor pagination cursor
   * @param pageSize max page size
   * @return paginated child block response
   */
  BlockList retrieveChildren(String blockId, String startCursor, Integer pageSize);

  /**
   * Updates a block using the id embedded in the request payload.
   *
   * <p>The PATCH body is block-shaped; there is no {@code UpdateBlockParams}. Build the typed
   * payload with {@link io.kristaxlab.notion.fluent.NotionBlocks} factories (including their {@link
   * Consumer} overloads), set the block id on the payload, then pass it here.
   *
   * <pre>{@code
   * Block patch = paragraph(p -> p.text(plainText("updated")));
   * patch.setId(blockId);
   * client.blocks().update(patch);
   * }</pre>
   *
   * @param request partial block payload with update fields and a non-blank id
   * @return updated block
   */
  Block update(Block request);

  /**
   * Updates an existing block.
   *
   * <p>The PATCH body is block-shaped; there is no {@code UpdateBlockParams}. Prefer {@link
   * io.kristaxlab.notion.fluent.NotionBlocks} factories (including their {@link Consumer}
   * overloads) to build the typed partial payload.
   *
   * <pre>{@code
   * client.blocks().update(blockId, paragraph(p -> p.text(plainText("updated"))));
   * client.blocks().update(blockId, todo(t -> t.text("done").checked(true)));
   * }</pre>
   *
   * @param blockId block identifier
   * @param request partial block payload with update fields
   * @return updated block
   */
  Block update(String blockId, Block request);

  /**
   * Archives (deletes) a block.
   *
   * @param blockId block identifier
   * @return archived block
   */
  Block delete(String blockId);

  /**
   * Restores a block from trash by clearing the archived state.
   *
   * @param blockId block identifier
   * @return restored block
   */
  Block restore(String blockId);

  /**
   * Creates a meeting notes block from the given params and begins processing its source media.
   *
   * @param params create body (file_upload+parent or block source)
   * @return created meeting notes block (full payload when the integration can read content)
   * @throws IllegalArgumentException if {@code params} is {@code null}
   * @see <a href="https://developers.notion.com/reference/create-meeting-note">Create a meeting
   *     note</a>
   */
  MeetingNotesBlock createMeetingNotes(CreateMeetingNotesParams params);

  /**
   * Creates a meeting notes block from a completed file upload under a page parent.
   *
   * @param pageId parent page id
   * @param fileUploadId completed file upload id
   * @return created meeting notes block
   * @throws IllegalArgumentException if either id is null or blank
   */
  MeetingNotesBlock createMeetingNotesFromFileUpload(String pageId, String fileUploadId);

  /**
   * Creates a meeting notes block from an existing audio, video, or file block.
   *
   * @param blockId source block id
   * @return created meeting notes block
   * @throws IllegalArgumentException if {@code blockId} is null or blank
   */
  MeetingNotesBlock createMeetingNotesFromBlock(String blockId);

  /**
   * Queries meeting notes in the workspace with optional filter, sort, and limit.
   *
   * @param params query body; use an empty instance for the server default (limit 50)
   * @return meeting notes list ({@code results} + {@code has_more}; not cursor-paginated)
   * @throws IllegalArgumentException if {@code params} is {@code null}
   * @see <a href="https://developers.notion.com/reference/query-meeting-notes">Query meeting
   *     notes</a>
   */
  MeetingNotesList queryMeetingNotes(QueryMeetingNotesParams params);
}
