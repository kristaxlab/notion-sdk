package suites;

import integration.blocks.IT22_Blocks_ParagraphCRUD;
import integration.blocks.IT26_Blocks_SeveralBlocksAtOnce;
import integration.blocks.IT31_Blocks_InsertToPosition;
import integration.fileuploads.IT14_FileUploads_SinglePartFile;
import integration.fileuploads.IT16_FileUploads_ListAll;
import integration.pages.*;
import integration.users.IT32_Users_ListAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("QuickCheck")
@SelectClasses({
  IT22_Blocks_ParagraphCRUD.class,
  IT26_Blocks_SeveralBlocksAtOnce.class,
  IT31_Blocks_InsertToPosition.class,
  IT1_Pages_CRUD.class,
  IT4_Pages_Move.class,
  IT6_Pages_PropertyValuesCRUD.class,
  IT7_Pages_PropertyStandalone.class,
  IT10_Pages_Content.class,
  IT14_FileUploads_SinglePartFile.class,
  IT16_FileUploads_ListAll.class,
  IT32_Users_ListAll.class
})
public class QuickCheck {}
