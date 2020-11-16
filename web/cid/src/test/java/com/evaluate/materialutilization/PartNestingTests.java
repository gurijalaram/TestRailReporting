package com.evaluate.materialutilization;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.materialutilization.MaterialUtilizationPage;
import com.pageobjects.pages.evaluate.materialutilization.PartNestingPage;
import com.pageobjects.pages.login.CidLoginPage;
import com.testsuites.suiteinterface.SmokeTests;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

public class PartNestingTests extends TestBase {

    private CidLoginPage loginPage;
    private MaterialUtilizationPage materialUtilizationPage;
    private PartNestingPage partNestingPage;

    private File resourceFile;

    public PartNestingTests() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"906"})
    @Description("Validate Part Nesting Tab can be accessed")
    public void partNestingTabAccessible() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab();
        assertThat(partNestingPage.getSelectedSheet(), is(equalTo("4.00 mm x 1250 mm x 2500 mm")));
        assertThat(partNestingPage.getBlankSize(), is(equalTo("470.7811 x 400.0018")));
        assertThat(partNestingPage.getPartsPerSheet(), is(equalTo("15")));
    }

    @Test
    @TestRail(testCaseId = {})
    @Description("Select Rectangular method of Part Nesting and cost")
    public void partNestingTabRectangularNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab()
            .selectRectangularNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab();

        assertThat(partNestingPage.isRectangularNesting("checked"), is("true"));
    }

    @Test
    @TestRail(testCaseId = {})
    @Description("Select True Part method of Part Nesting and cost")
    public void partNestingTabTruePartNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab()
            .selectRectangularNesting()
            .selectTrue_PartShapeNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab();

        assertThat(partNestingPage.isTruePartNesting("checked"), is("true"));
    }

    @Test
    @TestRail(testCaseId = {})
    @Description("Select Machine Default method of Part Nesting and cost")
    public void partNestingTabMachineDefaultNesting() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        partNestingPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab()
            .selectMachineDefaultNesting()
            .closePartNestingPanel()
            .costScenario()
            .openMaterialUtilization()
            .goToPartNestingTab();

        assertThat(partNestingPage.isMachineDefaultNesting("checked"), is("true"));
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"907"})
    @Description("Validate Part Nesting Tab can not be accessed for inappropriate Process Groups")
    public void partNestingTabDisabled() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.STOCK_MACHINING;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");

        loginPage = new CidLoginPage(driver);
        materialUtilizationPage = loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openMaterialUtilization();

        assertThat(materialUtilizationPage.getPartNestingButton(), containsString("disabled"));
    }
}
