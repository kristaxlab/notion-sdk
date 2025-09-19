package io.kristixlab.notion;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.api.model.authorization.IntrospectTokenResponse;

public class Main {

  public static final String NOTION_PRIV_INTEGRATION_TOKEN = "NOTION_PRIV_INTEGRATION_TOKEN";

  public static void main(String[] args) {

//    NotionApiClient notion = new NotionApiClient(
//            "25ad872b-594c-80f3-9105-00377ec19266",
//            "secret_DaQtu8UEPrySzLTpwcOlyJ6ODhyuQtBiQfYWgbS8qkz",
//            "https://www.krista.com/",
//            "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",
//            "nrt_530762011562PukLqSHwnrVuF1nCjBngnb61KQKIOP67Wx");


    NotionApiClient notion = new NotionApiClient();
    notion.getAuthSettings().setClientId("CLIENT_ID");
    notion.getAuthSettings().setClientSecret("CLIENT_SECRET");
    notion.getAuthSettings().setRedirectUri("https://www.redirecturl.com/");
    notion.getAuthSettings().setAccessToken("ACCESS_TOKEN");
    notion.getAuthSettings().setRefreshToken("REFRESH_TOKEN");


//
//    Page page = notion.pages().retrieve("pageId");
//    notion.blocks().appendChildren("blockId", ParagraphBlock.of("Some text"));
//
//    // many users - auth throw
//    String token = System.getenv(NOTION_PRIV_INTEGRATION_TOKEN);

    // TODO move requires workaround
//    notion
//        .pages()
//        .byId("269c5b96-8ec4-802f-8db0-c14ce20b9c23")
//        .moveToPage("269c5b96-8ec4-8092-92c3-f95d202e1bb2")
//        .execute();

//    notion
//        .pages()
//        .byId("269c5b96-8ec4-802f-8db0-c14ce20b9c23")
//        .appendBlock(ParagraphBlock.of("Hello", Color.BROWN))
//        .execute();

//    notion.databases().byId("68d0eeec-0823-4291-9c12-1ca4a0b56ce6").addPage(Map.of(
//                    "Name", RichText.of("New page with date string"),
//                    "Начало", LocalDateTime.of(2025, 6, 10, 14, 0)))
//            .execute();
    //https://www.notion.so/kristalamenweb/71e83792b32a4f60aeb01203b681ded6?v=a2e2b9629238403abb31421bddf3aa1f&source=copy_link
    //
    //    String token = "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";
    //    NotionClient notionClient = new NotionClient(token, null);
    //    Notion notion = new Notion(notionClient);
    //
    //    Page rs = notion.page("226c5b968ec4801fad8ecd6c19d8e0a8").withContent().execute();

    // BlocksResponse rs = notion.page("226c5b968ec4801fad8ecd6c19d8e0a8").withContent().execute();
    // JsonConverter.getInstance().print(rs);

    // Block block = notion.blocks().retrieve("247c5b96-8ec4-80c1-af61-d53dc8b7f7e9");
    // System.out.println(JsonConverter.getInstance().toJson(block));

    // TODO add possibility to find block by text or by type and sequence

    //        notion.databases().byId("").update(Columns.remove("name")
    //                .another().add().checkbox("name")
    //
    // .another().update().multiselect("asd").addOption("option").removeOption("option2")
    //        );
    //
    //        notion.databases().byId("").pages(Columns.remove("name")
    //                .another().add().checkbox("name")
    //
    // .another().update().multiselect("asd").addOption("option").removeOption("option2")
    //        );

  }

  //  Content Block Properties
  //          paragraph
  //  heading_1
  //          heading_2
  //  heading_3
  //          quote
  //  callout
  //          code
  //  equation
  //

  //  Embed Block Properties
  //          embed
  //  bookmark

  //  Layout Block Properties
  //          column_list
  //  column
  //          table
  //  table_row
  //  Special Block Properties
  //          divider
  //          breadcrumb
  //          table_of_contents
  //          link_to_page
  //          template    // unsupported
  //          synced_block
  //          ai_block  // unsupported
}
