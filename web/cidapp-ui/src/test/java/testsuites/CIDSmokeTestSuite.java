package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import com.evaluate.CostScenarioTests;
import com.evaluate.DeleteTests;
import com.evaluate.ListOfDigitalFactoryTests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.PublishTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.UploadAssembliesTests;
import com.evaluate.dtc.DFMRiskTests;
import com.evaluate.dtc.DTCCastingTests;
import com.evaluate.dtc.DTCMachiningTests;
import com.evaluate.dtc.DTCPlasticMouldingTests;
import com.evaluate.dtc.SheetMetalDTCTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.explore.ActionsTests;
import com.explore.PreviewPanelTests;
import com.explore.TableHeadersTests;
import com.explore.UploadComponentTests;
import com.explore.UploadTests;
import com.help.HelpTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("562")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    CostAllCadTests.class,
    CostScenarioTests.class,
    ListOfDigitalFactoryTests.class,
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
    DeleteTests.class,
    DFMRiskTests.class,
    PublishTests.class,
    SheetMetalDTCTests.class,
    DTCPlasticMouldingTests.class,
    SheetMetalDTCTests.class,
    ActionsTests.class,
    TableHeadersTests.class,
    DTCMachiningTests.class,
    DTCCastingTests.class
})
public class CIDSmokeTestSuite {
}
