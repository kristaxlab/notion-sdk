package io.kristaxlab.notion.model.block;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.kristaxlab.notion.model.common.richtext.RichText;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A Notion meeting notes block: metadata for a meeting-notes session.
 *
 * <p>On retrieve the type-named value field holds title, status, child block ids, and optional
 * calendar/recording metadata. Legacy API versions use wire type {@code transcription} with the
 * same payload under that key; both deserialize to this class. New instances and writes use {@code
 * meeting_notes} only.
 *
 * <p>Create and query use dedicated meeting-notes endpoints, not append/update children.
 *
 * @see <a href="https://developers.notion.com/reference/block">Notion Block object</a>
 */
@Getter
@Setter
public class MeetingNotesBlock extends Block {

  /**
   * Type-named value field. Also accepts the legacy {@code transcription} key on deserialize; only
   * {@code meeting_notes} is written.
   */
  @JsonAlias("transcription")
  private MeetingNotes meetingNotes;

  /**
   * Creates a meeting notes block initialized with an empty payload and type {@code meeting_notes}.
   */
  public MeetingNotesBlock() {
    setType(BlockType.MEETING_NOTES.getValue());
    meetingNotes = new MeetingNotes();
  }

  /** The inner content object of a meeting notes block. */
  @Getter
  @Setter
  public static class MeetingNotes {

    /** Display name of the meeting notes session. */
    private List<RichText> title;

    /**
     * Lifecycle status of the transcription (e.g. {@code notes_ready}). Kept as {@link String} per
     * ADR 0002.
     */
    private String status;

    /** Pointers to summary, notes, and transcript child blocks. */
    private Children children;

    /** Calendar metadata for the meeting, when present. */
    private CalendarEvent calendarEvent;

    /** Recording time window, when present. */
    private Recording recording;
  }

  /**
   * Linked summary / notes / transcript block ids from the type-named {@code children} object — not
   * nested content blocks under {@code has_children}.
   */
  @Getter
  @Setter
  public static class Children {

    private String summaryBlockId;

    private String notesBlockId;

    private String transcriptBlockId;
  }

  /** Calendar event start/end and optional attendee user ids. */
  @Getter
  @Setter
  public static class CalendarEvent {

    private String startTime;

    private String endTime;

    private List<String> attendees;
  }

  /** Recording start and end timestamps. */
  @Getter
  @Setter
  public static class Recording {

    private String startTime;

    private String endTime;
  }
}
