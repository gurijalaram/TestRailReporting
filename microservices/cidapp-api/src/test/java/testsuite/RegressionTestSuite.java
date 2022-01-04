package testsuite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuite.suiteinterfaces.SanityTests;
import testsuite.suiteinterfaces.SmokeTests;

@ProjectRunID("768")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory({SmokeTests.class, SanityTests.class})
@Suite.SuiteClasses({
    CostAllCadTests.class,
})
public class RegressionTestSuite {
}
