package io.kristaxlab.notion.util;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.block.MeetingNotesBlock;
import java.time.Duration;
import java.util.function.Predicate;

/**
 * Utility for polling a meeting notes block until processing reaches a ready status.
 *
 * <p>Notion processes transcription and summary asynchronously after create. Use {@link
 * #awaitNotesReady} (or {@link #awaitMeetingNotes} with a custom predicate) before reading child
 * block ids from the meeting notes payload.
 *
 * <pre>{@code
 * MeetingNotesBlock created = client.blocks().createMeetingNotesFromBlock(audioBlockId);
 * MeetingNotesBlock ready =
 *     MeetingNotesPoller.awaitNotesReady(
 *         client, created.getId(), PollingConfig.ofTimeout(Duration.ofMinutes(5)));
 * }</pre>
 *
 * <p>{@link MeetingNotesPollingException} is a {@link PollingException}; catch the base type when
 * any poller failure should be handled the same way.
 *
 * @see
 *     io.kristaxlab.notion.endpoints.BlocksEndpoint#createMeetingNotes(io.kristaxlab.notion.model.block.CreateMeetingNotesParams)
 */
public final class MeetingNotesPoller {

  private static final String NOTES_READY = "notes_ready";

  private MeetingNotesPoller() {}

  /**
   * Polls retrieve until {@code meeting_notes.status} is {@code notes_ready}.
   *
   * @param client Notion client for API calls
   * @param blockId meeting notes block id
   * @param config polling configuration (timeout, attempts, interval)
   * @return the block when status is {@code notes_ready}
   * @throws IllegalArgumentException if any argument is {@code null} or {@code blockId} is blank
   * @throws MeetingNotesPollingException if timeout, max attempts reached, or polling is
   *     interrupted
   */
  public static MeetingNotesBlock awaitNotesReady(
      NotionClient client, String blockId, PollingConfig config) {
    return awaitMeetingNotes(
        client,
        blockId,
        block -> NOTES_READY.equals(statusOf(block)),
        config,
        "notes_ready for block " + blockId);
  }

  /**
   * Polls retrieve until a readiness condition is met.
   *
   * @param client Notion client for API calls
   * @param blockId meeting notes block id
   * @param readinessCheck predicate that returns true when the block is ready
   * @param config polling configuration (timeout, attempts, interval)
   * @return the block when ready
   * @throws IllegalArgumentException if any argument is {@code null} or {@code blockId} is blank
   * @throws MeetingNotesPollingException if timeout, max attempts reached, or polling is
   *     interrupted
   */
  public static MeetingNotesBlock awaitMeetingNotes(
      NotionClient client,
      String blockId,
      Predicate<MeetingNotesBlock> readinessCheck,
      PollingConfig config) {
    return awaitMeetingNotes(client, blockId, readinessCheck, config, "block " + blockId);
  }

  private static MeetingNotesBlock awaitMeetingNotes(
      NotionClient client,
      String blockId,
      Predicate<MeetingNotesBlock> readinessCheck,
      PollingConfig config,
      String context) {
    if (client == null) {
      throw new IllegalArgumentException("client cannot be null");
    }
    if (blockId == null || blockId.isBlank()) {
      throw new IllegalArgumentException("blockId cannot be null or blank");
    }
    if (readinessCheck == null) {
      throw new IllegalArgumentException("readinessCheck cannot be null");
    }
    validateConfig(config);

    long startTime = System.currentTimeMillis();
    Duration timeout = config.getTimeout();
    Integer maxAttempts = config.getMaxAttempts();

    MeetingNotesBlock last = null;
    int attempt = 0;

    while (true) {
      attempt++;

      if (maxAttempts != null && attempt > maxAttempts) {
        throw new MeetingNotesPollingException(
            String.format(
                "Meeting notes polling exceeded max attempts (%d) for %s", maxAttempts, context));
      }

      if (timeout != null) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > timeout.toMillis()) {
          throw new MeetingNotesPollingException(
              String.format("Meeting notes polling timed out after %dms for %s", elapsed, context));
        }
      }

      last = client.blocks().retrieve(blockId).asMeetingNotes();
      if (readinessCheck.test(last)) {
        return last;
      }

      sleep(config.getPollingInterval(), context);
    }
  }

  private static String statusOf(MeetingNotesBlock block) {
    if (block == null || block.getMeetingNotes() == null) {
      return null;
    }
    return block.getMeetingNotes().getStatus();
  }

  private static void sleep(Duration interval, String context) {
    try {
      Thread.sleep(interval.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new MeetingNotesPollingException("Meeting notes polling interrupted for " + context, e);
    }
  }

  private static void validateConfig(PollingConfig config) {
    if (config == null) {
      throw new IllegalArgumentException("PollingConfig cannot be null");
    }
    if (config.getTimeout() == null && config.getMaxAttempts() == null) {
      throw new IllegalArgumentException(
          "At least one of timeout or maxAttempts must be configured");
    }
    if (config.getPollingInterval() == null) {
      throw new IllegalArgumentException("Polling interval cannot be null");
    }
  }
}
