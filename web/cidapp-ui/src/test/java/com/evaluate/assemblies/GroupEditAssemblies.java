package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.util.Arrays;
import java.util.List;

public class GroupEditAssemblies extends TestBase {

    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private EditScenarioStatusPage editScenarioStatusPage;
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    public GroupEditAssemblies() {
        super();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"10882", "10890", "10893"})
    @Description("Group edit subcomponents")
    public void editButtonAvailable() {

        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        SoftAssertions softAssertions = new SoftAssertions();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents("big ring, " + scenarioName + "", "pin, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(true);

        componentsListPage.editSubcomponent(EditComponentsPage.class)
            .overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class)
            .close(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, "big ring, pin");

        softAssertions.assertThat(componentsListPage.getRowDetails("pin", scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("big ring", scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("small ring", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10891", "11132"})
    @Description("Group edit subcomponents")
    public void overridePrivateSubComponent() {

        String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        SoftAssertions softAssertions = new SoftAssertions();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        editScenarioStatusPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents("big ring, " + scenarioName + "")
            .editSubcomponent(EditScenarioStatusPage.class);

        softAssertions.assertThat(editScenarioStatusPage.getEditScenarioMessage()).contains("Scenario was successfully edited, click here to open in the evaluate view.");

        componentsListPage = editScenarioStatusPage.close(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, "big ring");

        softAssertions.assertThat(componentsListPage.getRowDetails("pin", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("big ring", scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("small ring", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10889", "11953"})
    @Description("Group edit subcomponents")
    public void privateAndPublicSubComponents() {

        String assemblyName = "flange c";
        final String assemblyExtension = ".CATProduct";

        List<String> subComponentNames = Arrays.asList("bolt", "flange", "nut");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final String subComponentExtension = ".CATPart";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        SoftAssertions softAssertions = new SoftAssertions();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            ASSEMBLY,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .multiSelectSubcomponents("bolt, " + scenarioName + "")
            .editSubcomponent(EditScenarioStatusPage.class)
            .close(ComponentsListPage.class)
            .checkSubcomponentState(componentAssembly, "bolt")
            .multiSelectSubcomponents("bolt, " + scenarioName + "", "flange, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);
        softAssertions.assertThat(componentsListPage.getRowDetails("flange", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("nut", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("bolt", scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }
}
