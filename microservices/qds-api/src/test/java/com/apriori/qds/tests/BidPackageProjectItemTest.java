package com.apriori.qds.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BidPackageProjectItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectItemResponse bidPackageProjectItemResponse;
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
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(bidPackageProjectName, bidPackageResponse.getIdentity(), currentUser);
        bidPackageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13411", "13427"})
    @Description("Create and delete Bid Package Project Item " +
        "PreRequisites : 1. Create BidPackage 2. Create BidPackageItem 3. Create BidPackageProject")
    public void createAndDeleteBidPackageProjectItem() {

        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser);

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(bidPackageProjectItemResponse.getBidPackageItemIdentity()).isEqualTo(bidPackageItemResponse.identity);
    }

    @Test
    @TestRail(testCaseId = {"13420"})
    @Description("Get Bid Package Project Item")
    public void getBidPackageProjectItem() {
        BidPackageProjectItemResponse getBidPackageProjectItemResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectItemResponse.getProjectIdentity()).isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13418", "13419"})
    @Description("Get all Bid Package Project Items and verify pagination")
    public void getBidPackageProjectItems() {
        BidPackageProjectItemsResponse getBidPackageProjectItemResponse = BidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser, BidPackageProjectItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectItemResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageProjectItemResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"13412"})
    @Description("Create  Bid Package Project Item with out project")
    public void createProjectItemWithoutProject() {
        String bppName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(bppName, bidPackageResponse.getIdentity(), currentUser);

        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), currentUser);

        ErrorMessage bppiResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bppResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bppiResponse.getMessage()).contains("Can't find Project for bid package with identity '" + bppResponse.getBidPackageIdentity()
            + "' and identity '" + bppResponse.getIdentity());
    }

    @Test
    @TestRail(testCaseId = {"13413"})
    @Description("Create  Bid Package Project Item with out bidPackageItemIdentity")
    public void createProjectItemWithoutPackageItem() {
        ErrorMessage bppiResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            "",
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bppiResponse.getMessage()).contains("Can't find bidPackageItem for bid package with identity '"
            + bidPackageProjectResponse.getBidPackageIdentity()
            + "' and identity 'null'");
    }

    @Test
    @TestRail(testCaseId = {"13416"})
    @Description("Create  Bid Package Project Item with out bid Package")
    public void createProjectItemWithoutBidPackage() {
        String bpName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        String bppName = "PROJ" + new GenerateStringUtil().getRandomNumbers();

        BidPackageResponse bpResponse = BidPackageResources.createBidPackage(bpName, userContext);
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(bppName, bpResponse.getIdentity(), currentUser);
        BidPackageItemResponse bpiResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bpResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        BidPackageResources.deleteBidPackage(bpResponse.getIdentity(), currentUser);

        ErrorMessage bppiResponse = BidPackageResources.createBidPackageProjectItem(
            bpResponse.getIdentity(),
            bppResponse.getIdentity(),
            bpiResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bppiResponse.getMessage()).contains("Can't find bidPackage with identity '" + bppResponse.getBidPackageIdentity() + "'");
    }

    @Test
    @TestRail(testCaseId = {"13421", "13429", "13430"})
    @Description("Delete and Get project Item to verify project item is removed")
    public void deleteAndGetProjectItemWithNoItemIdentity() {
        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser);

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getIdentity(),
                bidPackageProjectResponse.getIdentity(),
                bidPackageProjectItemResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("Can't find ProjectItem for Project with identity '" + bidPackageProjectResponse.getIdentity()
            + "' and identity '" + bidPackageProjectItemResponse.getIdentity());

        ErrorMessage getErrorResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(getErrorResponse.getMessage()).contains("Can't find ProjectItem for Project with identity '" + bidPackageProjectResponse.getIdentity()
            + "' and identity '" + bidPackageProjectItemResponse.getIdentity());

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"13423"})
    @Description("Get project Item by Invalid Identity")
    public void grtProjectItemWithNoInvalidProjectIdentity() {
        ErrorMessage getErrorResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            "INVALIDPROJECTIDENTITY",
            bidPackageProjectItemResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(getErrorResponse.getMessage()).contains("'projectIdentity' is not a valid identity");

    }

    @Test
    @TestRail(testCaseId = {"13428"})
    @Description("Delete project Item by incorrect project Identity")
    public void deleteProjectItemWithInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getIdentity(),
                "INVALIDPROJECTIDENTITY",
                bidPackageProjectItemResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage()).contains("'projectIdentity' is not a valid identity");
    }

    @After
    public void testCleanup() {
        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser);
        BidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);
        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), currentUser);
        BidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
