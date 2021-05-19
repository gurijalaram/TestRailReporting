package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.ListOfDigitalFactoryTests;
import com.evaluate.PublishTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("562")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses ({
//    CostAllCadTests.class,
//    CostScenarioTests.class,
    ListOfDigitalFactoryTests.class,
//    ListProcessGroupTests.class,
//    NewScenarioNameTests.class,
//    ProcessGroupsTests.class,
//    ReCostScenarioTests.class,
//    SecondaryProcessTests.class,
//    ChangeMaterialSelectionTests.class,
//    MaterialStockTests.class,
//    UploadComponentTests.class,
//    UploadTests.class,
//    HelpTests.class,
//    LoginTests.class,
//    DecimalPlaceTests.class,
//    UploadAssembliesTests.class,
//    PublishExistingCostedTests.class,
//    PreviewPanelTests.class,
//    DFMRiskTests.class,
//    DeleteTests.class,
//    DFMRiskTests.class,
    PublishTests.class,
//    SheetMetalDTCTests.class,
//    DTCPlasticMouldingTests.class,
//    SheetMetalDTCTests.class,
//    ActionsTests.class,
//    TableHeadersTests.class
})
public class CIDSmokeTestSuite {
}
