package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import login.LoginTests;
import navbar.NavBarTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    NavBarTests.class,
})

public class CICTestSuite {
}
