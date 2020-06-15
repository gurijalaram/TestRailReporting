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
import testsuites.CIDTestSuite;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;
    private ProcessRoutingPage processRoutingPage;
    private CostDetailsPage costDetailsPage;

    File resourceFile;

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({CIDTestSuite.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaultsMax() {

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
        assertThat(evaluatePage.isUtilization("81.163688"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("110.820000"));
        assertThat(evaluatePage.getMaterialCost(), is("16.151375"));
        assertThat(evaluatePage.getPartCost(), is("19.734327"));
        assertThat(evaluatePage.getBurdenedCost(), is(19.734327));
        assertThat(evaluatePage.getCapitalInvestment(), is("0.000000"));
    }

    @Category({CIDTestSuite.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764"})
    @Description("User can change the default Displayed Decimal Places to the minimum")
    public void changeDecimalPlaceDefaultsMin() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ZERO.getDecimalPlaces())
            .save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5));
        assertThat(evaluatePage.isUtilization("81"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("111"));
        assertThat(evaluatePage.getMaterialCost(), is("16"));
        assertThat(evaluatePage.getPartCost(), is("20"));
        assertThat(evaluatePage.getBurdenedCost(), is(20));
        assertThat(evaluatePage.getCapitalInvestment(), is("0"));
    }

    @Category(SmokeTests.class)
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3792", "3764", "3765"})
    @Description("User can change the default Displayed Decimal Places multiple times and rounding adjusts")
    public void changeDecimalPlaceDefaultsUpdates() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FOUR.getDecimalPlaces())
            .save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5.3095));
        assertThat(evaluatePage.isUtilization("81.1637"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("110.8200"));
        assertThat(evaluatePage.getMaterialCost(), is("16.1514"));
        assertThat(evaluatePage.getPartCost(), is("19.7343"));
        assertThat(evaluatePage.getBurdenedCost(), is(19.7343));
        assertThat(evaluatePage.getCapitalInvestment(), is("0.0000"));

        processRoutingPage = evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), is("110.8200"));
        assertThat(processRoutingPage.getPiecePartCost(), is("19.7343"));
        assertThat(processRoutingPage.getFullyBurdenedCost(), is("19.7343"));
        assertThat(processRoutingPage.getCapitalInvestments(), is("0.0000"));

        costDetailsPage = evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), is("17.6663"));
        assertThat(costDetailsPage.getIndirectOverhead(), is("0.3180"));
        assertThat(costDetailsPage.getSGandA(), is("1.7500"));
        assertThat(costDetailsPage.getMargin(), is("0.0000"));
        assertThat(costDetailsPage.getPiecePartCost(), is("19.7343"));


        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ONE.getDecimalPlaces())
            .save(EvaluatePage.class);

        assertThat(evaluatePage.getFinishMass(), is(5.3));
        assertThat(evaluatePage.isUtilization("81.2"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("110.8"));
        assertThat(evaluatePage.getMaterialCost(), is("16.2"));
        assertThat(evaluatePage.getPartCost(), is("19.7"));
        assertThat(evaluatePage.getBurdenedCost(), is(19.7));
        assertThat(evaluatePage.getCapitalInvestment(), is("0.0"));

        evaluatePage.openProcessDetails();

        assertThat(processRoutingPage.getCycleTime(), is("110.8"));
        assertThat(processRoutingPage.getPiecePartCost(), is("19.7"));
        assertThat(processRoutingPage.getFullyBurdenedCost(), is("19.7"));
        assertThat(processRoutingPage.getCapitalInvestments(), is("0.0"));

        evaluatePage.openCostDetails();

        assertThat(costDetailsPage.getTotalVariableCosts(), is("17.7"));
        assertThat(costDetailsPage.getIndirectOverhead(), is("0.3"));
        assertThat(costDetailsPage.getSGandA(), is("1.7"));
        assertThat(costDetailsPage.getMargin(), is("0.0"));
        assertThat(costDetailsPage.getPiecePartCost(), is("19.7"));
    }

    @Category({CIDTestSuite.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3792", "3774"})
    @Description("User can change the default Displayed Decimal Places multiple times and rounding adjusts")
    public void changeDecimalPlaceDefaultsRecost() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FIVE.getDecimalPlaces())
            .save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5.30946));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("110.82000"));
        assertThat(evaluatePage.getMaterialCost(), is("16.15138"));
        assertThat(evaluatePage.getPartCost(), is("19.73433"));
        assertThat(evaluatePage.getBurdenedCost(), is(19.73433));
        assertThat(evaluatePage.getCapitalInvestment(), is("0.00000"));

        evaluatePage.selectVPE(VPEEnum.APRIORI_UNITED_KINGDOM.getVpe())
            .costScenario();

        assertThat(evaluatePage.getFinishMass(), is(5.30946));
        assertThat(evaluatePage.isUtilization("81.16369"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("110.82000"));
        assertThat(evaluatePage.getMaterialCost(), is("16.15138"));
        assertThat(evaluatePage.getPartCost(), is("19.55601"));
        assertThat(evaluatePage.getBurdenedCost(), is(19.55601));
        assertThat(evaluatePage.getCapitalInvestment(), is("0.00000"));
    }
}