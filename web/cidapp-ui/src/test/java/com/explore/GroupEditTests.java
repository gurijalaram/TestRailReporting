package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.EditComponentsPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
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
import java.util.List;

public class GroupEditTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private EditComponentsPage editComponentsPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"14723"})
    @Description("Verify user can edit multiple scenarios")
    public void testGroupEdit() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();


        String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final ProcessGroupEnum processGroupEnum3 = ProcessGroupEnum.CASTING_SAND;
        String componentName3 = "SandCast";
        final File resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum3, componentName3 + ".x_t");
        final String scenarioName3 = new GenerateStringUtil().generateScenarioName();
        final String scenarioName4 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        editComponentsPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName1, scenarioName1, resourceFile1, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentName3, scenarioName3, resourceFile3, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "", "" + componentName3 + ", " + scenarioName3 + "")
            .editScenario(EditComponentsPage.class);

        softAssertions.assertThat(editComponentsPage.getConflictForm()).contains("If you wish to retain existing private scenarios, change the scenario name, otherwise they will be overridden.");

        explorePage = editComponentsPage.overrideScenarios()
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName1, scenarioName1)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName2)).isEqualTo(1);

        explorePage.selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "", "" + componentName3 + ", " + scenarioName3 + "")
            .editScenario(EditComponentsPage.class)
            .renameScenarios()
            .enterScenarioName(scenarioName4)
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName1, scenarioName4)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName4)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName3, scenarioName4)).isEqualTo(1);

        softAssertions.assertAll();

    }

    @Test
    @TestRail(testCaseId = {"14724"})
    @Description("Attempt to edit multiple scenarios, including a private scenario")
    public void testGroupEditPublicAndPrivateScenario() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName1, scenarioName1, resourceFile1, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.selectFilter("Private")
            .multiSelectScenarios("" + componentName2 + ", " + scenarioName2 + "")
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14725"})
    @Description("Attempt to edit multiple scenarios, including one which is processing")
    public void testGroupEditScenarioInProcessingState() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        explorePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName1, scenarioName1, resourceFile1, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .uploadComponentAndOpen(componentName2, scenarioName2, resourceFile2, currentUser)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class)
            .waitForCostLabelNotContain(NewCostingLabelEnum.PROCESSING_PUBLISH_ACTION, 2)
            .clickExplore()
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "")
            .clickDelete()
            .clickDelete(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    // TODO: the test can be optimized by uploading and publishing multiple parts via API, but the current methods need to be reworked
    @TestRail(testCaseId = {"14726"})
    @Description("Attempt to edit more than 10 scenarios")
    public void testEditMoreThanTenScenarios() {
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
            assertThat(explorePage.getScenarioState(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), currentUser, ScenarioStateEnum.NOT_COSTED), is(ScenarioStateEnum.NOT_COSTED.getState())));

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

        multiComponents.forEach(component ->
            assertThat(explorePage.getScenarioState(component.getResourceFile().getName().split("\\.")[0],
                component.getScenarioName(), currentUser, ScenarioStateEnum.NOT_COSTED), is(ScenarioStateEnum.NOT_COSTED.getState())));

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

        softAssertions.assertAll();
    }
}
