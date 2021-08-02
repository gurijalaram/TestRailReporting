package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("262")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    LoginTests.class
})
public class EDCSmokeTestSuite {
}
