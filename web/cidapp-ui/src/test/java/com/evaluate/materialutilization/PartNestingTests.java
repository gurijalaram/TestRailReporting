package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.PartNestingPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PartNestingTests extends TestBase {

    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private PartNestingPage partNestingPage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    private File resourceFile;

    public PartNestingTests() {
        super();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"5922"})
    @Description("Validate Part Nesting Tab can be accessed")
    public void partNestingTabAccessible() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        partNestingPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getNestingInfo("Selected Sheet")).isEqualTo("4.00mm x 1,250.00mm x 2,500.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Blank Size")).isEqualTo("470.78mm x 400.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Parts Per Sheet")).isEqualTo("15");

        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"7699"})
    @Description("Select Rectangular method of Part Nesting and cost")
    public void partNestingTabRectangularNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Rectangular Nesting")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Rectangular Nesting")
            .closePanel();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE), is(true));
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"7698"})
    @Description("Select True Part method of Part Nesting and cost")
    public void partNestingTabTruePartNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Machine Default")
            .selectUtilizationModeDropDown("True-Part Shape Nesting")
            .closePanel();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE), is(true));
    }

    @Test
    @Category(ExtendedRegression.class)
    @Issue("Cost-280")
    @TestRail(testCaseId = {"7699"})
    @Description("Select Machine Default method of Part Nesting and cost")
    public void partNestingTabMachineDefaultNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Machine Default")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Machine Default")
            .closePanel();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE), is(true));
    }

    @Test
    @TestRail(testCaseId = {"5923"})
    @Description("Validate Part Nesting Tab can not be accessed for inappropriate Process Groups")
    public void partNestingTabDisabled() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        currentUser = UserUtil.getUser();

        materialProcessPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(componentName, new GenerateStringUtil().generateScenarioName(), resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.STOCK_MACHINING)
            .costScenario()
            .openMaterialProcess();

        assertThat(materialProcessPage.isPartNestingTabDisplayed(), is(false));
    }
}
