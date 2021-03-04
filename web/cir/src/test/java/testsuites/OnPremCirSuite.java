package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.login.LoginTests;
import com.navigation.ReportsNavigationTests;
import com.ootbreports.assemblycost.AssemblyCostReportTests;
import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingDetailsTests;
import com.ootbreports.cycletimevaluetracking.CycleTimeValueTrackingTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcReportTests;
import com.ootbreports.dtcmetrics.machiningdtc.MachiningDtcReportTests;
import com.ootbreports.dtcmetrics.plasticdtc.PlasticDtcReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcComparisonReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcDetailsReportTests;
import com.ootbreports.dtcmetrics.sheetmetaldtc.SheetMetalDtcReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostTrendTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingTests;
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
        CycleTimeValueTrackingTests.class,
        CycleTimeValueTrackingDetailsTests.class,
        LoginTests.class,
        PlasticDtcReportTests.class,
        MachiningDtcReportTests.class,
        ReportsNavigationTests.class,
        SheetMetalDtcReportTests.class,
        SheetMetalDtcDetailsReportTests.class,
        SheetMetalDtcComparisonReportTests.class,
        ScenarioComparisonReportTests.class,
        TargetAndQuotedCostTrendTests.class,
        TargetAndQuotedCostValueTrackingTests.class,
        TargetAndQuotedCostValueTrackingDetailsTests.class
})

public class OnPremCirSuite {
}