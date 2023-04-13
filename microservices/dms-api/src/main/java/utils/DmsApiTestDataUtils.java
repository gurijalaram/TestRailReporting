package utils;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsScenarioDiscussionResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

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
    protected static UserCredentials currentUser = UserUtil.getUser();

    /**
     * Create test data.
     */
    @BeforeClass
    public static void createTestData() {
        softAssertions = new SoftAssertions();
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
                    bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
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
                                        softAssertions.fail("Create DMS discussion comment view failed");
                                    }
                                } else {
                                    softAssertions.fail("Create DMS discussion comment failed");
                                }
                            } else {
                                softAssertions.fail("Get DMS discussion failed");
                            }
                        } else {
                            softAssertions.fail("Create QMS discussion failed");
                        }
                    } else {
                        softAssertions.fail("Create QMS Bidpackage Project failed");
                    }
                } else {
                    softAssertions.fail("Create QMS Bidpackage Item failed");
                }
            } else {
                softAssertions.fail("Create QMS Bidpackage failed");
            }
        } else {
            softAssertions.fail("Create & Publish Scenario via cid-app failed");
        }
    }

    @AfterClass
    public static void deleteTestData() {
        softAssertions.assertAll();
        if (scenarioItem != null) {
            deleteScenarioViaCidApp();
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
                SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)
            .get(0);
        if (scenarioItem != null) {
            componentInfoBuilder.setComponentIdentity(scenarioItem.getComponentIdentity());
            componentInfoBuilder.setScenarioIdentity(scenarioItem.getScenarioIdentity());
            new ScenariosUtil().publishScenario(componentInfoBuilder, ScenarioResponse.class, HttpStatus.SC_CREATED);
        }
        return scenarioItem;
    }

    /**
     * Delete scenario via Cid-app
     */
    private static void deleteScenarioViaCidApp() {
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }

    @Before
    public void testSetup() {
        Assume.assumeTrue(softAssertions.wasSuccess());
    }

    @After
    public void after() {
        softAssertions.assertAll();
    }
}


