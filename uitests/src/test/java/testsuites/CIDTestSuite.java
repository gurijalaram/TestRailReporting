package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import evaluate.ProcessGroupsTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("177")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
//    AddScenarioTests.class,
//    DeleteComparisonTests.class,
//    EditPublicComparisonTests.class,
//    DTCCastingTests.class,
//    DTCMachiningTests.class,
//    DTCPlasticMouldingTests.class,
//    SheetMetalDTCTests.class,
//    FailuresWarningsTests.class,
//    GeometryTests.class,
//    ThreadTests.class,
//    ToleranceTests.class,
//    ChangeStockSelectionTests.class,
//    ChangeMaterialSelectionTests.class,
//    AssemblyUploadTests.class,
//    CostAllCadTests.class,
//    DeletePrivateScenarioTests.class,
//    DeletePublicScenarioTests.class,
//    DeleteScenarioIterationsTests.class,
//    ListOfVPETests.class,
//    ListProcessGroupTests.class,
//    NewScenarioNameTests.class,
    ProcessGroupsTests.class,
//    ProcessRoutingTests.class,
//    PsoEditTests.class,
//    PublishExistingCostedTests.class,
//    PublishNewCostedTests.class,
//    PublishComparisonTests.class,
//    SaveAsComparisonTests.class,
//    ReCostScenarioTests.class,
//    SecondaryProcessTests.class,
//    ActionsTests.class,
//    FilterCriteriaTests.class,
//    PreviewPanelTests.class,
//    TableHeadersTests.class,
//    LoginTests.class,
//    SettingsTests.class,
//    MaterialStockTests.class,
//    PartNestingTests.class,
//    ReferencePanelTests.class,
//    MaterialPMITests.class,
//    RevertScenarioTests.class,
//    TwoModelMachiningTests.class,
})
public class CIDTestSuite {
}