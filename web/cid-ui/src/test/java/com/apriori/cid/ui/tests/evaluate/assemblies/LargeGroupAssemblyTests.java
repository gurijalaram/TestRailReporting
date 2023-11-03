package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.cid.ui.utils.PartNamesEnum.PIN;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;

import com.apriori.cid.api.models.dto.AssemblyDTORequest;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ButtonTypeEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class LargeGroupAssemblyTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private static ComponentInfoBuilder componentAssembly;
    private ComponentsTreePage componentsTreePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {11807, 11804})
    @Description("Publish button becomes unavailable when 11+ private sub-components selected")
    public void testPublishButtonAvailability() {

        componentAssembly = new AssemblyDTORequest().getAssembly("Gym Bike");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        List<ComponentInfoBuilder> filteredList = componentAssembly.getSubComponents().stream().filter(o -> !o.getComponentName().equalsIgnoreCase("Pin")).collect(Collectors.toList());

        loginPage = new CidAppLoginPage(driver);
        componentsTreePage = loginPage.login(componentAssembly.getUser())
            .navigateToScenario(componentAssembly)
            .openComponents();

        filteredList.forEach(component -> componentsTreePage.multiSelectSubcomponents(component.getComponentName() + ", " + componentAssembly.getScenarioName()));

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(true);

        componentsTreePage.multiSelectSubcomponents(PIN.getPartName() + ", " + componentAssembly.getScenarioName() + "");

        softAssertions.assertThat(componentsTreePage.isAssemblyTableButtonEnabled(ButtonTypeEnum.PUBLISH)).isEqualTo(false);

        softAssertions.assertAll();
    }
}
