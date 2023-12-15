package com.apriori.cid.ui.tests.evaluate.materialutilization;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.PartNestingPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

public class PartNestingTests extends TestBaseUI {

    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private PartNestingPage partNestingPage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    private File resourceFile;

    public PartNestingTests() {
        super();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {5922})
    @Description("Validate Part Nesting Tab can be accessed")
    public void partNestingTabAccessible() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        partNestingPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getNestingInfo("Selected Sheet")).isEqualTo("4.00mm x 1,250.00mm x 2,500.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Blank Size")).isEqualTo("470.78mm x 400.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Parts Per Sheet")).isEqualTo("15");

        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {7699})
    @Description("Select Rectangular method of Part Nesting and cost")
    public void partNestingTabRectangularNesting() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7698})
    @Description("Select True Part method of Part Nesting and cost")
    public void partNestingTabTruePartNesting() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab()
            .selectUtilizationModeDropDown("Machine Default")
            .selectUtilizationModeDropDown("True-Part Shape Nesting")
            .closePanel();

        assertThat(evaluatePage.isCostLabel(NewCostingLabelEnum.COST_COMPLETE), is(true));
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {7699})
    @Description("Select Machine Default method of Part Nesting and cost")
    public void partNestingTabMachineDefaultNesting() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
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
    @TestRail(id = {5923})
    @Description("Validate Part Nesting Tab can not be accessed for inappropriate Process Groups")
    public void partNestingTabDisabled() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(currentUser)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .costScenario()
            .openMaterialProcess();

        assertThat(materialProcessPage.isPartNestingTabDisplayed(), is(false));
    }
}
