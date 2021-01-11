package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    AssemblyCostReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcComparisonReportTests.class,
    ComponentCostReportTests.class,
    CycleTimeValueTrackingTests.class,
    CycleTimeValueTrackingDetailsTests.class,
    LoginTests.class,
    MachiningDtcReportTests.class,
    MachiningDtcDetailsReportTests.class,
    MachiningDtcComparisonReportTests.class,
    PlasticDtcReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcComparisonReportTests.class,
    ReportsNavigationTests.class,
    ScenarioComparisonReportTests.class,
    SheetMetalDtcReportTests.class,
    SheetMetalDtcDetailsReportTests.class,
    SheetMetalDtcComparisonReportTests.class
})
public class ReportingSuite {
}