package com.apriori.dms.tests;

import com.apriori.utils.ApwErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;

import entity.response.DmsDiscussionResponse;
import entity.response.DmsDiscussionsResponse;
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

public class DmsDiscussionTest extends DmsApiTestDataUtils {
    private static String discussionDescription = StringUtils.EMPTY;

    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
    }

    @Test
    @TestRail(testCaseId = {"13054", "14217"})
    @Description("update a valid discussion")
    public void updateValidDiscussion() {
        String description = new GenerateStringUtil().generateNotes();
        DmsDiscussionResponse discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(description, "ACTIVE", dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(discussionUpdateResponse.getDescription()).isEqualTo(description);
    }

    @Test
    @Description("update a invalid discussion")
    public void updateInValidDiscussion() {
        ApwErrorMessage discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription, "RESOLVED", "INVALIDDISCUSSION", currentUser, ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(discussionUpdateResponse.getMessage())
            .contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"13053", "14223"})
    @Description("get list of all discussion and verify pagination")
    public void getDiscussions() {
        DmsDiscussionsResponse responseWrapper = DmsApiTestUtils.getDiscussions(DmsDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }
}
