package com.explore;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColumnsEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;


public class TableHeadersTests extends TestBase {

    private final String ascending = "sort-asc";
    private final String descending = "sort-desc";
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ConfigurePage configurePage;

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the private workspace")
    public void testPrivateDefaultColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.NAME_SCENARIO.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_SAVED.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.TYPE.getColumns())
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getColumnHeaderNames(), hasItems(ColumnsEnum.TYPE.getColumns()));

        explorePage.configure()
            .selectColumn(ColumnsEnum.TYPE.getColumns())
            .moveColumn(DirectionEnum.LEFT)
            .selectSaveButton(ExplorePage.class);
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1095", "531"})
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getColumnHeaderNames(), hasItem(ColumnsEnum.ASSIGNEE.getColumns()));

        explorePage.configure()
            .selectColumn(ColumnsEnum.ASSIGNEE.getColumns())
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class);
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1611", "1094"})
    @Description("Test remove thumbnails")
    public void testRemoveThumbnails() {
        loginPage = new CidAppLoginPage(driver);
        configurePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.THUMBNAIL.getColumns())
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class)
            .configure();

        assertThat(configurePage.getIncludedList(), not(containsString(ColumnsEnum.THUMBNAIL.getColumns())));

        configurePage.selectColumn(ColumnsEnum.THUMBNAIL.getColumns())
            .moveColumn(DirectionEnum.RIGHT)

            // TODO: 10/05/2021 implement below
            .moveColumnToTop(ColumnsEnum.THUMBNAIL.getColumns())
            .selectSaveButton(ExplorePage.class)
            .configure();

        assertThat(tableColumnsPage.getIncludedList(), containsString(ColumnsEnum.THUMBNAIL.getColumns()));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1611", "1086"})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())

            // TODO: 10/05/2021 implement below
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
