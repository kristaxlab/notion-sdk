package io.kristaxlab.notion.model.block;

/**
 * Language hint tokens accepted when creating meeting notes.
 *
 * <p>Model fields stay {@link String}; use {@link #getValue()} when building requests (ADR 0002).
 */
public enum MeetingNotesLanguage {
  /** Automatic language detection. */
  AUTO("auto"),
  /** English. */
  EN("en"),
  /** Simplified Chinese. */
  ZH_CN("zh-CN"),
  /** Traditional Chinese. */
  ZH_TW("zh-TW"),
  /** Spanish. */
  ES("es"),
  /** French. */
  FR("fr"),
  /** German. */
  DE("de"),
  /** Japanese. */
  JA("ja"),
  /** Korean. */
  KO("ko"),
  /** Portuguese. */
  PT("pt"),
  /** Russian. */
  RU("ru"),
  /** Thai. */
  TH("th"),
  /** Vietnamese. */
  VI("vi"),
  /** Indonesian. */
  ID("id"),
  /** Danish. */
  DA("da"),
  /** Finnish. */
  FI("fi"),
  /** Norwegian. */
  NO("no"),
  /** Dutch. */
  NL("nl"),
  /** Italian. */
  IT("it"),
  /** Swedish. */
  SV("sv"),
  /** Arabic. */
  AR("ar"),
  /** Hebrew. */
  HE("he"),
  /** Polish. */
  PL("pl");

  private final String value;

  MeetingNotesLanguage(String value) {
    this.value = value;
  }

  /**
   * Returns the API token for this language.
   *
   * @return Notion language string (e.g. {@code "en"}, {@code "auto"})
   */
  public String getValue() {
    return value;
  }
}
