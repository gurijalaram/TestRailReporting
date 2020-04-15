package compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.common.ScenarioTablePage;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.tolerances.WarningPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class AddScenarioTests extends TestBase {

    private CIDLoginPage loginPage;
    private WarningPage warningPage;
    private ComparePage comparePage;
    private ScenarioTablePage scenarioTablePage;
    private EvaluatePage evaluatePage;

    private File resourceFile;

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"3847", "412", "1171"})
    @Description("Test filtering and adding a private scenario then searching component table for the scenario")
    public void filterAddPrivateScenario() {

        resourceFile = new FileResourceUtil().getResourceFile("Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface.catpart");
        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);
        evaluatePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_SAND.getProcessGroup())
            .costScenario();

        assertThat(evaluatePage.getDFMRiskIcon(), containsString("dtc-high-risk-icon"));
        assertThat(evaluatePage.getDfmRisk(), is(true));

        scenarioTablePage = evaluatePage.createNewComparison().enterComparisonName(new Util().getComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPrivateCriteria("Part", "Part Name", "Contains", "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface")
            .apply(ScenarioTablePage.class);

        assertThat(scenarioTablePage.findScenario(testScenarioName, "Machining-DTC_Issue_SharpCorner_CurvedWall-CurvedSurface").isDisplayed(), Matchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"448"})
    @Description("Test filtering and adding a public scenario then searching component table for the scenario")
    public void filterAddPublicScenario() {

        resourceFile = new FileResourceUtil().getResourceFile("Casting.prt");
        String testScenarioName = new Util().getScenarioName();

        loginPage = new CIDLoginPage(driver);

        scenarioTablePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.ADDITIVE_MANUFACTURING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .createNewComparison()
            .enterComparisonName(new Util().getComparisonName())
            .save(ComparePage.class)
            .addScenario()
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", "Casting")
            .apply(ScenarioTablePage.class);

        assertThat(scenarioTablePage.findScenario(testScenarioName, "Casting").isDisplayed(), Matchers.is(true));
    }

    @Test
    @TestRail(testCaseId = {"462"})
    @Description("Test warning message appears when the user does not enter a scenario name for a comparison")
    public void comparisonNoScenarioName() {

        loginPage = new CIDLoginPage(driver);
        warningPage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .save(WarningPage.class);

        assertThat(warningPage.getWarningText(), is(containsString("Some of the supplied inputs are invalid.")));
    }

    @Test
    @TestRail(testCaseId = {"414"})
    @Description("Test all available characters in a comparison name")
    public void comparisonAllCharacters() {

        String testComparisonName = (new Util().getComparisonName() + "!£$%^&*()_+{}~:?><`1-=[]#';|@");

        loginPage = new CIDLoginPage(driver);
        comparePage = loginPage.login(UserUtil.getUser())
            .createNewComparison()
            .enterComparisonName(testComparisonName)
            .save(ComparePage.class);

        assertThat(comparePage.isComparisonName(testComparisonName.toUpperCase()), is(true));
    }
}
