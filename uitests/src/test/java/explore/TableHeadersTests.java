package explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.TableColumnsPage;
import com.apriori.pageobjects.pages.login.LoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;

public class TableHeadersTests extends TestBase {

    private LoginPage loginPage;
    private ExplorePage explorePage;
    private TableColumnsPage tableColumnsPage;

    private final String ASCENDING = "sort-asc";
    private final String DESCENDING = "sort-desc";

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the private workspace")
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.LOCKED_WORKSPACE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @Issue("BA-893")
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
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
    @Issue("BA-893")
    @TestRail(testCaseId = {"1095", "531"})
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openColumnsTable()
            .addColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .selectSaveButton();

        assertThat(explorePage.getColumnHeaderNames(), hasItem(ColumnsEnum.ASSIGNEE.getColumns()));

        explorePage.openColumnsTable()
            .removeColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .selectSaveButton();
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1611", "1094"})
    @Description("Test remove thumbnails")
    public void testRemoveThumbnails() {
        loginPage = new LoginPage(driver);
        tableColumnsPage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .openColumnsTable()
            .removeColumn(ColumnsEnum.THUMBNAIL.getColumns())
            .selectSaveButton()
            .openColumnsTable();

        assertThat(tableColumnsPage.getIncludedList(), not(containsString(ColumnsEnum.THUMBNAIL.getColumns())));

        tableColumnsPage.addColumn(ColumnsEnum.THUMBNAIL.getColumns())
            .moveColumnToTop(ColumnsEnum.THUMBNAIL.getColumns())
            .selectSaveButton().openColumnsTable();

        assertThat(tableColumnsPage.getIncludedList(), containsString(ColumnsEnum.THUMBNAIL.getColumns()));
    }

    @Category(CustomerSmokeTests.class)
    @Test
    @TestRail(testCaseId = {"1611", "1086"})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new LoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .sortColumnDescending(ColumnsEnum.NAME_SCENARIO.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.NAME_SCENARIO.getColumns()), containsString(DESCENDING));

        explorePage.sortColumnAscending(ColumnsEnum.PROCESS_GROUP.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.PROCESS_GROUP.getColumns()), containsString(ASCENDING));

        explorePage.sortColumnAscending(ColumnsEnum.VPE.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.VPE.getColumns()), containsString(ASCENDING));

        explorePage.sortColumnDescending(ColumnsEnum.LAST_SAVED.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.LAST_SAVED.getColumns()), containsString(DESCENDING));
    }
}
