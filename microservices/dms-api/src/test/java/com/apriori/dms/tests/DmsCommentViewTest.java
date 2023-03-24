package com.apriori.dms.tests;


import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsCommentViewsResponse;
import entity.response.DmsScenarioDiscussionResponse;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestUtils;

public class DmsCommentViewTest extends TestUtil {

    private static String userContext;
    private static SoftAssertions softAssertions;
    private static String contentDesc = StringUtils.EMPTY;
    private static ScenarioItem scenarioItem;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static DmsCommentResponse dmsCommentResponse;
    private static DmsCommentViewResponse dmsCommentViewResponse;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());

        //Create scenario discussion on QMS
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        softAssertions.assertThat(scenarioItem.getComponentIdentity()).isNotNull();
        qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);

        //Get generic DMS discussion identity from QMS discussion
        dmsScenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(DmsScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse);
        dmsCommentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        dmsCommentViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserCustomerIdentity(),
            dmsCommentResponse.getCommentView().get(0).getParticipantIdentity(),
            currentUser);
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
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
