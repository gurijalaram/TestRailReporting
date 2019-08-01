package test.java.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.base.TestBase;
import main.java.enums.ColumnsEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class DefaultTableHeadersTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public DefaultTableHeadersTests() {
        super();
    }

    @Test
    @Description("Test default list of column headers in the private workspace")
    @Severity(SeverityLevel.MINOR)
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.STATUS.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @Description("Test default list of column headers in the public workspace")
    @Severity(SeverityLevel.MINOR)
    public void testPublicDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.STATUS.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }
}
