package testsuites;

import com.apriori.fieldsvalidation.MountTypePinCountTests;
import com.apriori.help.HelpTests;
import com.apriori.myuser.MyProfileTests;
import com.apriori.myuser.TermsOfUseTests;
import com.apriori.pcba.FilterPartsTests;
import com.apriori.pcba.UploadTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
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
