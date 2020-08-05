package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import ciadmintests.navigation.AdminNavigationTests;
import cireporttests.login.LoginTests;
import cireporttests.navigation.ReportsNavigationTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import cireporttests.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import cireporttests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    ReportsNavigationTests.class,
    AssemblyDetailsReportTests.class,
    MachiningDtcReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class
})
public class ReportingSuite {
}