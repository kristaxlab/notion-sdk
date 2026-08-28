## Integration Tests List (publicly available Notion Database)

https://kristalamenweb.notion.site/2e8c5b968ec4804d8b91c99c1e04b0ca

## Prerequisites

Integration tests interact with Notion API, often create new blocks, edit them, upload files and so on. So before
running integration tests, you need to have a Notion account and a Notion integration with access to the workspace where
you want to run the tests.

You need to set up the following environment variable with the value of your Notion integration token:

- NOTION_TEST_AUTH_TOKEN

And specify the "home" for running the tests, It must be an id of a data source where a new page will be created for 
each test session.

- NOTION_TESTS_HOME_ID

Or in **junit-platform.properties** file as 'notion.tests.home.id', example:

```notion.tests.home.id=337cd6cf-1406-80be-b839-d74142f3ebf4```

Many tests require prerequisites that are impossible to create via Notion API so the tests home page should 

## Rules for Integration Tests implementation

1. One test per class.
2. @DisplayName on the test method level is required. The value should reflect test id (ex, "IT-1:
   Pages - Check Basic CRUD operations"). If you do not know test id yet, use a placeholder like "IT-?: ..."
3. All the prerequisites for the test should be created in the test itself in @BeforeEach setup () method unless:
    - necessary state can not be created via API
        - in this case necessary blocks should be created via UI (manually) in the template of a Notion database hosting
          the test pages. The blocks should be included into a nested page named with test id (ex, IT-4: ...)
    - The prerequisite is a common entity that rarely changes and requires read call to Notion API (like a user)
        - in this case, prerequisite might be taken from the NotionTestContext if it is already available there. If not,
          consider extending NotionTestContext with new data (see {@link NotionTestPagesProvisioner,
          NotionTestRootPageProvisioner} for initializing context data). Simple usage of @BeforeEach setup () is still
          an option if extending NotionTestContext leads to the code overcomplication.

## Configuration supported

TODO make a table with all the configuration parameters and their description

Root Notion data source (or page) for running tests:
```notion.tests.home.id=337cd6cf-1406-80be-b839-d74142f3ebf4```

**Base url for Notion Test Page link in logs:**
'notion.tests.page.base.url' parameters lets to override the base url used by to log test page url after every test
execution (by default it is "https://www.notion.so/", see **NotionTestPageIdLoggingExtension** class)