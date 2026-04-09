package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.FileData;
import org.junit.jupiter.api.Test;

class MediaBlockTest {

  // ImageBlock

  @Test
  void image_constructor_setsTypeAndInitializes() {
    ImageBlock block = new ImageBlock();

    assertEquals("image", block.getType());
    assertNotNull(block.getImage());
  }

  @Test
  void image_of_setsFileData() {
    FileData fileData = FileData.external("https://example.com/image.png");

    ImageBlock block = ImageBlock.of(fileData);

    assertEquals("image", block.getType());
    assertSame(fileData, block.getImage());
  }

  @Test
  void image_of_fileUpload() {
    FileData fileData = FileData.fileUpload("upload-123");

    ImageBlock block = ImageBlock.of(fileData);

    assertEquals("file_upload", block.getImage().getType());
    assertEquals("upload-123", block.getImage().getFileUpload().getId());
  }

  // VideoBlock

  @Test
  void video_constructor_setsTypeAndInitializes() {
    VideoBlock block = new VideoBlock();

    assertEquals("video", block.getType());
    assertNotNull(block.getVideo());
  }

  @Test
  void video_of_setsFileData() {
    FileData fileData = FileData.external("https://example.com/video.mp4");

    VideoBlock block = VideoBlock.of(fileData);

    assertEquals("video", block.getType());
    assertSame(fileData, block.getVideo());
  }

  // AudioBlock

  @Test
  void audio_constructor_setsTypeAndInitializes() {
    AudioBlock block = new AudioBlock();

    assertEquals("audio", block.getType());
    assertNotNull(block.getAudio());
  }

  @Test
  void audio_of_setsFileData() {
    FileData fileData = FileData.external("https://example.com/audio.mp3");

    AudioBlock block = AudioBlock.of(fileData);

    assertEquals("audio", block.getType());
    assertSame(fileData, block.getAudio());
  }

  // PdfBlock

  @Test
  void pdf_constructor_setsTypeAndInitializes() {
    PdfBlock block = new PdfBlock();

    assertEquals("pdf", block.getType());
    assertNotNull(block.getPdf());
  }

  @Test
  void pdf_of_setsFileData() {
    FileData fileData = FileData.external("https://example.com/doc.pdf");

    PdfBlock block = PdfBlock.of(fileData);

    assertEquals("pdf", block.getType());
    assertSame(fileData, block.getPdf());
  }

  // FileBlock

  @Test
  void file_constructor_setsTypeAndInitializes() {
    FileBlock block = new FileBlock();

    assertEquals("file", block.getType());
    assertNotNull(block.getFile());
  }

  @Test
  void file_of_setsFileData() {
    FileData fileData = FileData.external("https://example.com/attachment.zip");

    FileBlock block = FileBlock.of(fileData);

    assertEquals("file", block.getType());
    assertSame(fileData, block.getFile());
  }
}
