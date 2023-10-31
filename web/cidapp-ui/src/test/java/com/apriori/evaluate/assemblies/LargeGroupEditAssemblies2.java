package com.apriori.evaluate.assemblies;

import static com.utils.PartNamesEnum.CENTRE_BOLT;
import static com.utils.PartNamesEnum.CENTRE_WASHER;
import static com.utils.PartNamesEnum.DISPLAY;
import static com.utils.PartNamesEnum.GASKET;
import static com.utils.PartNamesEnum.HANDLE;
import static com.utils.PartNamesEnum.LEFT_COVER;
import static com.utils.PartNamesEnum.LEFT_PADDLE;
import static com.utils.PartNamesEnum.MECHANISM_BODY;
import static com.utils.PartNamesEnum.PADDLE_BAR;
import static com.utils.PartNamesEnum.STEER_WHEEL_SUPPORT;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.dto.AssemblyDTORequest;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class LargeGroupEditAssemblies2 extends TestBaseUI {

    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private CidAppLoginPage loginPage;
    private ComponentsTablePage componentsTablePage;
    private SoftAssertions softAssertions = new SoftAssertions();


    @Test
    @TestRail(id = {10886, 10887, 10888})
    @Description("group Edit private sub Components disallowed")
    public void cannotEditPrivateComponents() {

        componentAssembly = new AssemblyDTORequest().getAssembly("Gym Bike");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        componentsTablePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents()
            .selectTableView()
            .multiSelectSubcomponents(CENTRE_BOLT.getPartName() + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents(CENTRE_WASHER.getPartName() + "," + componentAssembly.getScenarioName(), DISPLAY.getPartName()
            + "," + componentAssembly.getScenarioName(), GASKET.getPartName() + "," + componentAssembly.getScenarioName(), HANDLE.getPartName() + "," + componentAssembly.getScenarioName());
        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        componentsTablePage.multiSelectSubcomponents(LEFT_PADDLE.getPartName() + "," + componentAssembly.getScenarioName(), LEFT_COVER.getPartName()
            + "," + componentAssembly.getScenarioName(), STEER_WHEEL_SUPPORT.getPartName() + "," + componentAssembly.getScenarioName(), MECHANISM_BODY.getPartName()
            + "," + componentAssembly.getScenarioName(), PADDLE_BAR.getPartName() + "," + componentAssembly.getScenarioName());
        softAssertions.assertThat(componentsTablePage.isEditButtonDisabled()).isEqualTo(true);

        softAssertions.assertAll();
    }
}
