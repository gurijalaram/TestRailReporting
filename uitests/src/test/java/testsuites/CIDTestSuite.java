package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import compare.AddPrivateScenarioTests;
import compare.AddPublicScenarioTests;
import compare.DeletePrivateComparisonTests;
import compare.DeletePublicComparisonTests;
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
import evaluate.designguidance.thread.ThreadTests;
import evaluate.materialutilization.ChangeStockSelectionTests;
import explore.ActionsTests;
import explore.FilterCriteriaTests;
import explore.PreviewPanelTests;
import explore.TableHeadersTests;
import login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AddPrivateScenarioTests.class,
    AddPublicScenarioTests.class,
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
    DeletePrivateComparisonTests.class,
    DeleteScenarioIterationsTests.class,
    DeletePublicComparisonTests.class,
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
    ProcessRoutingTests.class
})
public class CIDTestSuite {
}