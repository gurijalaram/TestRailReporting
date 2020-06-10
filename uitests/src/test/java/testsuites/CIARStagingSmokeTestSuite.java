package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ciadmintests.navigation.NavigationTests;
import cireporttests.login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CIARStagingSmokeTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CIARStagingSmokeTest.class)
@Suite.SuiteClasses({
    NavigationTests.class,
    cireporttests.navigation.NavigationTests.class,
    LoginTests.class
})

public class CIARStagingSmokeTestSuite {
}
