package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;
import com.ootbreports.newreportstests.dtcmetrics.castingdtc.CastingDtcReportTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.ReportsApiTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(ReportsApiTest.class)
@Suite.SuiteClasses({
    CastingDtcReportTests.class
})
public class ReportsApiTestSuite {
}
