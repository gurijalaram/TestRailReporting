package com.explore;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ConfigurePage configurePage;

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(testCaseId = {"7928"})
    @Description("Test default list of column headers in the workspace")
    public void testDefaultColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.THUMBNAIL.getColumns(), ColumnsEnum.COMPONENT_NAME.getColumns(), ColumnsEnum.SCENARIO_NAME.getColumns(),
            ColumnsEnum.COMPONENT_TYPE.getColumns(), ColumnsEnum.STATE.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.DIGITAL_FACTORY.getColumns(), ColumnsEnum.CREATED_AT.getColumns(),
            ColumnsEnum.CREATED_BY.getColumns(), ColumnsEnum.ANNUAL_VOLUME.getColumns(), ColumnsEnum.BATCH_SIZE.getColumns(), ColumnsEnum.DFM_RISK.getColumns(), ColumnsEnum.FULLY_BURDENED_COST.getColumns()));
    }

    @Test
    @TestRail(testCaseId = {"6347"})
    @Description("Test added columns are displayed in the workspace")
    public void testAddColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.DESCRIPTION)
            .moveColumn(DirectionEnum.RIGHT)
            .submit(ExplorePage.class);

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.DESCRIPTION.getColumns()));

        explorePage.configure()
            .selectColumn(ColumnsEnum.DESCRIPTION)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class);
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"6346"})
    @Description("Test remove thumbnails")
    public void testRemoveThumbnails() {
        loginPage = new CidAppLoginPage(driver);
        configurePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class)
            .configure();

        assertThat(configurePage.getChosenList(), not(hasItem(ColumnsEnum.THUMBNAIL.getColumns())));

        configurePage.selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.RIGHT)
            .moveToTop(ColumnsEnum.THUMBNAIL)
            .submit(ExplorePage.class)
            .configure();

        assertThat(configurePage.getChosenList(), hasItem(ColumnsEnum.THUMBNAIL.getColumns()));
    }

    @Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"6340"})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .sortColumn(ColumnsEnum.SCENARIO_NAME, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getSortOrder(ColumnsEnum.SCENARIO_NAME), is(equalTo(SortOrderEnum.DESCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.PROCESS_GROUP, SortOrderEnum.ASCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.PROCESS_GROUP), is(equalTo(SortOrderEnum.ASCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.DIGITAL_FACTORY, SortOrderEnum.ASCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.DIGITAL_FACTORY), is(equalTo(SortOrderEnum.ASCENDING.getOrder())));

        explorePage.sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);
        assertThat(explorePage.getSortOrder(ColumnsEnum.CREATED_AT), is(equalTo(SortOrderEnum.DESCENDING.getOrder())));
    }
}
