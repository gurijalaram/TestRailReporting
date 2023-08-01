package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.OnPremTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(OnPremTest.class)
@Suite.SuiteClasses({
    LoginTests.class
})

public class OnPremCirSuite {
}