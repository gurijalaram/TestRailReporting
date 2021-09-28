package com.settings;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_UNITED_KINGDOM;
import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.cidappapi.utils.ResetSettingsUtil;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.DecimalPlaceEnum;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private MaterialProcessPage materialProcessPage;
    private CostDetailsPage costDetailsPage;

    public DecimalPlaceTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new ResetSettingsUtil().resetSettings(currentUser);
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"5287", "5288", "5291", "5297", "5290", "5295"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.SIX)
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .selectDigitalFactory(APRIORI_USA)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.getMaterialResult("Finish Mass"), closeTo(5.309458, 1));
        assertThat(evaluatePage.getMaterialResult("Utilization"), closeTo(81.163688, 1));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.400000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(26.712767, 10));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(31.595940, 10));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(31.595940, 10));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.000000, 1));

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.ONE)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.3kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.2%"));
        assertThat(evaluatePage.isProcessResultDisplayed("Total Cycle Time", "109.4s"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Material Cost", "$26.7"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Piece Part Cost", "$31.6"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Fully Burdened Cost", "$31.6"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Total Capital Investment", "$0.0"), is(true));
        materialProcessPage = evaluatePage.openMaterialProcess();

        assertThat(materialProcessPage.getTotalResult("Cycle Time"), closeTo(109.4, 1));
        assertThat(materialProcessPage.getTotalResult("Piece Part Cost"), closeTo(31.6, 10));
        assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost"), closeTo(31.6, 10));
        assertThat(materialProcessPage.getTotalResult("Total Capital Investment"), closeTo(0.0, 1));

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        assertThat(costDetailsPage.getCostSumValue("Total Variable Cost"), closeTo(18.9, 1));
        assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead"), closeTo(0.3, 1));
        assertThat(costDetailsPage.getCostContributionValue("SG&A"), closeTo(1.9, 1));
        assertThat(costDetailsPage.getCostContributionValue("Margin"), closeTo(0.0, 1));
        assertThat(costDetailsPage.getCostSumValue("Piece Part Cost"), closeTo(21.1, 1));

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.3095kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.1576%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.4000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(26.7128, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(31.5959, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(31.5959, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.0000, 1));

        materialProcessPage = evaluatePage.openMaterialProcess();

        assertThat(materialProcessPage.getTotalResult("Cycle Time"), closeTo(109.4000, 1));
        assertThat(materialProcessPage.getTotalResult("Piece Part Cost"), closeTo(31.5959, 1));
        assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost"), closeTo(31.5959, 1));
        assertThat(materialProcessPage.getTotalResult("Total Capital Investment"), closeTo(0.0000, 1));

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        assertThat(costDetailsPage.getCostSumValue("Total Variable Cost"), closeTo(28.4589, 1));
        assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead"), closeTo(0.3179, 1));
        assertThat(costDetailsPage.getCostContributionValue("SG&A"), closeTo(2.8191, 1));
        assertThat(costDetailsPage.getCostContributionValue("Margin"), closeTo(0.0000, 1));
        assertThat(costDetailsPage.getCostSumValue("Piece Part Cost"), closeTo(31.5959, 1));
        costDetailsPage.closePanel();

        evaluatePage.openSettings()
            .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.30946kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.15756%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(26.71277, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(31.59594, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(31.59594, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.00000, 1));

        evaluatePage.selectDigitalFactory(APRIORI_UNITED_KINGDOM)
            .costScenario();

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.30946kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.15756%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(26.71277, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(31.59594, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(31.59594, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.00000, 50));
    }
}