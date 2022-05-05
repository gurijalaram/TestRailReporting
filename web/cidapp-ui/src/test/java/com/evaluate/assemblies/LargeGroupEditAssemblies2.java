package com.evaluate.assemblies;

import static com.apriori.utils.enums.ProcessGroupEnum.ASSEMBLY;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsListPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupEditAssemblies2 extends TestBase {

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
    @TestRail(testCaseId = {"10886", "10887", "10888"})
    @Description("group Edit private sub Components disallowed")
    public void cannotEditPrivateComponents() {

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CidAppLoginPage(driver);
        componentsListPage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
                .openComponents()
                .multiSelectSubcomponents("centre bolt, " + scenarioName + "");

        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("centre washer, " + scenarioName + "", "display, " + scenarioName + "", "gasket, " + scenarioName + "", "Handle, " + scenarioName + "");
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        componentsListPage.multiSelectSubcomponents("left paddle, " + scenarioName + "", "leg cover, " + scenarioName + "", "steer wheel support, " + scenarioName + "", "mechanism body, " + scenarioName + "", "paddle bar, " + scenarioName + "");
        softAssertions.assertThat(componentsListPage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
