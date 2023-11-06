package testsuites;

import com.apriori.cir.ui.tests.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.componentcost.ComponentCostReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.digitalfactoryperformance.DigitalFactoryPerformanceDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.digitalfactoryperformance.DigitalFactoryPerformanceReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost.AssemblyCostA4ReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblycost.AssemblyCostLetterReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.general.basiccostavoidance.BasicCostAvoidanceReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.potentialsavingsvaluetracking.PotentialSavingsValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.potentialsavingsvaluetracking.PotentialSavingsValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.recommendedtestparts.RecommendedTestPartsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityDigitalFactoryActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityMaterialActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityProcessActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityProcessGroupActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenarioactivity.ScenarioActivityTotalActivityReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.scenariocomparison.ScenarioComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingDetailsSimplifiedReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.spendanalysisvaluetracking.SpendAnalysisValueTrackingSimplifiedReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetcosttrend.TargetCostTrendReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetcosttrend.TargetCostValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison.UpgradeComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.newreportstests.upgradecomparison.UpgradePartComparisonReportTests;

import org.junit.platform.suite.api.SelectClasses;
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
    TargetAndQuotedCostValueTrackingDetailsReportTests.class,
    UpgradeComparisonReportTests.class,
    UpgradePartComparisonReportTests.class
})
public class ReportsApiTestSuite {
}
