package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.header.GenericHeader;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.PublishPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.jobqueue.JobQueuePage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.Util;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.VPEEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;

public class PublishExistingCostedTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private EvaluatePage evaluatePage;
    private GenericHeader genericHeader;
    private JobQueuePage jobQueuePage;

    public PublishExistingCostedTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"389", "1091"})
    @Issue("AP-56845")
    @Description("Publish an existing scenario from the Public Workspace back to the Public Workspace")
    public void testPublishExistingCostedScenario() {

        String testScenarioName = new Util().getScenarioName();
        String partName = "testpart-4";

        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile(partName + ".prt"))
            .selectProcessGroup(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openScenario(testScenarioName, partName)
            .editScenario(EvaluatePage.class)
            .selectVPE(VPEEnum.APRIORI_CHINA.getVpe())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filterCriteria()
            .filterPublicCriteria("Part", "Part Name", "Contains", partName)
            .apply(ExplorePage.class);

        assertThat(explorePage.findScenario(testScenarioName, partName).isDisplayed(), is(true));
    }

    @Test
    @Issue("AP-57450")
    @TestRail(testCaseId = {"390", "569"})
    @Description("Edit & publish Scenario A from the public workspace as Scenario B")
    public void testPublishLockedScenario() {

        String testScenarioName = new Util().getScenarioName();

        loginPage = new LoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, new FileResourceUtil().getResourceFile("PowderMetalShaft.stp"))
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .highlightScenario(testScenarioName, "PowderMetalShaft");

        genericHeader = new GenericHeader(driver);
        genericHeader.toggleLock()
            .openJobQueue()
            .checkJobQueueActionStatus("PowderMetalShaft", testScenarioName, "Update", "okay")
            .closeJobQueue(ExplorePage.class);

        explorePage = new ExplorePage(driver);
        jobQueuePage = explorePage.openScenario(testScenarioName, "PowderMetalShaft")
            .editScenario(EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.POWDER_METAL.getProcessGroup())
            .selectVPE(VPEEnum.APRIORI_USA.getVpe())
            .costScenario()
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .openJobQueue();

        assertThat(jobQueuePage.getJobQueueRow("stop"), containsString("is locked and cannot be overridden"));
    }
}