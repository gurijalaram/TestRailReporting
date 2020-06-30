package testsuites;

import ciadmintests.navigation.AdminNavigationTests;
import cireporttests.navigation.ReportsNavigationTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.login.LoginTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;

import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MsSQLOracleLocalInstallTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(MsSQLOracleLocalInstallTest.class)
@Suite.SuiteClasses({
    AdminNavigationTests.class,
    ReportsNavigationTests.class,
    LoginTests.class,
    AssemblyDetailsReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    MachiningDtcReportTests.class
})

public class MsSQLOracleLocalInstallSuite {
}
