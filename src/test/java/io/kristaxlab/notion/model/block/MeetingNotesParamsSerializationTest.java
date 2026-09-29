package io.kristaxlab.notion.model.block;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import io.kristaxlab.notion.http.base.json.JacksonSerializer;
import io.kristaxlab.notion.model.common.SortDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingNotesParamsSerializationTest {

  private static final JacksonSerializer JSON = JacksonSerializer.withDefaults();

  @Test
  @DisplayName("create from file upload serializes source and parent")
  void createFromFileUpload_serializesSourceAndParent() {
    CreateMeetingNotesParams params =
        CreateMeetingNotesParams.builder()
            .fileUpload("page-1", "upload-1")
            .title("Team Sync")
            .language(MeetingNotesLanguage.EN)
            .kickoffSummary(true)
            .build();

    JsonNode root = JSON.toObject(JSON.toJson(params), JsonNode.class);

    assertEquals("Team Sync", root.get("title").asText());
    assertEquals("en", root.get("language").asText());
    assertTrue(root.get("options").get("kickoff_summary").asBoolean());
    assertEquals("file_upload", root.get("source").get("type").asText());
    assertEquals("upload-1", root.get("source").get("file_upload_id").asText());
    assertEquals("page_id", root.get("parent").get("type").asText());
    assertEquals("page-1", root.get("parent").get("page_id").asText());
  }

  @Test
  @DisplayName("create from block omits parent")
  void createFromBlock_omitsParent() {
    CreateMeetingNotesParams params = CreateMeetingNotesParams.fromBlock("audio-1");

    JsonNode root = JSON.toObject(JSON.toJson(params), JsonNode.class);

    assertEquals("block", root.get("source").get("type").asText());
    assertEquals("audio-1", root.get("source").get("block_id").asText());
    assertFalse(root.has("parent"));
  }

  @Test
  @DisplayName("query params serialize filter sort and limit")
  void queryParams_serializeFilterSortAndLimit() {
    QueryMeetingNotesParams params =
        QueryMeetingNotesParams.builder()
            .filter(
                MeetingNotesFilter.and(
                    MeetingNotesFilter.titleContains("Sync"),
                    MeetingNotesFilter.isNotEmpty(MeetingNotesProperty.ATTENDEES)))
            .sort(
                MeetingNotesSort.of(
                    MeetingNotesProperty.LAST_EDITED_TIME, SortDirection.DESCENDING))
            .limit(10)
            .build();

    JsonNode root = JSON.toObject(JSON.toJson(params), JsonNode.class);

    assertEquals("and", root.get("filter").get("operator").asText());
    assertEquals(2, root.get("filter").get("filters").size());
    assertEquals("title", root.get("filter").get("filters").get(0).get("property").asText());
    assertEquals(
        "string_contains",
        root.get("filter").get("filters").get(0).get("filter").get("operator").asText());
    assertEquals(
        "exact",
        root.get("filter").get("filters").get(0).get("filter").get("value").get("type").asText());
    assertEquals(
        "Sync",
        root.get("filter").get("filters").get(0).get("filter").get("value").get("value").asText());
    assertEquals("last_edited_time", root.get("sort").get(0).get("property").asText());
    assertEquals("descending", root.get("sort").get(0).get("direction").asText());
    assertEquals(10, root.get("limit").asInt());
  }

  @Test
  @DisplayName("query builder wraps bare property filter in and combinator")
  void queryParams_wrapsBarePropertyFilter() {
    QueryMeetingNotesParams params =
        QueryMeetingNotesParams.builder()
            .filter(MeetingNotesFilter.titleContains("IT-25"))
            .limit(50)
            .build();

    JsonNode root = JSON.toObject(JSON.toJson(params), JsonNode.class);

    assertEquals("and", root.get("filter").get("operator").asText());
    assertEquals(1, root.get("filter").get("filters").size());
    assertEquals("title", root.get("filter").get("filters").get(0).get("property").asText());
    assertEquals(
        "IT-25",
        root.get("filter").get("filters").get(0).get("filter").get("value").get("value").asText());
  }
}
