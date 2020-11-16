package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.casting.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.plastic.PlasticDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
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