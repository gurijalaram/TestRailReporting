package testsuite;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.CostAllCadTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuite.suiteinterfaces.SmokeTests;

@ProjectRunID("767")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    CostAllCadTests.class,
    // TODO: 04/01/2022 cn - comments to be removed as tests are converted from ui to api
//    CostScenarioTests.class,
//    ListOfDigitalFactoryTests.class,
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
//    DecimalPlaceTests.class,
//    UploadAssembliesTests.class,
//    PublishExistingCostedTests.class,
//    PreviewPanelTests.class,
//    DFMRiskTests.class,
//    DeleteTests.class,
//    PublishTests.class,
//    SheetMetalDTCTests.class,
//    DTCPlasticMouldingTests.class,
//    SheetMetalDTCTests.class,
//    ActionsTests.class,
//    TableHeadersTests.class,
//    DTCMachiningTests.class,
//    DTCCastingTests.class,
//    TwoModelMachiningTests.class,
//    SettingsTests.class,
//    ToleranceTests.class,
//    ComparisonTests.class,
//    ThreadTests.class,
//    PartNestingTests.class,
//    PsoEditTests.class
})
public class CIDSmokeTestSuite {
}
