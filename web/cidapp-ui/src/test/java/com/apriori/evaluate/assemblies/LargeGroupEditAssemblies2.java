package com.apriori.evaluate.assemblies;

import static com.apriori.enums.ProcessGroupEnum.ASSEMBLY;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class LargeGroupEditAssemblies2 extends TestBaseUI {

    private static String scenarioName;
    private static String scenarioName2;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserCredentials currentUser;
    private static ComponentInfoBuilder componentAssembly;
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @BeforeEach
    public void setupAssembly() {
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
    @TestRail(id = {10886, 10887, 10888})
    @Description("group Edit private sub Components disallowed")
    public void cannotEditPrivateComponents() {
        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(currentUser)
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents("centre bolt, " + scenarioName + "");

        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents("centre washer, " + scenarioName + "", "display, " + scenarioName + "", "gasket, " + scenarioName + "", "Handle, " + scenarioName + "");
        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents("left paddle, " + scenarioName + "", "leg cover, " + scenarioName + "", "steer wheel support, " + scenarioName + "", "mechanism body, " + scenarioName + "", "paddle bar, " + scenarioName + "");
        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
