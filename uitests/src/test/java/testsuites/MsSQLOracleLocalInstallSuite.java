package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;

@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory(MsSQLOracleLocalInstallTest.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class
})

public class MsSQLOracleLocalInstallSuite {
}
