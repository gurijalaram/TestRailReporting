package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.experimental.categories.Categories;
import testsuites.suiteinterface.MsSQLTest;
import cireporttests.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Categories.IncludeCategory(MsSQLTest.class)
@Suite.SuiteClasses ({
        LoginTests.class
})
public class MsSQLSuite {
}
