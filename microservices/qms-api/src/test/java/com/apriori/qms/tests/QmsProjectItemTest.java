package com.apriori.qms.tests;

import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Link;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestDataUtils;

public class QmsProjectItemTest extends QmsApiTestDataUtils {
    @BeforeClass
    public static void beforeClass() {
        createTestData();
    }

    @AfterClass
    public static void afterClass() {
        deleteTestDataAndClearEntities();
    }

    @Test
    @TestRail(testCaseId = {"13773", "14913"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1379")
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
    @TestRail(testCaseId = {"14914"})
    @Link("Defect - https://jira.apriori.com/browse/COL-1379")
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
