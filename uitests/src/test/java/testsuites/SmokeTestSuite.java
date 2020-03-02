package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import evaluate.NewScenarioNameTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class
})
public class SmokeTestSuite {
}
