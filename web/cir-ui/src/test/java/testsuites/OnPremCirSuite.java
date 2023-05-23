package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationDetailsReportTests;
import com.ootbreports.designoutlieridentification.DesignOutlierIdentificationReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
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
    ComponentCostReportTests.class,
    CostOutlierIdentificationDetailsReportTests.class,
    CycleTimeValueTrackingReportTests.class,
    CycleTimeValueTrackingDetailsReportTests.class,
    DesignOutlierIdentificationReportTests.class,
    DesignOutlierIdentificationDetailsReportTests.class,
    LoginTests.class,
    PlasticDtcReportTests.class,
    MachiningDtcReportTests.class,
    ReportsNavigationTests.class,
    SheetMetalDtcReportTests.class,
    ScenarioComparisonReportTests.class,
    TargetAndQuotedCostTrendReportTests.class,
    TargetAndQuotedCostValueTrackingReportTests.class
})

public class OnPremCirSuite {
}