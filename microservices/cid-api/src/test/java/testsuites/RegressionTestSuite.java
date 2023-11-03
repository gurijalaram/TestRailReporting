package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.IGNORE;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SANITY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.tests.SettingsTests;
import com.apriori.cid.api.tests.evaluate.ComponentRedirectTests;
import com.apriori.cid.api.tests.evaluate.CostAllCadTests;
import com.apriori.cid.api.tests.evaluate.GroupCostingTests;
import com.apriori.cid.api.tests.evaluate.GroupEditTests;
import com.apriori.cid.api.tests.evaluate.GroupPublishTests;
import com.apriori.cid.api.tests.evaluate.IncludeAndExcludeTests;
import com.apriori.cid.api.tests.evaluate.ListOfDigitalFactoryTests;
import com.apriori.cid.api.tests.evaluate.ListProcessGroupTests;
import com.apriori.cid.api.tests.evaluate.MaterialSelectionTests;
import com.apriori.cid.api.tests.evaluate.ReCostScenarioTests;
import com.apriori.cid.api.tests.evaluate.RoutingsTests;
import com.apriori.cid.api.tests.evaluate.ScenariosTests;
import com.apriori.cid.api.tests.newendpoint.ScenarioIterationPostTests;
import com.apriori.cid.api.tests.newendpoint.VerifyIfFailsTest;
import com.apriori.cid.api.tests.newendpoint.VerifyIfNotFailTests;
import com.apriori.cid.api.tests.oldendpoint.ScenarioIterationGetTests;

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
    RoutingsTests.class,
    ScenarioIterationPostTests.class,
    VerifyIfFailsTest.class,
    VerifyIfNotFailTests.class,
    ScenarioIterationGetTests.class
})
public class RegressionTestSuite {
}
