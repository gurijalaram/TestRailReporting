package com.settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DecimalPlaceEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.evaluate.CostDetailsPage;
import com.pageobjects.pages.evaluate.EvaluatePage;
import com.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    File resourceFile;
    private CidLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private ProcessRoutingPage processRoutingPage;
    private CostDetailsPage costDetailsPage;

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764", "3792", "3765", "3774"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaults() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.SIX.getDecimalPlaces())
            .save(ExplorePage.class)
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), closeTo(5.309458, 1));
        assertThat(evaluatePage.getUtilization(), closeTo(81.163688, 1));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(109.400000, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(17.341958, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(21.056577, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(21.056577, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.000000, 1));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ZERO.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5"), is(true));
        assertThat(evaluatePage.isUtilization("81"), is(true));
        assertThat((int) evaluatePage.getCycleTimeCount(), is(109));
        assertThat((int) evaluatePage.getMaterialCost(), is(17));
        assertThat((int) evaluatePage.getPartCost(), is(21));
        assertThat((int) evaluatePage.getBurdenedCost(), is(21));
        assertThat((int) evaluatePage.getCapitalInvestment(), is(0));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FOUR.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3095"), is(true));
        assertThat(evaluatePage.isUtilization("81.1637"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(109.4000, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(17.3420, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(21.0566, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(21.0566, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.0000, 1));

        processRoutingPage = evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), closeTo(109.4000, 1));
        assertThat(processRoutingPage.getPiecePartCost(), closeTo(21.0566, 1));
        assertThat(processRoutingPage.getFullyBurdenedCost(), closeTo(21.0566, 1));
        assertThat(processRoutingPage.getCapitalInvestments(), closeTo(0.0000, 1));

        costDetailsPage = evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), closeTo(18.8710, 1));
        assertThat(costDetailsPage.getIndirectOverhead(), closeTo(0.3163, 1));
        assertThat(costDetailsPage.getSGandA(), closeTo(1.8693, 1));
        assertThat(costDetailsPage.getMargin(), closeTo(0.0000, 1));
        assertThat(costDetailsPage.getPiecePartCost(), closeTo(21.0566, 1));


        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ONE.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3"), is(true));
        assertThat(evaluatePage.isUtilization("81.2"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(109.4, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(17.3, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(21.1, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(21.1, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.0, 50));

        evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), closeTo(109.4, 1));
        assertThat(processRoutingPage.getPiecePartCost(), closeTo(21.1, 1));
        assertThat(processRoutingPage.getFullyBurdenedCost(), closeTo(21.1, 1));
        assertThat(processRoutingPage.getCapitalInvestments(), closeTo(0.0, 1));

        evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), closeTo(18.9, 1));
        assertThat(costDetailsPage.getIndirectOverhead(), closeTo(0.3, 1));
        assertThat(costDetailsPage.getSGandA(), closeTo(1.9, 1));
        assertThat(costDetailsPage.getMargin(), closeTo(0.0, 1));
        assertThat(costDetailsPage.getPiecePartCost(), closeTo(21.1, 1));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FIVE.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.30946"), is(true));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(109.40000, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(17.34196, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(21.05658, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(21.05658, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.00000, 1));

        evaluatePage.selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario();

        assertThat(evaluatePage.isFinishMass("5.30946"), is(true));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(109.40000, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(17.34196, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(20.97339, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(20.97339, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.00000, 50));
    }
}