package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
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
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.OnPremTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(OnPremTest.class)
@Suite.SuiteClasses({
    AssemblyDetailsReportTests.class,
    AssemblyCostReportTests.class,
    CastingDtcReportTests.class,
    CastingDtcComparisonReportTests.class,
    CastingDtcDetailsReportTests.class,
    ComponentCostReportTests.class,
    CostOutlierIdentificationReportTests.class,
    CostOutlierIdentificationDetailsReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    CycleTimeValueTrackingReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    LoginTests.class,
    PlasticDtcComparisonReportTests.class,
    PlasticDtcDetailsReportTests.class,
    PlasticDtcReportTests.class,
    MachiningDtcComparisonReportTests.class,
    MachiningDtcDetailsReportTests.class,
    MachiningDtcReportTests.class,
    ReportsNavigationTests.class,
    SheetMetalDtcComparisonReportTests.class,
    SheetMetalDtcDetailsReportTests.class,
    SheetMetalDtcReportTests.class,
    ScenarioComparisonReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingDetailsReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class
})

public class FullOnPremCirSuite {
}