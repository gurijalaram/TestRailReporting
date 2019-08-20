package test.java.testsuites;

import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.compare.AddPrivateScenarioTests;
import test.java.compare.AddPublicScenarioTests;
import test.java.compare.DeletePrivateComparisonTests;
import test.java.compare.DeletePublicComparisonTests;
import test.java.compare.EditPublicComparisonTests;
import test.java.compare.PublishPublicComparisonTests;
import test.java.evaluate.CostAllCadTests;
import test.java.evaluate.DeletePrivateScenarioTests;
import test.java.evaluate.DeletePublicScenarioTests;
import test.java.evaluate.DeleteScenarioIterationsTests;
import test.java.evaluate.EvaluateTests;
import test.java.evaluate.ListOfVPETests;
import test.java.evaluate.NewScenarioNameTests;
import test.java.evaluate.ProcessGroupsTests;
import test.java.evaluate.ProcessRoutingTests;
import test.java.evaluate.PublishExistingCostedTests;
import test.java.evaluate.PublishNewCostedTests;
import test.java.evaluate.ReCostScenarioTests;
import test.java.evaluate.RevertScenarioTests;
import test.java.evaluate.SecondaryProcessTests;
import test.java.evaluate.designguidance.dtc.DTCMachiningTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingDraftTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingEdgeRadiusTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingPartThicknessTests;
import test.java.evaluate.designguidance.thread.ThreadTests;
import test.java.evaluate.materialutilization.ChangeStockSelectionTests;
import test.java.explore.ActionsTests;
import test.java.explore.FilterCriteriaTests;
import test.java.explore.PreviewPanelTests;
import test.java.explore.TableHeadersTests;
import test.java.login.LoginTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AddPrivateScenarioTests.class,
    AddPublicScenarioTests.class,
    DTCMachiningTests.class,
    DTCMouldingDraftTests.class,
    DTCMouldingEdgeRadiusTests.class,
    DTCMouldingPartThicknessTests.class,
    ThreadTests.class,
    EvaluateTests.class,
    SecondaryProcessTests.class,
    FilterCriteriaTests.class,
    LoginTests.class,
    ChangeStockSelectionTests.class,
    ProcessGroupsTests.class,
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
    ActionsTests.class
})
public class CIDTestSuite {
}