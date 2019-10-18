package testsuites;

import evaluate.NewScenarioNameTests;
import evaluate.ProcessGroupsTests;
import evaluate.PublishNewCostedTests;
import evaluate.designguidance.dtc.DTCCastingTests;
import evaluate.designguidance.dtc.DTCMouldingDraftTests;
import evaluate.designguidance.failures.FailuresWarningsTests;
import evaluate.designguidance.thread.ThreadTests;
import evaluate.designguidance.tolerance.ToleranceTests;
import explore.ActionsTests;
import login.LoginTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import settings.SettingsTests;
import testsuites.suiteinterface.CustomerSmokeTests;

@RunWith(Categories.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses({
    NewScenarioNameTests.class,
    ThreadTests.class,
    ProcessGroupsTests.class,
    ActionsTests.class,
    LoginTests.class,
    DTCCastingTests.class,
    ToleranceTests.class,
    SettingsTests.class,
    PublishNewCostedTests.class,
    FailuresWarningsTests.class,
    DTCMouldingDraftTests.class
})
public class CustomerSmokeTestSuite {
}
