package testsuites;

import com.apriori.pageobjects.pages.view.reports.ScenarioComparisonReportPage;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
    ScenarioComparisonReportPage.class
})

public class CiaCirTestDevSuite {
}
