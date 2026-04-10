package io.kristixlab.notion.api.endpoints.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility methods for file upload operations such as splitting files into parts, reading file
 * bytes, and cleaning up temporary files.
 */
public final class FileUploadUtils {

  private FileUploadUtils() {}

  /**
   * Reads the contents of a file into a byte array.
   *
   * @param file the file to read
   * @return the file contents as a byte array
   * @throws IOException if an I/O error occurs reading the file
   */
  public static byte[] fileToBytes(File file) throws IOException {
    return Files.readAllBytes(file.toPath());
  }

  /**
   * Calculates the number of parts needed to split a file of the given size.
   *
   * @param fileSizeBytes total file size in bytes
   * @param partSizeBytes maximum size of each part in bytes
   * @return the number of parts (at least 1)
   */
  public static int calculateNumberOfParts(long fileSizeBytes, long partSizeBytes) {
    if (partSizeBytes <= 0) {
      throw new IllegalArgumentException("Part size must be positive");
    }
    return (int) Math.ceil((double) fileSizeBytes / partSizeBytes);
  }

  /**
   * Splits a file into parts of the specified size and writes them to the output directory.
   *
   * <p>Part numbers are 1-based. Each part file is named {@code part_<number>}.
   *
   * @param file the file to split
   * @param partSizeBytes maximum size of each part in bytes
   * @param outputDir directory to write part files into
   * @return a map from part number (1-based) to the part file
   * @throws IOException if an I/O error occurs
   */
  public static Map<Integer, File> splitFileIntoParts(
      File file, long partSizeBytes, String outputDir) throws IOException {
    Path dir = getPartsBasePath(outputDir);
    Files.createDirectories(dir);

    Map<Integer, File> parts = new LinkedHashMap<>();
    try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
      long fileSize = raf.length();
      int totalParts = calculateNumberOfParts(fileSize, partSizeBytes);

      for (int i = 1; i <= totalParts; i++) {
        long offset = (long) (i - 1) * partSizeBytes;
        int length = (int) Math.min(partSizeBytes, fileSize - offset);
        byte[] buffer = new byte[length];
        raf.seek(offset);
        raf.readFully(buffer);

        File partFile = dir.resolve("part_" + i).toFile();
        Files.write(partFile.toPath(), buffer);
        parts.put(i, partFile);
      }
    }
    return parts;
  }

  /**
   * Deletes temporary part files created by {@link #splitFileIntoParts}.
   *
   * @param partFiles the collection of part files to delete
   */
  public static void cleanupPartsFiles(Collection<File> partFiles) {
    if (partFiles == null) {
      return;
    }
    for (File part : partFiles) {
      if (part != null && part.exists()) {
        part.delete();
      }
    }
  }

  /**
   * Generates a unique base path for part files in the specified directory to avoid collisions.
   *
   * @param dir the directory to create the base path in
   * @return a Path object representing the base path for part files
   */
  private static Path getPartsBasePath(String dir) {
    return Path.of(
        dir,
        String.format("parts_%d_%d", System.currentTimeMillis(), Thread.currentThread().getId()));
  }
}
