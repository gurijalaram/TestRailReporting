package testsuites;

import com.apriori.fieldsvalidation.MountTypePinCountTests;
import com.apriori.help.HelpTests;
import com.apriori.myuser.MyProfileTests;
import com.apriori.myuser.TermsOfUseTests;
import com.apriori.pcba.FilterPartsTests;
import com.apriori.pcba.UploadTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    MyProfileTests.class,
    TermsOfUseTests.class,
    HelpTests.class,
    UploadTests.class,
    FilterPartsTests.class,
    MountTypePinCountTests.class
})
public class RegressionTestSuite {
}
