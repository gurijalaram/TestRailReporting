package testsuites;

import evaluate.NewScenarioNameTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CustomerSmokeTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class
    /*ThreadTests.class,
    ProcessGroupsTests.class,
    ActionsTests.class,
    LoginTests.class,
    DTCCastingTests.class,
    ToleranceTests.class,
    SettingsTests.class,
    PublishNewCostedTests.class,
    TableHeadersTests.class,
    PublishNewCostedTests.class,
    FailuresWarningsTests.class,
    DTCPlasticMouldingTests.class,
    DTCCastingTests.class*/
})
public class CustomerSmokeTestSuite {
}
