package suites;

import integration.users.IT25_Users_RetrieveMe;
import integration.users.IT32_Users_ListAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Users")
@SelectClasses({IT25_Users_RetrieveMe.class, IT32_Users_ListAll.class})
public class UsersSuite {}
