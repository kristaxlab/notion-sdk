package io.kristixlab.notion.api.http.request;

import io.kristixlab.notion.api.http.client.HttpClient.*;
import io.kristixlab.notion.api.http.request.MultipartFormDataRequest;
import io.kristixlab.notion.api.json.JsonConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates library-agnostic {@link Body} instances from SDK request objects.
 *
 * <p>Supports two payload types:
 *
 * <ul>
 *   <li><b>JSON</b> — any non-{@code null} body that is not a {@link MultipartFormDataRequest} is
 *       serialized via {@link JsonConverter} into a {@link StringBody} with content type {@code
 *       application/json}.
 *   <li><b>Multipart</b> — a {@link MultipartFormDataRequest} is converted to a {@link
 *       MultipartBody}. File parts are always wrapped as {@link InputStreamPart}s and streamed in
 *       chunks; they are never loaded into memory regardless of file size.
 * </ul>
 *
 * <p>A {@code null} body produces {@code null} (used by GET / HEAD / DELETE).
 *
 * @see HttpClient.Body
 */
public class HttpBodyFactory {

  private static final String APPLICATION_JSON = "application/json";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

  private HttpBodyFactory() {}

  // ==================================================================
  // Public API
  // ==================================================================

  /**
   * Creates a library-agnostic {@link Body} for the given payload object.
   *
   * @param body the request payload — may be {@code null}, a {@link MultipartFormDataRequest}, or
   *     any JSON-serializable object
   * @return a {@link Body} instance, or {@code null} when {@code body} is {@code null}
   */
  public static Body create(Object body) {
    if (body == null) {
      return null;
    }
    if (body instanceof MultipartFormDataRequest multipart) {
      return createMultipartBody(multipart);
    }
    return createJsonBody(body);
  }

  /**
   * Returns {@code true} when the body will be serialized as JSON and the caller should set a
   * {@code Content-Type: application/json} request header.
   *
   * @param body the request payload (may be {@code null})
   * @return {@code true} for JSON payloads, {@code false} for multipart or {@code null} bodies
   */
  public static boolean requiresJsonContentType(Object body) {
    return body != null && !(body instanceof MultipartFormDataRequest);
  }

  // ==================================================================
  // JSON
  // ==================================================================

  private static StringBody createJsonBody(Object body) {
    String json = JsonConverter.getInstance().toJson(body);
    return new StringBody(json, APPLICATION_JSON);
  }

  // ==================================================================
  // Multipart — assembly
  // ==================================================================

  private static MultipartBody createMultipartBody(MultipartFormDataRequest multipart) {
    List<Part> parts = new ArrayList<>();

    for (var part : multipart.getParts()) {
      if (part instanceof MultipartFormDataRequest.FilePart filePart) {
        parts.add(toFilePart(filePart));

      } else if (part instanceof MultipartFormDataRequest.InputStreamPart streamPart) {
        parts.add(toInputStreamPart(streamPart));

      } else if (part instanceof MultipartFormDataRequest.ByteArrayPart bytesPart) {
        parts.add(toBytesPart(bytesPart));

      } else {
        // Plain text part
        parts.add(new TextPart(part.getName(), String.valueOf(part.getContent())));
      }
    }

    return new MultipartBody(parts);
  }

  // ==================================================================
  // Multipart — per-part conversion
  // ==================================================================

  /**
   * Converts a {@link MultipartFormDataRequest.FilePart} to an {@link InputStreamPart}.
   *
   * <p>Files are always streamed — they are never loaded into memory regardless of size.
   */
  private static InputStreamPart toFilePart(MultipartFormDataRequest.FilePart filePart) {
    File file = validatedFile(filePart);
    String fileName = filePart.getFileName() != null ? filePart.getFileName() : file.getName();
    String contentType = resolveContentType(filePart.getHeaders().get("Content-Type"));
    try {
      InputStream is = new FileInputStream(file);
      return new InputStreamPart(filePart.getName(), fileName, is, contentType);
    } catch (IOException e) {
      throw new RuntimeException("Failed to open file for streaming: " + file.getAbsolutePath(), e);
    }
  }

  /** Converts a {@link MultipartFormDataRequest.InputStreamPart} to an {@link InputStreamPart}. */
  private static InputStreamPart toInputStreamPart(
      MultipartFormDataRequest.InputStreamPart streamPart) {
    String fileName =
        streamPart.getFileName() != null ? streamPart.getFileName() : streamPart.getName();
    String contentType = resolveContentType(streamPart.getHeaders().get("Content-Type"));
    return new InputStreamPart(
        streamPart.getName(), fileName, streamPart.getContent(), contentType);
  }

  /** Converts a {@link MultipartFormDataRequest.ByteArrayPart} to a {@link BytesPart}. */
  private static BytesPart toBytesPart(MultipartFormDataRequest.ByteArrayPart bytesPart) {
    byte[] bytes = bytesPart.getContent();
    if (bytes == null || bytes.length == 0) {
      throw new IllegalArgumentException(
          "Byte array is null or empty for part: " + bytesPart.getName());
    }
    String contentType = resolveContentType(bytesPart.getHeaders().get("Content-Type"));
    return new BytesPart(bytesPart.getName(), bytesPart.getFileName(), bytes, contentType);
  }

  // ==================================================================
  // Shared utilities
  // ==================================================================

  /**
   * Resolves the content type, falling back to {@code application/octet-stream} when absent or
   * empty.
   */
  private static String resolveContentType(String contentType) {
    return (contentType != null && !contentType.isEmpty()) ? contentType : APPLICATION_OCTET_STREAM;
  }

  /** Validates that the file part references an existing file on disk. */
  private static File validatedFile(MultipartFormDataRequest.FilePart filePart) {
    File file = filePart.getContent();
    if (file == null || !file.exists()) {
      throw new IllegalArgumentException(
          "File is null or does not exist: " + (file != null ? file.getAbsolutePath() : "null"));
    }
    return file;
  }
}
