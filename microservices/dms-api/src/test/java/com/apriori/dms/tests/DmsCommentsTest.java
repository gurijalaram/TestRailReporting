package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.response.DiscussionResponse;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentsResponse;
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

public class DmsCommentsTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static ResponseWrapper<DiscussionResponse> discussionResponse;
    private static ResponseWrapper<DmsCommentResponse> commentResponse;
    private static String contentDesc = StringUtils.EMPTY;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        discussionResponse = DmsApiTestUtils.createDiscussion(contentDesc, currentUser);
        commentResponse = DmsApiTestUtils.createDiscussionComment(currentUser, contentDesc, discussionResponse.getResponseEntity().getIdentity());
    }

    @Test
    @Description("Create a valid comment")
    public void createComment() {
        softAssertions.assertThat(commentResponse.getResponseEntity().getContent()).isEqualTo(contentDesc);
    }

    @Test
    @Description("get a valid comments")
    public void getComments() {
        ResponseWrapper<DmsCommentsResponse> responseWrapper = DmsApiTestUtils.getDiscussionComments(currentUser,
            discussionResponse.getResponseEntity().getIdentity());
        softAssertions.assertThat(responseWrapper.getResponseEntity().items.size()).isGreaterThan(0);
    }

    @Test
    @Description("get a valid comment")
    public void getComment() {
        ResponseWrapper<DmsCommentResponse> responseWrapper = DmsApiTestUtils.getDiscussionComment(currentUser,
            discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity());
        softAssertions.assertThat(responseWrapper.getResponseEntity().getIdentity()).isNotNull();
    }

    @Test
    @Description("update a valid comment")
    public void updateComment() {
        ResponseWrapper<DmsCommentResponse> commentUpdateResponse = DmsApiTestUtils.updateComment(commentResponse.getResponseEntity().getStatus(),
            discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(), currentUser);

        softAssertions.assertThat(commentUpdateResponse.getResponseEntity().getMentionedUsers().size()).isGreaterThan(0);
    }

    @After
    public void testCleanup() {
        DmsApiTestUtils.deleteComment(discussionResponse.getResponseEntity().getIdentity(), commentResponse.getResponseEntity().getIdentity(), currentUser);
        DdsApiTestUtils.deleteDiscussion(discussionResponse.getResponseEntity().getIdentity(), userContext);
        softAssertions.assertAll();
    }
}
