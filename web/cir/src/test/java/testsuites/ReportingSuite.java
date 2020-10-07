package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import login.LoginTests;
import navigation.ReportsNavigationTests;
import ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
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