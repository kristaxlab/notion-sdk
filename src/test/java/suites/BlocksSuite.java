package suites;

import integration.blocks.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Blocks")
@SelectClasses({
  IT22_Blocks_ParagraphCRUD.class,
  IT23_Blocks_UploadedImage.class,
  IT26_Blocks_SeveralBlocksAtOnce.class,
  IT29_Blocks_NestedBulletList.class,
  IT30_Blocks_ChangeBlockType.class,
  IT31_Blocks_InsertToPosition.class,
  IT42_Blocks_TextualBlocks.class,
  IT57_Blocks_HeadingBlocks.class,
  IT58_Blocks_TableBlocks.class,
  IT59_Blocks_LinksAndMedia.class,
  IT60_Blocks_SyncedBlocks.class,
  IT61_Blocks_OtherBlocks.class
})
public class BlocksSuite {}
