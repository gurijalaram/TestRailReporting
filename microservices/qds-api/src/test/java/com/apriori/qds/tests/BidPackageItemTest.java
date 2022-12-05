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
import com.apriori.utils.ErrorMessage;
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
    @TestRail(testCaseId = {"13390", "13403", "13404"})
    @Description("Create, delete Bid Package Item and verify bid package item is removed")
    public void createAndDeleteBidPackageItem() {
        String bpName  = "BPN" + new GenerateStringUtil().getRandomNumbers();
        ResponseWrapper<BidPackageResponse> bpResponse = BidPackageResources.createBidPackage(bpName, userContext);

        softAssertions.assertThat(bpResponse.getResponseEntity().getName()).isEqualTo(bpName);

        ResponseWrapper<BidPackageItemResponse> bpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(bpiResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bpResponse.getResponseEntity().getIdentity());

        BidPackageResources.deleteBidPackageItem(bpResponse.getResponseEntity().getIdentity(),
            bpiResponse.getResponseEntity().getIdentity(), currentUser);

        ResponseWrapper<ErrorMessage> invalidBpiResponse = BidPackageResources.getBidPackageItem(
            bpResponse.getResponseEntity().getIdentity(),
            bpiResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(invalidBpiResponse.getResponseEntity().getMessage()).contains("Can't find bidPackageItem for bid package with identity '" + bpResponse.getResponseEntity().getIdentity()
            + "' and identity '" + bpiResponse.getResponseEntity().identity + "'");
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

    @Test
    @TestRail(testCaseId = {"13391"})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void createPackageItemWithInvalidComponentIdentity() {
        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder("INVALIDIDENTITY",
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"13443"})
    @Description("Create bid-package Item without component Identity")
    public void createPackageItemWithBlankComponentIdentity() {
        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder("",
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("No message available");
    }

    @Test
    @TestRail(testCaseId = {"13393"})
    @Description("Create Bid Package item without created bid package")
    public void createBidPackageItemWithNonExistBidPackage() {
        String bpName  = "BPN" + new GenerateStringUtil().getRandomNumbers();
        ResponseWrapper<BidPackageResponse> bpResponse = BidPackageResources.createBidPackage(bpName, userContext);

        softAssertions.assertThat(bpResponse.getResponseEntity().getName()).isEqualTo(bpName);

        BidPackageResources.deleteBidPackage(bpResponse.getResponseEntity().getIdentity(), currentUser);
        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("Can't find bidPackage with identity '" + bpResponse.getResponseEntity().getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"13405"})
    @Description("Create bid-package Item with existing iteration")
    public void createBidPackageItemWithExistingIteration() {
        String bpName  = "BPN" + new GenerateStringUtil().getRandomNumbers();
        ResponseWrapper<BidPackageResponse> bpResponse = BidPackageResources.createBidPackage(bpName, userContext);

        softAssertions.assertThat(bpResponse.getResponseEntity().getName()).isEqualTo(bpName);

        ResponseWrapper<BidPackageItemResponse> bpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class,
            HttpStatus.SC_CREATED);

        softAssertions.assertThat(bpiResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bpResponse.getResponseEntity().getIdentity());

        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("BidPackageItem for scenario with identity '" + scenarioItem.getScenarioIdentity()
            + "' already exists for bid package with identity '" + bpResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13444"})
    @Description("Create bid-package Item without Scenario Identity")
    public void createPackageItemWithoutScenarioIdentity() {
        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                "", scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);
        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("No message available");
    }

    @Test
    @TestRail(testCaseId = {"13394"})
    @Description("Find list of  Bid Package Items with invalid identity")
    public void updatePackageItemWithInvalidIdentity() {
        BidPackageItemRequest bpiRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();
        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.updateBidPackageItem(
            bpiRequestBuilder,
            bidPackageResponse.getResponseEntity().getIdentity(),
            "INVALIDIDENTITY",
            currentUser,
            ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"1339"})
    @Description("Create Bid Package item without created bid package")
    public void updateBidPackageItemWithoutBidPackage() {
        String bpName  = "BPN" + new GenerateStringUtil().getRandomNumbers();
        ResponseWrapper<BidPackageResponse> bpResponse = BidPackageResources.createBidPackage(bpName, userContext);

        softAssertions.assertThat(bpResponse.getResponseEntity().getName()).isEqualTo(bpName);

        ResponseWrapper<BidPackageItemResponse> bpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class,
            HttpStatus.SC_CREATED);

        softAssertions.assertThat(bpiResponse.getResponseEntity().getBidPackageIdentity()).isEqualTo(bpResponse.getResponseEntity().getIdentity());

        BidPackageResources.deleteBidPackage(bpResponse.getResponseEntity().getIdentity(), currentUser);

        ResponseWrapper<ErrorMessage> cBpiResponse = BidPackageResources.updateBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getResponseEntity().getIdentity(),
            bpiResponse.getResponseEntity().getIdentity(),
            currentUser,
            ErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cBpiResponse.getResponseEntity().getMessage()).contains("Can't find bidPackage with identity '" + bpResponse.getResponseEntity().getIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"13402"})
    @Description("Get Bid Package Item with invalid identity")
    public void getBidPackageItemWithInvalidIdentity() {
        ResponseWrapper<ErrorMessage> invalidBpiResponse = BidPackageResources.getBidPackageItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            "INVALIDITEMIDENTITY",
            currentUser,
            ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(invalidBpiResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
