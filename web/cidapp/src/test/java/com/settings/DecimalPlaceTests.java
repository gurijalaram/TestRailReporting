package com.settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.pageobjects.navtoolbars.ExploreToolbar;
import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.ProcessesPage;
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
import testsuites.SmokeTestSuite;

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

    @Category({SmokeTestSuite.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764", "3792", "3765", "3774"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {

        resourceFile = FileResourceUtil.getResourceAsFile("bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .setDropdown("Decimal Places", DecimalPlaceEnum.SIX.getDecimalPlaces())
            .submit(ExploreToolbar.class)
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getMaterialResult("Finish Mass"), closeTo(0.16, 1));
        assertThat(evaluatePage.getMaterialResult("Utilization"), closeTo(96.87, 1));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(36.71, 1));
        assertThat(evaluatePage.getCostResult("Material Cost"), closeTo(0.76, 1));
        assertThat(evaluatePage.getCostResult("Piece Part Cost"), closeTo(1.64, 1));
        assertThat(evaluatePage.getCostResult("Fully Burdened Cost"), closeTo(2.25, 1));
        assertThat(evaluatePage.getCostResult("Total Capital Investment"), closeTo(14141.50, 1));

        evaluatePage.openSettings()
            .setDropdown("Decimal Places", DecimalPlaceEnum.ONE.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterialDisplayed("Finish Mass", "0.16kg"), is(true));
        assertThat(evaluatePage.isMaterialDisplayed("Utilization", "96.87%"), is(true));
        assertThat(evaluatePage.isProcessResultDisplayed("Total Cycle Time", "109sec"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Material Cost", "$17"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Piece Part Cost", "$21"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Fully Burdened Cost", "$21"), is(true));
        assertThat(evaluatePage.isCostResultDisplayed("Total Capital Investment", "$0"), is(true));

        evaluatePage.openSettings()
            .setDropdown("Decimal Places", DecimalPlaceEnum.FOUR.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterialDisplayed("Finish Mass", "5.3095"), is(true));
        assertThat(evaluatePage.isMaterialDisplayed("Utilization", "81.1637"), is(true));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.4000, 1));
        assertThat(evaluatePage.getCostResult("Material Cost"), closeTo(17.3420, 1));
        assertThat(evaluatePage.getCostResult("Piece Part Cost"), closeTo(21.0566, 1));
        assertThat(evaluatePage.getCostResult("Fully Burdened Cost"), closeTo(21.0566, 1));
        assertThat(evaluatePage.getCostResult("Total Capital Investment"), closeTo(0.0000, 1));

        processesPage = evaluatePage.openProcesses();

        assertThat(processesPage.getTotalResult("Cycle Time"), closeTo(109.4000, 1));
        assertThat(processesPage.getTotalResult("Piece Part Cost"), closeTo(21.0566, 1));
        assertThat(processesPage.getTotalResult("Fully Burdened Cost"), closeTo(21.0566, 1));
        assertThat(processesPage.getTotalResult("Total Capital Investment"), closeTo(0.0000, 1));

        costDetailsPage = evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");

        assertThat(costDetailsPage.getChevronDropdownValue("Total Variable Cost"), closeTo(18.8710, 1));
        assertThat(costDetailsPage.getCostContribution("Indirect Overhead"), closeTo(0.3163, 1));
        assertThat(costDetailsPage.getCostContribution("SG&A"), closeTo(1.8693, 1));
        assertThat(costDetailsPage.getCostContribution("Margin"), closeTo(0.0000, 1));
        assertThat(costDetailsPage.getCostContribution("Piece Part Cost"), closeTo(21.0566, 1));


        evaluatePage.openSettings()
            .setDropdown("Decimal Places", DecimalPlaceEnum.ONE.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterialDisplayed("Finish Mass", "5.3"), is(true));
        assertThat(evaluatePage.isMaterialDisplayed("Utilization", "81.2"), is(true));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.4, 1));
        assertThat(evaluatePage.getCostResult("Material Cost"), closeTo(17.3, 1));
        assertThat(evaluatePage.getCostResult("Piece Part Cost"), closeTo(21.1, 1));
        assertThat(evaluatePage.getCostResult("Fully Burdened Cost"), closeTo(21.1, 1));
        assertThat(evaluatePage.getCostResult("Total Capital Investment"), closeTo(0.0, 50));

        evaluatePage.openProcesses();

        assertThat(processesPage.getTotalResult("Cycle Time"), closeTo(109.4, 1));
        assertThat(processesPage.getTotalResult("Piece Part Cost"), closeTo(21.1, 1));
        assertThat(processesPage.getTotalResult("Fully Burdened Cost"), closeTo(21.1, 1));
        assertThat(processesPage.getTotalResult("Total Capital Investment"), closeTo(0.0, 1));

        evaluatePage.openCostDetails()
            .expandDropDown("Piece Part Cost, Fully Burdened Cost");
        ;

        assertThat(costDetailsPage.getCostContribution("Total Variable Cost"), closeTo(18.9, 1));
        assertThat(costDetailsPage.getCostContribution("Indirect Overhead"), closeTo(0.3, 1));
        assertThat(costDetailsPage.getCostContribution("SG&A"), closeTo(1.9, 1));
        assertThat(costDetailsPage.getCostContribution("Margin"), closeTo(0.0, 1));
        assertThat(costDetailsPage.getCostContribution("Piece Part Cost"), closeTo(21.1, 1));

        evaluatePage.openSettings()
            .setDropdown("Decimal Places", DecimalPlaceEnum.FIVE.getDecimalPlaces())
            .submit(EvaluatePage.class);

        assertThat(evaluatePage.isMaterialDisplayed("Finish Mass", "5.30946"), is(true));
        assertThat(evaluatePage.isMaterialDisplayed("Utilization", "81.16369"), is(true));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResult("Material Cost"), closeTo(17.34196, 1));
        assertThat(evaluatePage.getCostResult("Piece Part Cost"), closeTo(21.05658, 1));
        assertThat(evaluatePage.getCostResult("Fully Burdened Cost"), closeTo(21.05658, 1));
        assertThat(evaluatePage.getCostResult("Total Capital Investment"), closeTo(0.00000, 1));

        evaluatePage.selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario();

        assertThat(evaluatePage.isMaterialDisplayed("Finish Mass", "5.30946"), is(true));
        assertThat(evaluatePage.isMaterialDisplayed("Utilization", "81.16369"), is(true));
        assertThat(evaluatePage.getProcessesResult("Total Cycle Time"), closeTo(109.40000, 1));
        assertThat(evaluatePage.getCostResult("Material Cost"), closeTo(17.34196, 1));
        assertThat(evaluatePage.getCostResult("Piece Part Cost"), closeTo(20.97339, 1));
        assertThat(evaluatePage.getCostResult("Fully Burdened Cost"), closeTo(20.97339, 1));
        assertThat(evaluatePage.getCostResult("Total Capital Investment"), closeTo(0.00000, 50));
    }
}