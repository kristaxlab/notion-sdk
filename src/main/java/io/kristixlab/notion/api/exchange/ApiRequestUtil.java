package io.kristixlab.notion.api.exchange;

import io.kristixlab.notion.api.exchange.transport.URLInfo;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Util class to work with okhttp3 librry
 */
public class ApiRequestUtil {

  /**
   * Builds url string with query and path params applied.
   *
   * @param baseUrl will be added as prefix to urlInfo if urlInfo url does not start with http:// or https://
   * @param urlInfo object containing info about url + query + pqth params
   * @return url string enriched by provided query and path param values
   */
  public static String buildURL(String baseUrl, URLInfo urlInfo) {
    String processedUrl = urlInfo.getUrl();
    if (processedUrl != null && !processedUrl.isEmpty()) {
      if (!urlInfo.getUrl().startsWith("http://") && !urlInfo.getUrl().startsWith("https://")) {
        processedUrl = baseUrl + (processedUrl.startsWith("/") ? "" : "/") + processedUrl;
      }
    }
    if (urlInfo.getPathParams() != null && !urlInfo.getPathParams().isEmpty()) {
      for (Map.Entry<String, String> entry : urlInfo.getPathParams().entrySet()) {
        String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
        processedUrl = processedUrl.replace("{" + entry.getKey() + "}", value);
      }
    }

    HttpUrl httpUrl = HttpUrl.parse(processedUrl);
    if (httpUrl == null) {
      throw new IllegalArgumentException("Invalid URL: " + processedUrl);
    }

    HttpUrl.Builder urlBuilder = httpUrl.newBuilder();

    // Add query parameters
    if (urlInfo.getQueryParams() != null && !urlInfo.getQueryParams().isEmpty()) {
      urlInfo.getQueryParams().forEach(
              (key, values) -> {
                if (values != null) {
                  for (String value : values) {
                    if (value != null) {
                      urlBuilder.addQueryParameter(key, value);
                    }
                  }
                }
              });
    }

    return urlBuilder.build().toString();
  }

  public static RequestBody fileToRequestBody(FileRequest file) {
    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    if (file.getFileContent() != null) {
      RequestBody fileBody = RequestBody.create(file.getFileContent(), MediaType.parse(file.getContentType()));
      multipartBuilder.addFormDataPart("file", file.getFileName(), fileBody);
    }
    if (file.getAdditionalInfo() != null) {
      file.getAdditionalInfo().forEach((key, value) -> multipartBuilder.addFormDataPart(key, value));
    }
    return multipartBuilder.build();
  }
}
