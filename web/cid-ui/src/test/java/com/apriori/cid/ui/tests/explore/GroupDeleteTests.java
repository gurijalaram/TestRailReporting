package com.apriori.cid.ui.tests.explore;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.DeletePage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class GroupDeleteTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentInfoBuilder component;
    private ComponentInfoBuilder componentB;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(id = {15013, 15011})
    @Description("Verify user can delete 2 or more parts")
    @Tag(SMOKE)
    public void testGroupDelete() {
        component = new ComponentRequestUtil().getComponent();
        componentB = new ComponentRequestUtil().getComponent();
        componentB.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .uploadComponent(componentB)
            .clickExplore()
            .selectFilter("Recent")
            .refresh()
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName());

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage = explorePage.multiSelectScenarios(componentB.getComponentName() + ", " + componentB.getScenarioName());

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class)
            .checkComponentDelete(component)
            .checkComponentDelete(componentB)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName())).isEqualTo(0);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15021, 15022})
    @Description("Verify Delete icon disabled if a selected part is in Processing/Costing state")
    public void testGroupDeleteProcessing() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .clickExplore()
            .selectFilter("Recent")
            .refresh()
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName())
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {15017, 15016, 15020})
    @Description("Verify user can delete 2 or more assemblies. Verify correct behavior of Delete button when multi-selecting.")
    @Tag(SMOKE)
    public void testGroupDeleteAssemblies() {
        final String assemblyName = "titan cordless drill";
        final String assemblyName2 = "titan charger ass";
        final String componentName = "2062987";

        ComponentInfoBuilder assembly = new AssemblyRequestUtil().getAssembly(assemblyName);
        ComponentInfoBuilder assemblyB = new AssemblyRequestUtil().getAssembly(assemblyName2);
        assemblyB.setUser(assembly.getUser());
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponent(componentName);
        component.setUser(assembly.getUser());

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(assembly.getUser())
            .uploadComponent(assembly)
            .uploadComponent(assemblyB)
            .uploadComponent(component)
            .clickExplore()
            .selectFilter("Recent")
            .refresh()
            .multiSelectScenarios(assembly.getComponentName() + ", " + assembly.getScenarioName());

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios(assemblyB.getComponentName() + ", " + assemblyB.getScenarioName());
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios(component.getComponent() + ", " + component.getScenarioName());
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        explorePage.multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName());
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class)
            .checkComponentDelete(assembly)
            .checkComponentDelete(assemblyB)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(assembly.getComponentName(), assembly.getScenarioName())).isEqualTo(0);
        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyB.getComponentName(), assemblyB.getScenarioName())).isEqualTo(0);

        softAssertions.assertAll();
    }
}
