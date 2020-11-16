package com.testsuites;

import com.apriori.utils.runner.CategorySuiteRunner;

import com.evaluate.NewScenarioNameTests;
import com.evaluate.ProcessGroupsTests;
import com.evaluate.PublishNewCostedTests;
import com.evaluate.TwoModelMachiningTests;
import com.evaluate.designguidance.dtc.DTCCastingTests;
import com.evaluate.designguidance.dtc.DTCPlasticMouldingTests;
import com.evaluate.designguidance.failures.FailuresWarningsTests;
import com.evaluate.designguidance.thread.ThreadTests;
import com.evaluate.designguidance.tolerance.ToleranceTests;
import com.explore.ActionsTests;
import com.explore.TableHeadersTests;
import com.login.LoginTests;
import com.settings.SettingsTests;
import com.testsuites.suiteinterface.CustomerSmokeTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(CategorySuiteRunner.class)
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
    TableHeadersTests.class,
    PublishNewCostedTests.class,
    FailuresWarningsTests.class,
    DTCPlasticMouldingTests.class,
    DTCCastingTests.class,
    TwoModelMachiningTests.class,
})
public class CustomerSmokeTestSuite {
}
