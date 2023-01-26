package testsuites;

import com.apriori.tests.MaterialSelectionTests;
import com.apriori.tests.SettingsTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.ComponentRedirectTests;
import com.evaluate.CostAllCadTests;
import com.evaluate.GroupCostingTests;
import com.evaluate.GroupEditTests;
import com.evaluate.GroupPublishTests;
import com.evaluate.IncludeAndExcludeTests;
import com.evaluate.ListOfDigitalFactoryTests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.ScenariosTests;
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
    ComponentRedirectTests.class,
    CostAllCadTests.class,
    ListOfDigitalFactoryTests.class,
    ListProcessGroupTests.class,
    ScenariosTests.class,
    ReCostScenarioTests.class,
    IncludeAndExcludeTests.class,
    GroupCostingTests.class,
    GroupEditTests.class,
    GroupPublishTests.class,
    ComponentRedirectTests.class,
    MaterialSelectionTests.class,
    SettingsTests.class
})
public class RegressionTestSuite {
}
