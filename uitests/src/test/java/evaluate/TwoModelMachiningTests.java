package evaluate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.compare.ComparisonTablePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessRoutingPage;
import com.apriori.pageobjects.pages.evaluate.process.ProcessSetupOptionsPage;
import com.apriori.pageobjects.pages.evaluate.process.secondaryprocess.SecondaryProcessPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.pageobjects.pages.settings.SettingsPage;
import com.apriori.pageobjects.pages.settings.ToleranceSettingsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class TwoModelMachiningTests extends TestBase {

    private CIDLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ToleranceSettingsPage toleranceSettingsPage;
    private SettingsPage settingsPage;
    private SecondaryProcessPage secondaryProcessPage;
    private ProcessRoutingPage processRoutingPage;
    private ProcessSetupOptionsPage processSetupOptionsPage;
    private UserCredentials currentUser;
    private ExplorePage explorePage;

    private File resourceFile;
    private File twoModelFile;

    public TwoModelMachiningTests() {
        super();
    }

    @Test
    @Description("")
    @TestRail(testCaseId = {""})
    public void testTwoModelMachining() {

        String testScenarioName = new Util().getScenarioName();

        resourceFile = new FileResourceUtil().getResourceFile("casting_BEFORE_machining.stp");
        twoModelFile = new FileResourceUtil().getResourceFile("casting_AFTER_machining.stp");
        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        new ExplorePage(driver).uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .refreshCurrentPage()
            .uploadFile(new Util().getScenarioName(), twoModelFile)
            .selectProcessGroup(ProcessGroupEnum.TWO_MODEL_MACHINING.getProcessGroup())
            .selectSourcePart();

        new ComparisonTablePage(driver).selectScenario(testScenarioName, "casting_BEFORE_machining")
            .apply(EvaluatePage.class)
            .costScenario();

        assertThat(evaluatePage.isSourceMaterial("Aluminum, Cast, ANSI AL380.0"), is(true));
        assertThat(evaluatePage.isSourcePartName("CASTING_BEFORE_MACHINING"), is(true));
        assertThat(evaluatePage.isSourceScenarioName(testScenarioName, is(true));
    }

    @Test
    @Description("")
    @TestRail(testCaseId = {""})
    public void testOpenSourceModel() {

        String testScenarioName = "2 model 2";
        String testPartName = "DIE CASTING LOWER CONTROL ARM (AS MACHINED)";
        String sourceScenarioName = "2 model 1";

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", testPartName)
            .apply(ExplorePage.class)
            .openScenario(testScenarioName, testPartName)
            .openSourceScenario();

        assertThat(evaluatePage.getCurrentScenarioName(sourceScenarioName), is(true));
    }

    @Test
    @Description("")
    @TestRail(testCaseId = {""})
    public void testSelectSourceModel() {

        String testScenarioName = "2 model 2";
        String testPartName = "DIE CASTING LOWER CONTROL ARM (AS MACHINED)";
        String sourceScenarioName = "2 model 1";
        String newSourceScenarioName = "second scenario";
        String sourcePartName = "DIE CASTING LOWER CONTROL ARM (AS CAST)";

        loginPage = new CIDLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        evaluatePage = explorePage.filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", testPartName)
            .apply(ExplorePage.class)
            .openScenario(testScenarioName, testPartName)
            .editScenario(EvaluatePage.class)
            .selectSourcePart()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(newSourceScenarioName, sourcePartName)
            .apply(EvaluatePage.class);

        assertThat(evaluatePage.isSourceScenarioName(newSourceScenarioName), is(true));
    }
}
