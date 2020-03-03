package testsuites;

import cireporttests.navigation.NavigationTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.experimental.categories.Categories;
import testsuites.suiteinterface.MsSQLTest;
import cireporttests.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.MySQLTest;

@RunWith(ConcurrentSuiteRunner.class)
@Categories.IncludeCategory(MsSQLTest.class)
@Suite.SuiteClasses ({
        LoginTests.class,
        NavigationTests.class,
        AssemblyDetailsReportTests.class,
        MachiningDtcReportTests.class,
        CastingDtcReportTests.class,
        CastingDtcComparisonReportTests.class,
        CastingDtcDetailsReportTests.class
})

public class MsSQLSuite {
}
