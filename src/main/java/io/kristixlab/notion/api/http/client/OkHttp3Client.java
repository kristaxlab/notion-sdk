package io.kristixlab.notion.api.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import okhttp3.*;
import okio.BufferedSink;

public class OkHttp3Client implements HttpClient {

  private final OkHttpClient ok;

  public OkHttp3Client() {
    this(new OkHttpClient());
  }

  public OkHttp3Client(OkHttpClient ok) {
    this.ok = Objects.requireNonNull(ok, "ok");
  }

  @Override
  public HttpResponse send(HttpRequest request) throws IOException {
    Objects.requireNonNull(request, "request");

    Request okReq = toOkHttpRequest(request);

    try (Response okRes = ok.newCall(okReq).execute()) {
      byte[] bytes = okRes.body() != null ? okRes.body().bytes() : null;

      Map<String, List<String>> headers = new LinkedHashMap<>();
      Headers h = okRes.headers();
      for (String name : h.names()) {
        headers.put(name, h.values(name));
      }

      return new HttpResponse(okRes.code(), headers, bytes);
    }
  }

  private Request toOkHttpRequest(HttpRequest r) {
    String url = Objects.requireNonNull(r.url(), "url");
    HttpMethod method = Objects.requireNonNull(r.method(), "method");

    Request.Builder b = new Request.Builder().url(url);

    if (r.headers() != null) {
      for (Map.Entry<String, String> e : r.headers().entrySet()) {
        if (e.getKey() != null && e.getValue() != null) {
          b.addHeader(e.getKey(), e.getValue());
        }
      }
    }

    RequestBody okBody = toOkHttpBody(method, r.body());

    b.method(method.name(), okBody);

    return b.build();
  }

  private RequestBody toOkHttpBody(HttpMethod method, Body body) {
    boolean allowsBody = allowsRequestBody(method);
    boolean requiresBody = requiresRequestBody(method);

    if (!allowsBody) {
      return null;
    }

    if (body == null) {
      return requiresBody ? emptyBody() : null;
    }

    if (body instanceof EmptyBody) {
      return emptyBody();
    }

    if (body instanceof StringBody sb) {
      MediaType mt = mediaTypeOrThrow(sb.contentType());
      return RequestBody.create(sb.content(), mt);
    }

    if (body instanceof BytesBody bb) {
      MediaType mt = mediaTypeOrThrow(bb.contentType());
      return RequestBody.create(bb.bytes(), mt);
    }

    if (body instanceof FileBody fb) {
      MediaType mt = mediaTypeOrThrow(fb.contentType());
      return RequestBody.create(fb.file(), mt);
    }

    if (body instanceof InputStreamBody isb) {
      return streamingBody(isb.inputStream(), isb.contentLength(), isb.contentType());
    }

    if (body instanceof MultipartBody mb) {
      okhttp3.MultipartBody.Builder mp =
          new okhttp3.MultipartBody.Builder().setType(okhttp3.MultipartBody.FORM);

      for (Part part : mb.parts()) {
        if (part instanceof TextPart tp) {
          mp.addFormDataPart(tp.name(), tp.value());
        } else if (part instanceof FilePart fp) {
          MediaType mt = mediaTypeOrThrow(fp.contentType());
          RequestBody partBody = RequestBody.create(fp.file(), mt);
          mp.addFormDataPart(fp.name(), fp.filename(), partBody);
        } else if (part instanceof BytesPart bp) {
          MediaType mt = mediaTypeOrThrow(bp.contentType());
          RequestBody partBody = RequestBody.create(bp.bytes(), mt);
          mp.addFormDataPart(bp.name(), bp.filename(), partBody);
        } else if (part instanceof InputStreamPart isp) {
          RequestBody partBody = streamingBody(isp.inputStream(), -1, isp.contentType());
          mp.addFormDataPart(isp.name(), isp.filename(), partBody);
        } else {
          throw new IllegalArgumentException("Unsupported multipart part: " + part.getClass());
        }
      }

      return mp.build();
    }

    throw new IllegalArgumentException("Unsupported Body: " + body.getClass());
  }

  private static MediaType mediaTypeOrThrow(String contentType) {
    if (contentType == null || contentType.isBlank()) {
      throw new IllegalArgumentException("contentType is required");
    }
    MediaType mt = MediaType.parse(contentType); // OkHttp 3
    if (mt == null) throw new IllegalArgumentException("Invalid contentType: " + contentType);
    return mt;
  }

  /**
   * Creates a streaming {@link RequestBody} that reads the {@link InputStream} in 8 KB chunks.
   *
   * @param inputStream the source stream (consumed once, then closed)
   * @param contentLength the known length, or {@code -1} if unknown (chunked transfer)
   * @param contentType the MIME type (must not be null/blank)
   */
  private static RequestBody streamingBody(
      InputStream inputStream, long contentLength, String contentType) {
    MediaType mt = mediaTypeOrThrow(contentType);
    return new RequestBody() {
      @Override
      public MediaType contentType() {
        return mt;
      }

      @Override
      public long contentLength() {
        return contentLength;
      }

      @Override
      public void writeTo(BufferedSink sink) throws IOException {
        try (inputStream) {
          byte[] buffer = new byte[8192];
          int bytesRead;
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            sink.write(buffer, 0, bytesRead);
          }
        }
      }
    };
  }

  private static RequestBody emptyBody() {
    return RequestBody.create(new byte[0], null);
  }

  private static boolean allowsRequestBody(HttpMethod method) {
    return method == HttpMethod.POST
        || method == HttpMethod.PUT
        || method == HttpMethod.PATCH
        || method == HttpMethod.DELETE;
  }

  private static boolean requiresRequestBody(HttpMethod method) {
    return method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH;
  }
}
