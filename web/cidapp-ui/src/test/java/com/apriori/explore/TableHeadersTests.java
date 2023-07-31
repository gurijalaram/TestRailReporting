package com.apriori.explore;

import static com.apriori.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.common.ConfigurePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.ColumnsEnum;
import com.utils.DirectionEnum;
import com.utils.SortOrderEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.jupiter.api.Tag;

public class TableHeadersTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ConfigurePage configurePage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public TableHeadersTests() {
        super();
    }

    @Test
    @TestRail(id = {7928})
    @Description("Test default list of column headers in the workspace")
    public void testDefaultColumnHeaders() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        assertThat(explorePage.getTableHeaders(), hasItems(ColumnsEnum.THUMBNAIL.getColumns(), ColumnsEnum.COMPONENT_NAME.getColumns(), ColumnsEnum.SCENARIO_NAME.getColumns(),
            ColumnsEnum.COMPONENT_TYPE.getColumns(), ColumnsEnum.STATE.getColumns(), ColumnsEnum.PROCESS_GROUP.getColumns(), ColumnsEnum.DIGITAL_FACTORY.getColumns(), ColumnsEnum.CREATED_AT.getColumns(),
            ColumnsEnum.CREATED_BY.getColumns(), ColumnsEnum.ANNUAL_VOLUME.getColumns(), ColumnsEnum.BATCH_SIZE.getColumns(), ColumnsEnum.DFM_RISK.getColumns(), ColumnsEnum.FULLY_BURDENED_COST.getColumns()));
    }

    @Test
    @TestRail(id = {6347})
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

    @Test
    @TestRail(id = {6346})
    @Description("Test remove thumbnails")
    public void testRemoveThumbnails() {
        loginPage = new CidAppLoginPage(driver);
        configurePage = loginPage.login(UserUtil.getUser())
            .configure()
            .selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.LEFT)
            .submit(ExplorePage.class)
            .configure();

        softAssertions.assertThat(configurePage.getChosenList()).doesNotContain(ColumnsEnum.THUMBNAIL.getColumns());

        configurePage.selectColumn(ColumnsEnum.THUMBNAIL)
            .moveColumn(DirectionEnum.RIGHT)
            .moveToTop(ColumnsEnum.THUMBNAIL)
            .submit(ExplorePage.class)
            .configure();

        softAssertions.assertThat(configurePage.getChosenList()).contains(ColumnsEnum.THUMBNAIL.getColumns());

        softAssertions.assertAll();
    }

    @Tag(SMOKE)
    @Test
    @TestRail(id = {6340})
    @Description("Test sort all columns")
    public void testSortColumns() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .sortColumn(ColumnsEnum.SCENARIO_NAME, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getSortOrder(ColumnsEnum.SCENARIO_NAME)).isEqualTo(SortOrderEnum.DESCENDING.getOrder());

        explorePage.sortColumn(ColumnsEnum.PROCESS_GROUP, SortOrderEnum.ASCENDING);

        softAssertions.assertThat(explorePage.getSortOrder(ColumnsEnum.PROCESS_GROUP)).isEqualTo(SortOrderEnum.ASCENDING.getOrder());

        explorePage.sortColumn(ColumnsEnum.DIGITAL_FACTORY, SortOrderEnum.ASCENDING);

        softAssertions.assertThat(explorePage.getSortOrder(ColumnsEnum.DIGITAL_FACTORY)).isEqualTo(SortOrderEnum.ASCENDING.getOrder());

        explorePage.sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        softAssertions.assertThat(explorePage.getSortOrder(ColumnsEnum.CREATED_AT)).isEqualTo(SortOrderEnum.DESCENDING.getOrder());

        softAssertions.assertAll();
    }
}
