package com.apriori.dms.api.utils;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static org.junit.jupiter.api.Assertions.fail;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.dms.api.models.response.DmsCommentResponse;
import com.apriori.dms.api.models.response.DmsCommentViewResponse;
import com.apriori.dms.api.models.response.DmsScenarioDiscussionResponse;
import com.apriori.qms.api.controller.QmsBidPackageResources;
import com.apriori.qms.api.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.api.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.api.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;

public abstract class DmsApiTestDataUtils extends TestUtil {
    protected static SoftAssertions softAssertions;
    protected static String bidPackageName;
    protected static String projectName;
    protected static String contentDesc = StringUtils.EMPTY;
    protected static BidPackageResponse bidPackageResponse;
    protected static BidPackageItemResponse bidPackageItemResponse;
    protected static BidPackageProjectResponse bidPackageProjectResponse;
    protected static DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    protected static ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    protected static DmsCommentResponse dmsCommentResponse;
    protected static DmsCommentViewResponse dmsCommentViewResponse;
    protected static ScenarioItem scenarioItem;
    protected static UserCredentials currentUser;
    private static SoftAssertions softAssertionsTestData;
    private static ComponentInfoBuilder componentInfoBuilder;

    /**
     * Create test data.
     */
    @BeforeAll
    @Description("Create Test Data")
    public static void createTestData() {
        try {
            currentUser = UserUtil.getUser("admin");
            softAssertionsTestData = new SoftAssertions();
            componentInfoBuilder = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING);
            bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
            projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
            contentDesc = RandomStringUtils.randomAlphabetic(12);
            componentInfoBuilder = new ScenariosUtil().postAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING, currentUser));
            scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
                    SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName()).get(0);
            if (scenarioItem != null) {
                bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
                bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
                    QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
                    bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);
                bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
                qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
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
            } else {
                softAssertionsTestData.fail("Create & Publish Scenario via cid-app failed");
            }
        } catch (Exception | AssertionError e) {
            softAssertionsTestData.fail(e.getMessage());
        }
    }

    @AfterAll
    @Description("Delete Test Data")
    public static void deleteTestData() {
        try {
            if (bidPackageResponse != null) {
                QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
            }
            if (scenarioItem != null) {
                deleteScenarioViaCidApp();
            }
        } catch (Exception e) {
            softAssertionsTestData.fail(e.getMessage());
        } finally {
            clearEntities();
        }
    }

    /**
     * Delete scenario via Cid-app
     */
    private static void deleteScenarioViaCidApp() {
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }

    private static void checkAllureTestDataError() {
        if (!softAssertionsTestData.wasSuccess()) {
            fail(softAssertionsTestData.errorsCollected().toString());
        }
    }

    private static void clearEntities() {
        scenarioItem = null;
        bidPackageResponse = null;
        bidPackageItemResponse = null;
        bidPackageProjectResponse = null;
    }

    @BeforeEach
    public void beforeTest() {
        softAssertions = new SoftAssertions();
        checkAllureTestDataError();
    }

    @AfterEach
    public void after() {
        softAssertions.assertAll();
    }
}