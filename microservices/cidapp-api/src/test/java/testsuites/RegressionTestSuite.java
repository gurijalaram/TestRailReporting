package testsuites;

import static com.apriori.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.SettingsTests;
import com.apriori.evaluate.ComponentRedirectTests;
import com.apriori.evaluate.CostAllCadTests;
import com.apriori.evaluate.GroupCostingTests;
import com.apriori.evaluate.GroupEditTests;
import com.apriori.evaluate.GroupPublishTests;
import com.apriori.evaluate.IncludeAndExcludeTests;
import com.apriori.evaluate.ListOfDigitalFactoryTests;
import com.apriori.evaluate.ListProcessGroupTests;
import com.apriori.evaluate.MaterialSelectionTests;
import com.apriori.evaluate.ReCostScenarioTests;
import com.apriori.evaluate.RoutingsTests;
import com.apriori.evaluate.ScenariosTests;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@ExcludeTags({SMOKE, SANITY, IGNORE})
@SelectClasses({
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
    SettingsTests.class,
    RoutingsTests.class
})
public class RegressionTestSuite {
}
