package com.explore;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.NewCostingLabelEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupCostTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemA;
    private final SoftAssertions softAssertions = new SoftAssertions();
    private final AssemblyUtils assemblyUtils = new AssemblyUtils();
    private CssComponent cssComponent = new CssComponent();

    @Test
    @TestRail(testCaseId = {"14796"})
    @Description("Select multiple private components and cost")
    public void testGroupCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        final String componentName2 = "bracket_basic";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".prt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        explorePage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .enterAnnualYears("7")
            .enterAnnualVolume("6000")
            .openMaterialSelectorTable()
            .search("1050A")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_1050A.getMaterialName())
            .submit(ComponentBasicPage.class)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemB, ScenarioStateEnum.COST_COMPLETE);

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).contains(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6,000");
        softAssertions.assertThat(explorePage.getRowDetails(componentName2, scenarioName2)).contains(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6,000");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14797"})
    @Description("Verify that Cost button is disabled when more than 10 private components are selected")
    public void testCostMoreThanTenScenarios() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan charger lead.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0005b.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .setPagination()
            .selectFilter("Recent");

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination()
            .multiSelectScenarios("" + "piston rod_model1" + ", " + scenarioName + "",
                "" + "Part0005b" + ", " + scenarioName + "",
                "" + "piston cover_model1" + ", " + scenarioName + "",
                "" + "Push Pin" + ", " + scenarioName + "",
                "" + "PowderMetalShaft" + ", " + scenarioName + "",
                "" + "bracket_basic" + ", " + scenarioName + "",
                "" + "Part0004" + ", " + scenarioName + "",
                "" + "small ring" + ", " + scenarioName + "",
                "" + "titan charger lead" + ", " + scenarioName + "",
                "" + "big ring" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + "piston_model1" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14798"})
    @Description("Verify that Cost button is disabled when a combination of private and public parts are selected")
    public void testGroupCostPrivateAndPublicScenarios() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final ProcessGroupEnum processGroupEnum3 = ProcessGroupEnum.CASTING_SAND;
        String componentName3 = "SandCast";
        final File resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum3, componentName3 + ".x_t");
        final String scenarioName3 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName1, scenarioName1, resourceFile1, currentUser);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);
        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName3, scenarioName3, resourceFile3, currentUser);

        explorePage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.openScenario(componentName3, scenarioName3)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName3 + ", " + scenarioName3 + "")
            .selectFilter("Private")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14800"})
    @Description("Verify that Cost button is disabled when a combination of parts and assemblies are selected")
    public void testGroupCostPartAndAssemblyScenarios() {
        currentUser = UserUtil.getUser();
        final String assemblyName = "Hinge assembly";
        final String assemblyExtension = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            assemblyScenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + "big ring" + ", " + assemblyScenarioName + "",
                "" + "Pin" + ", " + assemblyScenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + assemblyName + ", " + assemblyScenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(testCaseId = {"14801", "14799"})
    @Description("Verify that Cost button is disabled when any in a multi-selection of parts has Component Type Unknown")
    public void testGroupCostUnknownScenarios() {
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan charger lead.SLDPRT"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .selectFilter("Recent")
            .multiSelectScenarios("" + "big ring" + ", " + scenarioName + "",
                    "" + "titan charger lead" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .multiSelectScenarios("" + "big ring" + ", " + scenarioName + "",
                   "" + "titan charger lead" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + "big ring" + ", " + scenarioName + "")
            .clickCostButton(ComponentBasicPage.class)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .multiSelectScenarios("" + "big ring" + ", " + scenarioName + "",
                "" + "titan charger lead" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
