package io.kristixlab.notion.api.endpoints.util;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FileUploadUtils {

  public static byte[] fileToBytes(File file) {
    try (InputStream is = new FileInputStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
      byte[] data = new byte[8192];
      int nRead;
      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Error converting file to bytes", e);
    }
  }

  public static Map<Integer, File> splitFileIntoParts(File file, long partSizeInBytes, String dir) {
    Map<Integer, File> result = new HashMap<>();
    String filePartBasePath = prepareFilePartBasePath(file, dir);

    try (InputStream is = new FileInputStream(file)) {
      boolean completed = false;
      int partNumber = 1;
      while (!completed) {
        File outputFile = new File(filePartBasePath + partNumber);
        int bytesWritten = writePartToFile(outputFile, partSizeInBytes, is);
        if (bytesWritten > 0) {
          result.put(partNumber, outputFile);
          partNumber++;
        } else {
          completed = true;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error splitting file into parts", e);
    }

    return result;
  }

  private static int writePartToFile(File outputFile, long partSizeInBytes, InputStream is)
      throws IOException {
    int bytesRead = 0;
    try (OutputStream os = new FileOutputStream(outputFile)) {
      byte[] bytes = new byte[8192]; // 8 KB buffer
      while (bytesRead < partSizeInBytes) {
        int read = is.read(bytes);
        if (read == -1) {
          return bytesRead;
        }
        os.write(bytes);
        bytesRead += read;
      }
    }

    return bytesRead;
  }

  public static void cleanupPartsFiles(Collection<File> partsFiles) {
    for (File partFile : partsFiles) {
      if (partFile.exists()) {
        partFile.delete();
      }
    }
  }

  public static int calculateNumberOfParts(long fileSize, long partSizeInBytes) {
    if (fileSize % partSizeInBytes == 0) {
      return (int) (fileSize / partSizeInBytes);
    } else {
      return (int) (fileSize / partSizeInBytes) + 1;
    }
  }

  private static String prepareFilePartBasePath(File file, String dir) {
    String baseName = file.getName();
    int dotIndex = baseName.lastIndexOf('.');
    if (dotIndex != -1) {
      baseName = baseName.substring(0, dotIndex);
    }
    return String.format(
        "%s%s%s_parts_%d_%d_",
        dir, File.separator, baseName, System.currentTimeMillis(), Thread.currentThread().getId());
  }
}
