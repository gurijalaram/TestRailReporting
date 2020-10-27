package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import evaluate.CostAllCadTests;
import evaluate.CostScenarioTests;
import explore.UploadComponentTests;
import login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses ({
    LoginTests.class,
    CostScenarioTests.class,
    UploadComponentTests.class,
    CostAllCadTests.class,
})
public class SmokeTestSuite {
}
