package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageProjectItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<BidPackageResponse> bidPackageResponse;
    private static ResponseWrapper<BidPackageProjectResponse> bidPackageProjectResponse;
    private static ResponseWrapper<BidPackageItemResponse> bidPackageItemResponse;
    private static ResponseWrapper<BidPackageProjectItemResponse> bidPackageProjectItemResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String bidPackageProjectName;
    private static String userContext;
    private static ScenarioItem scenarioItem;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        bidPackageProjectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser)
            .getResponseEntity().getItems().get(0);
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(bidPackageProjectName,bidPackageResponse.getResponseEntity().getIdentity(),currentUser);
        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getResponseEntity().getIdentity(),
            currentUser,
            BidPackageItemResponse.class);

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectItemResponse.class);
    }

    @Test
    @TestRail(testCaseId = {"13411", "13427"})
    @Description("Create and delete Bid Package Project Item " +
        "PreRequisites : 1. Create BidPackage 2. Create BidPackageItem 3. Create BidPackageProject")
    public void createAndDeleteBidPackageProjectItem() {

        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectItemResponse.getResponseEntity().getIdentity(),
            currentUser);

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(),
            currentUser, BidPackageProjectItemResponse.class);
        softAssertions.assertThat(bidPackageProjectItemResponse.getResponseEntity().getBidPackageItemIdentity()).isEqualTo(bidPackageItemResponse.getResponseEntity().identity);
    }

    @Test
    @TestRail(testCaseId = {"13420"})
    @Description("Get Bid Package Project Item")
    public void getBidPackageProjectItem() {
        ResponseWrapper<BidPackageProjectItemResponse> getBidPackageProjectItemResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectItemResponse.getResponseEntity().getIdentity(),
            currentUser,BidPackageProjectItemResponse.class);

        softAssertions.assertThat(getBidPackageProjectItemResponse.getResponseEntity().getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13420", "13419"})
    @Description("Get all Bid Package Project Items and verify pagination")
    public void getBidPackageProjectItems() {
        ResponseWrapper<BidPackageProjectItemsResponse> getBidPackageProjectItemResponse = BidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            currentUser,BidPackageProjectItemsResponse.class);

        softAssertions.assertThat(getBidPackageProjectItemResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageProjectItemResponse.getResponseEntity().getIsFirstPage()).isTrue();
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageProjectResponse.getResponseEntity().getIdentity(),
            bidPackageProjectItemResponse.getResponseEntity().getIdentity(),
            currentUser);
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getResponseEntity().getIdentity(),
            bidPackageItemResponse.getResponseEntity().getIdentity(), currentUser);
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getResponseEntity().getIdentity(),bidPackageProjectResponse.getResponseEntity().getIdentity(),currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
