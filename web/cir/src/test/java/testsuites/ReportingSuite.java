package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
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
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    AssemblyCostReportTests.class,
    ComponentCostReportTests.class,
    MachiningDtcReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    LoginTests.class,
    PlasticDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    ReportsNavigationTests.class,
    ScenarioComparisonReportTests.class
})
public class ReportingSuite {
}