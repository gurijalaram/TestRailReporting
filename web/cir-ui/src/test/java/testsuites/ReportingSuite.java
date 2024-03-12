package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.REPORTS;

import com.apriori.cir.ui.tests.ootbreports.assemblycost.AssemblyCostReportTests;
import com.apriori.cir.ui.tests.ootbreports.componentcost.ComponentCostReportTests;
import com.apriori.cir.ui.tests.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import com.apriori.cir.ui.tests.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.apriori.cir.ui.tests.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.castingdtc.CastingDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.plasticdtc.PlasticDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.plasticdtc.PlasticDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.apriori.cir.ui.tests.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import com.apriori.cir.ui.tests.ootbreports.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.apriori.cir.ui.tests.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.apriori.cir.ui.tests.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori.cir.ui.tests.ootbreports")
@IncludeTags(REPORTS)
/*@SelectClasses({
    AssemblyDetailsReportTests.class,
    AssemblyCostReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    CastingDtcReportTests.class,
    ComponentCostReportTests.class,
    CostOutlierIdentificationDetailsReportTests.class,
    CostOutlierIdentificationReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    CycleTimeValueTrackingReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    MachiningDtcReportTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    ScenarioComparisonReportTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsReportTests.class,
    SheetMetalDtcReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingDetailsReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class
})*/
public class ReportingSuite {
}