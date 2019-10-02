package testsuites;

import evaluate.NewScenarioNameTests;
import evaluate.ProcessGroupsTests;
import evaluate.designguidance.thread.ThreadTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.CustomerSmokeTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class,
    ThreadTests.class,
    ProcessGroupsTests.class
})
public class CustomerSmokeTestSuite {
}
