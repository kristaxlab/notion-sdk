Write a set of unit tests for [Class] following these conventions established in this project:

Framework & libraries
JUnit 5 only — no mocking libraries. Use the existing io.kristixlab.notion.api.http.TransportStub
If you found a bug while creating unit tests, its ok that a unit tests fails until the bug is fixed. Report the bug(s)
you found to the team.

RULES FOR ENDPOINT TESTS

Test structure
One test class per endpoint, in package io.kristixlab.notion.api.endpoints.impl.

@BeforeEach setUp() instantiates TransportStub and the endpoint under test.
No inline comments (no Arrange / Act / Assert labels).

What every test must assert
transport.lastMethod — the expected HTTP verb ("GET", "POST", etc.).
transport.lastUrlInfo.getUrl() — the exact URL template string.
transport.lastUrlInfo.getPathParams() — all path parameters, when the URL contains {placeholders}.
transport.lastUrlInfo.getQueryParams() — every expected query parameter present, and every excluded one explicitly absent, when pagination or filters apply.
transport.lastBody — the request body object, for mutating calls (POST, PATCH, DELETE with body).

No assertions on the return value.
Validation / error tests
If possible, aggregate all IllegalArgumentException cases (null, "", blank " ") into a single @ParameterizedTest using @NullAndEmptySource + @ValueSource.

Naming
Short and descriptive, no test prefix.
Plain method name for the happy-path default call: e.g. retrieve(), listBlocks(), me().
Suffix pattern for variants: _withStartCursor, _withPageSize, _withBothPaginationParams, _rejectsBlankOrNullId, etc.

Reference files
Existing test to mirror: UsersEndpointImplTest.java
Transport stub: TransportStub.java