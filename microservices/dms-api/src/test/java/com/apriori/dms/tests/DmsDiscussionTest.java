package com.apriori.dms.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.authorization.response.ApwErrorMessage;
import com.apriori.testrail.TestRail;

import entity.request.DiscussionsRequest;
import entity.request.DiscussionsRequestParameters;
import entity.response.DmsDiscussionResponse;
import entity.response.DmsDiscussionsResponse;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestDataUtils;
import utils.DmsApiTestUtils;

public class DmsDiscussionTest extends DmsApiTestDataUtils {
    private static String discussionDescription = StringUtils.EMPTY;

    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
    }

    @Test
    @TestRail(id = {14217})
    @Description("Verify that user can Update description of Discussion")
    public void updateValidDiscussionDescription() {
        String description = new GenerateStringUtil().generateNotes();
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .description(description)
                .build())
            .build();
        DmsDiscussionResponse discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionsRequest, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getDescription()).isEqualTo(description);
    }

    @Test
    @TestRail(id = {13054})
    @Description("Verify that user can Update status of Discussion")
    public void updateValidDiscussionStatus() {
        DiscussionsRequest discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("RESOLVED")
                .build())
            .build();
        DmsDiscussionResponse discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionsRequest, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getStatus()).isEqualTo("RESOLVED");

        discussionsRequest = DiscussionsRequest.builder()
            .discussion(DiscussionsRequestParameters.builder()
                .status("ACTIVE")
                .build())
            .build();
        discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionsRequest, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @Description("update a invalid discussion")
    public void updateInValidDiscussion() {
        ApwErrorMessage discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription, "RESOLVED", "INVALIDDISCUSSION", currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(discussionUpdateResponse.getMessage())
            .contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(id = {13053, 14223})
    @Description("get list of all discussion and verify pagination")
    public void getDiscussions() {
        DmsDiscussionsResponse responseWrapper = DmsApiTestUtils.getDiscussions(DmsDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }
}
