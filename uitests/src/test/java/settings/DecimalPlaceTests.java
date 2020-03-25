package settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.utils.AfterTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.DecimalPlaceEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class DecimalPlaceTests extends TestBase {
    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    File resourceFile;

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764"})
    @Description("User can change the default Displayed Decimal Places")
    public void changeDecimalPlaceDefaultsMax() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.SIX.getDecimalPlaces());
        new SettingsPage(driver).save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isFinishMass("0.701755"), is(true));
        assertThat(evaluatePage.isUtilization("95.000000"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("92.698712"));
        assertThat(evaluatePage.getMaterialCost(), is("2.227365"));
        assertThat(evaluatePage.getPartCost(), is("6.513465"));
        assertThat(evaluatePage.getBurdenedCost("8.377654"), is(true));
        assertThat(evaluatePage.getCapitalInvestment(), is("51,265.177987"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3764"})
    @Description("User can change the default Displayed Decimal Places to the minimum")
    public void changeDecimalPlaceDefaultsMin() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.ZERO.getDecimalPlaces());
        new SettingsPage(driver).save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isFinishMass("0.701755"), is(true));
        assertThat(evaluatePage.isUtilization("95.000000"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("92.698712"));
        assertThat(evaluatePage.getMaterialCost(), is("2.227365"));
        assertThat(evaluatePage.getPartCost(), is("6.513465"));
        assertThat(evaluatePage.getBurdenedCost("8.377654"), is(true));
        assertThat(evaluatePage.getCapitalInvestment(), is("51,265.177987"));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"3730", "3738", "3792", "3764"})
    @Description("User can change the default Displayed Decimal Places multiple times and rounding adjusts")
    public void changeDecimalPlaceDefaultsUpdates() {

        resourceFile = new FileResourceUtil().getResourceFile("bracket_basic.prt");
        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        currentUser = UserUtil.getUser();

        loginPage.login(currentUser)
            .openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FIVE.getDecimalPlaces());
        new SettingsPage(driver).save(ExplorePage.class)
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.isFinishMass("0.701755"), is(true));
        assertThat(evaluatePage.isUtilization("95.000000"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("92.698712"));
        assertThat(evaluatePage.getMaterialCost(), is("2.227365"));
        assertThat(evaluatePage.getPartCost(), is("6.513465"));
        assertThat(evaluatePage.getBurdenedCost("8.377654"), is(true));
        assertThat(evaluatePage.getCapitalInvestment(), is("51,265.177987"));

        evaluatePage.openSettings()
            .changeDecimalPlaces(DecimalPlaceEnum.FOUR.getDecimalPlaces());
        new SettingsPage(driver).save(ExplorePage.class);

        assertThat(evaluatePage.isFinishMass("0.701755"), is(true));
        assertThat(evaluatePage.isUtilization("95.000000"), is(true));
        assertThat(evaluatePage.getCycleTimeCount(), is("92.698712"));
        assertThat(evaluatePage.getMaterialCost(), is("2.227365"));
        assertThat(evaluatePage.getPartCost(), is("6.513465"));
        assertThat(evaluatePage.getBurdenedCost("8.377654"), is(true));
        assertThat(evaluatePage.getCapitalInvestment(), is("51,265.177987"));
    }


}
