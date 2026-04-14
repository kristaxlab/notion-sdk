# Authentication

Authentication with Access Token is required to access the Notion API.

- **Access Token**
  - For internal integrations it is an **Integration Secret**. Obtain this from the [Notion integrations dashboard](https://www.notion.so/my-integrations). After creating an integration, copy the "Internal Integration Secret".
  - For public integrations, it is the **Access Token** obtained after completing the OAuth authorization process.

## Creating a NotionClient with an Access Token

To use the Notion API, create a `NotionClient` with your access token as follows:

```java
NotionClient client = NotionClient.newBuilder()
    .authToken("ntn_xxx")
    .build();

```

```java
NotionClient client = NotionClient.forToken("ntn_xxx");

```

## Handling the Notion OAuth Flow

*This section will cover how to implement the OAuth 2.0 flow for Notion integrations, including obtaining and refreshing access tokens.*

- **OAuth Credentials**: For OAuth flows, you will need:
    - **Client ID** and **Client Secret**: Provided when you register your integration in the Notion integrations dashboard.
    - **Redirect URIs**: Set these in your integration settings to match your application's OAuth redirect endpoints.

<!-- TODO: Add detailed guide for OAuth flow -->

## See Also

- [Getting Started](getting-started.md) -- installation and first API call
- [Configuration]() -- all supported auth methods
