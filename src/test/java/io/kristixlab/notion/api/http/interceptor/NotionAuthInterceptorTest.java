package io.kristixlab.notion.api.http.interceptor;

import io.kristixlab.notion.api.http.base.client.HttpClient.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotionAuthInterceptorTest {

    @Test
    @DisplayName("beforeSend adds Notion-Version and Authorization if missing")
    void beforeSend_addsHeadersIfMissing() {
        NotionAuthInterceptor interceptor = new NotionAuthInterceptor("Bearer test-token", "2026-03-11");
        HttpRequest req = HttpRequest.builder()
                .url("https://api.notion.com/v1/pages")
                .method(io.kristixlab.notion.api.http.base.client.HttpClient.HttpMethod.GET)
                .headers(Map.of())
                .build();
        HttpRequest result = interceptor.beforeSend(req);
        assertEquals("2026-03-11", result.headers().get("Notion-Version"));
        assertEquals("Bearer test-token", result.headers().get("Authorization"));
    }

    @Test
    @DisplayName("beforeSend does not overwrite existing Authorization header")
    void beforeSend_doesNotOverwriteAuthorization() {
        NotionAuthInterceptor interceptor = new NotionAuthInterceptor("Bearer test-token", "2026-03-11");
        HttpRequest req = HttpRequest.builder()
                .url("https://api.notion.com/v1/pages")
                .method(io.kristixlab.notion.api.http.base.client.HttpClient.HttpMethod.GET)
                .headers(Map.of("Authorization", "Basic abc123"))
                .build();
        HttpRequest result = interceptor.beforeSend(req);
        assertEquals("2026-03-11", result.headers().get("Notion-Version"));
        assertEquals("Basic abc123", result.headers().get("Authorization"));
    }

    @Test
    @DisplayName("beforeSend preserves other headers and adds Notion-Version")
    void beforeSend_preservesOtherHeaders() {
        NotionAuthInterceptor interceptor = new NotionAuthInterceptor("Bearer test-token", "2026-03-11");
        HttpRequest req = HttpRequest.builder()
                .url("https://api.notion.com/v1/pages")
                .method(io.kristixlab.notion.api.http.base.client.HttpClient.HttpMethod.GET)
                .headers(Map.of("X-Custom", "foo"))
                .build();
        HttpRequest result = interceptor.beforeSend(req);
        assertEquals("2026-03-11", result.headers().get("Notion-Version"));
        assertEquals("foo", result.headers().get("X-Custom"));
        assertEquals("Bearer test-token", result.headers().get("Authorization"));
    }
}

