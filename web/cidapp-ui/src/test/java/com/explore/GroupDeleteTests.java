package com.explore;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.navtoolbars.DeletePage;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class GroupDeleteTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"15013", "15011"})
    @Description("Verify user can delete 2 or more parts")
    @Category(SmokeTests.class)
    public void testGroupDelete() {
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

        explorePage = new ExplorePage(driver).selectFilter("Recent")
            .refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage = explorePage.multiSelectScenarios("" + componentName2 + ", " + scenarioName2 + "");

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .checkComponentDelete(cidComponentItemB)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName, scenarioName)).isEqualTo(0);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName2)).isEqualTo(0);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15021", "15022"})
    @Description("Verify Delete icon disabled if a selected part is in Processing/Costing state")
    public void testGroupDeleteProcessing() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        explorePage = new ExplorePage(driver).selectFilter("Recent")
            .refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class);

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"15017", "15016", "15020"})
    @Description("Verify user can delete 2 or more assemblies. Verify correct behavior of Delete button when multi-selecting.")
    @Category(SmokeTests.class)
    public void testGroupDeleteAssemblies() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;
        final String assemblyName = "titan cordless drill";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, assemblyName + ".SLDASM");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.ASSEMBLY;
        final String assemblyName2 = "titan charger ass";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, assemblyName2 + ".SLDASM");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        final ProcessGroupEnum processGroupEnum3 = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName = "2062987";
        final File resourceFile3 = FileResourceUtil.getCloudFile(processGroupEnum3, componentName + ".prt");
        final String scenarioName3 = new GenerateStringUtil().generateScenarioName();

        currentUser = UserUtil.getUser();
        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(assemblyName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).uploadComponent(assemblyName2, scenarioName2, resourceFile2, currentUser);

        cidComponentItemC = new ExplorePage(driver).uploadComponent(componentName, scenarioName3, resourceFile3, currentUser);

        explorePage = new ExplorePage(driver).selectFilter("Recent")
            .refresh()
            .multiSelectScenarios("" + assemblyName + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + assemblyName2 + ", " + scenarioName2 + "");
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName3 + "");
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(false);

        explorePage.multiSelectScenarios("" + componentName + ", " + scenarioName3 + "");
        softAssertions.assertThat(explorePage.isDeleteButtonEnabled()).isEqualTo(true);

        explorePage.clickDeleteIcon()
            .clickDelete(DeletePage.class)
            .clickClose(ExplorePage.class)
            .checkComponentDelete(cidComponentItem)
            .checkComponentDelete(cidComponentItemB)
            .refresh();

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName, scenarioName)).isEqualTo(0);
        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName2, scenarioName2)).isEqualTo(0);

        softAssertions.assertAll();
    }
}
