package io.kristixlab.notion.api.exchange;

import java.util.HashMap;
import java.util.Map;

public class ApiRequestUtil {

  /**
   * Creates a map of path parameters for API requests.
   *
   * @param key The key for the path parameter
   * @param value The value for the path parameter
   * @return Map containing the path parameters
   */
  public static Map<String, String> createPathParams(String key, String value) {
    return createPathParams(key, value, null, null);
  }

  /**
   * Creates a map of path parameters for API requests.
   *
   * @param key The key for the path parameter
   * @param value The value for the path parameter
   * @return Map containing the path parameters
   */
  public static Map<String, String> createPathParams(
      String key, String value, String key2, String value2) {
    Map<String, String> pathParams = new HashMap<>();
    if (key != null && value != null) {
      pathParams.put(key, value);
    }
    if (key2 != null && value2 != null) {
      pathParams.put(key2, value2);
    }
    return pathParams;
  }

  /**
   * Creates a map of query parameters for pagination.
   *
   * @param startCursor The cursor to start from (optional)
   * @param pageSize The number of items to return (optional, max 100)
   * @return Map containing the query parameters
   */
  public static Map<String, String[]> createQueryParams(String startCursor, Integer pageSize) {
    Map<String, String[]> queryParams = new HashMap<>();
    if (startCursor != null && !startCursor.trim().isEmpty()) {
      queryParams.put("start_cursor", new String[] {startCursor});
    }
    if (pageSize != null) {
      validatePageSize(pageSize);
      queryParams.put("page_size", new String[] {pageSize.toString()});
    }
    return queryParams;
  }

  /**
   * Validates the page size.
   *
   * @param pageSize The page size to validate
   * @throws IllegalArgumentException if the page size is less than 1 or greater than 100
   */
  private static void validatePageSize(int pageSize) {
    if (pageSize < 1 || pageSize > 100) {
      throw new IllegalArgumentException("Page size must be between 1 and 100");
    }
  }
}
