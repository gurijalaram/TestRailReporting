package settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.CostDetailsPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.AfterTestUtil;
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
import testsuites.SmokeTestSuite;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    File resourceFile;
    private CIDLoginPage loginPage;
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

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.SIX.getDecimalPlaces())
            .save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5.309458));
        assertThat(evaluatePage.getUtilization(), is(81.163688));
        assertThat(evaluatePage.getCycleTimeCount(), is(129.607902));
        assertThat(evaluatePage.getMaterialCost(), is(16.151375));
        assertThat(evaluatePage.getPartCost(), is(20.158792));
        assertThat(evaluatePage.getBurdenedCost(), is(20.174472));
        assertThat(evaluatePage.getCapitalInvestment(), is(431.200988));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ZERO.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5"), is(true));
        assertThat(evaluatePage.getUtilization(), is(81));
        assertThat(evaluatePage.getCycleTimeCount(), is(130));
        assertThat(evaluatePage.getMaterialCost(), is(16));
        assertThat(evaluatePage.getPartCost(), is(20));
        assertThat(evaluatePage.getBurdenedCost(), is(20));
        assertThat(evaluatePage.getCapitalInvestment(), is(431));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FOUR.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3095"), is(true));
        assertThat(evaluatePage.getUtilization(), is(81.1637));
        assertThat(evaluatePage.getCycleTimeCount(), is(129.6079));
        assertThat(evaluatePage.getMaterialCost(), is(16.1514));
        assertThat(evaluatePage.getPartCost(), is(20.1588));
        assertThat(evaluatePage.getBurdenedCost(), is(20.1745));
        assertThat(evaluatePage.getCapitalInvestment(), is(431.2010));

        processRoutingPage = evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), is(129.6079));
        assertThat(processRoutingPage.getPiecePartCost(), is(20.1588));
        assertThat(processRoutingPage.getFullyBurdenedCost(), is(20.1745));
        assertThat(processRoutingPage.getCapitalInvestments(), is(431.2010));

        costDetailsPage = evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), is(17.6663));
        assertThat(costDetailsPage.getIndirectOverhead(), is(0.3180));
        assertThat(costDetailsPage.getSGandA(), is(1.7500));
        assertThat(costDetailsPage.getMargin(), is(0.0000));
        assertThat(costDetailsPage.getPiecePartCost(), is(20.1588));


        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ONE.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.isFinishMass("5.3"), is(true));
        assertThat(evaluatePage.getUtilization(), is(81.2));
        assertThat(evaluatePage.getCycleTimeCount(), is(129.6));
        assertThat(evaluatePage.getMaterialCost(), is(16.2));
        assertThat(evaluatePage.getPartCost(), is(20.2));
        assertThat(evaluatePage.getBurdenedCost(), is(20.2));
        assertThat(evaluatePage.getCapitalInvestment(), is(431.2));

        evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), is(129.6));
        assertThat(processRoutingPage.getPiecePartCost(), is(20.1));
        assertThat(processRoutingPage.getFullyBurdenedCost(), is(20.1));
        assertThat(processRoutingPage.getCapitalInvestments(), is(431.2));

        evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), is(17.7));
        assertThat(costDetailsPage.getIndirectOverhead(), is(0.3));
        assertThat(costDetailsPage.getSGandA(), is(1.7));
        assertThat(costDetailsPage.getMargin(), is(0.0));
        assertThat(costDetailsPage.getPiecePartCost(), is(20.1));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FIVE.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.getFinishMass(), is(5.30946));
        assertThat(evaluatePage.getUtilization(), is(81.16369));
        assertThat(evaluatePage.getCycleTimeCount(), is(129.60790));
        assertThat(evaluatePage.getMaterialCost(), is(16.15138));
        assertThat(evaluatePage.getPartCost(), is(20.15879));
        assertThat(evaluatePage.getBurdenedCost(), is(20.15879));
        assertThat(evaluatePage.getCapitalInvestment(), is(431.20099));

        evaluatePage.selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5.30946));
        assertThat(evaluatePage.getUtilization(), is(81.16369));
        assertThat(evaluatePage.getCycleTimeCount(), is(129.60790));
        assertThat(evaluatePage.getMaterialCost(), is(16.15138));
        assertThat(evaluatePage.getPartCost(), is(19.55601));
        assertThat(evaluatePage.getBurdenedCost(), is(19.55601));
        assertThat(evaluatePage.getCapitalInvestment(), is(431.20099));
    }
}