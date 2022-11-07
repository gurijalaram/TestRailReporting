package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.response.DiscussionResponse;
import entity.response.DiscussionsResponse;
import enums.DMSApiEnum;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DdsApiTestUtils;
import utils.DmsApiTestUtils;

public class DmsDiscussionTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DiscussionResponse> discussionResponse;
    UserCredentials currentUser = UserUtil.getUserWithCloudContext();
    private static String discussionDescription = StringUtils.EMPTY;


    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        discussionResponse = DmsApiTestUtils.createDiscussion(discussionDescription, currentUser);
    }

    @Test
    @Description("create a valid discussion")
    public void createDiscussions() {
        softAssertions.assertThat(discussionResponse.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(discussionResponse.getResponseEntity().getDescription()).isEqualTo(discussionDescription);
    }

    @Test
    @Description("update a valid discussion")
    public void UpdateValidDiscussion() {
        ResponseWrapper<DiscussionResponse> discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED", discussionResponse.getResponseEntity().getIdentity(), currentUser, DiscussionResponse.class);

        softAssertions.assertThat(discussionUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getStatus()).isEqualTo("RESOLVED");
    }


    @Test
    @Description("update a invalid discussion")
    public void UpdateInValidDiscussion() {
        ResponseWrapper<ErrorMessage> discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription,
            "RESOLVED","INVALIDDISCUSSION", currentUser, ErrorMessage.class);

        softAssertions.assertThat(discussionUpdateResponse.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(discussionUpdateResponse.getResponseEntity().getMessage()).contains("'discussionIdentity' is not a valid identity");
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
                .body(discussionsRequest))
            .post();

        softAssertions.assertThat(errorMessageResponseWrapper.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(errorMessageResponseWrapper.getResponseEntity().getMessage()).contains("'customerIdentity' is not a valid identity");
    }


    @After
    public void testCleanup() {
        ResponseWrapper<String> discussionsResponse = DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()));
        softAssertions.assertThat(discussionsResponse.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        softAssertions.assertAll();
    }
}
