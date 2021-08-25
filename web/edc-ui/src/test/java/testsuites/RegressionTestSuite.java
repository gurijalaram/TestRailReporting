package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.help.HelpTests;
import com.login.LoginTests;
import com.logout.LogoutTests;
import com.myuser.MyProfileTests;
import com.myuser.TermsOfUseTests;
import com.upload.pcba.UploadTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("262")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    MyProfileTests.class,
    TermsOfUseTests.class,
    LogoutTests.class,
    HelpTests.class,
    UploadTests.class
})
public class RegressionTestSuite {
}
