package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.ootbreports.costoutlieridentification.CostOutlierIdentificationDetailsReportTests;
import com.ootbreports.dtcmetrics.castingdtc.CastingDtcComparisonReportTests;
import com.ootbreports.scenariocomparison.ScenarioComparisonReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostTrendReportTests;
import com.ootbreports.targetquotedcosttrend.TargetAndQuotedCostValueTrackingReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
        ScenarioComparisonReportTests.class
})

public class CiaCirTestDevSuite {
}
