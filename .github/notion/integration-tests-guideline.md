
## Integration Tests List (publicly available Notion Database)
https://kristalamenweb.notion.site/2e8c5b968ec4804d8b91c99c1e04b0ca

## Rules for Integration Tests implementation

1. One test per class @DisplayName on the test method level is required. The value should reflect test id (ex, "IT-1:
   Pages - Check Basic CRUD operations"). If you do not know test id yet, use a placeholder like "IT-?: ..."
3. All the prerequisites for the test should be created in the test itself in @BeforeEach setup () method unless:
    - necessary state can not be created via API
        - _in this case necessary blocks should be created via UI (manually) in the template of a Notion database
          hosting the test pages. The blocks should be included into a nested page named with test id (ex, IT-4_)
    - The prerequisite is a common entity that rarely changes and requires read call to Notion API (like a user)
        - in this case, prerequisite might be taken from the NotionTestContext if it is already available there. If not,
          consider extending NotionTestContext with new data (see {@link NotionTestPagesProvisioner,
          NotionTestRootPageProvisioner} for initializing context data). Simple usage of @BeforeEach setup () is still
          an option if NotionTestContext extensions makes code too complex.

## Configuration supported

TO BE DONE ADDED