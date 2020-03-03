package testsuites;

import cireporttests.navigation.NavigationTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.experimental.categories.Categories;
import testsuites.suiteinterface.OracleTest;
import cireporttests.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Categories.IncludeCategory(OracleTest.class)
@Suite.SuiteClasses({
        LoginTests.class,
        NavigationTests.class,
        AssemblyDetailsReportTests.class,
        MachiningDtcReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcComparisonReportTests.class,
        CastingDtcDetailsReportTests.class,
        ciadmintests.navigation.NavigationTests.class
})

public class OracleSuite {
}
