package utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.enums.CssSearch;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.util.concurrent.TimeUnit;

public abstract class QmsApiTestDataUtils extends TestUtil {
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
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        contentDesc = RandomStringUtils.randomAlphabetic(12);
        scenarioItem = createAndPublishComponentViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        discussionCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), contentDesc, "ACTIVE", currentUser);
    }

    @AfterClass
    public static void deleteTestData() {
        QmsScenarioDiscussionResources.deleteScenarioDiscussion(scenarioDiscussionResponse.getIdentity(), currentUser);
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        deleteScenarioViaCidApp(scenarioItem, currentUser);
    }

    /**
     * Create Component via Cid-app
     *
     * @param processGroupEnum - ProcessGroupEnum
     * @param componentName    - component name
     * @param currentUser      - UserCredentials
     * @return ScenarioItem object
     */
    public static ScenarioItem createAndPublishComponentViaCidApp(ProcessGroupEnum processGroupEnum, String componentName, UserCredentials currentUser) {
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();
        new ComponentsUtil().postComponent(componentInfoBuilder);
        //Delay to sync till component is created
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ScenarioItem scenarioItem = new CssComponent().getBaseCssComponents(currentUser,
            CssSearch.COMPONENT_NAME_EQ.getKey() + "Casting",
            CssSearch.SCENARIO_NAME_EQ.getKey() + scenarioName).get(0);
        componentInfoBuilder.setComponentIdentity(scenarioItem.getComponentIdentity());
        componentInfoBuilder.setScenarioIdentity(scenarioItem.getScenarioIdentity());
        new ScenariosUtil().publishScenario(componentInfoBuilder, ScenarioResponse.class, HttpStatus.SC_CREATED);
        return scenarioItem;
    }

    /**
     * Delete scenario via Cid-app
     *
     * @param scenarioItem the scenario item
     * @param currentUser  the current user
     */
    public static void deleteScenarioViaCidApp(ScenarioItem scenarioItem, UserCredentials currentUser) {
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }

}
