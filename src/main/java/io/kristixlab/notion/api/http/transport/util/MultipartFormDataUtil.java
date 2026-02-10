package io.kristixlab.notion.api.http.transport.util;

import io.kristixlab.notion.api.http.transport.rq.MultipartFormDataRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.*;
import okio.BufferedSink;

/** Util class to work with okhttp3 librry */
public class MultipartFormDataUtil {

  public static RequestBody toRequestBody(
      MultipartFormDataRequest multipartFormDataRequest, boolean asStream) {
    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    multipartFormDataRequest
        .getParts()
        .forEach(
            part -> {
              if (part instanceof MultipartFormDataRequest.FilePart filePart) {
                addFileToMultipart(filePart, builder, asStream);
              } else if (part instanceof MultipartFormDataRequest.ByteArrayPart byteArrayPart) {
                addBytesArrayToMultipart(byteArrayPart, builder);
              } else if (part instanceof MultipartFormDataRequest.InputStreamPart inputStreamPart) {
                addInputStreamToMultipart(inputStreamPart, builder);
              } else {
                builder.addFormDataPart(part.getName(), String.valueOf(part.getContent()));
              }
            });

    return builder.build();
  }

  /** adds file to request */
  private static void addFileToMultipart(
      MultipartFormDataRequest.FilePart filePart,
      MultipartBody.Builder rqBuilder,
      boolean asStream) {

    File file = filePart.getContent();
    if (file == null || !file.exists()) {
      String filePath = (file != null) ? file.getAbsolutePath() : "null";
      throw new IllegalArgumentException("File is null or does not exist: " + filePath);
    }

    RequestBody fileBody = null;
    String contentType = filePart.getHeaders().get("Content-Type");

    if (asStream) {
      fileBody = asStreamRequestBody(file, contentType);
    } else {
      try (InputStream inputStream = new FileInputStream(file)) {
        byte[] fileContent = inputStream.readAllBytes();
        if (contentType == null || contentType.isEmpty()) {
          fileBody = RequestBody.create(fileContent);
        } else {
          fileBody = RequestBody.create(fileContent, MediaType.parse(contentType));
        }
      } catch (IOException e) {
        throw new RuntimeException("Failed to read file content: " + file.getAbsolutePath(), e);
      }
    }

    if (filePart.getFileName() != null) {
      rqBuilder.addFormDataPart(filePart.getName(), filePart.getFileName(), fileBody);
    } else {
      rqBuilder.addPart(
          Headers.of(
              "Content-Disposition",
              "form-data; name=\""
                  + filePart.getName()
                  + "\"; filename=\""
                  + file.getName()
                  + "\""),
          fileBody);
    }
  }

  private static RequestBody asStreamRequestBody(File file, String contentType) {
    return new RequestBody() {
      private final File fileRef = file;

      @Override
      public MediaType contentType() {
        if (contentType != null && !contentType.isEmpty()) {
          return MediaType.parse(contentType);
        }
        return MediaType.parse("application/octet-stream");
      }

      @Override
      public long contentLength() throws IOException {
        return fileRef.length();
      }

      @Override
      public void writeTo(BufferedSink sink) throws IOException {
        try (InputStream is = new FileInputStream(fileRef)) {
          byte[] buffer = new byte[8192]; // 8 KB buffer
          int bytesRead;
          while ((bytesRead = is.read(buffer)) != -1) {
            sink.write(buffer, 0, bytesRead);
          }
        } catch (IOException e) {
          throw new IOException("Error reading file: " + fileRef.getAbsolutePath(), e);
        }
      }
    };
  }

  private static void addInputStreamToMultipart(
      MultipartFormDataRequest.InputStreamPart inputStreamPart, MultipartBody.Builder rqBuilder) {

    RequestBody isBody =
        asStreamRequestBody(
            inputStreamPart.getContent(), inputStreamPart.getHeaders().get("Content-Type"));
    if (inputStreamPart.getFileName() != null) {
      rqBuilder.addFormDataPart(inputStreamPart.getName(), inputStreamPart.getFileName(), isBody);
    } else {
      rqBuilder.addPart(
          Headers.of(
              "Content-Disposition",
              "form-data; name=\""
                  + inputStreamPart.getName()
                  + "\"; filename=\""
                  + inputStreamPart.getName()
                  + "\""),
          isBody);
    }
  }

  public static RequestBody asStreamRequestBody(InputStream inputStream, String contentType) {
    return new RequestBody() {

      @Override
      public MediaType contentType() {
        if (contentType != null && !contentType.isEmpty()) {
          return MediaType.parse(contentType);
        }
        return MediaType.parse("application/octet-stream");
      }

      @Override
      public void writeTo(BufferedSink sink) throws IOException {
        try (inputStream) {
          byte[] buffer = new byte[8192]; // 8 KB buffer
          int bytesRead;
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            sink.write(buffer, 0, bytesRead);
          }
        }
      }
    };
  }

  private static void addBytesArrayToMultipart(
      MultipartFormDataRequest.ByteArrayPart byteArrayPart, MultipartBody.Builder rqBuilder) {
    byte[] bytes = byteArrayPart.getContent();
    if (bytes == null || bytes.length == 0) {
      throw new IllegalArgumentException(
          "Byte array is null or empty for part: " + byteArrayPart.getName());
    }
    String contentType = byteArrayPart.getHeaders().get("Content-Type");
    RequestBody byteArrayBody;
    if (contentType == null || contentType.isEmpty()) {
      byteArrayBody = RequestBody.create(bytes);
    } else {
      byteArrayBody = RequestBody.create(bytes, MediaType.parse(contentType));
    }

    if (byteArrayPart.getFileName() != null) {
      rqBuilder.addFormDataPart(
          byteArrayPart.getName(), byteArrayPart.getFileName(), byteArrayBody);
    } else {
      rqBuilder.addPart(
          Headers.of("Content-Disposition", "form-data; name=\"" + byteArrayPart.getName() + "\""),
          byteArrayBody);
    }
  }
}
