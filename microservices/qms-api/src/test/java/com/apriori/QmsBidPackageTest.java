package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ApwErrorMessage;
import com.apriori.models.response.component.ScenarioItem;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.qms.models.request.bidpackage.BidPackageParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageRequest;
import com.apriori.qms.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.models.response.bidpackage.BidPackagesResponse;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

@ExtendWith(TestRulesAPI.class)
public class QmsBidPackageTest extends TestUtil {
    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static ScenarioItem scenarioItem;
    private static String bidPackageName;
    private static String userContext;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {13361, 14127})
    @Description("Create, Delete and verify Bid Package is deleted")
    public void createDeleteAndVerifyBidPackage() {
        String bpName = bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse createBidPackageResponse = QmsBidPackageResources.createBidPackage(bpName, currentUser);
        softAssertions.assertThat(createBidPackageResponse.getName()).isEqualTo(bpName);
        QmsBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(),
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage())
            .contains("Can't find bidPackage with identity '" + createBidPackageResponse.getIdentity() + "'");
    }

    @Test
    @TestRail(id = {13362})
    @Description("Create Bid Package greater than 64 characters")
    public void createBidPackageNameMoreThan64() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("description")
                .name(RandomStringUtils.randomAlphabetic(70))
                .status("NEW")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage())
            .contains("should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {13365})
    @Description("Create Bid Package empty name")
    public void createBidPackageEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("description")
                .name("")
                .status("NEW")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {13806, 13372, 13373})
    @Description("Create Bid Package is equal to 64 characters, delete bid package and verify bid package is deleted")
    public void createBidPackageNameEqualTo64() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("description")
                .name(RandomStringUtils.randomAlphabetic(64))
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();

        BidPackageResponse bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, BidPackageResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bidPackageResponse.getAssignedTo()).isEqualTo(userIdentity);

        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage bidPackageErrorResponse = QmsBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(bidPackageErrorResponse.getMessage())
            .contains("Can't find bidPackage with identity '" + bidPackageResponse.getIdentity() + "'");
    }

    @Test
    @TestRail(id = {13807})
    @Description("Create Bid Package description is equal to 254 characters")
    public void createBidPackageNameEqualTo254() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(254))
                .name(RandomStringUtils.randomAlphabetic(15))
                .status("NEW")
                .assignedTo(userIdentity)
                .build())
            .build();

        BidPackageResponse createBidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, BidPackageResponse.class, HttpStatus.SC_CREATED, currentUser);

        softAssertions.assertThat(createBidPackageResponse.getAssignedTo()).isEqualTo(userIdentity);
        QmsBidPackageResources.deleteBidPackage(createBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

    }

    @Test
    @TestRail(id = {13363})
    @Description("Create Bid Package description is greater 254 characters")
    public void createBidPackageNameGreaterThan254() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(260))
                .name(bidPackageName)
                .status("NEW")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage())
            .contains("should not be more than 254 characters");
    }

    @Test
    @TestRail(id = {13364})
    @Description("Create Bid Package with empty name")
    public void createBidPackageWithEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("TEST")
                .name("")
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGES, ApwErrorMessage.class)
            .body(bidPackageRequest)
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ApwErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage())
            .contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {13364})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyDescription() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("")
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("NEW")
                .build())
            .build();

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage()).contains("'description' should not be null");
    }

    @Test
    @TestRail(id = {13886})
    @Description("Create Bid Package with empty Status")
    public void createBidPackageWithEmptyStatus() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(15))
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("")
                .build())
            .build();

        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.createBidPackage(bidPackageRequest, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage()).contains("'status' should not be null");
    }

    @Test
    @TestRail(id = {13366})
    @Description("Updated existing Bid Package status")
    public void updateBidPackageStatus() {
        BidPackageRequest bidPackageRequestBuilder = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .name(bidPackageResponse.getName())
                .description(bidPackageResponse.getDescription())
                .status("ASSIGNED")
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        BidPackageResponse bidPackageUpdateResponse = QmsBidPackageResources.updateBidPackage(bidPackageResponse.getIdentity(),
            bidPackageRequestBuilder, BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bidPackageUpdateResponse.getStatus()).isEqualTo("ASSIGNED");
    }

    @Test
    @TestRail(id = {13887})
    @Description("Updated existing Bid Package description")
    public void updateBidPackageDescription() {
        String bpDesc = RandomStringUtils.randomAlphabetic(15);
        BidPackageRequest bidPackageRequestBuilder = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .name(bidPackageResponse.getName())
                .description(bpDesc)
                .status(bidPackageResponse.getStatus())
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        BidPackageResponse bidPackageUpdateResponse = QmsBidPackageResources.updateBidPackage(bidPackageResponse.getIdentity(),
            bidPackageRequestBuilder, BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bidPackageUpdateResponse.getDescription()).isEqualTo(bpDesc);
    }

    @Test
    @TestRail(id = {13888})
    @Description("Updated existing Bid Package Name")
    public void updateBidPackageName() {
        String bpName = RandomStringUtils.randomAlphabetic(15);
        BidPackageRequest bidPackageRequestBuilder = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .name(bpName)
                .description(bidPackageResponse.getDescription())
                .status(bidPackageResponse.getStatus())
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        BidPackageResponse bidPackageUpdateResponse = QmsBidPackageResources.updateBidPackage(bidPackageResponse.getIdentity(),
            bidPackageRequestBuilder, BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bidPackageUpdateResponse.getName()).isEqualTo(bpName);
    }

    @Test
    @TestRail(id = {14125})
    @Description("Verify that user can assign another user")
    public void updateBidPackageAssigned() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequestBuilder = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .name(bidPackageResponse.getName())
                .description(bidPackageResponse.getDescription())
                .status(bidPackageResponse.getStatus())
                .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                .build())
            .build();

        BidPackageResponse bidPackageUpdateResponse = QmsBidPackageResources.updateBidPackage(bidPackageResponse.getIdentity(),
            bidPackageRequestBuilder, BidPackageResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(bidPackageUpdateResponse.getAssignedTo()).isEqualTo(userIdentity);
    }

    @Test
    @TestRail(id = {13367, 13874, 14743})
    @Description("Find List of bid packages and verify pagination for customer identity")
    public void getBidPackages() {
        BidPackagesResponse getBidPackagesResponse = QmsBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(getBidPackagesResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(getBidPackagesResponse.getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(getBidPackagesResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13368})
    @Description("Find List of bid packages from another user")
    public void getBidPackagesFromOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        BidPackagesResponse bidPackagesResponse = QmsBidPackageResources.getBidPackages(BidPackagesResponse.class, HttpStatus.SC_OK, otherUser);
        softAssertions.assertThat(bidPackagesResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13371})
    @Description("get bid package by identity from another user")
    public void getBidPackageFromOtherUser() {
        UserCredentials otherUser = QmsApiTestUtils.getCustomerUser();
        ApwErrorMessage qmsErrorMessageResponse = QmsBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND, otherUser);

        softAssertions.assertThat(qmsErrorMessageResponse.getMessage())
            .contains("Can't find bidPackage with identity '" + bidPackageResponse.getIdentity()
                + "' for customerIdentity '");
    }

    @Test
    @TestRail(id = {13893})
    @Description("Find bid package from other customer identity user")
    public void getBidPackagesFromAnotherCustomer() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(QmsApiTestUtils.getCustomerUser()
            .getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, ApwErrorMessage.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ApwErrorMessage> getBidPackagesResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(getBidPackagesResponse.getResponseEntity().getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity", bidPackageResponse.getIdentity()));
        softAssertions.assertThat(getBidPackagesResponse.getResponseEntity().getStatus()).isEqualTo(404);
        softAssertions.assertThat(getBidPackagesResponse.getResponseEntity().getError()).isEqualTo("Not Found");
    }

    @Test
    @TestRail(id = {13369, 14744})
    @Description("Get bid package by identity")
    public void getBidPackage() {
        BidPackageResponse getBidPackageResp = QmsBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageResp.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(id = {14745, 14746})
    @Description("1. Create Bid Package" +
        "2. Create Bid Package Item" +
        "3. Get Bid Package by Identity and verify bid package item" +
        "4. Create Bid Package Project" +
        "5. Get Bid Package by Identity and verify bid project and package item")
    public void getBidPackageWithAddedPackageItem() {
        String projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        HashMap<String, String> projectAttributesMap = new HashMap<>();
        projectAttributesMap.put("projectName", projectName);
        BidPackageResources.createBidPackageItem(
            BidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class,
            HttpStatus.SC_CREATED);

        BidPackageResponse getBidPackageResp = QmsBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageResp.getName()).isEqualTo(bidPackageName);
        softAssertions.assertThat(getBidPackageResp.getItems().size()).isGreaterThan(0);

        BidPackageProjectResponse bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectAttributesMap, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bidPackageProjectResponse.getName()).isEqualTo(projectName);

        BidPackageResponse getBidPackageProjectResp = QmsBidPackageResources.getBidPackage(bidPackageResponse.getIdentity(), BidPackageResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(getBidPackageProjectResp.getName()).isEqualTo(bidPackageName);
        softAssertions.assertThat(getBidPackageProjectResp.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageProjectResp.getProjects().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13370})
    @Description("Get bid package with invalid identity")
    public void getBidPackageWithInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.BID_PACKAGE, ApwErrorMessage.class)
            .inlineVariables("INVALID IDENTITY")
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ApwErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage())
            .contains("'bidPackageIdentity' is not a valid identity");
    }

    @AfterEach
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
