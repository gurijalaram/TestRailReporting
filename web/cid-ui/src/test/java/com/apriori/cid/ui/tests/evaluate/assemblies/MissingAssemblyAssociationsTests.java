package com.apriori.cid.ui.tests.evaluate.assemblies;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.enums.PreferencesEnum;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MissingAssemblyAssociationsTests extends TestBaseUI {

    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();
    private CidAppLoginPage loginPage;
    private ComponentsTreePage componentsTreePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;

    @AfterEach
    public void deleteScenarios() {
        if (componentAssembly != null) {
            userPreferencesUtil.resetSettings(componentAssembly.getUser());

            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
        }
    }

    @Test
    @TestRail(id = {21669, 21670})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with same scenario name are associated to assembly")
    public void testMaturityPresetPrivateWithSameNameAndMissing() {
        final String newScenarioName = new GenerateStringUtil().generateScenarioName();

        String asmStrategy = "PREFER_HIGH_MATURITY";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(componentAssembly.getUser(), updateStrategy);

        componentAssembly = new AssemblyRequestUtil().getAssembly("Miss_Fuse_Block_Asm");
        ComponentInfoBuilder componentAssemblyB = componentAssembly;
        componentAssemblyB.setScenarioName(newScenarioName);
        ComponentInfoBuilder conductor = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("Miss_conductor")).findFirst().get();
        ComponentInfoBuilder housing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("Miss_Fuse_Connector_Housing")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .uploadComponent(componentAssemblyB)
            .refresh()
            .navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor.getComponentName(), componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor.getComponentName(), componentAssembly.getScenarioName())).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing.getComponentName(), componentAssembly.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing.getComponentName(), componentAssembly.getScenarioName())).contains(StatusIconEnum.VERIFIED.getStatusIcon());

        componentsTreePage.closePanel()
            .navigateToScenario(componentAssemblyB)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor.getComponentName(), componentAssemblyB.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor.getComponentName(), componentAssemblyB.getScenarioName())).contains(StatusIconEnum.MISSING.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing.getComponentName(), componentAssemblyB.getScenarioName())).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing.getComponentName(), componentAssemblyB.getScenarioName())).contains(StatusIconEnum.MISSING.getStatusIcon());

        softAssertions.assertAll();
    }
}

