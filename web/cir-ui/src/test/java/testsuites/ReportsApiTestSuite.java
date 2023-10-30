package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.ootbreports.newreportstests.componentcost.ComponentCostReportTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationDetailsTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationTests;
import com.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.newreportstests.digitalfactoryperformance.DigitalFactoryPerformanceDetailsReportTests;
import com.ootbreports.newreportstests.digitalfactoryperformance.DigitalFactoryPerformanceReportTests;
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
import com.ootbreports.newreportstests.general.basiccostavoidance.BasicCostAvoidanceReportTests;
import com.ootbreports.newreportstests.potentialsavingsvaluetracking.PotentialSavingsValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.potentialsavingsvaluetracking.PotentialSavingsValueTrackingReportTests;
import com.ootbreports.newreportstests.recommendedtestparts.RecommendedTestPartsReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityDigitalFactoryActivityReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityMaterialActivityReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityProcessActivityReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityProcessGroupActivityReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityReportTests;
import com.ootbreports.newreportstests.scenarioactivity.ScenarioActivityTotalActivityReportTests;
import com.ootbreports.newreportstests.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingDetailsSimplifiedReportTests;
import com.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingReportTests;
import com.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingSimplifiedReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostTrendReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import com.ootbreports.newreportstests.upgradecomparison.UpgradeComparisonReportTests;
import com.ootbreports.newreportstests.upgradecomparison.UpgradePartComparisonTests;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags(API_SANITY)
@SelectClasses({
    AssemblyCostA4ReportTests.class,
    AssemblyCostLetterReportTests.class,
    AssemblyDetailsReportTests.class,
    BasicCostAvoidanceReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    /*ComponentCostReportTests.class,
    CostOutlierIdentificationTests.class,
    CostOutlierIdentificationDetailsTests.class,
    CycleTimeValueTrackingReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    DigitalFactoryPerformanceReportTests.class,
    DigitalFactoryPerformanceDetailsReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    PotentialSavingsValueTrackingReportTests.class,
    PotentialSavingsValueTrackingDetailsReportTests.class,
    RecommendedTestPartsReportTests.class,
    ScenarioComparisonReportTests.class,
    ScenarioActivityReportTests.class,
    ScenarioActivityDigitalFactoryActivityReportTests.class,
    ScenarioActivityMaterialActivityReportTests.class,
    ScenarioActivityProcessActivityReportTests.class,
    ScenarioActivityProcessGroupActivityReportTests.class,
    ScenarioActivityTotalActivityReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsTests.class,*/
    SheetMetalDtcReportTests.class,
    SpendAnalysisValueTrackingReportTests.class,
    SpendAnalysisValueTrackingDetailsReportTests.class,
    SpendAnalysisValueTrackingSimplifiedReportTests.class,
    SpendAnalysisValueTrackingDetailsSimplifiedReportTests.class,
    TargetCostTrendReportTests.class,
    TargetCostValueTrackingReportTests.class,
    TargetCostValueTrackingDetailsReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class,
    TargetAndQuotedCostValueTrackingDetailsReportTests.class,
    UpgradeComparisonReportTests.class,
    UpgradePartComparisonTests.class
})
public class ReportsApiTestSuite {
}
