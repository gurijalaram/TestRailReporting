package com.apriori.dms.utils;

import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;
import static org.junit.jupiter.api.Assertions.fail;

import com.apriori.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.dms.models.response.DmsCommentResponse;
import com.apriori.dms.models.response.DmsCommentViewResponse;
import com.apriori.dms.models.response.DmsScenarioDiscussionResponse;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.component.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageResponse;
import com.apriori.qms.models.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
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

    /**
     * Create test data.
     */
    @BeforeAll
    @Description("Create Test Data")
    public static void createTestData() {
        try {
            currentUser = UserUtil.getUser("admin");
            softAssertionsTestData = new SoftAssertions();
            bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
            projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
            contentDesc = RandomStringUtils.randomAlphabetic(12);
            scenarioItem = createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
            if (scenarioItem != null) {
                bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
                if (bidPackageResponse != null) {
                    bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
                        QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
                        bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);
                    if (bidPackageItemResponse != null) {
                        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
                        if (bidPackageProjectResponse != null) {
                            qmsScenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
                            if (qmsScenarioDiscussionResponse != null) {
                                dmsScenarioDiscussionResponse = DmsApiTestUtils.getScenarioDiscussions(DmsScenarioDiscussionResponse.class, HttpStatus.SC_OK, currentUser, qmsScenarioDiscussionResponse);
                                if (dmsScenarioDiscussionResponse != null) {
                                    dmsCommentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, dmsScenarioDiscussionResponse.getItems()
                                        .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
                                    if (dmsCommentResponse != null) {
                                        dmsCommentViewResponse = DmsApiTestUtils.markCommentViewAsRead(
                                            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
                                            dmsCommentResponse.getIdentity(),
                                            dmsCommentResponse.getCommentView().get(0).getUserIdentity(),
                                            dmsCommentResponse.getCommentView().get(0).getUserCustomerIdentity(),
                                            dmsCommentResponse.getCommentView().get(0).getParticipantIdentity(),
                                            currentUser);
                                        if (dmsCommentViewResponse == null) {
                                            softAssertionsTestData.fail("Create DMS discussion comment view failed");
                                        }
                                    } else {
                                        softAssertionsTestData.fail("Create DMS discussion comment failed");
                                    }
                                } else {
                                    softAssertionsTestData.fail("Get DMS discussion failed");
                                }
                            } else {
                                softAssertionsTestData.fail("Create QMS discussion failed");
                            }
                        } else {
                            softAssertionsTestData.fail("Create QMS Bidpackage Project failed");
                        }
                    } else {
                        softAssertionsTestData.fail("Create QMS Bidpackage Item failed");
                    }
                } else {
                    softAssertionsTestData.fail("Create QMS Bidpackage failed");
                }
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
     * Create & Publish Scenario via Cid-app
     *
     * @param processGroupEnum - ProcessGroupEnum
     * @param componentName    - component name
     * @param currentUser      - UserCredentials
     * @return ScenarioItem object
     */
    private static ScenarioItem createAndPublishScenarioViaCidApp(ProcessGroupEnum processGroupEnum, String componentName, UserCredentials currentUser) {
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();
        new ComponentsUtil().postComponent(componentInfoBuilder);
        scenarioItem = new CssComponent().getWaitBaseCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
                SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName())
            .get(0);
        if (scenarioItem != null) {
            componentInfoBuilder.setComponentIdentity(scenarioItem.getComponentIdentity());
            componentInfoBuilder.setScenarioIdentity(scenarioItem.getScenarioIdentity());
            new ScenariosUtil().publishScenario(componentInfoBuilder, ScenarioResponse.class, HttpStatus.SC_CREATED);
            scenarioItem = new CssComponent().getWaitBaseCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
                    SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName())
                .get(0);
        }
        return scenarioItem;
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