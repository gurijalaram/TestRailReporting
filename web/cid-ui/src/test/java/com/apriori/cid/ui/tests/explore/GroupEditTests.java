package com.apriori.cid.ui.tests.explore;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.EditComponentsPage;
import com.apriori.cid.ui.pageobjects.explore.EditScenarioStatusPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.NewCostingLabelEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.enums.ScenarioStateEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class GroupEditTests extends TestBaseUI {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EditComponentsPage editComponentsPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private CssComponent cssComponent = new CssComponent();

    @Test
    @TestRail(id = {14723})
    @Description("Verify user can edit multiple scenarios")
    public void testGroupEdit() {
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentC = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);

        editComponentsPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentC)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName(),
                componentC.getComponentName() + ", " + componentB.getScenarioName())
            .editScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("If you wish to retain existing private scenarios, change the scenario name, otherwise they will be overridden.");

        explorePage = editComponentsPage.overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentA.getComponentName(), componentA.getScenarioName())).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), componentB.getScenarioName())).isEqualTo(1);

        explorePage.selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName() + ", " + componentB.getScenarioName(),
                componentC.getComponentName() + ", " + componentB.getScenarioName())
            .editScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(scenarioName)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentA.getComponentName(), scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentB.getComponentName(), scenarioName)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentC.getComponentName(), scenarioName)).isEqualTo(1);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(id = {14724})
    @Description("Attempt to edit multiple scenarios, including a private scenario")
    public void testGroupEditPublicAndPrivateScenario() {
        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.selectFilter("Private")
            .multiSelectScenarios(componentB.getComponentName() + ", " + componentB.getScenarioName())
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14725})
    @Description("Attempt to edit multiple scenarios, including one which is processing")
    public void testGroupEditScenarioInProcessingState() {
//        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
//        String componentName1 = "titan charger lead";
//        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
//        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
//        currentUser = UserUtil.getUser();
//
//        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
//        String componentName2 = "Part0004";
//        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
//        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentA = new ComponentRequestUtil().getComponent();
        ComponentInfoBuilder componentB = new ComponentRequestUtil().getComponent();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentA)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName())
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios(componentA.getComponentName() + ", " + componentA.getScenarioName(), componentB.getComponentName());

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14726, 15015})
    @Description("Attempt to edit more than 10 scenarios")
    public void testEditMoreThanTenScenarios() {
        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final File resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT");
        final File resourceFile2 = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan charger lead.SLDPRT");
        final File resourceFile3 = FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT");
        final File resourceFile4 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt");
        final File resourceFile5 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt");
        final File resourceFile6 = FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp");
        final File resourceFile7 = FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp");
        final File resourceFile8 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt");
        final File resourceFile9 = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0005b.ipt");
        final File resourceFile10 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt");
        final File resourceFile11 = FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt");

        loginPage = new CidAppLoginPage(driver);
        List<ScenarioItem> componentItems = loginPage.login(currentUser)
            .uploadMultiComponentsCSS(Arrays.asList(resourceFile, resourceFile2, resourceFile3, resourceFile4, resourceFile5, resourceFile6, resourceFile7, resourceFile8, resourceFile9, resourceFile10, resourceFile11),
                scenarioName, currentUser);

        componentItems.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getComponentName(),
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage = new ExplorePage(driver);
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
                "" + "big ring" + ", " + scenarioName + "")
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class);

        componentItems.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getComponentName(),
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination()
            .selectFilter("Public")
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

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.selectFilter("Private")
            .openScenario("piston_model1", scenarioName)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios("" + "piston rod_model1" + ", " + scenarioName + "",
                "" + "Part0005b" + ", " + scenarioName + "",
                "" + "piston cover_model1" + ", " + scenarioName + "",
                "" + "Push Pin" + ", " + scenarioName + "",
                "" + "PowderMetalShaft" + ", " + scenarioName + "",
                "" + "bracket_basic" + ", " + scenarioName + "",
                "" + "Part0004" + ", " + scenarioName + "",
                "" + "small ring" + ", " + scenarioName + "",
                "" + "titan charger lead" + ", " + scenarioName + "",
                "" + "big ring" + ", " + scenarioName + "",
                "" + "piston_model1" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
