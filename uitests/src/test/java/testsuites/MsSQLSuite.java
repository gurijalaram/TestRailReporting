package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

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
import testsuites.suiteinterface.MsSQLTest;

@RunWith(ConcurrentSuiteRunner.class)
@Categories.IncludeCategory(MsSQLTest.class)
@Suite.SuiteClasses ({
        LoginTests.class,
        NavigationTests.class,
        AssemblyDetailsReportTests.class,
        MachiningDtcReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcComparisonReportTests.class,
        CastingDtcDetailsReportTests.class,
        ciadmintests.navigation.NavigationTests.class
})

public class MsSQLSuite {
}
