package testsuites;

import cireporttests.login.LoginTests;
import cireporttests.navigation.NavigationTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(MsSQLOracleLocalInstallTest.class)
@Suite.SuiteClasses({
    LoginTests.class,
    NavigationTests.class,
    ciadmintests.navigation.NavigationTests.class,
    AssemblyDetailsReportTests.class
})

public class MsSQLOracleLocalInstallSuite {
}
