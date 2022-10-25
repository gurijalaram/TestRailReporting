package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.fieldsvalidation.mounttypepincount.MountTypePinCountTests;
import com.help.HelpTests;
import com.myuser.MyProfileTests;
import com.myuser.TermsOfUseTests;
import com.upload.pcba.FilterPartsTests;
import com.upload.pcba.UploadTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("262")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    MyProfileTests.class,
    TermsOfUseTests.class,
    HelpTests.class,
    UploadTests.class,
    FilterPartsTests.class,
    MountTypePinCountTests.class
})
public class RegressionTestSuite {
}
