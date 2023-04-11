package com.apriori.dms.tests;


import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;

import entity.response.DmsCommentViewResponse;
import entity.response.DmsCommentViewsResponse;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestDataUtils;
import utils.DmsApiTestUtils;

public class DmsCommentViewTest extends DmsApiTestDataUtils {
    private static String userContext;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
    }

    @Test
    @TestRail(testCaseId = {"15478", "15481"})
    @Description("Mark and Delete a comment as a viewed")
    public void markAndDeleteCommentView() {
        DmsCommentViewResponse markViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserCustomerIdentity(),
            dmsCommentResponse.getCommentView().get(0).getParticipantIdentity(),
            currentUser);
        softAssertions.assertThat(markViewResponse.getUserIdentity())
            .isEqualTo(dmsCommentResponse.getCommentView().get(0).getUserIdentity());

        DmsApiTestUtils.deleteCommentView(dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            markViewResponse.getIdentity(),
            currentUser);
    }

    @Test
    @TestRail(testCaseId = {"15479"})
    @Description("Find comment views")
    public void getCommentViews() {
        DmsCommentViewsResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentViews(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"15480"})
    @Description("get a comment view by identity")
    public void getCommentView() {
        DmsCommentViewResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentView(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), dmsCommentResponse.getIdentity(),
            dmsCommentViewResponse.getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }

    @After
    public void testCleanup() {
        softAssertions.assertAll();
    }
}
