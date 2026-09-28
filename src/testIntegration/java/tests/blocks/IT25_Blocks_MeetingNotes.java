package tests.blocks;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.fluent.NotionBlocksViewer;
import io.kristaxlab.notion.model.block.AudioBlock;
import io.kristaxlab.notion.model.block.BlockList;
import io.kristaxlab.notion.model.block.BlockType;
import io.kristaxlab.notion.model.block.CreateMeetingNotesParams;
import io.kristaxlab.notion.model.block.MeetingNotesBlock;
import io.kristaxlab.notion.model.block.MeetingNotesFilter;
import io.kristaxlab.notion.model.block.MeetingNotesList;
import io.kristaxlab.notion.model.block.QueryMeetingNotesParams;
import io.kristaxlab.notion.util.MeetingNotesPoller;
import io.kristaxlab.notion.util.PollingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testkit.WithTestPageFixture;
import testkit.ext.NotionWorkspaseException;

/**
 * Creates meeting notes from an audio block on a fixture page, polls until {@code notes_ready},
 * then queries once.
 *
 * <p>Requires a fixture page titled {@code IT-25} that contains at least one audio block. The test
 * fails until that page exists on the session template.
 */
public class IT25_Blocks_MeetingNotes extends WithTestPageFixture {

  private static final String TITLE = "IT-25 meeting notes";
  private static final PollingConfig POLLING = PollingConfig.of(ofMinutes(5), ofMillis(2000));

  @Test
  @DisplayName("IT-25: Blocks - Create meeting notes from audio, poll notes_ready, query once")
  public void testCreatePollAndQueryMeetingNotes() {
    BlockList children = getSetupClient().blocks().retrieveChildren(getTestPageId());
    AudioBlock audio =
        NotionBlocksViewer.of(children)
            .first(AudioBlock.class)
            .orElseThrow(
                () ->
                    new NotionWorkspaseException(
                        "IT-25 requires a fixture page titled IT-25 with an audio block"));

    MeetingNotesBlock created =
        getNotionClient()
            .blocks()
            .createMeetingNotes(
                CreateMeetingNotesParams.builder().block(audio.getId()).title(TITLE).build());

    assertNotNull(created.getId());
    assertInstanceOf(MeetingNotesBlock.class, created);

    MeetingNotesBlock ready =
        MeetingNotesPoller.awaitNotesReady(getNotionClient(), created.getId(), POLLING);

    assertEquals(BlockType.MEETING_NOTES.getValue(), ready.getType());
    assertEquals("notes_ready", ready.getMeetingNotes().getStatus());
    assertNotNull(ready.getMeetingNotes().getChildren());

    MeetingNotesList queried =
        getNotionClient()
            .blocks()
            .queryMeetingNotes(
                QueryMeetingNotesParams.builder()
                    .filter(MeetingNotesFilter.titleContains("IT-25"))
                    .limit(50)
                    .build());

    assertNotNull(queried.getResults());
    assertTrue(
        queried.getResults().stream().anyMatch(b -> created.getId().equals(b.getId())),
        "query once must include the created meeting notes block");
  }
}
