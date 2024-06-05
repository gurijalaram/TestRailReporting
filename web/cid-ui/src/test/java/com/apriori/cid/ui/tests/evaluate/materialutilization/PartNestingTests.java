package com.apriori.cid.ui.tests.evaluate.materialutilization;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.cid.ui.pageobjects.evaluate.materialprocess.PartNestingPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PartNestingTests extends TestBaseUI {

    private EvaluatePage evaluatePage;
    private MaterialProcessPage materialProcessPage;
    private PartNestingPage partNestingPage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

    public PartNestingTests() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {7699, 5922})
    @Description("Select Rectangular method of Part Nesting and cost")
    public void partNestingTabRectangularNesting() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("bracket_basic", ProcessGroupEnum.SHEET_METAL);

        partNestingPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/Turret/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getNestingInfo("Selected Sheet")).isEqualTo("4.00mm x 1,250.00mm x 2,500.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Blank Size")).isEqualTo("470.78mm x 400.00mm");
        softAssertions.assertThat(partNestingPage.getNestingInfo("Parts Per Sheet")).isEqualTo("15");

        partNestingPage = partNestingPage.selectUtilizationModeDropDown("Rectangular Nesting")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getUtilizationMode()).isEqualTo("Rectangular Nesting");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {7698, 7699})
    @Description("Select True Part method of Part Nesting and cost")
    public void partNestingTabTruePartNesting() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("bracket_basic", ProcessGroupEnum.SHEET_METAL);

        partNestingPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("[CTL]/Turret/[Bend]")
            .submit(EvaluatePage.class)
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getUtilizationMode()).isEqualTo("True-Part Shape Nesting");

        partNestingPage.selectUtilizationModeDropDown("Machine Default")
            .closePanel()
            .costScenario()
            .openMaterialProcess()
            .openPartNestingTab();

        softAssertions.assertThat(partNestingPage.getUtilizationMode()).isEqualTo("Machine Default");

        softAssertions.assertAll();
    }


    @Test
    @TestRail(id = {5923})
    @Description("Validate Part Nesting Tab can not be accessed for inappropriate Process Groups")
    public void partNestingTabDisabled() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        materialProcessPage = new CidAppLoginPage(driver)
            .login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING)
            .costScenario()
            .openMaterialProcess();

        assertThat(materialProcessPage.isPartNestingTabDisplayed(), is(false));
    }
}
