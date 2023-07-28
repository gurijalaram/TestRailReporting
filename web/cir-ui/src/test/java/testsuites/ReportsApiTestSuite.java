package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.ootbreports.newreportstests.componentcost.ComponentCostReportTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationDetailsTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationTests;
import com.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcDetailsTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.newreportstests.general.assemblycost.AssemblyCostA4ReportTests;
import com.ootbreports.newreportstests.general.assemblycost.AssemblyCostLetterReportTests;
import com.ootbreports.newreportstests.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.newreportstests.recommendedtestparts.RecommendedTestPartsReportTests;
import com.ootbreports.newreportstests.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostTrendReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AssemblyCostA4ReportTests.class,
    AssemblyCostLetterReportTests.class,
    AssemblyDetailsReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    ComponentCostReportTests.class,
    CostOutlierIdentificationTests.class,
    CostOutlierIdentificationDetailsTests.class,
    CycleTimeValueTrackingReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    RecommendedTestPartsReportTests.class,
    ScenarioComparisonReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsTests.class,
    SheetMetalDtcReportTests.class,
    TargetCostTrendReportTests.class,
    TargetCostValueTrackingReportTests.class,
    TargetCostValueTrackingDetailsReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class,
    TargetAndQuotedCostValueTrackingDetailsReportTests.class
})
public class ReportsApiTestSuite {
}
