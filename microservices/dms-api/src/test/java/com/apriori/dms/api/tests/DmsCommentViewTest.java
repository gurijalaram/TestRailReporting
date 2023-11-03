package com.apriori.dms.api.tests;

import com.apriori.dms.api.models.response.DmsCommentViewResponse;
import com.apriori.dms.api.models.response.DmsCommentViewsResponse;
import com.apriori.dms.api.utils.DmsApiTestDataUtils;
import com.apriori.dms.api.utils.DmsApiTestUtils;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class DmsCommentViewTest extends DmsApiTestDataUtils {
    private static String userContext;

    @BeforeEach
    public void testSetup() {
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
    }

    @Test
    @TestRail(id = {15478, 15481})
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
    @TestRail(id = {15479})
    @Description("Find comment views")
    public void getCommentViews() {
        DmsCommentViewsResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentViews(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {15480})
    @Description("get a comment view by identity")
    public void getCommentView() {
        DmsCommentViewResponse responseWrapper = DmsApiTestUtils.getDiscussionCommentView(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(), dmsCommentResponse.getIdentity(),
            dmsCommentViewResponse.getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getIdentity()).isNotNull();
    }
}
