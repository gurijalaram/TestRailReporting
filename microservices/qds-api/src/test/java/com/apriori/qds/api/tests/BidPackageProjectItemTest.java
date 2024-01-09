package com.apriori.qds.api.tests;

import com.apriori.css.api.utils.CssComponent;
import com.apriori.qds.api.controller.BidPackageResources;
import com.apriori.qds.api.enums.QDSAPIEnum;
import com.apriori.qds.api.enums.UserRole;
import com.apriori.qds.api.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageProjectUserResponse;
import com.apriori.qds.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.qds.api.utils.QdsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.ErrorMessage;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

@ExtendWith(TestRulesAPI.class)
public class BidPackageProjectItemTest extends QdsApiTestUtils {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectItemResponse bidPackageProjectItemResponse;
    private static String userContext;
    private static ScenarioItem scenarioItem;
    UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
        bidPackageProjectResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);
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
    @TestRail(id = {13411, 13427})
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

        softAssertions.assertThat(bidPackageProjectItemResponse.getBidPackageItemIdentity())
            .isEqualTo(bidPackageItemResponse.identity);
    }

    @Test
    @TestRail(id = {13420})
    @Description("Get Bid Package Project Item")
    public void getBidPackageProjectItem() {
        BidPackageProjectItemResponse getBidPackageProjectItemResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageProjectItemResponse.getProjectIdentity())
            .isEqualTo(bidPackageProjectResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13418, 13419})
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
    @TestRail(id = {13412})
    @Description("Create  Bid Package Project Item with out project")
    public void createProjectItemWithoutProject() {
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), currentUser);

        BidPackageResources.deleteBidPackageProject(bidPackageResponse.getIdentity(), bppResponse.getIdentity(), currentUser);

        ErrorMessage bppiResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bppResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bppiResponse.getMessage())
            .contains("Can't find Project for bid package with identity '" + bppResponse.getBidPackageIdentity()
                + "' and identity '" + bppResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13413})
    @Description("Create  Bid Package Project Item with out bidPackageItemIdentity")
    public void createProjectItemWithoutPackageItem() {
        ErrorMessage bppiResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            "",
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(bppiResponse.getMessage())
            .contains("Can't find bidPackageItem for bid package with identity '"
                + bidPackageProjectResponse.getBidPackageIdentity()
                + "' and identity 'null'");
    }

    @Test
    @TestRail(id = {13416})
    @Description("Create  Bid Package Project Item with out bid Package")
    public void createProjectItemWithoutBidPackage() {
        String bpName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse bpResponse = BidPackageResources.createBidPackage(bpName, userContext);
        BidPackageProjectResponse bppResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), bpResponse.getIdentity(), currentUser);
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

        softAssertions.assertThat(bppiResponse.getMessage())
            .contains("Can't find bidPackage with identity '" + bppResponse.getBidPackageIdentity() + "'");
    }

    @Test
    @TestRail(id = {13421, 13429, 13430})
    @Description("Delete and Get project Item to verify project item is removed")
    public void deleteAndGetProjectItemWithNoItemIdentity() {
        BidPackageResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser);

        RequestEntity requestEntity = requestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getIdentity(),
                bidPackageProjectResponse.getIdentity(),
                bidPackageProjectItemResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage())
            .contains("Can't find ProjectItem for Project with identity '" + bidPackageProjectResponse.getIdentity()
                + "' and identity '" + bidPackageProjectItemResponse.getIdentity());

        ErrorMessage getErrorResponse = BidPackageResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser, ErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(getErrorResponse.getMessage())
            .contains("Can't find ProjectItem for Project with identity '" + bidPackageProjectResponse.getIdentity()
                + "' and identity '" + bidPackageProjectItemResponse.getIdentity());

        bidPackageProjectItemResponse = BidPackageResources.createBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser, BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(id = {13423})
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
    @TestRail(id = {13428})
    @Description("Delete project Item by incorrect project Identity")
    public void deleteProjectItemWithInvalidIdentity() {
        RequestEntity requestEntity = requestEntityUtil.init(QDSAPIEnum.BID_PACKAGE_PROJECT_ITEM, ErrorMessage.class)
            .inlineVariables(bidPackageResponse.getIdentity(),
                "INVALIDPROJECTIDENTITY",
                bidPackageProjectItemResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> responseWrapper = HTTPRequest.build(requestEntity).delete();

        softAssertions.assertThat(responseWrapper.getResponseEntity().getMessage())
            .contains("'projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(id = {13736, 13739})
    @Description("Create and Delete Project Item by Admin")
    public void createProjectItemByAdmin() {
        String packageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse packageResponse = BidPackageResources.createBidPackage(packageName, userContext);
        BidPackageProjectResponse packageProjectResponse = BidPackageResources.createBidPackageProject(new HashMap<>(), packageResponse.getIdentity(), currentUser);
        BidPackageItemResponse packageItemResponse = BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            packageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        UserCredentials adminUser = UserUtil.getUser();
        BidPackageProjectUserResponse bidPackageDefaultProjectUserResponse = BidPackageResources.createBidPackageProjectUser(UserRole.ADMIN.getUserRole(),
            packageResponse.getIdentity(), packageProjectResponse.getIdentity(), currentUser, adminUser);

        softAssertions.assertThat(bidPackageDefaultProjectUserResponse.getProjectIdentity()).isEqualTo(packageProjectResponse.getIdentity());

        BidPackageProjectItemResponse projectItemResponse = BidPackageResources.createBidPackageProjectItem(
            packageResponse.getIdentity(),
            packageProjectResponse.getIdentity(),
            packageItemResponse.getIdentity(),
            adminUser, BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED);

        softAssertions.assertThat(projectItemResponse.getBidPackageItemIdentity()).isEqualTo(packageItemResponse.identity);

        BidPackageResources.deleteBidPackageProjectItem(packageResponse.getIdentity(),
            packageProjectResponse.getIdentity(),
            projectItemResponse.getIdentity(),
            adminUser);

        BidPackageResources.deleteBidPackageItem(packageResponse.getIdentity(), packageItemResponse.getIdentity(), adminUser);
        BidPackageResources.deleteBidPackageProject(packageResponse.getIdentity(), packageProjectResponse.getIdentity(), adminUser);
        BidPackageResources.deleteBidPackage(packageResponse.getIdentity(), currentUser);
    }

    @AfterEach
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
