package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.REPORTS_API;

import com.ootbreports.newreportstests.componentcost.ComponentCostReportTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.newreportstests.costoutlieridentification.CostOutlierIdentificationReportTests;
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
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
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
import com.ootbreports.newreportstests.upgradecomparison.UpgradePartComparisonReportTests;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AssemblyCostA4ReportTests.class,
    AssemblyCostLetterReportTests.class,
    AssemblyDetailsReportTests.class,
    BasicCostAvoidanceReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    ComponentCostReportTests.class,
    CostOutlierIdentificationReportTests.class,
    CostOutlierIdentificationDetailsReportTests.class,
    CycleTimeValueTrackingReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    DigitalFactoryPerformanceReportTests.class,
    DigitalFactoryPerformanceDetailsReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    PotentialSavingsValueTrackingDetailsReportTests.class,
    PotentialSavingsValueTrackingReportTests.class,
    RecommendedTestPartsReportTests.class,
    ScenarioActivityDigitalFactoryActivityReportTests.class,
    ScenarioActivityMaterialActivityReportTests.class,
    ScenarioActivityProcessActivityReportTests.class,
    ScenarioActivityProcessGroupActivityReportTests.class,
    ScenarioActivityReportTests.class,
    ScenarioActivityTotalActivityReportTests.class,
    ScenarioComparisonReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsReportTests.class,
    SheetMetalDtcReportTests.class,
    SpendAnalysisValueTrackingDetailsReportTests.class,
    SpendAnalysisValueTrackingDetailsSimplifiedReportTests.class,
    SpendAnalysisValueTrackingReportTests.class,
    SpendAnalysisValueTrackingSimplifiedReportTests.class,
    TargetCostTrendReportTests.class,
    TargetCostValueTrackingDetailsReportTests.class,
    TargetCostValueTrackingReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingDetailsReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class,
    UpgradeComparisonReportTests.class,
    UpgradePartComparisonReportTests.class
})
public class ReportsApiTestSuite {
}
