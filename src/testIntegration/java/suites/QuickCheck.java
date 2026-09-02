package suites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import tests.blocks.IT22_Blocks_ParagraphCRUD;
import tests.blocks.IT26_Blocks_SeveralBlocksAtOnce;
import tests.blocks.IT31_Blocks_InsertToPosition;
import tests.fileuploads.IT14_FileUploads_SinglePartFile;
import tests.fileuploads.IT16_FileUploads_ListAll;
import tests.pages.*;
import tests.users.IT32_Users_ListAll;

@Suite
@SuiteDisplayName("QuickCheck")
@SelectClasses({
  IT22_Blocks_ParagraphCRUD.class,
  IT26_Blocks_SeveralBlocksAtOnce.class,
  IT31_Blocks_InsertToPosition.class,
  IT1_Pages_CRUD.class,
  IT4_Pages_Move.class,
  IT6_Pages_PropertyValuesCRUD.class,
  IT7_Pages_RichTextPropertyPaginated.class,
  IT10_Pages_Content.class,
  IT14_FileUploads_SinglePartFile.class,
  IT16_FileUploads_ListAll.class,
  IT32_Users_ListAll.class
})
public class QuickCheck {}
