package com.apriori;

import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ScenarioItem;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qds.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageItemsResponse;
import com.apriori.qds.models.response.bidpackage.BidPackageResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class BidPackageItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;
    private static ScenarioItem scenarioItem;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class,
            HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(id = {13390, 13403})
    @Description("Create and delete Bid Package Item")
    public void createAndDeleteBidPackageItem() {
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);

        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(bidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13392})
    @Description("update Bid Package Item")
    public void updateBidPackageItem() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        BidPackageItemResponse updateBidPackageItemResponse = BidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13401})
    @Description("Get Bid Package Item")
    public void getBidPackageItem() {
        BidPackageItemResponse updateBidPackageItemResponse = BidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13396, 13400})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void getBidPackageItems() {
        BidPackageItemsResponse updateBidPackageItemResponse = BidPackageResources.getBidPackageItems(
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(updateBidPackageItemResponse.getIsFirstPage()).isTrue();
    }

    @AfterEach
    public void testCleanup() {
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
