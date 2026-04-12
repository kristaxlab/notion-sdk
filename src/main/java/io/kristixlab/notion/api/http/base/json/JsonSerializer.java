package io.kristixlab.notion.api.http.base.json;

/**
 * Library-agnostic serialization contract used throughout the HTTP layer.
 *
 * <p>All classes in {@link io.kristixlab.notion.api.http} that need JSON serialization or
 * deserialization depend on this interface rather than on a concrete implementation. This enables:
 *
 * <ul>
 *   <li>Swapping the underlying library (Jackson, Gson, Moshi, …) without touching HTTP code
 *   <li>Injecting test doubles or mocks in unit tests
 * </ul>
 */
public interface JsonSerializer {

  /**
   * Deserializes a JSON string into an instance of the given type.
   *
   * @param json the JSON string
   * @param type the target class
   * @param <T> the target type
   * @return the deserialized object
   */
  <T> T toObject(String json, Class<T> type);

  /**
   * Serializes an object to a JSON string.
   *
   * @param object the object to serialize
   * @return the JSON representation
   */
  String toJson(Object object);
}
