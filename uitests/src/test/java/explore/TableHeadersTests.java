package test.java.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import io.qameta.allure.Description;
import main.java.base.TestBase;
import main.java.enums.ColumnsEnum;
import main.java.enums.UsersEnum;
import main.java.enums.WorkspaceEnum;
import main.java.pages.explore.ExplorePage;
import main.java.pages.login.LoginPage;
import org.junit.Test;

public class TableHeadersTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public TableHeadersTests() {
        super();
    }

    @Test
    @Description("Test default list of column headers in the private workspace")
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.STATUS.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.STATUS.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openColumnsTable()
            .addColumn(ColumnsEnum.TYPE.getColumns())
            .selectSaveButton()
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.TYPE.getColumns()));
    }

    @Test
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .openColumnsTable()
            .addColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .selectSaveButton()
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.ASSIGNEE.getColumns()));
    }
}
