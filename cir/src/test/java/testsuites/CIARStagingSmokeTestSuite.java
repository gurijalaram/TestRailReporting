package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import ciadmintests.navigation.AdminNavigationTests;
import login.LoginTests;
import navigation.ReportsNavigationTests;
import ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CIARStagingSmokeTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CIARStagingSmokeTest.class)
@Suite.SuiteClasses({
    AdminNavigationTests.class,
    ReportsNavigationTests.class,
    LoginTests.class,
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcComparisonReportTests.class
})

public class CIARStagingSmokeTestSuite {
}
