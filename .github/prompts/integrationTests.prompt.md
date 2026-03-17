I want you to generate integration test for the following endpoint: {endpoint}
An integration test must inherit from the base test class integration.BaseIntegrationTest.
It should be placed in src/test/java/integration/ folder.
Name it: {endpoint}IT.

Every test should contain @DisplayName annotaion with text:
[TEST_ID]: {endpoint} - TEST_NAME

I will provide you with the list of tests to be implemented in the json format (it is a resonce from my
Notion database with the list of tests). You will fine TEST_ID in column 'ID' and TEST_NAME in column 'Name' (the title).
Pay attention to the 'Scenario' column, it contains the steps to be performed in the test.
You should implement these steps in the test method.

You will find a json attached to this prompt with the list of tests to be implemented.

Implementation best practices:
1. Many models have shortcut constructors (static methods "of", "from", etc.), prefer them over the multi-lined object building.
2. Assertions should check the bare minimum (not nulls and the values very specific to the test step processed)

If it is not clear for you what exactly did I mean, ask me a question before proceeding with the implementation.
Do not start implementing the tests until you are sure that you understand the requirements and the steps to be
performed in each test.

