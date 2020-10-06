package settings;

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

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pageobjects.pages.evaluate.CostDetailsPage;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.process.ProcessRoutingPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;
import testsuites.SmokeTestSuite;

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

    @Category({SmokeTestSuite.class})
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
        assertThat(evaluatePage.getMaterialCost(), closeTo(15.942042, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(19.478459, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(19.478459, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(0.000000, 1));

        evaluatePage.openSettings()
                .changeDecimalPlaces(DecimalPlaceEnum.ZERO.getDecimalPlaces())
                .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5"), is(true));
        assertThat(evaluatePage.isUtilization("81"), is(true));
        assertThat((int) evaluatePage.getCycleTimeCount(), is(109));
        assertThat((int) evaluatePage.getMaterialCost(), is(16));
        assertThat((int) evaluatePage.getPartCost(), is(19));
        assertThat((int) evaluatePage.getBurdenedCost(), is(19));
        assertThat((int) evaluatePage.getCapitalInvestment(), is(431));

        evaluatePage.openSettings()
                .changeDecimalPlaces(DecimalPlaceEnum.FOUR.getDecimalPlaces())
                .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3095"), is(true));
        assertThat(evaluatePage.isUtilization("81.1637"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(129.6079, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(16.1514, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(20.1588, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(20.1745, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(431.2010, 1));

        processRoutingPage = evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), closeTo(129.6079, 1));
        assertThat(processRoutingPage.getPiecePartCost(), closeTo(20.1588, 1));
        assertThat(processRoutingPage.getFullyBurdenedCost(), closeTo(20.1745, 1));
        assertThat(processRoutingPage.getCapitalInvestments(), closeTo(431.2010, 1));

        costDetailsPage = evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), closeTo(17.6663, 1));
        assertThat(costDetailsPage.getIndirectOverhead(), closeTo(0.3180, 1));
        assertThat(costDetailsPage.getSGandA(), closeTo(1.7500, 1));
        assertThat(costDetailsPage.getMargin(), closeTo(0.0000, 1));
        assertThat(costDetailsPage.getPiecePartCost(), closeTo(20.1588, 1));


        evaluatePage.openSettings()
                .changeDecimalPlaces(DecimalPlaceEnum.ONE.getDecimalPlaces())
                .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3"), is(true));
        assertThat(evaluatePage.isUtilization("81.2"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(129.6, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(16.2, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(20.2, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(20.2, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(431.2, 50));

        evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), closeTo(129.6, 1));
        assertThat(processRoutingPage.getPiecePartCost(), closeTo(20.1, 1));
        assertThat(processRoutingPage.getFullyBurdenedCost(), closeTo(20.1, 1));
        assertThat(processRoutingPage.getCapitalInvestments(), closeTo(431.2, 1));

        evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), closeTo(17.7, 1));
        assertThat(costDetailsPage.getIndirectOverhead(), closeTo(0.3, 1));
        assertThat(costDetailsPage.getSGandA(), closeTo(1.7, 1));
        assertThat(costDetailsPage.getMargin(), closeTo(0.0, 1));
        assertThat(costDetailsPage.getPiecePartCost(), closeTo(20.1, 1));

        evaluatePage.openSettings()
                .changeDecimalPlaces(DecimalPlaceEnum.FIVE.getDecimalPlaces())
                .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.30946"), is(true));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(129.60790, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(16.15138, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(20.15879, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(20.15879, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(431.20099, 1));

        evaluatePage.selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
                .costScenario();

        assertThat(evaluatePage.isFinishMass("5.30946"), is(true));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), closeTo(129.60790, 1));
        assertThat(evaluatePage.getMaterialCost(), closeTo(16.15138, 1));
        assertThat(evaluatePage.getPartCost(), closeTo(19.93822, 1));
        assertThat(evaluatePage.getBurdenedCost(), closeTo(19.95206, 1));
        assertThat(evaluatePage.getCapitalInvestment(), closeTo(380.73596, 50));
    }
}