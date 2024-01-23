package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.MultiUpload;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.AssemblyRequestUtil;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupCostTests extends TestBaseUI {

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private CssComponent cssComponent = new CssComponent();

    @Test
    @TestRail(id = {14796})
    @Description("Select multiple private components and cost")
    public void testGroupCost() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        componentB.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .uploadComponent(componentB).refresh()
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
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
            .checkComponentStateRefresh(component, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(componentB, ScenarioStateEnum.COST_COMPLETE);

        softAssertions.assertThat(explorePage.getRowDetails(component.getComponentName(), component.getScenarioName())).contains(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6,000");
        softAssertions.assertThat(explorePage.getRowDetails(componentB.getComponentName(), componentB.getScenarioName())).contains(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6,000");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14797})
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
    @TestRail(id = {14798})
    @Description("Verify that Cost button is disabled when a combination of private and public parts are selected")
    public void testGroupCostPrivateAndPublicScenarios() {
        ComponentInfoBuilder component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        componentB.setUser(component.getUser());
        ComponentInfoBuilder componentC = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_SAND);
        componentC.setUser(component.getUser());

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(component.getUser())
            .uploadComponent(component)
            .uploadComponent(componentB)
            .uploadComponent(componentC)
            .refresh()
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName());

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.openScenario(componentC.getComponentName(), componentC.getScenarioName())
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentC.getComponentName() + ", " + componentC.getScenarioName())
            .selectFilter("Private")
            .multiSelectScenarios(component.getComponentName() + ", " + component.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName());

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14800})
    @Description("Verify that Cost button is disabled when a combination of parts and assemblies are selected")
    public void testGroupCostPartAndAssemblyScenarios() {
        ComponentInfoBuilder componentAssembly = new AssemblyRequestUtil().getAssembly("Hinge assembly");
        ComponentInfoBuilder pin = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("pin")).findFirst().get();
        ComponentInfoBuilder bigRing = componentAssembly.getSubComponents().stream().filter(o -> o.getComponentName().equalsIgnoreCase("big ring")).findFirst().get();

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(componentAssembly.getUser())
            .multiSelectScenarios(bigRing.getComponentName() + "," + bigRing.getScenarioName(), pin.getComponentName() + "," + pin.getScenarioName());

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios(componentAssembly.getComponentName() + ", " + componentAssembly.getScenarioName());

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {14801, 14799})
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
            .multiSelectScenarios("big ring" + ", " + scenarioName, "titan charger lead" + ", " + scenarioName);

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .multiSelectScenarios("big ring" + ", " + scenarioName, "titan charger lead" + ", " + scenarioName);

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("big ring" + ", " + scenarioName)
            .clickCostButton(ComponentBasicPage.class)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .multiSelectScenarios("big ring" + ", " + scenarioName, "titan charger lead" + ", " + scenarioName);

        softAssertions.assertThat(explorePage.isCostButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
