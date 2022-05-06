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
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupEditAssemblies extends TestBase {

    private static String scenarioName;
    private static String scenarioName2;
    private CidAppLoginPage loginPage;
    private ComponentsListPage componentsListPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;

    @BeforeClass
    public static void setupAssembly() {
        final String assemblyName = "Gym Bike";
        final String assemblyExtension = ".iam";

        List<String> subComponentNames = Arrays.asList("centre bolt", "centre washer", "display", "gasket", "Handle", "left paddle", "leg cover", "leg", "mechanism body", "paddle bar", "pin", "right paddle", "seat lock", "seat", "steer wheel support", "washer");
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.PLASTIC_MOLDING;
        final String subComponentExtension = ".ipt";

        currentUser = UserUtil.getUser();
        scenarioName = new GenerateStringUtil().generateScenarioName();
        scenarioName2 = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
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
    }

    @Test
    @TestRail(testCaseId = {"10883", "10884", "10885", "10894", "11140"})
    @Description("group Edit sub Components")
    public void editButtonUnavailable() {

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
                .multiSelectSubcomponents("centre bolt, " + scenarioName + "", "centre washer, " + scenarioName + "", "display, " + scenarioName + "", "gasket, " + scenarioName + "", "Handle, " + scenarioName + "", "left paddle, " + scenarioName + "", "leg cover, " + scenarioName + "", "leg, " + scenarioName + "", "mechanism body, " + scenarioName + "", "paddle bar, " + scenarioName + "", "pin, " + scenarioName + "");
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
}
