package test.java.testsuites;

import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.compare.AddPrivateScenarioTests;
import test.java.compare.AddPublicScenarioTests;
import test.java.evaluate.EvaluateTests;
import test.java.evaluate.ProcessGroupsTests;
import test.java.evaluate.SecondaryProcessTests;
import test.java.evaluate.designguidance.dtc.DTCMachiningTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingDraftTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingEdgeRadiusTests;
import test.java.evaluate.designguidance.dtc.DTCMouldingPartThicknessTests;
import test.java.evaluate.designguidance.tolerance.TolerancesTests;
import test.java.evaluate.materialutilization.ChangeStockSelectionTests;
import test.java.explore.FilterCriteriaTests;
import test.java.login.LoginTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    AddPrivateScenarioTests.class,
    AddPublicScenarioTests.class,
    DTCMachiningTests.class,
    DTCMouldingDraftTests.class,
    DTCMouldingEdgeRadiusTests.class,
    DTCMouldingPartThicknessTests.class,
    TolerancesTests.class,
    EvaluateTests.class,
    SecondaryProcessTests.class,
    FilterCriteriaTests.class,
    LoginTests.class,
    ChangeStockSelectionTests.class,
    ProcessGroupsTests.class
})
public class CIDTestSuite {
}
