package test.java.explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.UsersEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import main.java.utils.FileResourceUtil;
import org.junit.Test;

import java.time.LocalDateTime;

public class PreviewPanelTests extends TestBase {

    private final String scenarioName = "AutoScenario" + LocalDateTime.now();

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    public void testLogin() {
        loginPage = new LoginPage(driver);
        loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .uploadFile(scenarioName, new FileResourceUtil().getResourceFile("Casting.prt"))
            .selectExploreButton()
            .highlightScenario("Preview Panel", "Casting");

        explorePage = new ExplorePage(driver);
        explorePage.openPreviewPanel();

        assertThat(explorePage.viewPreviewPanelData(), is(true));
    }
}
