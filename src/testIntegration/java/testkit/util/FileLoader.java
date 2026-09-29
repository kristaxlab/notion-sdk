package testkit.util;

import static org.junit.jupiter.api.Assertions.fail;

import io.kristaxlab.notion.NotionClient;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import java.net.URL;

/** Uploads a classpath resource through the setup client. */
public class FileLoader {

  public static String uploadFile(String filePath, String fileName, NotionClient client) {
    URL url = FileLoader.class.getClassLoader().getResource(filePath);
    if (url == null) {
      throw new IllegalStateException(
          String.format("File %s should exist to proceed with the test", filePath));
    }

    File file = new File(url.getFile());
    FileUpload fu = client.fileUploads().create(FileUploadCreateParams.singlePart(fileName));
    client
        .fileUploads()
        .upload(
            fu.getId(),
            FileUploadSendParams.builder().file(file).contentType(fu.getContentType()).build());
    return fu.getId();
  }

  public static File loadFileFailIfMissing(String filePath) {
    return loadFileFailIfMissing(filePath, FileLoader.class.getClassLoader());
  }

  public static File loadFileFailIfMissing(String filePath, ClassLoader classLoader) {
    URL url = FileLoader.class.getClassLoader().getResource(filePath);

    if (url == null) {
      fail(
          String.format(
              "File %s should exist in resources directory to proceed with the test", filePath));
    }

    return new File(url.getFile());
  }

  public static String readFileToString(File file) {
    try (java.util.Scanner scanner = new java.util.Scanner(file)) {
      scanner.useDelimiter("\\A");
      return scanner.hasNext() ? scanner.next() : "";
    } catch (java.io.FileNotFoundException e) {
      fail(String.format("File %s should exist to proceed with the test", file.getPath()));
      return null; // Unreachable, but required for compilation
    }
  }
}
