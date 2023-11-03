package com.apriori.qms.api.tests;

import com.apriori.qms.api.controller.QmsProjectResources;
import com.apriori.qms.api.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.qms.api.utils.QmsApiTestUtils;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class QmsProjectItemTest extends TestUtil {
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeAll
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        bidPackageItemResponse = QmsApiTestUtils.createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        bidPackageProjectResponse = QmsApiTestUtils.createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(scenarioItem, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);
    }

    @AfterAll
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @BeforeEach
    public void beforeTest() {
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void afterTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {13773, 14913})
    @Issue("COL-1379")
    @Description("Find all project Items for particular project")
    public void getAllProjectItems() {
        BidPackageProjectItemsResponse bpPItemResponse = QmsProjectResources.getProjectItems(
            bidPackageProjectResponse.getIdentity(),
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK,
            currentUser);
        softAssertions.assertThat(bpPItemResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(bpPItemResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bpPItemResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {14914})
    @Issue("COL-1379")
    @Description("Get project Item for particular project using project URL")
    public void getAllProjectItemByIdentity() {
        BidPackageProjectItemsResponse bpPItemResponse = QmsProjectResources.getProjectItem(
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK,
            currentUser);

        softAssertions.assertThat(bpPItemResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bpPItemResponse.getItems().get(0).getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getItems().get(0).getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getItems().get(0).getBidPackageItem().getComponentIdentity())
                .isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bpPItemResponse.getItems().get(0).getBidPackageItem().getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bpPItemResponse.getItems().get(0).getBidPackageItem().getIterationIdentity())
                .isEqualTo(bidPackageItemResponse.getIterationIdentity());
        }
    }
}
