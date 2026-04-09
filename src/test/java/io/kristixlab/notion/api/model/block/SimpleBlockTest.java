package io.kristixlab.notion.api.model.block;

import static org.junit.jupiter.api.Assertions.*;

import io.kristixlab.notion.api.model.common.Color;
import org.junit.jupiter.api.Test;

class SimpleBlockTest {

  // DividerBlock

  @Test
  void divider_constructor_setsTypeAndDivider() {
    DividerBlock block = new DividerBlock();

    assertEquals("divider", block.getType());
    assertNotNull(block.getDivider());
  }

  // BreadcrumbBlock

  @Test
  void breadcrumb_constructor_setsTypeAndBreadcrumb() {
    BreadcrumbBlock block = new BreadcrumbBlock();

    assertEquals("breadcrumb", block.getType());
    assertNotNull(block.getBreadcrumb());
  }

  @Test
  void breadcrumb_of_createsNewInstance() {
    BreadcrumbBlock block = BreadcrumbBlock.of();

    assertEquals("breadcrumb", block.getType());
    assertNotNull(block.getBreadcrumb());
  }

  // EquationBlock

  @Test
  void equation_constructor_setsTypeAndEquation() {
    EquationBlock block = new EquationBlock();

    assertEquals("equation", block.getType());
    assertNotNull(block.getEquation());
  }

  @Test
  void equation_of_setsExpression() {
    EquationBlock block = EquationBlock.of("E = mc^2");

    assertEquals("equation", block.getType());
    assertEquals("E = mc^2", block.getEquation().getExpression());
  }

  @Test
  void equation_getterSetter() {
    EquationBlock block = new EquationBlock();

    block.getEquation().setExpression("x^2 + y^2 = r^2");
    assertEquals("x^2 + y^2 = r^2", block.getEquation().getExpression());
  }

  // TableOfContentsBlock

  @Test
  void tableOfContents_constructor_setsType() {
    TableOfContentsBlock block = new TableOfContentsBlock();

    assertEquals("table_of_contents", block.getType());
    assertNotNull(block.getTableOfContents());
  }

  @Test
  void tableOfContents_of_createsNewInstance() {
    TableOfContentsBlock block = TableOfContentsBlock.of();

    assertEquals("table_of_contents", block.getType());
    assertNull(block.getTableOfContents().getColor());
  }

  @Test
  void tableOfContents_ofColor_setsColor() {
    TableOfContentsBlock block = TableOfContentsBlock.of(Color.GRAY);

    assertEquals("gray", block.getTableOfContents().getColor());
  }

  @Test
  void tableOfContents_ofNullColor_noColorSet() {
    TableOfContentsBlock block = TableOfContentsBlock.of(null);

    assertNull(block.getTableOfContents().getColor());
  }

  @Test
  void tableOfContents_innerClass_getterSetter() {
    TableOfContentsBlock.TableOfContents toc = new TableOfContentsBlock.TableOfContents();

    assertNull(toc.getColor());
    toc.setColor("blue");
    assertEquals("blue", toc.getColor());
  }

  // UnknownBlock

  @Test
  void unknownBlock_isSubclassOfBlock() {
    UnknownBlock block = new UnknownBlock();

    assertInstanceOf(Block.class, block);
  }

  // UnsupportedBlock

  @Test
  void unsupportedBlock_constructor_setsNothing() {
    UnsupportedBlock block = new UnsupportedBlock();

    assertInstanceOf(Block.class, block);
    assertNull(block.getUnsupported());
  }

  // ChildPageBlock

  @Test
  void childPage_constructor_setsType() {
    ChildPageBlock block = new ChildPageBlock();

    assertEquals("child_page", block.getType());
    assertNotNull(block.getChildPage());
  }

  @Test
  void childPage_of_setsTitle() {
    ChildPageBlock block = ChildPageBlock.of("My Page");

    assertEquals("child_page", block.getType());
    assertEquals("My Page", block.getChildPage().getTitle());
  }

  @Test
  void childPage_innerGetterSetter() {
    ChildPageBlock.ChildPage cp = new ChildPageBlock.ChildPage();

    assertNull(cp.getTitle());
    cp.setTitle("Test");
    assertEquals("Test", cp.getTitle());
  }

  // ChildDatabaseBlock

  @Test
  void childDatabase_constructor_setsType() {
    ChildDatabaseBlock block = new ChildDatabaseBlock();

    assertEquals("child_database", block.getType());
    assertNotNull(block.getChildDatabase());
  }

  @Test
  void childDatabase_innerGetterSetter() {
    ChildDatabaseBlock.ChildDatabase cd = new ChildDatabaseBlock.ChildDatabase();

    assertNull(cd.getTitle());
    cd.setTitle("My DB");
    assertEquals("My DB", cd.getTitle());
  }

  // LinkPreviewBlock

  @Test
  void linkPreview_constructor_setsType() {
    LinkPreviewBlock block = new LinkPreviewBlock();

    assertEquals("link_preview", block.getType());
    assertNotNull(block.getLinkPreview());
  }

  @Test
  void linkPreview_innerGetterSetter() {
    LinkPreviewBlock.LinkPreview lp = new LinkPreviewBlock.LinkPreview();

    assertNull(lp.getUrl());
    lp.setUrl("https://example.com");
    assertEquals("https://example.com", lp.getUrl());
  }

  // TemplateBlock

  @Test
  void template_constructor_setsType() {
    TemplateBlock block = new TemplateBlock();

    assertEquals("template", block.getType());
    assertNotNull(block.getTemplate());
  }

  @Test
  void template_innerGetterSetter() {
    TemplateBlock.Template template = new TemplateBlock.Template();

    assertNull(template.getRichText());
    assertNull(template.getChildren());
  }
}
