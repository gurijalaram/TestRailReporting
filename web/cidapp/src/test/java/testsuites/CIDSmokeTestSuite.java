package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.ListOfVPETests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.explore.UploadComponentTests;
import com.explore.UploadTests;
import com.help.HelpTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("562")
@RunWith(CategorySuiteRunner.class)
//TODO: Uncomment this when ready to split out @Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses ({
    CostAllCadTests.class,
    CostScenarioTests.class,
    ListOfVPETests.class,
    ListProcessGroupTests.class,
    NewScenarioNameTests.class,
    ProcessGroupsTests.class,
    ReCostScenarioTests.class,
    SecondaryProcessTests.class,
    ChangeMaterialSelectionTests.class,
    MaterialStockTests.class,
    UploadComponentTests.class,
    UploadTests.class,
    HelpTests.class,
    LoginTests.class,
    DecimalPlaceTests.class
})
public class CIDSmokeTestSuite {
}
