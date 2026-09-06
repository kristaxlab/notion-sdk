package suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import tests.blocks.IT13_Blocks_SeveralBlocksAtOnce;
import tests.blocks.IT16_Blocks_InsertToPosition;
import tests.blocks.IT22_Blocks_ParagraphCRUD;
import tests.fileuploads.IT3_FileUploads_SinglePartFile;
import tests.fileuploads.IT5_FileUploads_ListAll;
import tests.pages.*;
import tests.users.IT32_Users_ListAll;

@Suite
@SuiteDisplayName("QuickCheck")
@SelectClasses({
  IT22_Blocks_ParagraphCRUD.class,
  IT13_Blocks_SeveralBlocksAtOnce.class,
  IT16_Blocks_InsertToPosition.class,
  IT1_Pages_CRUD.class,
  IT2_Pages_Move.class,
  IT3_Pages_PropertyValuesCRUD.class,
  IT4_Pages_Content.class,
  IT5_Pages_Markdown.class,
  IT10_Pages_RichTextPropertyPaginated.class,
  IT3_FileUploads_SinglePartFile.class,
  IT5_FileUploads_ListAll.class,
  IT32_Users_ListAll.class
})
public class QuickCheck {}
