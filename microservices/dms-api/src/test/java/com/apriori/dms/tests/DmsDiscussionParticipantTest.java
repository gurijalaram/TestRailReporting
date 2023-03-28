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
        scenarioItem = postTestingComponentAndAddToRemoveList();
        publishAssembly(ComponentInfoBuilder.builder().scenarioName(scenarioItem.getScenarioName()).user(testingUser)
            .componentIdentity(scenarioItem.getComponentIdentity()).scenarioIdentity(scenarioItem.getScenarioIdentity())
            .build(), Scenario.class, HttpStatus.SC_OK);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
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

    @After
    public void testCleanup() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(qmsScenarioDiscussionResponse.getIdentity(), currentUser);
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
