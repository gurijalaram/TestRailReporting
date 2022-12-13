package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.request.DmsAttributesRequest;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentsResponse;
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
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsDiscussionTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static DmsDiscussionResponse discussionResponse;
    UserCredentials currentUser;
    private static String discussionDescription = StringUtils.EMPTY;


    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUserWithCloudContext();
        discussionResponse = DmsApiTestUtils.createDiscussion(discussionDescription, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"13052"})
    @Description("create a valid discussion")
    public void createDiscussions() {
        softAssertions.assertThat(discussionResponse.getDescription()).isEqualTo(discussionDescription);
    }

    @Test
    @TestRail(testCaseId = {"13054", "14217"})
    @Description("update a valid discussion")
    public void UpdateValidDiscussion() {
        String description = new GenerateStringUtil().generateNotes();
        DmsDiscussionResponse discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(description,
            "RESOLVED", discussionResponse.getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(discussionUpdateResponse.getStatus()).isEqualTo("RESOLVED");
        softAssertions.assertThat(discussionUpdateResponse.getDescription()).isEqualTo(description);
    }


    @Test
    @Description("update a invalid discussion")
    public void UpdateInValidDiscussion() {
        ErrorMessage discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED", "INVALIDDISCUSSION", currentUser, ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(discussionUpdateResponse.getMessage()).contains("'discussionIdentity' is not a valid identity");
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

    @Test
    @TestRail(testCaseId = {"14221"})
    @Description("create a discussion with attributes")
    public void createDiscussionWithAttributes() {
        String desc = new GenerateStringUtil().getRandomString();
        DiscussionsRequest discussionsRequestBuilder = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .description(desc)
                .attributes(DmsAttributesRequest.builder()
                    .attribute("Monday")
                    .build())
                .status("ACTIVE")
                .build())
            .build();

        DmsDiscussionResponse cDWAResponse = DmsApiTestUtils.createDiscussion(discussionsRequestBuilder, currentUser);
        softAssertions.assertThat(cDWAResponse.getDescription()).isEqualTo(desc);
        softAssertions.assertThat(cDWAResponse.getAttributes().getAttribute()).isEqualTo("Monday");
    }

    @Test
    @TestRail(testCaseId = {"14221"})
    @Description("create a discussion with another assignee email address")
    public void updateDiscussionWithAssigneeEmail() {
        UserCredentials otherUser = UserUtil.getUser();
        String desc = new GenerateStringUtil().getRandomString();
        DiscussionsRequest discussionsRequestBuilder = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .description(desc)
                .assigneeEmail(otherUser.getEmail())
                .status("ACTIVE")
                .build())
            .build();

        DmsDiscussionResponse cDWAResponse = DmsApiTestUtils.updateDiscussion(discussionsRequestBuilder,
            discussionResponse.getIdentity(),
            currentUser,
            DmsDiscussionResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(cDWAResponse.getDescription()).isEqualTo(desc);
    }

    @Test
    @TestRail(testCaseId = {"13053", "14223"})
    @Description("get list of all discussion and verify pagination")
    public void getDiscussions() {
        DmsDiscussionsResponse responseWrapper = DmsApiTestUtils.getDiscussions(DmsDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }


    @After
    public void testCleanup() {
        DmsApiTestUtils.updateDiscussion(new GenerateStringUtil().generateNotes(),
            "RESOLVED", discussionResponse.getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertAll();
    }
}
