package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.explore.UploadComponentTests;
import com.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
//@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses ({
    LoginTests.class,
    CostScenarioTests.class,
    UploadComponentTests.class,
    CostAllCadTests.class,
})
public class SmokeTestSuite {
}
