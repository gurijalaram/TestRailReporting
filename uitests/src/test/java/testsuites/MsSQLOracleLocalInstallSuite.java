package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;
import testsuites.suiteinterface.MySQLTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(MsSQLOracleLocalInstallTest.class)
@Categories.ExcludeCategory(MySQLTest.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class
})

public class MsSQLOracleLocalInstallSuite {
}
