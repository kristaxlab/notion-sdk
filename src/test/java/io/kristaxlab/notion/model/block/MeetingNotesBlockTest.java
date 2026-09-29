package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingNotesBlockTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  @Test
  @DisplayName("constructor sets type to meeting_notes and initializes empty payload")
  void constructor_setsTypeAndInitializesPayload() {
    MeetingNotesBlock block = new MeetingNotesBlock();

    assertEquals("meeting_notes", block.getType());
    assertNotNull(block.getMeetingNotes());
    assertNull(block.getMeetingNotes().getTitle());
    assertNull(block.getMeetingNotes().getStatus());
    assertNull(block.getMeetingNotes().getChildren());
    assertNull(block.getMeetingNotes().getCalendarEvent());
    assertNull(block.getMeetingNotes().getRecording());
  }

  @Test
  @DisplayName("deserialize meeting_notes yields MeetingNotesBlock with full payload")
  void deserialize_meetingNotes_yieldsMeetingNotesBlock() {
    String json =
        """
        {
          "object": "block",
          "id": "d7b3c8f4-9e6e-4c1a-b5b8-2c0f4a0c5b8e",
          "type": "meeting_notes",
          "has_children": true,
          "meeting_notes": {
            "title": [
              {
                "type": "text",
                "text": { "content": "Team Sync", "link": null },
                "plain_text": "Team Sync",
                "href": null
              }
            ],
            "status": "notes_ready",
            "children": {
              "summary_block_id": "a1b2c3d4-5678-9abc-def0-1234567890ab",
              "notes_block_id": "b2c3d4e5-6789-abcd-ef01-234567890abc",
              "transcript_block_id": "c3d4e5f6-789a-bcde-f012-34567890abcd"
            },
            "calendar_event": {
              "attendees": ["ee5f0f84-409a-440f-983a-a5315961c6e4"],
              "start_time": "2026-02-24T10:00:00.000Z",
              "end_time": "2026-02-24T10:45:00.000Z"
            },
            "recording": {
              "start_time": "2026-02-24T10:00:00.000Z",
              "end_time": "2026-02-24T10:45:00.000Z"
            }
          }
        }
        """;

    Block block = JSON.toObject(json, Block.class);

    assertInstanceOf(MeetingNotesBlock.class, block);
    assertEquals("meeting_notes", block.getType());
    MeetingNotesBlock.MeetingNotes payload = block.asMeetingNotes().getMeetingNotes();
    assertEquals("Team Sync", payload.getTitle().get(0).getPlainText());
    assertEquals("notes_ready", payload.getStatus());
    assertEquals("a1b2c3d4-5678-9abc-def0-1234567890ab", payload.getChildren().getSummaryBlockId());
    assertEquals("b2c3d4e5-6789-abcd-ef01-234567890abc", payload.getChildren().getNotesBlockId());
    assertEquals(
        "c3d4e5f6-789a-bcde-f012-34567890abcd", payload.getChildren().getTranscriptBlockId());
    assertEquals("2026-02-24T10:00:00.000Z", payload.getCalendarEvent().getStartTime());
    assertEquals("2026-02-24T10:45:00.000Z", payload.getCalendarEvent().getEndTime());
    assertEquals(
        "ee5f0f84-409a-440f-983a-a5315961c6e4", payload.getCalendarEvent().getAttendees().get(0));
    assertEquals("2026-02-24T10:00:00.000Z", payload.getRecording().getStartTime());
    assertEquals("2026-02-24T10:45:00.000Z", payload.getRecording().getEndTime());
  }

  @Test
  @DisplayName("deserialize legacy transcription yields MeetingNotesBlock")
  void deserialize_transcription_yieldsMeetingNotesBlock() {
    String json =
        """
        {
          "object": "block",
          "id": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
          "type": "transcription",
          "has_children": true,
          "transcription": {
            "title": [
              {
                "type": "text",
                "text": { "content": "Legacy Sync", "link": null },
                "plain_text": "Legacy Sync",
                "href": null
              }
            ],
            "status": "transcription_in_progress",
            "children": {
              "summary_block_id": "11111111-1111-1111-1111-111111111111",
              "notes_block_id": "22222222-2222-2222-2222-222222222222",
              "transcript_block_id": "33333333-3333-3333-3333-333333333333"
            },
            "calendar_event": {
              "start_time": "2025-01-01T09:00:00.000Z",
              "end_time": "2025-01-01T09:30:00.000Z"
            },
            "recording": {
              "start_time": "2025-01-01T09:00:00.000Z",
              "end_time": "2025-01-01T09:30:00.000Z"
            }
          }
        }
        """;

    Block block = JSON.toObject(json, Block.class);

    assertInstanceOf(MeetingNotesBlock.class, block);
    assertEquals("transcription", block.getType());
    MeetingNotesBlock.MeetingNotes payload = block.asMeetingNotes().getMeetingNotes();
    assertEquals("Legacy Sync", payload.getTitle().get(0).getPlainText());
    assertEquals("transcription_in_progress", payload.getStatus());
    assertEquals("11111111-1111-1111-1111-111111111111", payload.getChildren().getSummaryBlockId());
    assertEquals("22222222-2222-2222-2222-222222222222", payload.getChildren().getNotesBlockId());
    assertEquals(
        "33333333-3333-3333-3333-333333333333", payload.getChildren().getTranscriptBlockId());
    assertEquals("2025-01-01T09:00:00.000Z", payload.getCalendarEvent().getStartTime());
    assertEquals("2025-01-01T09:30:00.000Z", payload.getRecording().getEndTime());
  }

  @Test
  @DisplayName("serialize writes meeting_notes type and type-named field only")
  void serialize_writesMeetingNotesOnly() {
    MeetingNotesBlock block = new MeetingNotesBlock();
    MeetingNotesBlock.Children children = new MeetingNotesBlock.Children();
    children.setSummaryBlockId("a1b2c3d4-5678-9abc-def0-1234567890ab");
    block.getMeetingNotes().setStatus("notes_ready");
    block.getMeetingNotes().setChildren(children);

    String json = JSON.toJson(block);
    JsonNode root = JSON.toObject(json, JsonNode.class);

    assertEquals("meeting_notes", root.get("type").asText());
    assertTrue(root.has("meeting_notes"));
    assertFalse(root.has("transcription"));
    assertEquals("notes_ready", root.get("meeting_notes").get("status").asText());
    assertEquals(
        "a1b2c3d4-5678-9abc-def0-1234567890ab",
        root.get("meeting_notes").get("children").get("summary_block_id").asText());
  }
}
