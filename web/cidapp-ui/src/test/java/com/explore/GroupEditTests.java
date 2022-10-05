package com.explore;

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
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

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
            .enterScenarioName("Renamed Scenario")
            .clickContinue(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .selectFilter("Private");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName1, "Renamed Scenario")).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, "Renamed Scenario")).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName3, "Renamed Scenario")).isEqualTo(1);

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
            .delete()
            .submit(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isEditButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
