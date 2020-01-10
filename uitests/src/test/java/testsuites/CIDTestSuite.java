package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import compare.AddScenarioTests;
import compare.DeleteComparisonTests;
import compare.EditPublicComparisonTests;
import compare.PublishComparisonTests;
import compare.SaveAsComparisonTests;
import evaluate.AssemblyUploadTests;
import evaluate.CostAllCadTests;
import evaluate.DeletePrivateScenarioTests;
import evaluate.DeletePublicScenarioTests;
import evaluate.DeleteScenarioIterationsTests;
import evaluate.ListOfVPETests;
import evaluate.ListProcessGroupTests;
import evaluate.NewScenarioNameTests;
import evaluate.ProcessGroupsTests;
import evaluate.ProcessRoutingTests;
import evaluate.PsoEditTests;
import evaluate.PublishExistingCostedTests;
import evaluate.PublishNewCostedTests;
import evaluate.ReCostScenarioTests;
import evaluate.ReferencePanelTests;
import evaluate.RevertScenarioTests;
import evaluate.SecondaryProcessTests;
import evaluate.designguidance.dtc.DTCCastingTests;
import evaluate.designguidance.dtc.DTCMachiningTests;
import evaluate.designguidance.dtc.DTCPlasticMouldingTests;
import evaluate.designguidance.dtc.SheetMetalDTCTests;
import evaluate.designguidance.failures.FailuresWarningsTests;
import evaluate.designguidance.geometry.GeometryTests;
import evaluate.designguidance.thread.ThreadTests;
import evaluate.designguidance.tolerance.ToleranceTests;
import evaluate.materialutilization.ChangeMaterialSelectionTests;
import evaluate.materialutilization.ChangeStockSelectionTests;
import evaluate.materialutilization.MaterialPMITests;
import evaluate.materialutilization.MaterialStockTests;
import evaluate.materialutilization.PartNestingTests;
import explore.ActionsTests;
import explore.FilterCriteriaTests;
import explore.PreviewPanelTests;
import explore.TableHeadersTests;
import login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import settings.SettingsTests;

@ProjectRunID("177")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    AddScenarioTests.class,
    DeleteComparisonTests.class,
    EditPublicComparisonTests.class,
    DTCCastingTests.class,
    DTCMachiningTests.class,
    DTCPlasticMouldingTests.class,
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
})
public class CIDTestSuite {
}