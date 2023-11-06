package testsuites;

import com.apriori.edc.ui.tests.fieldsvalidation.MountTypePinCountTests;
import com.apriori.edc.ui.tests.help.HelpTests;
import com.apriori.edc.ui.tests.myuser.MyProfileTests;
import com.apriori.edc.ui.tests.myuser.TermsOfUseTests;
import com.apriori.edc.ui.tests.pcba.FilterPartsTests;
import com.apriori.edc.ui.tests.pcba.UploadTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
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
