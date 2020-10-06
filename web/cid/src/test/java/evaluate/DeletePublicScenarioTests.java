package evaluate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import pageobjects.pages.evaluate.EvaluatePage;
import pageobjects.pages.evaluate.PublishPage;
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.login.CidLoginPage;

import java.io.File;

public class DeletePublicScenarioTests extends TestBase {

    private final String noComponentMessage = "You have no components that match the selected filter";
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private String testScenarioName;
    private File resourceFile;

    public DeletePublicScenarioTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"587"})
    @Description("Test a public scenario can be deleted from the component table")
    public void testDeletePublicScenario() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.WITHOUT_PG;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "Casting.prt");
        testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser());

        explorePage = new ExplorePage(driver);
        explorePage.uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .selectPublishButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .filter()
            .setWorkspace("Public")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class)
            .highlightScenario(testScenarioName, "casting");

        explorePage = new ExplorePage(driver);
        explorePage.delete()
            .deleteScenario()
            .filter()
            .setWorkspace("Private")
            .setScenarioType("Part")
            .setRowOne("Scenario Name", "Contains", testScenarioName)
            .apply(ExplorePage.class);

        assertThat(explorePage.getNoComponentText(), is(containsString(noComponentMessage)));
    }
}