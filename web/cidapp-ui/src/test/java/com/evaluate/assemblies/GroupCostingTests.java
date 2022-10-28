package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTablePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Percentage;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GroupCostingTests extends TestBase {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private EvaluatePage evaluatePage;
    private ComponentsTablePage componentsTablePage;
    private ComponentBasicPage componentBasicPage;
    private UserCredentials currentUser;
    private List<File> subComponents = new ArrayList<File>();

    public GroupCostingTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"11088", "11089", "11093", "12041"})
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
        componentsTablePage = evaluatePage.openComponents()
            .selectTableView();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        componentsTablePage.multiSelectSubcomponents(arc + "," + scenarioName, cube50 + "," + scenarioName, ellipse + "," + scenarioName, octagon + "," + scenarioName,
            cube75 + "," + scenarioName, hexagon + "," + scenarioName, cube100 + "," + scenarioName, slot + "," + scenarioName, cuboid + "," + scenarioName,
            cylinder + "," + scenarioName);

        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        evaluatePage = componentsTablePage.closePanel();

        softAssertions.assertThat(evaluatePage.getListOfProcessGroups()).containsOnly(ProcessGroupEnum.ASSEMBLY.getProcessGroup());

        componentsTablePage = evaluatePage.openComponents()
            .selectTableView()
            .multiSelectSubcomponents(arc + "," + scenarioName, cube50 + "," + scenarioName, ellipse + "," + scenarioName,
                octagon + "," + scenarioName, cube75 + "," + scenarioName, hexagon + "," + scenarioName, cube100 + "," + scenarioName, slot + "," + scenarioName,
                cuboid + "," + scenarioName, cylinder + "," + scenarioName, blob + "," + scenarioName);

        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isFalse();

        Random rand = new Random();
        componentsTablePage.clickScenarioCheckbox(subComponentNames.get(rand.nextInt(subComponentNames.size())).toUpperCase(), scenarioName);
        softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).as("Set Inputs Button Enabled").isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"11097", "11090", "11092", "11094", "11091"})
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
        componentsTablePage = evaluatePage.openComponents()
            .selectTableView();

        SoftAssertions softAssertions = new SoftAssertions();

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTablePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon").isEqualTo("circle-minus");
        });

        subComponentNames.forEach(subComponentName -> componentsTablePage.clickScenarioCheckbox(subComponentName.toUpperCase(), scenarioName));

        componentBasicPage = componentsTablePage.setInputs();

        softAssertions.assertThat(componentBasicPage.getProcessGroup()).as("Process Group Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getDigitalFactory()).as("Digital Factory Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getMaterialPlaceholder()).as("Material Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getAnnualVolumePlaceholder()).as("Annual Volume Text").isEqualTo(retainText);
        softAssertions.assertThat(componentBasicPage.getYearsPlaceholder()).as("Years Text").isEqualTo(retainText);

        componentsTablePage = componentBasicPage.selectProcessGroup(prtProcessGroupEnum)
            .clickApplyAndCost(EditScenarioStatusPage.class)
            .close(ComponentsTablePage.class);

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTablePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon").isEqualTo("gear");
            componentsTablePage.multiSelectSubcomponents(subComponentName + "," + scenarioName);
            softAssertions.assertThat(componentsTablePage.isSetInputsEnabled()).as("Set Inputs Button state").isFalse();
            componentsTablePage.multiSelectSubcomponents(subComponentName + "," + scenarioName);
        });

        componentsTablePage.checkSubcomponentState(componentAssembly, subComponentNames.toArray(new String[subComponentNames.size()]));
        evaluatePage.refresh();
        componentsTablePage = evaluatePage.openComponents()
            .selectTableView();

        subComponentNames.forEach(subComponentName -> {
            softAssertions.assertThat(
                componentsTablePage.getScenarioState(subComponentName.toUpperCase(), scenarioName)).as("Costing Icon - " + subComponentName).isEqualTo("check");
            softAssertions.assertThat(
                componentsTablePage.getScenarioFullyBurdenedCost(subComponentName.toUpperCase(), scenarioName)
            ).as("Fully Burdened Cost - " + subComponentName
            ).isCloseTo(fullyBurdenedCosts.get(subComponentName), Percentage.withPercentage(10));
        });

        softAssertions.assertAll();
    }
}
