package com.apriori.cis.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.TestUtil;
import com.apriori.cisapi.controller.CisBidPackageProjectItemResources;
import com.apriori.cisapi.controller.CisBidPackageProjectResources;
import com.apriori.cisapi.controller.CisBidPackageResources;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.bidpackage.CisErrorMessage;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CisBidPackageProjectsItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static BidPackageProjectItemResponse bidPackageProjectItemResponse;
    private final UserCredentials currentUser = UserUtil.getUser();

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        String projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        ScenarioItem scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        /*
                bidPackageItemResponse = CisBidPackageResources.createBidPackageItem(
                    CisBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                        scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
                    bidPackageResponse.getIdentity(),
                    currentUser,
                    BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        */
        bidPackageProjectResponse = CisBidPackageProjectResources.createBidPackageProject(projectName, bidPackageResponse.getIdentity(),
            BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        /*
                bidPackageProjectItemResponse = CisBidPackageProjectItemResources.createBidPackageProjectItem(bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity(),
                    bidPackageProjectResponse.getIdentity(), BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED, currentUser);
        */
    }

    @Test
    @Ignore
    @TestRail(id = {14091})
    @Description("Create Project Item with valid inputs")
    public void createValidBidPackageProjectItemValid() {
        CisBidPackageProjectItemResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectItemResponse.getIdentity(), null, currentUser);
        BidPackageProjectItemResponse bppItemResponse = CisBidPackageProjectItemResources.createBidPackageProjectItem(bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(), BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppItemResponse.getBidPackageItem().getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @Ignore
    @TestRail(id = {14092})
    @Description("Create Project Item with valid inputs")
    public void createInvalidBidPackageProjectItem() {
        CisBidPackageProjectItemResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(), bidPackageProjectResponse.getIdentity(), bidPackageProjectItemResponse.getIdentity(), null, currentUser);
        BidPackageProjectItemResponse bppItemResponse = CisBidPackageProjectItemResources.createBidPackageProjectItem("INVALID_BP_ID", "INVALID_BPI_ID",
            "INVALID_BPP_ID", BidPackageProjectItemResponse.class, HttpStatus.SC_CREATED, currentUser);
        softAssertions.assertThat(bppItemResponse.getBidPackageItem().getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @Ignore
    @TestRail(id = {13487})
    @Description("Get Project Item")
    public void getBidPackageProjectItem() {
        BidPackageProjectItemsResponse getBidPackageProjectItemsResponse = CisBidPackageProjectItemResources.getBidPackageProjectItem(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getItems().get(0).getIdentity())
            .isEqualTo(bidPackageProjectItemResponse.getIdentity());
    }

    @Test
    @Ignore
    @TestRail(id = {14098})
    @Description("Delete valid Bid Package Project Item")
    public void deleteValidBidPackageProjectItem() {
        CisBidPackageProjectItemResources.deleteBidPackageProjectItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), bidPackageProjectItemResponse.getIdentity(), null, currentUser);
    }

    @Test
    @Ignore
    @TestRail(id = {14413})
    @Description("Delete In-valid Bid Package Project Item")
    public void deleteInValidBidPackageProjectItem() {
        CisErrorMessage cisErrorMessage = CisBidPackageProjectItemResources.deleteBidPackageProjectItem("INVALID_BP_ID",
            "INVALID_BPP_ID", "INVALID_BPPI_ID", CisErrorMessage.class, currentUser);
        softAssertions.assertThat(cisErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {14093})
    @Description("Get Project Items with valid data")
    public void getValidBidPackageProjectItems() {
        BidPackageProjectItemsResponse getBidPackageProjectItemsResponse = CisBidPackageProjectItemResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getItems().size()).isEqualTo(0);
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

    @Test
    @Ignore
    @TestRail(id = {13489})
    @Description("Mention a User in a Reply (Retrieve the Project users)")
    public void getBidPackageProjectItemUsers() {
        CisErrorMessage getBidPackageProjectItemsResponse = CisBidPackageProjectItemResources.getBidPackageProjectItemUsers(
            bidPackageProjectResponse.getIdentity(),
            bidPackageProjectItemResponse.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);
        softAssertions.assertThat(getBidPackageProjectItemsResponse.getMessage())
            .contains("2 validation failures were found:* 'projectIdentity' is not a valid identity.* 'bidPackageIdentity' is not a valid identity.");
    }

    @After
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
