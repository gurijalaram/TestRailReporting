package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ciadmintests.navigation.NavigationTests;
import cireporttests.login.LoginTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CIARStagingSmokeTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CIARStagingSmokeTest.class)
@Suite.SuiteClasses({
    NavigationTests.class,
    cireporttests.navigation.NavigationTests.class,
    LoginTests.class,
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcComparisonReportTests.class
})

public class CIARStagingSmokeTestSuite {
}
