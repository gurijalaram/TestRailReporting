package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.navigation.AdminNavigationTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.AdminSmokeTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(AdminSmokeTest.class)
@Suite.SuiteClasses(
        AdminNavigationTests.class
)

public class AdminSmokeSuite {
}
