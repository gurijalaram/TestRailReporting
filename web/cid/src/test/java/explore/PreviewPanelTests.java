package explore;

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
import pageobjects.pages.explore.ExplorePage;
import pageobjects.pages.explore.PreviewPanelPage;
import pageobjects.pages.login.CidLoginPage;

import java.io.File;

public class PreviewPanelTests extends TestBase {

    private CidLoginPage loginPage;
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
        resourceFile = FileResourceUtil.getResourceAsFile(partName + ".prt");

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
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

        resourceFile = FileResourceUtil.getResourceAsFile("225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFileAndOk(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
            .costScenario()
            .selectExploreButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .highlightScenario(testScenarioName, "225_gasket-1-solid1");

        explorePage = new ExplorePage(driver);
        previewPanelPage = explorePage.openPreviewPanel();

        assertThat(previewPanelPage.isImageDisplayed(), is(true));
        assertThat(previewPanelPage.getPiecePartCost(), is("0.91"));
        assertThat(previewPanelPage.getFullyBurdenedCost(), is("1.64"));
        assertThat(previewPanelPage.getTotalCapitalInvestment(), is("20,057.85"));
    }
}
