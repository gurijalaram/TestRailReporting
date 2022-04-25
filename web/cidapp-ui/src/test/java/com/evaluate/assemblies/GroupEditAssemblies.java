package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
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

import java.util.Arrays;
import java.util.List;

public class GroupEditAssemblies extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsListPage componentsListPage;
    private UserCredentials currentUser;
    private ComponentsUtil componentsUtil = new ComponentsUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

    public GroupEditAssemblies() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"10882", "10890", "10893"})
    @Description("group Edit sub Components")
    public void editButtonAvailable() {

        String assemblyName = "Hinge assembly";
        final ProcessGroupEnum assemblyProcessGroup = ASSEMBLY;
        final String assemblyExtension = ".SLDASM";

        List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final String subComponentExtension = ".SLDPRT";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
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

        SoftAssertions softAssertions = new SoftAssertions();
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
    @TestRail(testCaseId = {"10883", "10884", "10885", "10894"})
    @Description("group Edit sub Components")
    public void editButtonUnavailable() {

        String assemblyName = "Gym Bike";
        final ProcessGroupEnum assemblyProcessGroup = ASSEMBLY;
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList("centre bolt", "centre washer", "display", "gasket", "Handle", "left paddle", "leg cover", "leg", "mechanism body", "paddle bar", "pin", "right paddle", "seat lock", "seat", "steer wheel support", "washer");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final String subComponentExtension = ".ipt";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
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
                .multiSelectSubcomponents("centre bolt, " + scenarioName + "", "centre washer, " + scenarioName + "", "display, " + scenarioName + "", "gasket, " + scenarioName + "", "Handle, " + scenarioName + "", "left paddle, " + scenarioName + "", "leg cover, " + scenarioName + "", "leg, " + scenarioName + "", "mechanism body, " + scenarioName + "", "paddle bar, " + scenarioName + "", "pin, " + scenarioName + "");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("centre bolt, " + scenarioName + "")
                .editSubcomponent(EditComponentsPage.class)
                .renameScenarios()
                .enterScenarioName(scenarioName2)
                .clickContinue(EditScenarioStatusPage.class)
                .close(ComponentsListPage.class)
                .checkSubcomponentState(componentAssembly, "centre washer, display, gasket, Handle, left paddle, leg cover, leg, mechanism body, paddle bar, pin");

        softAssertions.assertThat(componentsListPage.getRowDetails("centre washer", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("display", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("gasket", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("Handle", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("left paddle", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("leg cover", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("leg", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("mechanism body", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("paddle bar", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("pin", scenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("centre bolt", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("seat", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("steer wheel support", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsListPage.getRowDetails("washer", scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"10886", "10887", "10888"})
    @Description("group Edit private sub Components disallowed")
    public void cannotEditPrivateComponents() {

        String assemblyName = "Gym Bike";
        final ProcessGroupEnum assemblyProcessGroup = ASSEMBLY;
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList("centre bolt", "centre washer", "display", "gasket", "Handle", "left paddle", "leg cover", "leg", "mechanism body", "paddle bar", "pin", "right paddle", "seat lock", "seat", "steer wheel support", "washer");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final String subComponentExtension = ".ipt";

        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
                assemblyExtension,
                assemblyProcessGroup,
                subComponentNames,
                subComponentExtension,
                subComponentProcessGroup,
                scenarioName,
                currentUser);
        assemblyUtils.uploadSubComponents(componentAssembly)
                .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
                .navigateToScenario(componentAssembly)
                .openComponents()
                .multiSelectSubcomponents("centre bolt, " + scenarioName + "");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("centre washer, " + scenarioName + "", "display, " + scenarioName + "", "gasket, " + scenarioName + "", "Handle, " + scenarioName + "");
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("left paddle, " + scenarioName + "", "leg cover, " + scenarioName + "", "steer wheel support, " + scenarioName + "", "mechanism body, " + scenarioName + "", "paddle bar, " + scenarioName + "");
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
