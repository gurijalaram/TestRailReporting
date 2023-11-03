package com.apriori.cid.ui.tests.evaluate.assemblies;

import com.apriori.cid.api.models.dto.AssemblyDTORequest;
import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.stream.Collectors;

public class GroupCostingTests extends TestBaseUI {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentBasicPage componentBasicPage;

    public GroupCostingTests() {
        super();
    }

    @Test
    @TestRail(id = {11088, 11089, 11093, 12041})
    @Description("Verify set inputs button only available for 10 or less sub-components")
    public void selectMaxTenSubComponentsTest() {

        String blob = "500mmBlob";

        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("RandomShapeAsm");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName());

        componentsTreePage = evaluatePage.openComponents();


        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        componentAssembly.getSubComponents().stream().filter(o -> !o.getComponentName().equalsIgnoreCase(blob)).collect(Collectors.toList())
            .forEach(subComponent -> componentsTreePage.multiSelectSubcomponents(subComponent.getComponentName(), subComponent.getScenarioName()));

        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        evaluatePage = componentsTreePage.closePanel();

        softAssertions.assertThat(evaluatePage.getListOfProcessGroups()).containsOnly(ProcessGroupEnum.ASSEMBLY.getProcessGroup());

        componentsTreePage = evaluatePage.openComponents()
            .multiSelectSubcomponents(blob + "," + componentAssembly.getScenarioName());

        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        componentsTreePage.clickScenarioCheckbox(componentAssembly.getSubComponents().stream().findAny().get().getComponentName().toUpperCase(), componentAssembly.getScenarioName());
        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11097, 11090, 11092, 11094, 11091})
    @Description("Verify sub-components are group costed successfully.")
    public void groupCostSubComponentsTest() {
        final String retainText = "Retain Existing Input";

        String bigRing = "big ring";
        String smallRing = "small ring";
        String pin = "Pin";

        final HashMap<String, Double> fullyBurdenedCosts = new HashMap<>();
        fullyBurdenedCosts.put(bigRing, 7.34);
        fullyBurdenedCosts.put(smallRing, 3.97);
        fullyBurdenedCosts.put(pin, 3.31);

        ComponentInfoBuilder componentAssembly = new AssemblyDTORequest().getAssembly("Hinge assembly");

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(componentAssembly.getUser())
            .openScenario(componentAssembly.getComponentName(), componentAssembly.getScenarioName());
        componentsTreePage = evaluatePage.openComponents();

        SoftAssertions softAssertions = new SoftAssertions();

        componentAssembly.getSubComponents().forEach(subComponentName -> softAssertions.assertThat(
            componentsTreePage.getScenarioState(subComponentName.getComponentName().toUpperCase(), componentAssembly.getScenarioName())).as("Costing Icon").isEqualTo("circle-minus"));

        componentAssembly.getSubComponents().forEach(subComponentName -> componentsTreePage.clickScenarioCheckbox(subComponentName.getComponentName().toUpperCase(), componentAssembly.getScenarioName()));

        componentBasicPage = componentsTreePage.setInputs();

        softAssertions.assertThat(componentBasicPage.getProcessGroup()).as("Process Group Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getDigitalFactory()).as("Digital Factory Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getMaterialPlaceholder()).as("Material Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getAnnualVolumePlaceholder()).as("Annual Volume Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getYearsPlaceholder()).as("Years Text").isEqualTo(retainText);

        componentsTreePage = componentBasicPage.selectProcessGroup(componentAssembly.getSubComponents().get(0).getProcessGroup())
            .clickApplyAndCost(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class);

        componentAssembly.getSubComponents().forEach(subComponent -> {
            softAssertions.assertThat(
                componentsTreePage.getScenarioState(subComponent.getComponentName().toUpperCase(), subComponent.getScenarioName())).as("Costing Icon").isEqualTo("gear");
            componentsTreePage.multiSelectSubcomponents(subComponent.getComponentName() + "," + subComponent.getScenarioName());
            softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button state").isFalse();
            componentsTreePage.multiSelectSubcomponents(subComponent.getComponentName().toUpperCase() + "," + subComponent.getScenarioName());
        });

        componentAssembly.getSubComponents().forEach(subComponent  -> componentsTreePage.checkSubcomponentState(componentAssembly, subComponent.getComponentName()));
        evaluatePage.refresh();
        componentsTreePage = evaluatePage.openComponents();

        componentAssembly.getSubComponents().forEach(componentName -> {
            softAssertions.assertThat(
                componentsTreePage.getScenarioState(componentName.getComponentName().toUpperCase(), componentName.getScenarioName())).as("Costing Icon - " + componentName).isEqualTo("check");
            softAssertions.assertThat(
                componentsTreePage.getScenarioFullyBurdenedCost(componentName.getComponentName().toUpperCase(), componentName.getScenarioName())
            ).as("Fully Burdened Cost - " + componentName
            ).isCloseTo(fullyBurdenedCosts.get(componentName.getComponentName()), Percentage.withPercentage(10));
        });

        softAssertions.assertAll();
    }
}
