package test.java.explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import main.java.utils.TestRail;
import main.java.utils.Util;
import org.junit.Test;

public class PreviewPanelTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(testCaseId = {"1102", "1103"})
    public void testPreviewPanelDisplay() {

        String partName = "Casting";

        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(new Util().getScenarioName(), new FileResourceUtil().getResourceFile(partName + ".prt"))
            .selectExploreButton()
            .highlightScenario(new Util().getScenarioName(), partName);

        explorePage = new ExplorePage(driver);
        explorePage.openPreviewPanel();

        assertThat(explorePage.viewPreviewPanelData(), is(true));
    }
}
