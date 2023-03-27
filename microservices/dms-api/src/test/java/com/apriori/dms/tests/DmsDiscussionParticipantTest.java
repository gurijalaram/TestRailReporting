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

import java.util.HashSet;

public class DmsDiscussionParticipantTest extends SDSTestUtil {
    private static SoftAssertions softAssertions;
    private static String discussionDescription = StringUtils.EMPTY;
    private static String bidPackageName;
    private static String projectName;
    private static ScenarioItem scenarioItem;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    private static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    private static final UserCredentials currentUser = testingUser;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        discussionDescription = RandomStringUtils.randomAlphabetic(12);
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();

        // Create new Component and published Scenario via SDS
        scenarioItem = postTestingComponentAndAddToRemoveList();
        publishAssembly(ComponentInfoBuilder.builder().scenarioName(scenarioItem.getScenarioName()).user(testingUser)
            .componentIdentity(scenarioItem.getComponentIdentity()).scenarioIdentity(scenarioItem.getScenarioIdentity())
            .build(), Scenario.class, HttpStatus.SC_OK);

        //Create new bid-package & project
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);

        //Create scenario discussion on QMS
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
        if (otherUser.getEmail().equals(currentUser.getEmail())) {
            otherUser = UserUtil.getUser();
        }
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
        //Delete Scenario Discussion
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);

        //Delete Bidpackage and Scenario
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        if (!scenariosToDelete.isEmpty()) {
            scenariosToDelete.forEach(component -> {
                removeTestingScenario(component.getComponentIdentity(), component.getScenarioIdentity());
            });
        }
        scenariosToDelete = new HashSet<>();
        softAssertions.assertAll();
    }
}
