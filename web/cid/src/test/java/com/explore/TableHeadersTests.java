package com.explore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.enums.WorkspaceEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.explore.TableColumnsPage;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;

public class TableHeadersTests extends TestBase {

    private final String ascending = "sort-asc";
    private final String descending = "sort-desc";
    private CidLoginPage loginPage;
    private ExplorePage explorePage;
    private TableColumnsPage tableColumnsPage;

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the private workspace")
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PUBLIC.getWorkspace());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new CidLoginPage(driver);
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
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1095", "531"})
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new CidLoginPage(driver);
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

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1611", "1094"})
    @Description("Test remove thumbnails")
    public void testRemoveThumbnails() {
        loginPage = new CidLoginPage(driver);
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

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1611", "1086"})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectWorkSpace(WorkspaceEnum.PRIVATE.getWorkspace())
            .sortColumnDescending(ColumnsEnum.NAME_SCENARIO.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.NAME_SCENARIO.getColumns()), containsString(descending));

        explorePage.sortColumnAscending(ColumnsEnum.PROCESS_GROUP.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.PROCESS_GROUP.getColumns()), containsString(ascending));

        explorePage.sortColumnAscending(ColumnsEnum.VPE.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.VPE.getColumns()), containsString(ascending));

        explorePage.sortColumnDescending(ColumnsEnum.LAST_SAVED.getColumns());
        assertThat(explorePage.getColumnOrder(ColumnsEnum.LAST_SAVED.getColumns()), containsString(descending));
    }
}
