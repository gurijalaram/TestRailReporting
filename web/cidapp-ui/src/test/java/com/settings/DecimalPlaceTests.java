package com.settings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.ProcessesPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DecimalPlaceEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    File resourceFile;
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private ProcessesPage processesPage;
    private CostDetailsPage costDetailsPage;

    //@After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"5287", "5288", "5291", "5297", "5290", "5295"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum,"bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .typeAheadInSection("Decimal Places", DecimalPlaceEnum.SIX.getDecimalPlaces())
            .submit(ExplorePage.class)
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .inputVpe(VPEEnum.APRIORI_USA.getVpe())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.getMaterialResult("Finish Mass"), closeTo(5.309458, 1));
        assertThat(evaluatePage.getMaterialResult("Utilization"), closeTo(81.163688, 1));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.400000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(17.342570, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(21.057265, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(21.057265, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.000000, 1));

        evaluatePage.openSettings()
            .typeAheadInSection("Decimal Places", DecimalPlaceEnum.ONE.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.3kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.2%"));
        assertThat(evaluatePage.isProcessResultDisplayed("Total Cycle Time", "109.4 sec"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Material Cost", "$17.3 "), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Piece Part Cost", "$21.1"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Fully Burdened Cost", "$21.1"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Total Capital Investment", "$0.0"), is(true));
        processesPage = evaluatePage.openProcesses();

        assertThat(processesPage.getTotalResult("Cycle Time"), closeTo(109.4, 1));
        assertThat(processesPage.getTotalResult("Piece Part Cost"), closeTo(21.1, 1));
        assertThat(processesPage.getTotalResult("Fully Burdened Cost"), closeTo(21.1, 1));
        assertThat(processesPage.getTotalResult("Total Capital Investment"), closeTo(0.0, 1));

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        assertThat(costDetailsPage.getCostSumValue("Total Variable Cost"), closeTo(18.9, 1));
        assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead"), closeTo(0.3, 1));
        assertThat(costDetailsPage.getCostContributionValue("SG&A"), closeTo(1.9, 1));
        assertThat(costDetailsPage.getCostContributionValue("Margin"), closeTo(0.0, 1));
        assertThat(costDetailsPage.getCostSumValue("Piece Part Cost"), closeTo(21.1, 1));

        evaluatePage.openSettings()
            .typeAheadInSection("Decimal Places", DecimalPlaceEnum.FOUR.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.3095kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.1575%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.4000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(15.9420, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(19.4785, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(19.4785, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.0000, 1));

        processesPage = evaluatePage.openProcesses();

        assertThat(processesPage.getTotalResult("Cycle Time"), closeTo(109.4000, 1));
        assertThat(processesPage.getTotalResult("Piece Part Cost"), closeTo(19.4785, 1));
        assertThat(processesPage.getTotalResult("Fully Burdened Cost"), closeTo(19.4785, 1));
        assertThat(processesPage.getTotalResult("Total Capital Investment"), closeTo(0.0000, 1));

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        assertThat(costDetailsPage.getCostSumValue("Total Variable Cost"), closeTo(17.4366, 1));
        assertThat(costDetailsPage.getCostContributionValue("Indirect Overhead"), closeTo(0.3146, 1));
        assertThat(costDetailsPage.getCostContributionValue("SG&A"), closeTo(1.7272, 1));
        assertThat(costDetailsPage.getCostContributionValue("Margin"), closeTo(0.0000, 1));
        assertThat(costDetailsPage.getCostSumValue("Piece Part Cost"), closeTo(19.4785, 1));
        costDetailsPage.closePanel();

        evaluatePage.openSettings()
            .typeAheadInSection("Decimal Places", DecimalPlaceEnum.FIVE.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.30946kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.15752%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(17.34257, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(21.05727, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(21.05727, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.00000, 1));

        evaluatePage.inputVpe(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterial("Finish Mass"), equalTo("5.30946kg"));
        assertThat(evaluatePage.isMaterial("Utilization"), equalTo("81.16369%"));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResults("Material Cost"), closeTo(15.94204, 1));
        assertThat(evaluatePage.getCostResults("Piece Part Cost"), closeTo(19.36951, 1));
        assertThat(evaluatePage.getCostResults("Fully Burdened Cost"), closeTo(19.36951, 1));
        assertThat(evaluatePage.getCostResults("Total Capital Investment"), closeTo(0.00000, 50));
    }
}