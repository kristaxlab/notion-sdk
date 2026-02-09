package io.kristixlab.notion.api.http.transport.rq;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class MultipartFormDataRequest {
  private String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
  private List<Part> parts = new ArrayList<>();

  public void addFilePart(String name, File file, String fileName, String contentType) {
    FilePart part = new FilePart();
    part.setName(name);
    part.setFileName(fileName);
    part.setContent(file);
    part.getHeaders().put("Content-Type", contentType);
    parts.add(part);
  }

  public void addFilePart(String name, File file) {
    FilePart part = new FilePart();
    part.setName(name);
    part.setContent(file);
    parts.add(part);
  }

  public void addInputStreamPart(String name, InputStream inputStream, String fileName, String contentType) {
    InputStreamPart part = new InputStreamPart();
    part.setName(name);
    part.setFileName(fileName);
    part.setContent(inputStream);
    part.getHeaders().put("Content-Type", contentType);
    parts.add(part);
  }

  public void addByteArrayPart(String name, byte[] array, String fileName, String contentType) {
    ByteArrayPart part = new ByteArrayPart();
    part.setName(name);
    part.setContent(array);
    part.setFileName(fileName);
    part.getHeaders().put("Content-Type", contentType);
    parts.add(part);
  }

  public void addPart(String name, Object value) {
    Part part = new Part();
    part.setName(name);
    part.setContent(value);
    parts.add(part);
  }

  @Data
  public static class Part<T> {
    String name;
    Map<String, String> headers = new HashMap<>();

    T content;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class FilePart extends Part<File> {
    String fileName;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class InputStreamPart extends Part<InputStream> {
    String fileName;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ByteArrayPart extends Part<byte[]> {
    String fileName;
  }
}
