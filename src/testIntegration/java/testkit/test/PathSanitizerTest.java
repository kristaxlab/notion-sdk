package testkit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import testkit.util.PathSanitizer;

class PathSanitizerTest {

  @ParameterizedTest(name = "[{index}] {0} → {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "IT1_Pages_CRUD | IT1_Pages_CRUD",
        "IT5_Pages_Retrieve | IT5_Pages_Retrieve",
        "unknownClass | unknownClass",
        "Outer$Inner | Outer$Inner",
        "a/b\\c | a_b_c",
        "foo:bar*baz | foo_bar_baz",
        "name with spaces | name_with_spaces",
        "file.name.ext | file_name_ext",
        "___keep___ | keep",
        " leading | leading",
        "trailing_ | trailing",
        "weird<>name | weird_name",
        "q?u*o[t]e | q_u_o_t_e"
      })
  void sanitizesToFilesystemSafeIdentifier(String name, String expected) {
    assertEquals(expected, PathSanitizer.sanitize(name));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void nullOrEmptyReturnsEmpty(String name) {
    assertEquals("", PathSanitizer.sanitize(name));
  }
}
