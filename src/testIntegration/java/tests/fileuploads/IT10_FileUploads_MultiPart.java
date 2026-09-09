package tests.fileuploads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.kristaxlab.notion.endpoints.util.FileUploadUtils;
import io.kristaxlab.notion.model.file.FileUpload;
import io.kristaxlab.notion.model.file.FileUploadCreateParams;
import io.kristaxlab.notion.model.file.FileUploadSendParams;
import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import testkit.BaseIntegrationTest;
import testkit.util.FileLoader;

/**
 * Multi-part uploads are rejected for files under 5 MB, so this test needs a large file and a paid
 * plan to run.
 */
@Tag("paid_plan")
public class IT10_FileUploads_MultiPart extends BaseIntegrationTest {

  private static final String VIDEO_PATH = "files/video_sakura_18mb.MOV";
  private static final String UPLOADED_FILENAME = "it-20-video_18mb.MOV";
  private static final long PART_SIZE_IN_BYTES = 5242880L; // 5 MB
  private static final String PARTS_DIR = "./temp/";

  private File videoFile;
  private int numberOfParts;
  private Map<Integer, File> parts;

  @BeforeEach
  public void setup() throws Exception {
    videoFile = FileLoader.loadFileFailIfMissing(VIDEO_PATH, getClass().getClassLoader());
    numberOfParts = FileUploadUtils.calculateNumberOfParts(videoFile.length(), PART_SIZE_IN_BYTES);
    assertTrue(numberOfParts > 1, "The test file must be large enough to be split into parts");

    parts = FileUploadUtils.splitFileIntoParts(videoFile, PART_SIZE_IN_BYTES, PARTS_DIR);
    assertEquals(numberOfParts, parts.size());
  }

  @Test
  @DisplayName("IT-10: File Uploads - Upload a multi-part file")
  public void testUploadMultiPartFile() {
    // 1. Create the multi-part upload announcing how many parts will follow
    FileUpload created =
        getNotionClient()
            .fileUploads()
            .create(FileUploadCreateParams.multiPart(UPLOADED_FILENAME, numberOfParts));

    assertEquals("pending", created.getStatus());
    assertEquals(UPLOADED_FILENAME, created.getFilename());
    assertNotNull(created.getNumberOfParts());
    assertEquals(numberOfParts, created.getNumberOfParts().getTotal().intValue());

    // 2. Send every part, the upload stays pending until the last one arrives
    String fileUploadId = created.getId();
    parts.forEach(
        (partNumber, part) -> {
          FileUpload partRs =
              getNotionClient()
                  .fileUploads()
                  .upload(
                      fileUploadId,
                      FileUploadSendParams.builder()
                          .partNumber(partNumber)
                          .file(part)
                          .filename(UPLOADED_FILENAME)
                          .contentType(created.getContentType())
                          .build());

          assertEquals("pending", partRs.getStatus());
          assertNotNull(partRs.getNumberOfParts());
          assertEquals(numberOfParts, partRs.getNumberOfParts().getTotal().intValue());
          assertEquals(partNumber, partRs.getNumberOfParts().getSent());
        });

    // 3. Complete the upload
    FileUpload completed = getNotionClient().fileUploads().complete(fileUploadId);

    assertEquals(fileUploadId, completed.getId());
    assertEquals("uploaded", completed.getStatus());
    assertEquals(UPLOADED_FILENAME, completed.getFilename());
    assertNotNull(completed.getContentType());
    assertNotNull(completed.getContentLength());
    assertEquals(videoFile.length(), completed.getContentLength().longValue());
    assertEquals(numberOfParts, completed.getNumberOfParts().getTotal().intValue());
    assertEquals(numberOfParts, completed.getNumberOfParts().getSent().intValue());
  }

  @AfterEach
  public void cleanupParts() {
    if (parts != null) {
      FileUploadUtils.cleanupPartsFiles(parts.values());
    }
  }
}
