package io.kristaxlab.notion.model.block;

import io.kristaxlab.notion.model.common.NotionList;
import lombok.Getter;
import lombok.Setter;

/**
 * Response of the meeting-notes query endpoint. Not cursor-paginated: only {@code results} and
 * {@code has_more} are present (no {@code next_cursor}).
 *
 * @see io.kristaxlab.notion.endpoints.BlocksEndpoint#queryMeetingNotes(QueryMeetingNotesParams)
 */
@Getter
@Setter
public class MeetingNotesList extends NotionList<MeetingNotesBlock> {}
