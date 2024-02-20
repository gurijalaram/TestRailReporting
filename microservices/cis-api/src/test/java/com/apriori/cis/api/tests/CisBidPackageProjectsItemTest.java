package com.apriori.cis.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisBidPackageProjectItemResources;
import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisBidPackageResources;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.models.response.bidpackage.GetBidPackageProjectItemsResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CisBidPackageProjectsItemTest extends CISTestUtil {

    private SoftAssertions softAssertions;
    private BidPackageResponse bidPackageResponse;
    private BidPackageProjectResponse bidPackageProjectResponse;
    private UserCredentials currentUser;
    private ScenarioItem scenarioItem;
    private ComponentInfoBuilder componentInfoBuilder;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        String projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        currentUser = UserUtil.getUser();
        componentInfoBuilder = new ScenariosUtil().uploadAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING, currentUser));
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName()).get(0);
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
    }

    @Test
    @TestRail(id = {24046})
    @Description("Create Project Item with valid inputs")
    public void createValidBidPackageProjectItemValid() {
        BidPackageProjectItemsResponse bppItemResponse = CisBidPackageProjectItemResources.createBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), scenarioItem, BidPackageProjectItemsResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppItemResponse.getProjectItems().get(0).getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {14093})
    @Description("Get Project Items with valid data")
    public void getValidBidPackageProjectItems() {
        GetBidPackageProjectItemsResponse getBidPackageProjectItemsResponse = CisBidPackageProjectItemResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            GetBidPackageProjectItemsResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getProjectItems().size()).isEqualTo(0);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(id = {14412})
    @Description("Get Project Items with in-valid data")
    public void getInvalidBidPackageProjectItems() {
        CisErrorMessage getBidPackageProjectItemsResponse = CisBidPackageProjectItemResources.getBidPackageProjectItems(
            "INVALID_BP_ID",
            "INVALID_BPP_ID",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getMessage())
            .contains("2 validation failures were found:* 'projectIdentity' is not a valid identity.* 'bidPackageIdentity' is not a valid identity.");
    }

    @AfterEach
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        softAssertions.assertAll();
    }
}
