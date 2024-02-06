package com.apriori.cid.ui.tests.explore;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cid.ui.pageobjects.common.ConfigurePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.DirectionEnum;
import com.apriori.cid.ui.utils.SortOrderEnum;
import com.apriori.cid.ui.utils.UploadStatusEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TableHeadersTests extends TestBaseUI {

    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private ConfigurePage configurePage;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder component;

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

    @Test
    @TestRail(id = {29433, 29434, 29436, 29437})
    @Description("Test Last updated Timestamp")
    public void testLastUpdatedAlert() {
        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser());

        List<ComponentInfoBuilder> components = new ComponentRequestUtil().getComponents(2);
        component = new ComponentRequestUtil().getComponent();

        String timestamp1 = explorePage.getUpdateTimestamp();
        String formattedTime = DateTime.now().toString("h:mma");

        softAssertions.assertThat(timestamp1).as("Verify Last Updated Timestamp on initial load").contains("Last updated at " + formattedTime);

        explorePage.importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentBuilderDetails(components)
            .submit()
            .clickClose();

        String timestamp2 = explorePage.getUpdateTimestamp();
        formattedTime = DateTime.now().toString("h:mma");

        softAssertions.assertThat(timestamp2).as("Verify Last Updated Timestamp is updated after scenario is created").contains("Last updated at " + formattedTime);

        explorePage.importCadFile()
            .inputComponentDetails(component.getScenarioName(), component.getResourceFile())
            .waitForUploadStatus(component.getComponentName() + component.getExtension(), UploadStatusEnum.UPLOADED)
            .submit()
            .openComponent(component.getComponentName())
            .clickExplore();

        String timestamp3 = explorePage.getUpdateTimestamp();
        formattedTime = DateTime.now().toString("h:mma");

        softAssertions.assertThat(timestamp3).as("Verify Last Updated Timestamp after additional single upload").contains("Last updated at " + formattedTime);

        explorePage.clickEvaluate()
            .costScenario()
            .clickExplore();

        softAssertions.assertThat(timestamp3).as("Verify Last Updated Timestamp remains the same").contains("Last updated at " + formattedTime);

        explorePage.refresh();

        String timestamp4 = explorePage.getUpdateTimestamp();
        formattedTime = DateTime.now().toString("h:mma");

        softAssertions.assertThat(timestamp4).as("Verify Last Updated Timestamp after refresh").contains("Last updated at " + formattedTime);

        softAssertions.assertAll();
    }
}
