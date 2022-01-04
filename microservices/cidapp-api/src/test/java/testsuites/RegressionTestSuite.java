package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterfaces.SanityTests;
import testsuites.suiteinterfaces.SmokeTests;

@ProjectRunID("768")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory({SmokeTests.class, SanityTests.class})
@Suite.SuiteClasses({
    CostAllCadTests.class,
})
public class RegressionTestSuite {
}
