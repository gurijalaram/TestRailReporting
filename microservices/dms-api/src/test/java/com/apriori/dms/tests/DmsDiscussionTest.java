package com.apriori.dms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.ErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.response.DmsDiscussionResponse;
import entity.response.DmsDiscussionsResponse;
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

public class DmsDiscussionTest extends TestUtil {
    private static SoftAssertions softAssertions;
    private static String discussionDescription = StringUtils.EMPTY;
    private static ScenarioItem scenarioItem;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
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
    @TestRail(testCaseId = {"13054", "14217"})
    @Description("update a valid discussion")
    public void UpdateValidDiscussion() {
        String description = new GenerateStringUtil().generateNotes();
        DmsDiscussionResponse discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(description, "ACTIVE", dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), currentUser, DmsDiscussionResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(discussionUpdateResponse.getStatus()).isEqualTo("ACTIVE");
        softAssertions.assertThat(discussionUpdateResponse.getDescription()).isEqualTo(description);
    }

    @Test
    @Description("update a invalid discussion")
    public void UpdateInValidDiscussion() {
        ErrorMessage discussionUpdateResponse = DmsApiTestUtils.updateDiscussion(discussionDescription, "RESOLVED", "INVALIDDISCUSSION", currentUser, ErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(discussionUpdateResponse.getMessage()).contains("'discussionIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"13053", "14223"})
    @Description("get list of all discussion and verify pagination")
    public void getDiscussions() {
        DmsDiscussionsResponse responseWrapper = DmsApiTestUtils.getDiscussions(DmsDiscussionsResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(responseWrapper.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(responseWrapper.getIsFirstPage()).isTrue();
    }

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
