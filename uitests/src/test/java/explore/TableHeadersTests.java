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
import main.java.utils.TestRail;
import org.junit.Test;

public class TableHeadersTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the private workspace")
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace())
            .openColumnsTable()
            .addColumn(ColumnsEnum.TYPE.getColumns())
            .selectSaveButton();

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.TYPE.getColumns()));

        explorePage.openColumnsTable()
            .removeColumn(ColumnsEnum.TYPE.getColumns())
            .selectSaveButton();
    }

    @Test
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UsersEnum.CID_TE_USER.getUsername(), UsersEnum.CID_TE_USER.getPassword())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openColumnsTable()
            .addColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .selectSaveButton();

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.ASSIGNEE.getColumns()));

        explorePage.openColumnsTable()
            .removeColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .selectSaveButton();
    }
}
