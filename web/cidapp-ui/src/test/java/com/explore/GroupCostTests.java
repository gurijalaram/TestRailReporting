package com.explore;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.pageobjects.pages.evaluate.components.inputs.ComponentBasicPage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class GroupCostTests extends TestBase {

    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private File resourceFile;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemB;

    @Test
    // TODO: 12/09/2022 qa to add testrail id
    @TestRail(testCaseId = {""})
    @Description("Test group cost")
    public void testGroupCost() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;
        final String componentName = "bracket_basic";
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName2 = "M3CapScrew";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".CATPart");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName, scenarioName, resourceFile, currentUser);

        cidComponentItemB = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        explorePage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName + ", " + scenarioName + "", "" + componentName2 + ", " + scenarioName2 + "")
            .clickCostButton(ComponentBasicPage.class)
            .selectDigitalFactory(DigitalFactoryEnum.APRIORI_CHINA)
            .enterAnnualYears("7")
            .enterAnnualVolume("6000")
            .openMaterialSelectorTable()
            .search("1050A")
            .selectMaterial("Aluminum, Cast, ANSI 1050A")
            .submit(ComponentBasicPage.class)
            .applyAndCost(EditScenarioStatusPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(cidComponentItem, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(cidComponentItemB, ScenarioStateEnum.COST_COMPLETE);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(explorePage.getRowDetails(componentName, scenarioName)).containsExactlyInAnyOrder(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6000");
        softAssertions.assertThat(explorePage.getRowDetails(componentName2, scenarioName2)).containsExactlyInAnyOrder(DigitalFactoryEnum.APRIORI_CHINA.getDigitalFactory(), "6000");
    }
}
