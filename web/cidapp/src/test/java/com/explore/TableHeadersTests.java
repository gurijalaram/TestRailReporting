package com.explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.CustomerSmokeTests;
import testsuites.suiteinterface.SmokeTests;
import utils.ColumnsEnum;
import utils.DirectionEnum;
import utils.SortOrderEnum;


public class TableHeadersTests extends TestBase {

    private final String ascending = "sort-down";
    private final String descending = "sort-up";
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

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.SCENARIO_NAME.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_UPDATED_BY.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1096"})
    @Description("Test default list of column headers in the public workspace")
    public void testPublicDefaultColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.SCENARIO_NAME.getColumns(), ColumnsEnum.STATE.getColumns(),
            ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.VPE.getColumns(), ColumnsEnum.LAST_UPDATED_BY.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"1095"})
    @Description("Test added columns are displayed in the public workspace")
    public void testPublicAddColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.COMPONENT_TYPE)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.COMPONENT_TYPE.getColumns()));

        explorePage.configure()
            .selectColumn(ColumnsEnum.COMPONENT_TYPE)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class);
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1095", "531"})
    @Description("Test added columns are displayed in the private workspace")
    public void testPrivateAddColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.ASSIGNEE)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getTableHeaders(), hasItem(ColumnsEnum.ASSIGNEE.getColumns()));

        explorePage.configure()
            .selectColumn(ColumnsEnum.ASSIGNEE)
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
            .selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class)
            .configure();

        assertThat(configurePage.getIncludedList(), not(containsString(ColumnsEnum.THUMBNAIL.getColumns())));

        configurePage.selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.RIGHT)
            .moveToTop(ColumnsEnum.THUMBNAIL)
            .submit(ExplorePage.class)
            .configure();

        assertThat(configurePage.getIncludedList(), containsString(ColumnsEnum.THUMBNAIL.getColumns()));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1611", "1086"})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .sortColumn(ColumnsEnum.SCENARIO_NAME, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getSortOrder(ColumnsEnum.SCENARIO_NAME), is(equalTo(SortOrderEnum.DESCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.PROCESS_GROUP, SortOrderEnum.ASCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.PROCESS_GROUP), is(equalTo(SortOrderEnum.ASCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.VPE, SortOrderEnum.ASCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.VPE), is(equalTo(SortOrderEnum.ASCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.LAST_UPDATED_AT, SortOrderEnum.DESCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.LAST_UPDATED_AT), is(equalTo(SortOrderEnum.DESCENDING.getOrder())));
    }
}
