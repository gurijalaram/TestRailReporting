package com.settings;

import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_UNITED_KINGDOM;
import static com.apriori.utils.enums.DigitalFactoryEnum.APRIORI_USA;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.materialprocess.MaterialProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.DecimalPlaceEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.Offset;
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
            new UserPreferencesUtil().resetSettings(currentUser);
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

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(evaluatePage.getMaterialResult("Finish Mass")).isCloseTo(Double.valueOf(5.309458), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getMaterialResult("Utilization")).isCloseTo(Double.valueOf(81.163688), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.400000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.87099), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.444918), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.000000), Offset.offset(15.0));

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.ONE)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isMaterial("Finish Mass")).isEqualTo("5.3kg");
        softAssertions.assertThat(evaluatePage.isMaterial("Utilization")).isEqualTo("81.2%");
        softAssertions.assertThat(evaluatePage.isProcessResultDisplayed("Total Cycle Time", "109.4s")).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Material Cost", "$33.9")).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Piece Part Cost", "$40.4")).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Fully Burdened Cost", "$40.4")).isTrue();
        softAssertions.assertThat(evaluatePage.isCostResultDisplayed("Total Capital Investment", "$0.0")).isTrue();
        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(materialProcessPage.getTotalResult("Cycle Time")).isCloseTo(Double.valueOf(109.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Piece Part Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Total Capital Investment")).isCloseTo(Double.valueOf(0.0), Offset.offset(15.0));

        costDetailsPage = evaluatePage.openCostDetails()
                .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(costDetailsPage.getCostSumValue("Total Variable Cost")).isCloseTo(Double.valueOf(46.2), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead")).isCloseTo(Double.valueOf(0.3), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("SG&A")).isCloseTo(Double.valueOf(4.6), Offset.offset(5.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Margin")).isCloseTo(Double.valueOf(0.0), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostSumValue("Piece Part Cost")).isCloseTo(Double.valueOf(40.4), Offset.offset(15.0));

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.FOUR)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isMaterial("Finish Mass")).isEqualTo("5.3095kg");
        softAssertions.assertThat(evaluatePage.isMaterial("Utilization")).isEqualTo("81.1637%");
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.4000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.871), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));

        materialProcessPage = evaluatePage.openMaterialProcess();

        softAssertions.assertThat(materialProcessPage.getTotalResult("Cycle Time")).isCloseTo(Double.valueOf(109.4000), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        softAssertions.assertThat(materialProcessPage.getTotalResult("Total Capital Investment")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));

        costDetailsPage = evaluatePage.openCostDetails()
                .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        softAssertions.assertThat(costDetailsPage.getCostSumValue("Total Variable Cost")).isCloseTo(Double.valueOf(46.1543), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead")).isCloseTo(Double.valueOf(0.3179), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("SG&A")).isCloseTo(Double.valueOf(4.5720), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostContributionValue("Margin")).isCloseTo(Double.valueOf(0.0000), Offset.offset(15.0));
        softAssertions.assertThat(costDetailsPage.getCostSumValue("Piece Part Cost")).isCloseTo(Double.valueOf(40.4449), Offset.offset(15.0));
        costDetailsPage.closePanel();

        evaluatePage.openSettings()
                .selectDecimalPlaces(DecimalPlaceEnum.FIVE)
                .submit(EvaluatePage.class);

        softAssertions.assertThat(evaluatePage.isMaterial("Finish Mass")).isEqualTo("5.30946kg");
        softAssertions.assertThat(evaluatePage.isMaterial("Utilization")).isEqualTo("81.16369%");
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.40000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(33.87099), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(40.44494), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(40.44494), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(0.00000), Offset.offset(15.0));

        evaluatePage.selectDigitalFactory(APRIORI_UNITED_KINGDOM)
                .costScenario();

        softAssertions.assertThat(evaluatePage.isMaterial("Finish Mass")).isEqualTo("5.30946kg");
        softAssertions.assertThat(evaluatePage.isMaterial("Utilization")).isEqualTo("81.16369%");
        softAssertions.assertThat(evaluatePage.getProcessesResult("Total Cycle Time")).isCloseTo(Double.valueOf(109.40000), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Material Cost")).isCloseTo(Double.valueOf(21.91453), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Piece Part Cost")).isCloseTo(Double.valueOf(29.01580), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Fully Burdened Cost")).isCloseTo(Double.valueOf(29.01580), Offset.offset(15.0));
        softAssertions.assertThat(evaluatePage.getCostResults("Total Capital Investment")).isCloseTo(Double.valueOf(00.00000), Offset.offset(15.0));

        softAssertions.assertAll();
    }
}