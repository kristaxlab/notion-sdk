# Meeting notes

Create and query meeting notes blocks with `BlocksEndpoint` (`client.blocks()`). Notion processes
transcription asynchronously after create — wait with `MeetingNotesPoller` before reading linked
child block ids.

Meeting notes are **not** created with `appendChildren`. Use the dedicated create / query paths.

Official references: [Create a meeting note](https://developers.notion.com/reference/create-meeting-note),
[Query meeting notes](https://developers.notion.com/reference/query-meeting-notes).

## Create from an existing media block

```java
MeetingNotesBlock created =
    client.blocks().createMeetingNotesFromBlock("audio-or-video-block-id");
```

Or with a title (and other options) via the params builder:

```java
MeetingNotesBlock created =
    client.blocks()
        .createMeetingNotes(
            CreateMeetingNotesParams.builder()
                .block("audio-or-video-block-id")
                .title("Sprint planning")
                .build());
```

## Create from a file upload

```java
MeetingNotesBlock created =
    client.blocks().createMeetingNotesFromFileUpload("page-id", "file-upload-id");
```

## Poll until notes are ready

```java
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofMillis;

MeetingNotesBlock ready =
    MeetingNotesPoller.awaitNotesReady(
        client,
        created.getId(),
        PollingConfig.of(ofMinutes(5), ofMillis(2000)));
```

When `status` is `notes_ready`, the type-named payload exposes linked child ids:

```java
MeetingNotesBlock.Children children = ready.getMeetingNotes().getChildren();

Block summary = client.blocks().retrieve(children.getSummaryBlockId());
Block notes = client.blocks().retrieve(children.getNotesBlockId());
Block transcript = client.blocks().retrieve(children.getTranscriptBlockId());
```

Those ids are pointers in the meeting-notes payload — not nested content under
`has_children` on the meeting notes block itself.

## Query meeting notes

Query is not cursor-paginated. Use `filter`, `sort`, and `limit` (1–50). The wire `filter` must be
a combinator; a bare property filter passed to the builder is wrapped in `and`.

```java
MeetingNotesList results =
    client.blocks()
        .queryMeetingNotes(
            QueryMeetingNotesParams.builder()
                .filter(MeetingNotesFilter.titleContains("Sprint"))
                .sort(MeetingNotesSort.byLastEditedTimeDescending())
                .limit(20)
                .build());
```

Combine conditions:

```java
QueryMeetingNotesParams params =
    QueryMeetingNotesParams.builder()
        .filter(
            MeetingNotesFilter.and(
                MeetingNotesFilter.titleContains("planning"),
                MeetingNotesFilter.isNotEmpty(MeetingNotesProperty.ATTENDEES)))
        .limit(50)
        .build();
```

## Legacy wire type

Older API versions may return `type: "transcription"` with the same payload under that key. The SDK
deserializes both shapes to `MeetingNotesBlock`. Writes always use `meeting_notes`.

## Related cookbook pages

- [Adding blocks](adding-blocks.md)
- [Files and media uploads](files-and-media.md)
- [Updating blocks](updating-blocks.md)
- [Search by title](search.md)
- [Back to README](../../README.md#cookbook)
