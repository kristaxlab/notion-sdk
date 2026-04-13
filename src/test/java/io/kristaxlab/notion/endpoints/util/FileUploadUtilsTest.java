package io.kristaxlab.notion.endpoints.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileUploadUtilsTest {

  @TempDir Path tempDir;

  // ── calculateNumberOfParts ─────────────────────────────────────────────────

  @Test
  void calculateNumberOfParts_exactDivision_returnsQuotient() {
    assertEquals(2, FileUploadUtils.calculateNumberOfParts(10, 5));
  }

  @Test
  void calculateNumberOfParts_nonExactDivision_returnsCeiling() {
    assertEquals(3, FileUploadUtils.calculateNumberOfParts(11, 5));
  }

  @Test
  void calculateNumberOfParts_fileSmallerThanPartSize_returnsOne() {
    assertEquals(1, FileUploadUtils.calculateNumberOfParts(3, 5));
  }

  @Test
  void calculateNumberOfParts_fileSizeEqualsPartSize_returnsOne() {
    assertEquals(1, FileUploadUtils.calculateNumberOfParts(5, 5));
  }

  @Test
  void calculateNumberOfParts_fileSizeOfOne_returnsOne() {
    assertEquals(1, FileUploadUtils.calculateNumberOfParts(1, 1000));
  }

  @Test
  void calculateNumberOfParts_zeroFileSize_returnsZero() {
    assertEquals(0, FileUploadUtils.calculateNumberOfParts(0, 5));
  }

  @Test
  void calculateNumberOfParts_zeroPartSize_shouldThrowException() {
    assertThrows(
        IllegalArgumentException.class, () -> FileUploadUtils.calculateNumberOfParts(10, 0));
  }

  @ParameterizedTest
  @ValueSource(longs = {-1, -100, Long.MIN_VALUE})
  void calculateNumberOfParts_negativePartSize_shouldThrowException(long partSize) {
    assertThrows(
        IllegalArgumentException.class, () -> FileUploadUtils.calculateNumberOfParts(10, partSize));
  }

  // ── fileToBytes ────────────────────────────────────────────────────────────

  @Test
  void fileToBytes_shouldReturnFileContents() throws IOException {
    byte[] expected = {1, 2, 3, 4, 5};
    Path file = tempDir.resolve("data.bin");
    Files.write(file, expected);

    byte[] actual = FileUploadUtils.fileToBytes(file.toFile());

    assertArrayEquals(expected, actual);
  }

  @Test
  void fileToBytes_emptyFile_shouldReturnEmptyArray() throws IOException {
    Path file = tempDir.resolve("empty.bin");
    Files.write(file, new byte[0]);

    byte[] actual = FileUploadUtils.fileToBytes(file.toFile());

    assertNotNull(actual);
    assertEquals(0, actual.length);
  }

  @Test
  void fileToBytes_nonExistentFile_shouldThrowIOException() {
    File missing = tempDir.resolve("missing.bin").toFile();

    assertThrows(IOException.class, () -> FileUploadUtils.fileToBytes(missing));
  }

  // ── splitFileIntoParts ─────────────────────────────────────────────────────

  @Test
  void splitFileIntoParts_exactDivision_returnsCorrectNumberOfParts() throws IOException {
    byte[] content = new byte[10];
    Arrays.fill(content, (byte) 1);
    Path file = tempDir.resolve("input.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 5, tempDir.toString());

    assertEquals(2, parts.size());
  }

  @Test
  void splitFileIntoParts_nonExactDivision_lastPartIsSmallerThanPartSize() throws IOException {
    byte[] content = new byte[11];
    Arrays.fill(content, (byte) 7);
    Path file = tempDir.resolve("input.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 5, tempDir.toString());

    assertEquals(3, parts.size());
    assertEquals(5, parts.get(1).length());
    assertEquals(5, parts.get(2).length());
    assertEquals(1, parts.get(3).length());
  }

  @Test
  void splitFileIntoParts_fileSmallerThanPartSize_returnsSinglePart() throws IOException {
    byte[] content = {10, 20, 30};
    Path file = tempDir.resolve("small.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 1000, tempDir.toString());

    assertEquals(1, parts.size());
    assertArrayEquals(content, Files.readAllBytes(parts.get(1).toPath()));
  }

  @Test
  void splitFileIntoParts_partNumbersAreOneBased() throws IOException {
    byte[] content = new byte[10];
    Path file = tempDir.resolve("input.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 5, tempDir.toString());

    assertTrue(parts.containsKey(1));
    assertTrue(parts.containsKey(2));
    assertFalse(parts.containsKey(0));
  }

  @Test
  void splitFileIntoParts_concatenatedPartsMatchOriginalContent() throws IOException {
    byte[] content = new byte[13];
    for (int i = 0; i < content.length; i++) {
      content[i] = (byte) i;
    }
    Path file = tempDir.resolve("seq.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 5, tempDir.toString());

    byte[] reconstructed = new byte[content.length];
    int offset = 0;
    for (int i = 1; i <= parts.size(); i++) {
      byte[] partBytes = Files.readAllBytes(parts.get(i).toPath());
      System.arraycopy(partBytes, 0, reconstructed, offset, partBytes.length);
      offset += partBytes.length;
    }
    assertArrayEquals(content, reconstructed);
  }

  @Test
  void splitFileIntoParts_createsOutputDirectoryIfNotExists() throws IOException {
    byte[] content = {1, 2, 3};
    Path file = tempDir.resolve("input.bin");
    Files.write(file, content);
    String nonExistentDir = tempDir.resolve("new_subdir").toString();

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 10, nonExistentDir);

    assertEquals(1, parts.size());
    assertTrue(parts.get(1).exists());
  }

  @Test
  void splitFileIntoParts_eachPartFileExists() throws IOException {
    byte[] content = new byte[15];
    Path file = tempDir.resolve("input.bin");
    Files.write(file, content);

    Map<Integer, File> parts =
        FileUploadUtils.splitFileIntoParts(file.toFile(), 5, tempDir.toString());

    parts.values().forEach(part -> assertTrue(part.exists(), "Part file should exist: " + part));
  }

  // ── cleanupPartsFiles ──────────────────────────────────────────────────────

  @Test
  void cleanupPartsFiles_shouldDeleteAllExistingFiles() throws IOException {
    Path f1 = Files.createTempFile(tempDir, "part1", ".bin");
    Path f2 = Files.createTempFile(tempDir, "part2", ".bin");
    List<File> parts = List.of(f1.toFile(), f2.toFile());

    FileUploadUtils.cleanupPartsFiles(parts);

    assertFalse(f1.toFile().exists());
    assertFalse(f2.toFile().exists());
  }

  @Test
  void cleanupPartsFiles_nullCollection_shouldNotThrow() {
    assertDoesNotThrow(() -> FileUploadUtils.cleanupPartsFiles(null));
  }

  @Test
  void cleanupPartsFiles_collectionWithNullElements_shouldNotThrow() throws IOException {
    Path existing = Files.createTempFile(tempDir, "part", ".bin");
    List<File> parts = new ArrayList<>();
    parts.add(null);
    parts.add(existing.toFile());
    parts.add(null);

    assertDoesNotThrow(() -> FileUploadUtils.cleanupPartsFiles(parts));
    assertFalse(existing.toFile().exists());
  }

  @Test
  void cleanupPartsFiles_nonExistentFile_shouldNotThrow() {
    File ghost = tempDir.resolve("ghost.bin").toFile();

    assertDoesNotThrow(() -> FileUploadUtils.cleanupPartsFiles(List.of(ghost)));
  }

  @Test
  void cleanupPartsFiles_emptyCollection_shouldNotThrow() {
    assertDoesNotThrow(() -> FileUploadUtils.cleanupPartsFiles(List.of()));
  }
}
