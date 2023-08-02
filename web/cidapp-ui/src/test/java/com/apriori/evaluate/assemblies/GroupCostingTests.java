package com.apriori.evaluate.assemblies;

import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GroupCostingTests extends TestBaseUI {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private ComponentBasicPage componentBasicPage;
    private UserCredentials currentUser;
    private List<File> subComponents = new ArrayList<File>();

    public GroupCostingTests() {
        super();
    }

    @Test
    @TestRail(id = {11088, 11089, 11093, 12041})
    @Description("Verify set inputs button only available for 10 or less sub-components")
    public void selectMaxTenSubComponentsTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String assemblyName = "RandomShapeAsm";
        String assemblyExtension = ".SLDASM";
        String subComponentExtension = ".SLDPRT";
        String arc = "50mmArc";
        String cube50 = "50mmCube";
        String ellipse = "50mmEllipse";
        String octagon = "50mmOctagon";
        String cube75 = "75mmCube";
        String hexagon = "75mmHexagon";
        String cube100 = "100mmCube";
        String slot = "100mmSlot";
        String cuboid = "150mmCuboid";
        String cylinder = "200mmCylinder";
        String blob = "500mmBlob";

        final List<String> subComponentNames = Arrays.asList(
            arc, cube50, ellipse, octagon, cube75, hexagon, cube100, slot, cuboid, cylinder, blob);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            processGroupEnum,
            subComponentNames,
            subComponentExtension,
            processGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .openScenario(assemblyName, scenarioName);
        componentsTreePage = evaluatePage.openComponents();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        componentsTreePage.multiSelectSubcomponents(arc + "," + scenarioName, cube50 + "," + scenarioName, ellipse + "," + scenarioName, octagon + "," + scenarioName,
            cube75 + "," + scenarioName, hexagon + "," + scenarioName, cube100 + "," + scenarioName, slot + "," + scenarioName, cuboid + "," + scenarioName,
            cylinder + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        evaluatePage = componentsTreePage.closePanel();

        softAssertions.assertThat(evaluatePage.getListOfProcessGroups()).containsOnly(ProcessGroupEnum.ASSEMBLY.getProcessGroup());

        componentsTreePage = evaluatePage.openComponents()
            .multiSelectSubcomponents(blob + "," + scenarioName);

        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        Random rand = new Random();
        componentsTreePage.clickScenarioCheckbox(subComponentNames.get(rand.nextInt(subComponentNames.size())).toUpperCase(), scenarioName);
        softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {11097, 11090, 11092, 11094, 11091})
    @Description("Verify sub-components are group costed successfully.")
    public void groupCostSubComponentsTest() {
        final String retainText = "Retain Existing Input";
        final ProcessGroupEnum prtProcessGroupEnum = ProcessGroupEnum.FORGING;
        final ProcessGroupEnum asmProcessGroupEnum = ProcessGroupEnum.ASSEMBLY;
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        String bigRing = "big ring";
        String smallRing = "small ring";
        String pin = "Pin";
        String subComponentExtension = ".SLDPRT";
        String assemblyName = "Hinge assembly";
        String assemblyExtension = ".SLDASM";

        final List<String> subComponentNames = Arrays.asList(bigRing, smallRing, pin);
        final HashMap<String, Double> fullyBurdenedCosts = new HashMap<String, Double>();
        fullyBurdenedCosts.put(bigRing, 7.34);
        fullyBurdenedCosts.put(smallRing, 3.97);
        fullyBurdenedCosts.put(pin, 3.31);

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            asmProcessGroupEnum,
            subComponentNames,
            subComponentExtension,
            prtProcessGroupEnum,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        evaluatePage = new CidAppLoginPage(driver)
            .login(currentUser)
            .openScenario(assemblyName, scenarioName);
        componentsTreePage = evaluatePage.openComponents();

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTreePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon").isEqualTo("circle-minus");
        });

        subComponentNames.forEach(subComponentName -> componentsTreePage.clickScenarioCheckbox(subComponentName.toUpperCase(), scenarioName));

        componentBasicPage = componentsTreePage.setInputs();

        softAssertions.assertThat(componentBasicPage.getProcessGroup()).as("Process Group Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getDigitalFactory()).as("Digital Factory Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getMaterialPlaceholder()).as("Material Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getAnnualVolumePlaceholder()).as("Annual Volume Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getYearsPlaceholder()).as("Years Text").isEqualTo(retainText);

        componentsTreePage = componentBasicPage.selectProcessGroup(prtProcessGroupEnum)
            .clickApplyAndCost(EditScenarioStatusPage.class)
            .close(ComponentsTreePage.class);

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTreePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon").isEqualTo("gear");
            componentsTreePage.multiSelectSubcomponents(subComponentName + "," + scenarioName);
            softAssertions.assertThat(componentsTreePage.isSetInputsEnabled()).as("Set Inputs Button state").isFalse();
            componentsTreePage.multiSelectSubcomponents(subComponentName + "," + scenarioName);
        });

        componentsTreePage.checkSubcomponentState(componentAssembly, subComponentNames.toArray(new String[subComponentNames.size()]));
        evaluatePage.refresh();
        componentsTreePage = evaluatePage.openComponents();

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTreePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon - " + subComponentName).isEqualTo("check");
            softAssertions.assertThat(
                componentsTreePage.getScenarioFullyBurdenedCost(subComponentName.toUpperCase(), scenarioName)
            ).as("Fully Burdened Cost - " + subComponentName
            ).isCloseTo(fullyBurdenedCosts.get(subComponentName), Percentage.withPercentage(10));
        });

        softAssertions.assertAll();
    }
}
