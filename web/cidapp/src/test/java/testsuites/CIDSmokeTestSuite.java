package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.DeleteTests;
import com.evaluate.ListOfVPETests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.explore.PreviewPanelTests;
import com.explore.UploadComponentTests;
import com.explore.UploadTests;
import com.help.HelpTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("562")
@RunWith(CategorySuiteRunner.class)
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
    DecimalPlaceTests.class,
    UploadAssembliesTests.class,
    PublishExistingCostedTests.class,
    PreviewPanelTests.class,
    DFMRiskTests.class,
    DeleteTests.class
})
public class CIDSmokeTestSuite {
}
