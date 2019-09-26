package test.java.testsuites;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.java.evaluate.NewScenarioNameTests;
import test.java.testsuites.suiteinterface.SmokeTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class
})
public class SmokeTestSuite {
}
