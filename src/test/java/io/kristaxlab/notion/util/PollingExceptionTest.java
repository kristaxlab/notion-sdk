package io.kristaxlab.notion.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PollingException hierarchy")
class PollingExceptionTest {

  @Test
  @DisplayName("TemplatePollingException is a PollingException")
  void templatePollingException_extendsPollingException() {
    TemplatePollingException exception = new TemplatePollingException("timed out");

    assertInstanceOf(PollingException.class, exception);
    assertEquals("timed out", exception.getMessage());
  }

  @Test
  @DisplayName("TemplatePollingException with cause preserves cause through the base type")
  void templatePollingException_withCause() {
    InterruptedException cause = new InterruptedException("stopped");
    TemplatePollingException exception = new TemplatePollingException("interrupted", cause);

    PollingException asBase =
        assertThrows(
            PollingException.class,
            () -> {
              throw exception;
            });

    assertSame(cause, asBase.getCause());
  }
}
