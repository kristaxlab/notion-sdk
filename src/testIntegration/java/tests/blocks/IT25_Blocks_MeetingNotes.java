package tests.blocks;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.*;

import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.*;
import io.kristaxlab.notion.util.MeetingNotesPoller;
import io.kristaxlab.notion.util.PollingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testkit.WithTestPageFixture;
import testkit.ext.NotionWorkspaseException;

/**
 * Creates meeting notes from an audio block on a fixture page, polls until {@code notes_ready},
 * retrieves linked summary/notes/transcript children, then queries once.
 *
 * <p>Requires a fixture page titled {@code IT-25} with at least one audio block. Page existence is
 * enforced by {@link testkit.ext.FixturePageIdProvisioner}; the audio block is resolved in {@link
 * #setup()}.
 */
@Tag("paid_plan")
public class IT25_Blocks_MeetingNotes extends WithTestPageFixture {

  private static final PollingConfig POLLING = PollingConfig.of(ofMinutes(1), ofMillis(3000));

  private String audioBlockId;

  @BeforeEach
  public void setup() {
    BlockList children = getSetupClient().blocks().retrieveChildren(getTestPageId());
    AudioBlock audio =
        NotionBlocksViewer.of(children)
            .first(AudioBlock.class)
            .orElseThrow(
                () ->
                    new NotionWorkspaseException("IT-25 fixture page must contain an audio block"));
    audioBlockId = audio.getId();
  }

  @Test
  @DisplayName(
      "IT-25: Blocks - Create meeting notes from audio, poll notes_ready, retrieve children, query once")
  public void testCreatePollAndQueryMeetingNotes() {
    String title = "Meeting Notes: " + System.currentTimeMillis();

    MeetingNotesBlock created =
        getNotionClient()
            .blocks()
            .createMeetingNotes(
                CreateMeetingNotesParams.builder().block(audioBlockId).title(title).build());

    assertNotNull(created.getId());
    assertInstanceOf(MeetingNotesBlock.class, created);

    MeetingNotesBlock ready =
        MeetingNotesPoller.awaitNotesReady(getNotionClient(), created.getId(), POLLING);

    assertEquals(BlockType.MEETING_NOTES.getValue(), ready.getType());
    assertEquals("notes_ready", ready.getMeetingNotes().getStatus());

    MeetingNotesBlock.Children linked = ready.getMeetingNotes().getChildren();
    assertNotNull(linked);
    assertNotNull(linked.getSummaryBlockId());
    assertNotNull(linked.getNotesBlockId());
    assertNotNull(linked.getTranscriptBlockId());

    MeetingNotesList queried =
        getNotionClient()
            .blocks()
            .queryMeetingNotes(
                QueryMeetingNotesParams.builder()
                    .filter(MeetingNotesFilter.titleContains(title))
                    .limit(5)
                    .build());

    assertNotNull(queried.getResults());
    assertTrue(
        queried.getResults().stream().anyMatch(b -> created.getId().equals(b.getId())),
        "query result must include the created meeting notes block");
  }
}
