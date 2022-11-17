package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
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

public class QmsBidPackageItemTest extends TestUtil {

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
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser)
            .getResponseEntity().getItems().get(0);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13755", "13767"})
    @Description("Create and delete Bid Package Item")
    public void createAndDeleteBidPackageItem() {

        QmsBidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);

        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(bidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13760"})
    @Description("update Bid Package Item")
    public void updateBidPackageItem() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        ResponseWrapper<BidPackageItemResponse> updateBidPackageItemResponse = QmsBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13765"})
    @Description("Get Bid Package Item")
    public void getBidPackageItem() {
        ResponseWrapper<BidPackageItemResponse> updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bidPackageResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13763", "13764"})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void getBidPackageItems() {
        ResponseWrapper<BidPackageItemsResponse> updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItems(
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(updateBidPackageItemResponse.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @After
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
