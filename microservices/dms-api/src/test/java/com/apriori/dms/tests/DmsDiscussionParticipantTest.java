package com.apriori.dms.tests;

import com.apriori.testrail.TestRail;

import entity.response.DmsDiscussionParticipantsResponse;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import utils.DmsApiTestDataUtils;
import utils.DmsApiTestUtils;

public class DmsDiscussionParticipantTest extends DmsApiTestDataUtils {
    private static String discussionDescription = StringUtils.EMPTY;

    @Before
    public void testSetup() {
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
    }

    @Test
    @TestRail(id = {13167})
    @Description("get discussion participants")
    public void getDiscussionParticipants() {
        DmsDiscussionParticipantsResponse responseWrapper = DmsApiTestUtils.getDiscussionParticipants(dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }
}
