package integration.extension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class NotionTestIdRetrieverTest {

  @ParameterizedTest(name = "[{index}] {0} → {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "it-123: Some test description | IT-123",
        "IT-123: Some test description | IT-123",
        "it-??: Some test description | IT-??",
        "IT-??: Some test description | IT-??",
        "IT-?: Users - Retrieve me, then retrieve me by id | IT-?",
        "IT-9999: Test with many digits | IT-9999",
        "IT-1: Single digit test | IT-1",
        "It-456: Mixed case prefix | IT-456",
        "IT-789 | IT-789"
      })
  void shouldExtractValidTestId(String displayName, String expectedId) {
    Optional<String> result = NotionTestIdRetriever.retrieveTestId(displayName);
    assertTrue(result.isPresent());
    assertEquals(expectedId, result.get());
  }

  @ParameterizedTest(name = "[{index}] \"{0}\" → empty")
  @ValueSource(strings = {"Some test without an ID", "IT-abc: Invalid characters", ""})
  void shouldReturnEmptyForInvalidInput(String displayName) {
    Optional<String> result = NotionTestIdRetriever.retrieveTestId(displayName);
    assertFalse(result.isPresent());
  }
}
