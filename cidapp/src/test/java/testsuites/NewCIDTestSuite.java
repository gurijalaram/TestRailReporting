package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("177")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    LoginTests.class,
})
public class NewCIDTestSuite {
}