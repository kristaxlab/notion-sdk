package suites;

import integration.pages.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Pages")
@SelectClasses({
  IT1_Pages_CRUD.class,
  IT4_Pages_Move.class,
  IT6_Pages_PropertyValuesCRUD.class,
  IT7_Pages_PropertyStandalone.class,
  IT8_Pages_Templates.class,
  IT9_Pages_Duplicate.class,
  IT10_Pages_Content.class,
  IT40_Pages_Lock.class,
  IT43_Pages_Markdown.class
})
public class PagesSuite {}
