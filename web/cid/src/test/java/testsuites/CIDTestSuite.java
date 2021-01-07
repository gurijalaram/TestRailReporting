package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.compare.AddScenarioTests;
import com.compare.DeleteComparisonTests;
import com.compare.EditPublicComparisonTests;
import com.compare.PublishComparisonTests;
import com.compare.SaveAsComparisonTests;
import com.evaluate.AssemblyUploadTests;
import com.evaluate.CostAllCadTests;
import com.evaluate.DeletePrivateScenarioTests;
import com.evaluate.DeletePublicScenarioTests;
import com.evaluate.DeleteScenarioIterationsTests;
import com.evaluate.ListOfVPETests;
import com.evaluate.ListProcessGroupTests;
import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.ProcessRoutingTests;
import com.evaluate.PsoEditTests;
import com.evaluate.PublishExistingCostedTests;
import com.evaluate.PublishNewCostedTests;
import com.evaluate.ReCostScenarioTests;
import com.evaluate.ReferencePanelTests;
import com.evaluate.RevertScenarioTests;
import com.evaluate.SecondaryProcessTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.designguidance.dtc.DTCCastingTests;
import com.evaluate.designguidance.dtc.DTCMachiningTests;
import com.evaluate.designguidance.dtc.DTCPlasticMouldingTests;
import com.evaluate.designguidance.dtc.SheetMetalDTCTests;
import com.evaluate.designguidance.failures.FailuresWarningsTests;
import com.evaluate.designguidance.geometry.GeometryTests;
import com.evaluate.designguidance.thread.ThreadTests;
import com.evaluate.designguidance.tolerance.ToleranceTests;
import com.evaluate.materialutilization.ChangeMaterialSelectionTests;
import com.evaluate.materialutilization.ChangeStockSelectionTests;
import com.evaluate.materialutilization.MaterialPMITests;
import com.evaluate.materialutilization.MaterialStockTests;
import com.evaluate.materialutilization.PartNestingTests;
import com.explore.ActionsTests;
import com.explore.FilterCriteriaTests;
import com.explore.PreviewPanelTests;
import com.explore.TableHeadersTests;
import com.explore.UploadTests;
import com.login.LoginTests;
import com.settings.DecimalPlaceTests;
import com.settings.SettingsTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import testsuites.suiteinterface.SmokeTests;

@ProjectRunID("177")
@RunWith(CategorySuiteRunner.class)
@Categories.ExcludeCategory(SmokeTests.class)
@Suite.SuiteClasses({
    AddScenarioTests.class,
    DeleteComparisonTests.class,
    EditPublicComparisonTests.class,
    DTCCastingTests.class,
    DTCMachiningTests.class,
    DTCPlasticMouldingTests.class,
    SheetMetalDTCTests.class,
    FailuresWarningsTests.class,
    GeometryTests.class,
    ThreadTests.class,
    ToleranceTests.class,
    ChangeStockSelectionTests.class,
    ChangeMaterialSelectionTests.class,
    AssemblyUploadTests.class,
    CostAllCadTests.class,
    DeletePrivateScenarioTests.class,
    DeletePublicScenarioTests.class,
    DeleteScenarioIterationsTests.class,
    ListOfVPETests.class,
    ListProcessGroupTests.class,
    NewScenarioNameTests.class,
    ProcessGroupsTests.class,
    ProcessRoutingTests.class,
    PsoEditTests.class,
    PublishExistingCostedTests.class,
    PublishNewCostedTests.class,
    PublishComparisonTests.class,
    SaveAsComparisonTests.class,
    ReCostScenarioTests.class,
    SecondaryProcessTests.class,
    ActionsTests.class,
    FilterCriteriaTests.class,
    PreviewPanelTests.class,
    TableHeadersTests.class,
    LoginTests.class,
    SettingsTests.class,
    MaterialStockTests.class,
    PartNestingTests.class,
    ReferencePanelTests.class,
    MaterialPMITests.class,
    RevertScenarioTests.class,
    TwoModelMachiningTests.class,
    DecimalPlaceTests.class,
    UploadTests.class,
    ListOfVPETests.class,
})
public class CIDTestSuite {
}