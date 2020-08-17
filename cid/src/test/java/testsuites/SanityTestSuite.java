package testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import compare.PublishComparisonTests;
import evaluate.AssemblyUploadTests;
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
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import settings.SettingsTests;
import testsuites.suiteinterface.SanityTests;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory(SanityTests.class)
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
    TableHeadersTests.class,
    FailuresWarningsTests.class,
    DTCPlasticMouldingTests.class,
    DTCCastingTests.class,
    AssemblyUploadTests.class,
    PublishComparisonTests.class,

})
public class SanityTestSuite {
}
