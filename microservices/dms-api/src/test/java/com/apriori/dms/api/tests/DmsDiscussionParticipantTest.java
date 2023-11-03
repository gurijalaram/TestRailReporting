package com.apriori.dms.api.tests;

import com.apriori.dms.api.models.response.DmsDiscussionParticipantsResponse;
import com.apriori.dms.api.utils.DmsApiTestDataUtils;
import com.apriori.dms.api.utils.DmsApiTestUtils;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class DmsDiscussionParticipantTest extends DmsApiTestDataUtils {
    private static String discussionDescription = StringUtils.EMPTY;

    @BeforeEach
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
