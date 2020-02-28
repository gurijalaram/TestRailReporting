package testsuites;

import cireporttests.login.LoginTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLTest;

@RunWith(ConcurrentSuiteRunner.class)
@Categories.IncludeCategory(MsSQLTest.class)
@Suite.SuiteClasses ({
        LoginTests.class
})
public class MsSQLSuite {
}
