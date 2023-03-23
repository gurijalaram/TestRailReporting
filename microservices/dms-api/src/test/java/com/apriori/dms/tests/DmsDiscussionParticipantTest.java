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

import entity.response.DmsDiscussionParticipantsResponse;
import entity.response.DmsErrorMessageResponse;
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

public class DmsDiscussionParticipantTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static ScenarioItem scenarioItem;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static String discussionDescription = StringUtils.EMPTY;
    UserCredentials currentUser;

    @Before
    public void testSetup() {
        currentUser = UserUtil.getUser();
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        softAssertions = new SoftAssertions();

        //Create scenario discussion on QMS
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        softAssertions.assertThat(scenarioItem.getComponentIdentity()).isNotNull();
        qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);

        //Get generic DMS discussion identity from QMS discussion
        dmsScenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(DmsScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse);
    }

    @Test
    @TestRail(testCaseId = {"13167"})
    @Description("get discussion participants")
    public void getDiscussionParticipants() {
        DmsDiscussionParticipantsResponse responseWrapper = DmsApiTestUtils.getDiscussionParticipants(dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser);

        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"16444"})
    @Description("Verify that only discussion's participants can change its status")
    public void verifyDiscussionParticipantCanChangeStatus() {
        UserCredentials otherUser = UserUtil.getUser();
        DmsErrorMessageResponse dmsErrorMessageResponse = DmsApiTestUtils
            .updateDiscussion(discussionDescription, "RESOLVED", dmsScenarioDiscussionResponse.getItems().get(0)
                .getIdentity(), otherUser, DmsErrorMessageResponse.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(dmsErrorMessageResponse.getErrorMessage())
            .contains("Participant with user identity '" +
                new AuthUserContextUtil().getAuthUserIdentity(otherUser.getEmail()) + "' is not a participant in discussion with identity '" +
                dmsScenarioDiscussionResponse.getItems().get(0).getIdentity() + "'");
    }

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
