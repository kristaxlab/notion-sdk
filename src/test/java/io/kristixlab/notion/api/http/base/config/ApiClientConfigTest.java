package io.kristixlab.notion.api.http.base.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ApiClientConfigTest {

    @Test
    @DisplayName("Builder sets and gets values correctly")
    void builder_setsAndGetsValues() {
        ApiClientConfig config = ApiClientConfig.builder()
                .apiBaseUrl("https://api.example.com")
                .build();
        assertEquals("https://api.example.com", config.get(ApiClientConfig.API_BASE_URL).orElse(null));
    }

    @Test
    @DisplayName("getOrDefault returns fallback if value is absent")
    void getOrDefault_returnsFallbackIfAbsent() {
        ApiClientConfig config = ApiClientConfig.defaults();
        String fallback = "https://default.example.com";
        assertEquals(fallback, config.getOrDefault(ApiClientConfig.API_BASE_URL, fallback));
    }

    @Test
    @DisplayName("get returns Optional.empty if value is absent")
    void get_returnsEmptyIfAbsent() {
        ApiClientConfig config = ApiClientConfig.defaults();
        assertEquals(Optional.empty(), config.get(ApiClientConfig.API_BASE_URL));
    }

    @Test
    @DisplayName("Builder sets and gets custom ConfigKey values")
    void builder_setCustomKey() {
        ConfigKey<Integer> customKey = ConfigKey.of("customInt");
        ApiClientConfig config = ApiClientConfig.builder()
                .set(customKey, 42)
                .build();
        assertEquals(42, config.get(customKey).orElse(-1));
    }
}

