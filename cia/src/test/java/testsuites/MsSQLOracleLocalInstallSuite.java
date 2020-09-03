package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import navigation.AdminNavigationTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(MsSQLOracleLocalInstallTest.class)
@Suite.SuiteClasses({
    AdminNavigationTests.class,
})

public class MsSQLOracleLocalInstallSuite {
}
