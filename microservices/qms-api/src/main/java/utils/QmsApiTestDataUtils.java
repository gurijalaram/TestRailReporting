package utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class QmsApiTestDataUtils extends TestUtil {
    protected static SoftAssertions softAssertions;
    protected static String bidPackageName;
    protected static String projectName;
    protected static String contentDesc = StringUtils.EMPTY;
    protected static BidPackageResponse bidPackageResponse;
    protected static BidPackageItemResponse bidPackageItemResponse;
    protected static BidPackageProjectResponse bidPackageProjectResponse;
    protected static ScenarioDiscussionResponse scenarioDiscussionResponse;
    protected static DiscussionCommentResponse discussionCommentResponse;
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
        scenarioItem = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        if (scenarioItem != null) {
            bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
            if (bidPackageResponse != null) {
                bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
                    QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
                    bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);
                if (bidPackageItemResponse != null) {
                    bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
                    if (bidPackageProjectResponse != null) {
                        scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
                        if (scenarioDiscussionResponse != null) {
                            discussionCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), contentDesc, "ACTIVE", currentUser);
                            if (discussionCommentResponse == null) {
                                softAssertions.fail("Create QMS discussion comment failed");
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

    @AfterClass()
    public static void deleteTestData() {
        if (bidPackageResponse != null) {
            QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        }
        if (scenarioItem != null) {
            QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
        }
    }

    @Before
    public void beforeTest() {
        if (!softAssertions.wasSuccess()) {
            Assert.fail(softAssertions.errorsCollected().toString());
        }
    }

    @After
    public void afterTest() {
        softAssertions.assertAll();
    }

}
