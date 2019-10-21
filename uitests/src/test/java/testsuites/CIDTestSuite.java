package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import compare.AddScenarioTests;
import compare.DeleteComparisonTests;
import compare.EditPublicComparisonTests;
import compare.PublishPublicComparisonTests;
import evaluate.AssemblyUploadTests;
import evaluate.CostAllCadTests;
import evaluate.DeletePrivateScenarioTests;
import evaluate.DeletePublicScenarioTests;
import evaluate.DeleteScenarioIterationsTests;
import evaluate.ListOfVPETests;
import evaluate.NewScenarioNameTests;
import evaluate.ProcessGroupsTests;
import evaluate.ProcessRoutingTests;
import evaluate.PsoEditTests;
import evaluate.PublishExistingCostedTests;
import evaluate.PublishNewCostedTests;
import evaluate.ReCostScenarioTests;
import evaluate.RevertScenarioTests;
import evaluate.SecondaryProcessTests;
import evaluate.designguidance.dtc.DTCCastingTests;
import evaluate.designguidance.dtc.DTCMachiningTests;
import evaluate.designguidance.dtc.DTCMouldingDraftTests;
import evaluate.designguidance.dtc.DTCMouldingEdgeRadiusTests;
import evaluate.designguidance.dtc.DTCMouldingPartThicknessTests;
import evaluate.designguidance.failures.FailuresWarningsTests;
import evaluate.designguidance.geometry.GeometryTests;
import evaluate.designguidance.thread.ThreadTests;
import evaluate.materialutilization.ChangeStockSelectionTests;
import explore.ActionsTests;
import explore.FilterCriteriaTests;
import explore.PreviewPanelTests;
import explore.TableHeadersTests;
import login.LoginTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import settings.SettingsTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    AddScenarioTests.class,
    DTCMachiningTests.class,
    DTCMouldingDraftTests.class,
    DTCMouldingEdgeRadiusTests.class,
    DTCMouldingPartThicknessTests.class,
    ThreadTests.class,
    SecondaryProcessTests.class,
    FilterCriteriaTests.class,
    LoginTests.class,
    ChangeStockSelectionTests.class,
    ProcessGroupsTests.class,
    AssemblyUploadTests.class,
    CostAllCadTests.class,
    DeletePrivateScenarioTests.class,
    DeletePublicScenarioTests.class,
    DeleteComparisonTests.class,
    DeleteScenarioIterationsTests.class,
    EditPublicComparisonTests.class,
    NewScenarioNameTests.class,
    PublishExistingCostedTests.class,
    PublishNewCostedTests.class,
    PublishPublicComparisonTests.class,
    ReCostScenarioTests.class,
    RevertScenarioTests.class,
    PreviewPanelTests.class,
    ListOfVPETests.class,
    ProcessGroupsTests.class,
    TableHeadersTests.class,
    ProcessRoutingTests.class,
    ActionsTests.class,
    DTCCastingTests.class,
    ProcessRoutingTests.class,
    PsoEditTests.class,
    SettingsTests.class,
    GeometryTests.class,
    FailuresWarningsTests.class
})
public class CIDTestSuite {
}