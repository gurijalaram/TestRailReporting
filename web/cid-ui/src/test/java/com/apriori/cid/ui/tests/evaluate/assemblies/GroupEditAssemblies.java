package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.EditComponentsPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class GroupEditAssemblies extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ComponentsTreePage componentsTreePage;
    private EditScenarioStatusPage editScenarioStatusPage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;

    public GroupEditAssemblies() {
        super();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {10882, 10890, 10893})
    @Description("Group edit subcomponents")
    public void editButtonAvailable() {
        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + componentAssembly.getScenarioName() + "", PIN + "," + componentAssembly.getScenarioName() + "");

        softAssertions.assertThat(componentsTreePage.isEditButtonDisabled()).isEqualTo(false);

        componentsTreePage.editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class)
            .checkSubcomponentState(componentAssembly, BIG_RING + "," + PIN);

        softAssertions.assertThat(componentsTreePage.getRowDetails(PIN, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {10891, 11132})
    @Description("Group edit subcomponents")
    public void overridePrivateSubComponent() {

        final String BIG_RING = "big ring";
        final String PIN = "Pin";
        final String SMALL_RING = "small ring";

        componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BIG_RING + "," + componentAssembly.getScenarioName() + "")
            .editSubcomponent(EditScenarioStatusPage.class);

        softAssertions.assertThat(editScenarioStatusPage.getEditScenarioMessage()).contains("Scenario was successfully edited, click here to open in the evaluate view.");

        componentsTreePage = editScenarioStatusPage.close(ComponentsTreePage.class)
            .checkSubcomponentState(componentAssembly, BIG_RING);

        softAssertions.assertThat(componentsTreePage.getRowDetails(PIN, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(BIG_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(SMALL_RING, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {10889, 11953})
    @Description("Group edit subcomponents")
    public void privateAndPublicSubComponents() {

        final String BOLT = "bolt";
        final String FLANGE = "flange";
        final String NUT = "nut";

        componentAssembly = new AssemblyRequestUtil().getAssembly("flange c");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName() + "")
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class)
            .checkSubcomponentState(componentAssembly, BOLT)
            .multiSelectSubcomponents(BOLT + "," + componentAssembly.getScenarioName() + "", FLANGE + "," + componentAssembly.getScenarioName() + "");

        softAssertions.assertThat(componentsTreePage.isEditButtonDisabled()).isEqualTo(true);
        softAssertions.assertThat(componentsTreePage.getRowDetails(FLANGE, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(NUT, componentAssembly.getScenarioName())).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(BOLT, componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }
}
