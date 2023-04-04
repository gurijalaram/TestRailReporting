package com.apriori.dms.tests;


import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.sds.entity.response.Scenario;
import com.apriori.sds.util.SDSTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

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
import utils.DmsApiTestDataUtils;
import utils.DmsApiTestUtils;

import java.util.HashSet;

public class DmsCommentViewTest extends DmsApiTestDataUtils {
    private static String userContext;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        userContext = new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail());
        createTestData();
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
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
