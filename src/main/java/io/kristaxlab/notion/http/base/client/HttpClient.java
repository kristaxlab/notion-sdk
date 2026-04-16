package io.kristaxlab.notion.http.base.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Library-agnostic HTTP transport interface. Implementations bridge to a concrete HTTP library. */
public interface HttpClient {

  /**
   * Sends the request and blocks until the full response (including body) is available.
   *
   * @param request HTTP request to execute
   * @return HTTP response payload
   * @throws IOException when the underlying transport fails
   */
  HttpResponse send(HttpRequest request) throws IOException;

  /** Supported HTTP methods. */
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

    /**
     * Creates a {@link Builder} pre-populated with the values of this request.
     *
     * @return pre-populated request builder
     */
    public Builder toBuilder() {
      return new Builder().url(url).method(method).headers(new LinkedHashMap<>(headers)).body(body);
    }

    /**
     * Creates a new request builder.
     *
     * @return request builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Mutable builder for {@link HttpRequest}. */
    public static final class Builder {
      private String url;
      private HttpMethod method = HttpMethod.GET;
      private Map<String, String> headers = new LinkedHashMap<>();
      private Body body;

      /**
       * Sets request URL.
       *
       * @param url absolute URL
       * @return this builder
       */
      public Builder url(String url) {
        this.url = url;
        return this;
      }

      /**
       * Sets request method.
       *
       * @param method HTTP method
       * @return this builder
       */
      public Builder method(HttpMethod method) {
        this.method = method;
        return this;
      }

      /**
       * Replaces all request headers.
       *
       * @param headers header map
       * @return this builder
       */
      public Builder headers(Map<String, String> headers) {
        this.headers = headers != null ? new LinkedHashMap<>(headers) : new LinkedHashMap<>();
        return this;
      }

      /** Adds or replaces a single header. */
      public Builder header(String name, String value) {
        this.headers.put(name, value);
        return this;
      }

      /**
       * Sets request body.
       *
       * @param body request body
       * @return this builder
       */
      public Builder body(Body body) {
        this.body = body;
        return this;
      }

      /**
       * Builds an immutable request object.
       *
       * @return immutable request
       * @throws IllegalArgumentException when required fields are missing
       */
      public HttpRequest build() {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("url is required");
        if (method == null) throw new IllegalArgumentException("method is required");
        return new HttpRequest(url, method, headers, body);
      }
    }
  }

  record HttpResponse(int statusCode, Map<String, List<String>> headers, byte[] bodyBytes) {
    /**
     * Decodes response bytes as UTF-8 text.
     *
     * @return response body string, or {@code null} when no body is present
     */
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
