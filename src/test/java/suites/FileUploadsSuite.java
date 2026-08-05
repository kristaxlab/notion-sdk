package suites;

import integration.fileuploads.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("File Uploads")
@SelectClasses({
  IT14_FileUploads_SinglePartFile.class,
  IT15_FileUploads_SinglePartStream.class,
  IT16_FileUploads_ListAll.class,
  IT17_FileUploads_ListExpired.class,
  IT18_FileUploads_SinglePartBytes.class,
  IT19_FileUploads_ImportExternal.class,
  IT20_FileUploads_MultiPart.class
})
public class FileUploadsSuite {}
