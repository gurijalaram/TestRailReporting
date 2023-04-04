package utils;

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
import com.apriori.utils.reader.file.user.UserCredentials;
import entity.response.DmsCommentResponse;
import entity.response.DmsCommentViewResponse;
import entity.response.DmsScenarioDiscussionResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

public class DmsApiTestDataUtils extends SDSTestUtil {
    protected String bidPackageName;
    protected String projectName;
    protected String contentDesc = StringUtils.EMPTY;
    protected ScenarioItem scenarioItem;
    protected BidPackageResponse bidPackageResponse;
    protected BidPackageItemResponse bidPackageItemResponse;
    protected BidPackageProjectResponse bidPackageProjectResponse;
    protected DmsScenarioDiscussionResponse dmsScenarioDiscussionResponse;
    protected ScenarioDiscussionResponse qmsScenarioDiscussionResponse;
    protected DmsCommentResponse dmsCommentResponse;
    protected DmsCommentViewResponse dmsCommentViewResponse;
    protected final UserCredentials currentUser = testingUser;

    protected void createTestData() {
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        contentDesc = RandomStringUtils.randomAlphabetic(12);

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
        dmsCommentResponse = DmsApiTestUtils.addCommentToDiscussion(currentUser, contentDesc, dmsScenarioDiscussionResponse.getItems()
            .get(0).getIdentity(), DmsCommentResponse.class, HttpStatus.SC_CREATED);
        dmsCommentViewResponse = DmsApiTestUtils.markCommentViewAsRead(
            dmsScenarioDiscussionResponse.getItems().get(0).getIdentity(),
            dmsCommentResponse.getIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserIdentity(),
            dmsCommentResponse.getCommentView().get(0).getUserCustomerIdentity(),
            dmsCommentResponse.getCommentView().get(0).getParticipantIdentity(),
            currentUser);
    }

}
