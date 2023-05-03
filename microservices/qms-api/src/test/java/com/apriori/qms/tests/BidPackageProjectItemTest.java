package com.apriori.qms.tests;

import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import utils.QmsApiTestDataUtils;

public class BidPackageProjectItemTest extends QmsApiTestDataUtils {

    @Test
    @TestRail(testCaseId = {"14685", "14912"})
    @Description("Verify that Project Item creates automatically with Project creation")
    public void createAndGetProjectItem() {
        softAssertions.assertThat(bidPackageProjectResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                .getComponentIdentity()).isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                    .getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                .getIterationIdentity()).isEqualTo(bidPackageItemResponse.getIterationIdentity());

            BidPackageProjectItemResponse bpPItemResponse = QmsBidPackageResources.getBidPackageProjectItem(
                bidPackageResponse.getIdentity(),
                bidPackageProjectResponse.getIdentity(),
                bidPackageProjectResponse.getItems().get(0).getIdentity(),
                currentUser,
                BidPackageProjectItemResponse.class,
                HttpStatus.SC_OK);
            softAssertions.assertThat(bpPItemResponse.getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getComponentIdentity())
                .isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getIterationIdentity())
                .isEqualTo(bidPackageItemResponse.getIterationIdentity());
        }
    }

    @Test
    @TestRail(testCaseId = {"14911"})
    @Description("Find all project Items for particular project using bid-package URL")
    public void getAllProjectItems() {
        BidPackageProjectItemsResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bpPItemsResponse.getItems().get(0).getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bpPItemsResponse.getItems().get(0).getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bpPItemsResponse.getItems().get(0).getBidPackageItem().getComponentIdentity())
                .isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bpPItemsResponse.getItems().get(0).getBidPackageItem().getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bpPItemsResponse.getItems().get(0).getBidPackageItem().getIterationIdentity())
                .isEqualTo(bidPackageItemResponse.getIterationIdentity());
        }
    }
}
