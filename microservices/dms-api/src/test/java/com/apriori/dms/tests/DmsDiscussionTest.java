package com.apriori.dms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.response.DmsDiscussionResponse;
import entity.response.DmsDiscussionsResponse;
import enums.DMSApiEnum;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsDiscussionTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DmsDiscussionResponse> discussionResponse;
    UserCredentials currentUser = UserUtil.getUserWithCloudContext();
    private static String discussionDescription = StringUtils.EMPTY;


    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        discussionResponse = DmsApiTestUtils.createDiscussion(discussionDescription, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13052"})
    @Description("create a valid discussion")
    public void createDiscussions() {
        softAssertions.assertThat(discussionResponse.getResponseEntity().getDescription()).isEqualTo(discussionDescription);
    }

    @Test
    @TestRail(testCaseId = {"13054"})
    @Description("update a valid discussion")
    public void updateValidDiscussion() {
        ResponseWrapper<DmsDiscussionResponse> discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED", discussionResponse.getResponseEntity().getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getStatus()).isEqualTo("RESOLVED");
    }

    @Test
    @TestRail(testCaseId = {"13053"})
    @Description("get list of all discussions")
    public void getDiscussions() {
        ResponseWrapper<DmsDiscussionsResponse> discussionUpdateResponse = DmsApiTestUtils.getDiscussions(currentUser);

        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getItems().size()).isGreaterThan(0);
    }

    @Test
    @Ignore
    @Description("update a invalid discussion")
    public void UpdateInValidDiscussion() {
        ResponseWrapper<ErrorMessage> discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED", "INVALIDDISCUSSION", currentUser, ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getMessage()).contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"14217"})
    @Description("update a discussion description")
    public void updateDiscussionDescription() {
        String discussionDesc = RandomStringUtils.randomAlphabetic(12);
        ResponseWrapper<DmsDiscussionResponse> discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDesc,
            "ACTIVE", discussionResponse.getResponseEntity().getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getDescription()).isEqualTo(discussionDesc);
    }

    @Test
    @Description("Create Discussion with invalid Customer Identity")
    public void createDiscussionWithInvalidCustomer() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .description(RandomStringUtils.randomAlphabetic(12))
                .build())
            .build();

        ResponseWrapper<ErrorMessage> errorMessageResponseWrapper = HTTPRequest.build(RequestEntityUtil
                .init(DMSApiEnum.CUSTOMER_DISCUSSIONS, ErrorMessage.class)
                .inlineVariables("INVALIDCUSTOMER")
                .headers(DmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body(discussionsRequest)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST))
            .post();

        softAssertions.assertThat(errorMessageResponseWrapper.getResponseEntity().getMessage()).contains("'customerIdentity' is not a valid identity");
    }


    @After
    public void testCleanup() {
        //    DmsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
