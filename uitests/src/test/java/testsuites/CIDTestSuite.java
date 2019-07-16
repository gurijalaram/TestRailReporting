package test.java.testsuites;

import main.java.compare.AddPrivateScenarioTests;
import main.java.compare.AddPublicScenarioTests;
import main.java.evaluate.EvaluateTests;
import main.java.evaluate.SecondaryProcessTests;
import main.java.evaluate.designguidance.dtc.DTCMachiningTests;
import main.java.evaluate.designguidance.dtc.DTCMouldingDraftTests;
import main.java.evaluate.designguidance.dtc.DTCMouldingEdgeRadiusTests;
import main.java.evaluate.designguidance.dtc.DTCMouldingPartThicknessTests;
import main.java.evaluate.designguidance.tolerance.TolerancesTests;
import main.java.evaluate.materialutilization.ChangeStockSelectionTests;
import main.java.explore.FilterCriteriaTests;
import main.java.login.LoginTests;
import main.java.runner.ConcurrentSuiteRunner;
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
    TolerancesTests.class,
    EvaluateTests.class,
    SecondaryProcessTests.class,
    FilterCriteriaTests.class,
    LoginTests.class,
    ChangeStockSelectionTests.class,
})
public class CIDTestSuite {
}
