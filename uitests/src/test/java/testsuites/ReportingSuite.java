package testsuites;

import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import cireporttests.navigation.NavigationTests;
import cireporttests.login.LoginTests;
import com.apriori.utils.ProjectRunID;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class
})
public class ReportingSuite {
}