package com.apriori;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ErrorMessage;
import com.apriori.qds.controller.BidPackageResources;
import com.apriori.qds.enums.QDSAPIEnum;
import com.apriori.qds.models.request.bidpackage.BidPackageParameters;
import com.apriori.qds.models.request.bidpackage.BidPackageRequest;
import com.apriori.qds.models.response.bidpackage.BidPackageResponse;
import com.apriori.qds.models.response.bidpackage.BidPackagesResponse;
import com.apriori.qds.utils.QdsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class BidPackageTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static String userContext;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        bidPackageResponse = BidPackageResources.createBidPackage(bidPackageName, userContext);
    }

    @Test
    @TestRail(id = {13312})
    @Description("Create Bid Package")
    public void createBidPackage() {
        softAssertions.assertThat(bidPackageResponse.getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(id = {13693})
    @Description("Create Bid Package with existing name")
    public void createBidPackageWithExistingName() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = QdsApiTestUtils.getBidPackageRequest(userIdentity, bidPackageName, bidPackageName);
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CONFLICT);
        ResponseWrapper<ErrorMessage> bidPackageErrorResponse = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(bidPackageErrorResponse.getResponseEntity().getMessage()).contains("already exists for Customer");
    }

    @Test
    @TestRail(id = {13313})
    @Description("Create Bid Package greater than 64 characters")
    public void createBidPackageNameMoreThan64() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = QdsApiTestUtils.getBidPackageRequest(userIdentity, RandomStringUtils.randomAlphabetic(70), "descripton");
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 64 characters");
    }

    @Test
    @TestRail(id = {13710, 13331, 13332})
    @Description("Create Bid Package is equal to 64 characters, delete bid package and verify bid package is deleted")
    public void createBidPackageNameEqualTo64() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = QdsApiTestUtils.getBidPackageRequest(userIdentity, RandomStringUtils.randomAlphabetic(64), "descripton");
        ResponseWrapper<BidPackageResponse> bidPackageCreatedResponse = HTTPRequest.build(
                RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
                    .body(bidPackageRequest)
                    .headers(QdsApiTestUtils.setUpHeader())
                    .apUserContext(userContext)
                    .expectedResponseCode(HttpStatus.SC_CREATED))
            .post();
        softAssertions.assertThat(bidPackageCreatedResponse.getResponseEntity().getAssignedTo())
            .isEqualTo(userIdentity);
        ResponseWrapper<String> deleteBidResponse = BidPackageResources.deleteBidPackage(bidPackageCreatedResponse.getResponseEntity()
            .getIdentity(), currentUser);
        ResponseWrapper<ErrorMessage> bidPackageResponse = HTTPRequest.build(
                RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, ErrorMessage.class)
                    .inlineVariables(bidPackageCreatedResponse.getResponseEntity().getIdentity())
                    .headers(QdsApiTestUtils.setUpHeader())
                    .apUserContext(userContext)
                    .expectedResponseCode(HttpStatus.SC_NOT_FOUND))
            .get();

        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getMessage())
            .contains("Can't find bidPackage with identity '" + bidPackageCreatedResponse.getResponseEntity()
                .getIdentity() + "'");
    }

    @Test
    @TestRail(id = {13636})
    @Description("Create Bid Package description is equal to 254 characters")
    public void createBidPackageNameEqualTo254() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = QdsApiTestUtils.getBidPackageRequest(userIdentity,RandomStringUtils.randomAlphabetic(15),RandomStringUtils.randomAlphabetic(254));
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackageResponse.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_CREATED);
        ResponseWrapper<BidPackageResponse> bidPackageAssignedResponse = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(bidPackageAssignedResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);
        BidPackageResources.deleteBidPackage(bidPackageAssignedResponse.getResponseEntity().getIdentity(), currentUser);
    }

    @Test
    @TestRail(id = {13325})
    @Description("Create Bid Package description is greater 254 characters")
    public void createBidPackageNameGreaterThan254() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        BidPackageRequest bidPackageRequest = QdsApiTestUtils.getBidPackageRequest(userIdentity, bidPackageName, RandomStringUtils.randomAlphabetic(260));
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("should not be more than 254 characters");
    }

    @Test
    @TestRail(id = {13350})
    @Description("Create Bid Package with empty name")
    public void createBidPackageWithEmptyName() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("TEST")
                .name("")
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'name' should not be null");
    }

    @Test
    @TestRail(id = {13349})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyDescription() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description("")
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("NEW")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'description' should not be null");
    }

    @Test
    @TestRail(id = {13823})
    @Description("Create Bid Package with empty Description")
    public void createBidPackageWithEmptyStatus() {
        BidPackageRequest bidPackageRequest = BidPackageRequest.builder()
            .bidPackage(BidPackageParameters.builder()
                .description(RandomStringUtils.randomAlphabetic(15))
                .name(RandomStringUtils.randomAlphabetic(10))
                .status("")
                .build())
            .build();

        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, ErrorMessage.class)
            .body(bidPackageRequest)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).post();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'status' should not be null");
    }

    @Test
    @TestRail(id = {13326})
    @Description("Updated existing Bid Package status")
    public void updateBidPackageStatus() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getName())
                    .description(bidPackageResponse.getDescription())
                    .status("ASSIGNED")
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getStatus()).isEqualTo("ASSIGNED");
    }

    @Test
    @TestRail(id = {13872})
    @Description("Updated existing Bid Package description")
    public void updateBidPackageDescription() {
        String bpDesc = RandomStringUtils.randomAlphabetic(15);
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(bidPackageResponse.getName())
                    .description(bpDesc)
                    .status(bidPackageResponse.getStatus())
                    .assignedTo(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getDescription()).isEqualTo(bpDesc);
    }

    @Test
    @TestRail(id = {13693})
    @Description("Updated existing Bid Package name")
    public void updateBidPackageAssigned() {
        String userIdentity = new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .body(BidPackageRequest.builder()
                .bidPackage(BidPackageParameters.builder()
                    .name(RandomStringUtils.randomAlphabetic(10))
                    .description(bidPackageResponse.getDescription())
                    .status(bidPackageResponse.getStatus())
                    .assignedTo(userIdentity)
                    .build())
                .build())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageUpdateResponse = HTTPRequest.build(requestEntity).patch();

        softAssertions.assertThat(bidPackageUpdateResponse.getResponseEntity().getAssignedTo()).isEqualTo(userIdentity);
    }

    @Test
    @TestRail(id = {13327, 13873, 13409})
    @Description("Find List of bid packages, pagination from the user for same customer")
    public void getBidPackages() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGES, BidPackagesResponse.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackagesResponse> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getIsFirstPage()).isTrue();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getPageNumber()).isEqualTo(1);
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {13408})
    @Description("Find bid package from other customer identity")
    public void getBidPackagesFromAnotherCustomer() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext("testUser1@widgets.aprioritest.com");
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, ErrorMessage.class)
            .headers(QdsApiTestUtils.setUpHeader())
            .inlineVariables(bidPackageResponse.getIdentity())
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_NOT_FOUND);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).get();
        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage())
            .contains(String.format("Can't find bidPackage with identity '%s' for customerIdentity", bidPackageResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {13329})
    @Description("Get bid package")
    public void getBidPackage() {
        String otherUserContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, BidPackageResponse.class)
            .inlineVariables(bidPackageResponse.getIdentity())
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(otherUserContext)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<BidPackageResponse> bidPackageResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackageResponse.getResponseEntity().getName()).isEqualTo(bidPackageName);
    }

    @Test
    @TestRail(id = {13333})
    @Description("Get bid package with invalid identity")
    public void getBidPackageWithInvalidIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(QDSAPIEnum.BID_PACKAGE, ErrorMessage.class)
            .inlineVariables("INVALID IDENTITY")
            .headers(QdsApiTestUtils.setUpHeader())
            .apUserContext(userContext)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);

        ResponseWrapper<ErrorMessage> bidPackagesResponse = HTTPRequest.build(requestEntity).get();

        softAssertions.assertThat(bidPackagesResponse.getResponseEntity().getMessage()).contains("'identity' is not a valid identity");
    }

    @AfterEach
    public void testCleanup() {
        ResponseWrapper<String> deleteBidResponse = BidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), currentUser);
        softAssertions.assertThat(deleteBidResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
