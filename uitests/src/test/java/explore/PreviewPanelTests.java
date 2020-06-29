package explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.PreviewPanelPage;
import com.apriori.pageobjects.pages.login.CIDLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class PreviewPanelTests extends TestBase {

    private CIDLoginPage loginPage;
    private ExplorePage explorePage;
    private PreviewPanelPage previewPanelPage;

    private File resourceFile;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(testCaseId = {"1102", "1103"})
    public void testPreviewPanelDisplay() {

        String partName = "Casting";
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = new FileResourceUtil().getResourceFile(partName + ".prt");

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .highlightScenario(testScenarioName, partName);

        explorePage = new ExplorePage(driver);
        explorePage.openPreviewPanel();

        assertThat(explorePage.viewPreviewPanelData(), is(true));
    }

    @Test
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(testCaseId = {"1104", "1105"})
    public void previewPanelMetrics() {

        resourceFile = new FileResourceUtil().getResourceFile("225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CIDLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .highlightScenario(testScenarioName, "225_gasket-1-solid1");

        explorePage = new ExplorePage(driver);
        previewPanelPage = explorePage.openPreviewPanel();

        assertThat(previewPanelPage.isImageDisplayed(), is(true));
        assertThat(previewPanelPage.getPiecePartCost(), is("1.07"));
        assertThat(previewPanelPage.getFullyBurdenedCost(), is("1.79"));
        assertThat(previewPanelPage.getTotalCapitalInvestment(), is("19,665.07"));
    }
}
