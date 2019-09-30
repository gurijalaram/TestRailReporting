package test.java.testsuites;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.evaluate.NewScenarioNameTests;
import test.java.evaluate.designguidance.thread.ThreadTests;
import test.java.testsuites.suiteinterface.CustomerSmokeTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class,
    ThreadTests.class
})
public class CustomerSmokeTestSuite {
}
