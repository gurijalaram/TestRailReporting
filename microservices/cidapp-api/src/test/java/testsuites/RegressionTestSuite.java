package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.ListOfDigitalFactoryTests;
import com.evaluate.ListProcessGroupTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterfaces.IgnoreTests;
import testsuites.suiteinterfaces.SanityTests;
import testsuites.suiteinterfaces.SmokeTests;

@ProjectRunID("768")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory({SmokeTests.class, SanityTests.class, IgnoreTests.class})
@Suite.SuiteClasses({
    CostAllCadTests.class,
    ListOfDigitalFactoryTests.class,
    ListProcessGroupTests.class
})
public class RegressionTestSuite {
}
