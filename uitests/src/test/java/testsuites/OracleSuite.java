package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import cireporttests.login.LoginTests;
import cireporttests.navigation.NavigationTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.OracleTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(OracleTest.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class
})

public class OracleSuite {
}
