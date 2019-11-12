package testsuites;

import evaluate.NewScenarioNameTests;
import evaluate.ProcessGroupsTests;
import evaluate.PublishNewCostedTests;
import evaluate.designguidance.dtc.DTCCastingTests;
import evaluate.designguidance.dtc.DTCPlasticMouldingTests;
import evaluate.designguidance.failures.FailuresWarningsTests;
import evaluate.designguidance.thread.ThreadTests;
import evaluate.designguidance.tolerance.ToleranceTests;
import explore.ActionsTests;
import explore.TableHeadersTests;
import login.LoginTests;
import settings.SettingsTests;
import testsuites.suiteinterface.CustomerSmokeTests;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(CustomerSmokeTests.class)
@Suite.SuiteClasses( {
    NewScenarioNameTests.class,
    ThreadTests.class,
    ProcessGroupsTests.class,
    ActionsTests.class,
    LoginTests.class,
    DTCCastingTests.class,
    ToleranceTests.class,
    SettingsTests.class,
    PublishNewCostedTests.class,
    TableHeadersTests.class,
    PublishNewCostedTests.class,
    FailuresWarningsTests.class,
    DTCPlasticMouldingTests.class,
    DTCCastingTests.class
})
public class CustomerSmokeTestSuite {
}
