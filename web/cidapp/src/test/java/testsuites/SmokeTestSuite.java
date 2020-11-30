package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.explore.UploadComponentTests;
import com.help.HelpTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
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
    ReCostScenarioTests.class,
    SecondaryProcessTests.class,
    ChangeMaterialSelectionTests.class,
    HelpTests.class,
    DecimalPlaceTests.class
})
public class SmokeTestSuite {
}
