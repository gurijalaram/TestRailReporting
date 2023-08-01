package com.apriori.evaluate.assemblies;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.enums.PreferencesEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.StatusIconEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissingAssemblyAssociationsTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ComponentsTreePage componentsTreePage;
    private UserCredentials currentUser;

    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder cidComponentItemA;

    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

    @AfterEach
    public void deleteScenarios() {
        if (currentUser != null) {
            userPreferencesUtil.resetSettings(currentUser);

            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
        }

        if (cidComponentItemA != null) {
            scenariosUtil.deleteScenario(cidComponentItemA.getComponentIdentity(), cidComponentItemA.getScenarioIdentity(), currentUser);
            cidComponentItemA = null;
        }
    }

    @Test
    @TestRail(id = {21669, 21670})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with same scenario name are associated to assembly")
    public void testMaturityPresetPrivateWithSameNameAndMissing() {
        final String fuse_block_asm = "Miss_Fuse_Block_Asm";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final String assemblyExtension = ".CATProduct";
        final String conductor = "Miss_Conductor";
        final String housing = "Miss_Fuse_Connector_Housing";
        final String subComponentExtension = ".CATPart";
        final List<String> subComponentNames = Arrays.asList(housing);
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;
        final File resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuse_block_asm + assemblyExtension);

        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuse_block_asm,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser)
            .uploadComponent(fuse_block_asm, newScenarioName, resourceFile, currentUser);

        componentsTreePage = new EvaluatePage(driver).refresh()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());

        componentsTreePage.closePanel()
            .navigateToScenario(cidComponentItemA)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, newScenarioName)).contains(StatusIconEnum.MISSING.getStatusIcon());

        softAssertions.assertAll();
    }
}

