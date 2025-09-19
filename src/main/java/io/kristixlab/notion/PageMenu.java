package io.kristixlab.notion;

import io.kristixlab.notion.api.model.blocks.Block;
import io.kristixlab.notion.api.model.pages.Page;

import java.util.ArrayList;
import java.util.List;

import io.kristixlab.notion.api.NotionApiClient;
import io.kristixlab.notion.client.NotionClient;
import lombok.Data;

@Data
public class PageMenu {

  private final NotionClient client;
  private final String pageId;
  private String moveToPage;
  private String moveToDatasource;
  private Boolean moveToTopLevel;
  private List<Block> blocksToAppend = new ArrayList<>();

  public PageMenu(NotionClient client, String pageId) {
    this.client = client;
    this.pageId = pageId;
  }

  public PageMenu moveToPage(String pageId) {
    this.moveToPage = pageId;
    return this;
  }

  public PageMenu moveToDatasource(String datasourceId) {
    this.moveToDatasource = datasourceId;
    return this;
  }

  public PageMenu moveToTopLeve() {
    this.moveToTopLevel = true;
    return this;
  }

  public PageMenu appendBlock(Block block) {
    this.blocksToAppend.add(block);
    return this;
  }

  public Page execute() {
    return client.execute(this);
  }
}
