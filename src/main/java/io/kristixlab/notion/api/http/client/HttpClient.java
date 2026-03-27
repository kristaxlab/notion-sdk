package io.kristixlab.notion.api.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Library-agnostic HTTP transport interface. Implementations bridge to a concrete HTTP library. */
public interface HttpClient {

  /** Sends the request and blocks until the full response (including body) is available. */
  HttpResponse send(HttpRequest request) throws IOException;

  enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE
  }

  record HttpRequest(String url, HttpMethod method, Map<String, String> headers, Body body) {
    public HttpRequest {
      headers = headers != null ? Map.copyOf(headers) : Map.of();
    }

    /** Creates a {@link Builder} pre-populated with the values of this request. */
    public Builder toBuilder() {
      return new Builder().url(url).method(method).headers(new LinkedHashMap<>(headers)).body(body);
    }

    public static Builder builder() {
      return new Builder();
    }

    public static final class Builder {
      private String url;
      private HttpMethod method = HttpMethod.GET;
      private Map<String, String> headers = new LinkedHashMap<>();
      private Body body;

      public Builder url(String url) {
        this.url = url;
        return this;
      }

      public Builder method(HttpMethod method) {
        this.method = method;
        return this;
      }

      public Builder headers(Map<String, String> headers) {
        this.headers = headers != null ? new LinkedHashMap<>(headers) : new LinkedHashMap<>();
        return this;
      }

      /** Adds or replaces a single header. */
      public Builder header(String name, String value) {
        this.headers.put(name, value);
        return this;
      }

      public Builder body(Body body) {
        this.body = body;
        return this;
      }

      public HttpRequest build() {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("url is required");
        if (method == null) throw new IllegalArgumentException("method is required");
        return new HttpRequest(url, method, headers, body);
      }
    }
  }

  record HttpResponse(int statusCode, Map<String, List<String>> headers, byte[] bodyBytes) {
    public String bodyAsString() {
      return bodyBytes == null
          ? null
          : new String(bodyBytes, java.nio.charset.StandardCharsets.UTF_8);
    }
  }

  /** Sealed body hierarchy — implementations map these to the underlying HTTP library. */
  sealed interface Body
      permits EmptyBody, StringBody, BytesBody, FileBody, InputStreamBody, MultipartBody {}

  /** Explicit empty body for POST/PUT/PATCH requests that require a body but carry no content. */
  record EmptyBody() implements Body {}

  /** A string body with an explicit content-type (e.g., JSON). */
  record StringBody(String content, String contentType) implements Body {}

  /** Raw bytes body with an explicit content-type. */
  record BytesBody(byte[] bytes, String contentType) implements Body {}

  /** File-backed request body. */
  record FileBody(File file, String contentType) implements Body {}

  /** Streaming request body. The stream is consumed once and is not re-readable. */
  record InputStreamBody(InputStream inputStream, long contentLength, String contentType)
      implements Body {}

  /** Multipart/form-data body. */
  record MultipartBody(List<Part> parts) implements Body {}

  sealed interface Part permits TextPart, FilePart, BytesPart, InputStreamPart {}

  record TextPart(String name, String value) implements Part {}

  record FilePart(String name, String filename, File file, String contentType) implements Part {}

  record BytesPart(String name, String filename, byte[] bytes, String contentType)
      implements Part {}

  /** Streaming multipart part. The stream is consumed once and is not re-readable. */
  record InputStreamPart(String name, String filename, InputStream inputStream, String contentType)
      implements Part {}
}
