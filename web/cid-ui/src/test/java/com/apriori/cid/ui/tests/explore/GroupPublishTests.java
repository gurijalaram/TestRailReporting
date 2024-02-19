package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.AssignPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.InfoPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.MultiUpload;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.CostingTemplate;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPublishTests extends TestBaseUI {

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final CssComponent cssComponent = new CssComponent();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private final AssemblyUtils assemblyUtils = new AssemblyUtils();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private PublishPage publishPage;
    private AssignPage assignPage;
    private InfoPage infoPage;

    @Test
    @TestRail(id = {14458})
    @Description("Publish multiple components")
    public void testGroupPublishPartScenarios() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        componentB.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(componentA.getUser())
            .uploadComponent(componentA)
            .uploadComponent(componentB)
            .refresh()
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class);

        Arrays.asList(componentA, componentB).forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentA.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentA.getComponentName(), componentA.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16110})
    @Description("Publish maximum of 10 components")
    public void testMaxGroupPublishPartScenarios() {
        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(10);
        List<String> multiSelectString = components.stream().map(component -> component.getComponentName() + ", " + component.getScenarioName()).collect(Collectors.toList());

        componentsUtil.postCadUploadComponentSuccess(components);

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(components.get(0).getUser())
            .multiSelectScenarios(multiSelectString.toArray(new String[components.size()]))
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class);

        components.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(component.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .selectFilter("Public");

        components.forEach(component ->
            softAssertions.assertThat(explorePage.getListOfScenarios(component.getComponentName(), component.getScenarioName()))
                .as("Verify newly published components are visible in Public filter").isEqualTo(1));

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {14463, 14460})
    @Description("Publish multiple assemblies")
    public void testGroupPublishAssemblyScenarios() {
        currentUser = UserUtil.getUser();
        final String assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup1 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            assemblyProcessGroup1,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        // TODO: 20/02/2023 cn - nick pls fix these templates
        CostingTemplate pinTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup()).productionLife(9.0).build();
        CostingTemplate bigRingTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.POWDER_METAL.getProcessGroup()).annualVolume(700).build();

        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly1, pinTemplate, "pin", "small ring");
        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly1, bigRingTemplate, "big ring");

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);
        assemblyUtils.publishSubComponents(componentAssembly1);

        final String assemblyName2 = "flange c";
        final String assemblyExtension2 = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup2 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames2 = Arrays.asList("flange", "nut", "bolt");
        final String subComponentExtension2 = ".CATPart";
        final ProcessGroupEnum subComponentProcessGroup2 = ProcessGroupEnum.PLASTIC_MOLDING;

        final String assemblyScenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName2,
            assemblyExtension2,
            assemblyProcessGroup2,
            subComponentNames2,
            subComponentExtension2,
            subComponentProcessGroup2,
            assemblyScenarioName2,
            currentUser);

        // TODO: 20/02/2023 cn - nick pls fix these templates
        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()).build();

        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly2, costingTemplate, "flange", "nut", "bolt");

        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);
        assemblyUtils.costSubComponents(componentAssembly2).costAssembly(componentAssembly2);
        assemblyUtils.publishSubComponents(componentAssembly2);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "", "" + assemblyName2 + ", " + assemblyScenarioName2 + "")
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly1, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(componentAssembly2, ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName1, assemblyScenarioName1)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName2, assemblyScenarioName2)).isEqualTo(1);

        explorePage.multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "", "" + assemblyName2 + ", " + assemblyScenarioName2 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14459})
    @Description("Publish a mix of components and assemblies")
    public void testGroupPublishComponentAndAssembly() {
        currentUser = UserUtil.getUser();
        final String assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup1 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            assemblyProcessGroup1,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + "big ring" + ", " + assemblyScenarioName1 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14464, 21551})
    @Description("Publish multiple components and set inputs in modal")
    public void testGroupPublishWithInputs() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.PLASTIC_MOLDING);
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.SHEET_METAL);
        componentB.setUser(componentA.getUser());

        loginPage = new CidAppLoginPage(driver);

        publishPage = loginPage.login(componentA.getUser())
            .uploadComponent(componentA)
            .uploadComponent(componentB)
            .refresh()
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName())
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class);

        softAssertions.assertThat(publishPage.getAssociationAlert()).contains("High maturity and complete status scenarios can be prioritized to make more accurate associations when uploading new assemblies.");

        publishPage.selectStatus("Analysis")
            .selectCostMaturity("Low")
            .selectAssignee(componentA.getUser())
            .publish(PublishPage.class);

        explorePage = publishPage.close(ExplorePage.class);

        Arrays.asList(componentA, componentB).forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(componentA.getUser(), COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        assignPage = explorePage.refresh()
            .selectFilter("Public")
            .openScenario(componentA.getComponentName(), componentA.getScenarioName())
            .clickActions()
            .assign();

        String scenarioAssignedTo = scenariosUtil.getScenarioCompleted(componentA).getCreatedByName();

        softAssertions.assertThat(assignPage.isAssigneeDisplayed(scenarioAssignedTo)).isEqualTo(true);

        infoPage = assignPage.cancel(EvaluatePage.class)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");

        infoPage.cancel(EvaluatePage.class)
            .clickExplore()
            .openScenario(componentB.getComponentName(), componentB.getScenarioName())
            .clickActions()
            .assign();

        softAssertions.assertThat(assignPage.isAssigneeDisplayed(scenarioAssignedTo)).isEqualTo(true);

        infoPage = assignPage.cancel(EvaluatePage.class)
            .clickActions()
            .info();
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14461, 14462})
    @Description("Attempt to publish more than 10 components/assemblies")
    public void testGroupPublishMoreThanTenScenarios() {
        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
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
            .inputDefaultScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .setPagination()
            .selectFilter("Recent")
            .multiSelectScenarios("" + "piston rod_model1" + ", " + scenarioName + "",
                "" + "Part0005b" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

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

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + "piston_model1" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
