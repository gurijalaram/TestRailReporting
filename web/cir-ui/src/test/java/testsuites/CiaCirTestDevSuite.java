package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.componentcost.ComponentCostReportTests;
import com.ootbreports.general.assemblydetails.AssemblyDetailsReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingDetailsReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        ComponentCostReportTests.class
})

public class CiaCirTestDevSuite {
}
