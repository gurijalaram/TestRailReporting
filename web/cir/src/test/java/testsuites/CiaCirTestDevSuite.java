package testsuites;

import ootbreports.general.scenariocomparison.ScenarioComparisonTests;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CiaCirTestDevTest;

@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CiaCirTestDevTest.class)
@Suite.SuiteClasses({
    ScenarioComparisonTests.class
})

public class CiaCirTestDevSuite {
}
