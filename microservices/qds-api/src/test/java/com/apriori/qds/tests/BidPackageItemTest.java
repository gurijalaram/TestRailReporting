package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qds.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qds.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageItemsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    private static ResponseWrapper<BidPackageItemResponse> bidPackageItemResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;
    private static ScenarioItem scenarioItem;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class,
            HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13390", "13403"})
    @Description("Create and delete Bid Package Item")
    public void createAndDeleteBidPackageItem() {
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);

        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(bidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13392"})
    @Description("update Bid Package Item")
    public void updateBidPackageItem() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        ResponseWrapper<BidPackageItemResponse> updateBidPackageItemResponse = BidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13401"})
    @Description("Get Bid Package Item")
    public void getBidPackageItem() {
        ResponseWrapper<BidPackageItemResponse> updateBidPackageItemResponse = BidPackageResources.getBidPackageItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13396", "13400"})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void getBidPackageItems() {
        ResponseWrapper<BidPackageItemsResponse> updateBidPackageItemResponse = BidPackageResources.getBidPackageItems(
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
