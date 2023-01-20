package com.apriori.cis.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.cisapi.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageItemsResponse;
import com.apriori.cisapi.entity.response.bidpackage.BidPackageResponse;
import com.apriori.cisapi.entity.response.bidpackage.CisErrorMessage;
import com.apriori.cisapi.utils.CisBidPackageResources;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.utils.CssComponent;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CisBidPackageItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static UserCredentials currentUser = UserUtil.getUser();
    private static String bidPackageName;
    private static ScenarioItem scenarioItem;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = CisBidPackageResources.createBidPackageItem(
            CisBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(testCaseId = {"14599", "14604"})
    @Description("Create and delete Bid Package Item and verify bid package item is removed")
    public void testCreateBidPackageItemWithValidData() {
        String bPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();

        BidPackageResponse bidPackage = CisBidPackageResources.createBidPackage(bPackageName, currentUser);

        BidPackageItemResponse bidPackageItem = CisBidPackageResources.createBidPackageItem(
            CisBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackage.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageResources.getBidPackageItem(
            bidPackage.getIdentity(),
            bidPackageItem.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackage.getIdentity());

        CisBidPackageResources.deleteBidPackageItem(bidPackage.getIdentity(),
            bidPackageItem.getIdentity(), currentUser);

        CisErrorMessage cisErrorMessage = CisBidPackageResources.getBidPackageItem(
            bidPackage.getIdentity(),
            bidPackageItem.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackage.getIdentity(), bidPackageItem.getIdentity()));
    }

    @Test
    @TestRail(testCaseId = {"14600"})
    @Description("Update Bid package Item with Valid Data")
    public void testUpdateBidPackageItem() {
        ScenarioItem updateScenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(1);

        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(updateScenarioItem.getIterationIdentity())
                .build())
            .build();

        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            BidPackageItemResponse.class, HttpStatus.SC_OK, currentUser);

        assertThat(updateBidPackageItemResponse.getIterationIdentity(), not(equalTo(bidPackageItemResponse.getIterationIdentity())));
    }

    @Test
    @TestRail(testCaseId = {"14601"})
    @Description("Update Bid package Item with Invalid Data")
    public void testUpdateBidPackageItemWithInvalidData() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity("Invalid Iteration ID")
                .build())
            .build();

        CisErrorMessage cisErrorMessage = CisBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        assertThat(cisErrorMessage.getMessage(), containsString("2 validation failures were found:\n" +
            "* 'bidPackageIdentity' is not a valid identity.\n" +
            "* 'identity' is not a valid identity."));
    }

    @Test
    @TestRail(testCaseId = {"14604"})
    @Description("Delete valid Bid Package Item")
    public void testDeleteBidPackageItem() {
        CisBidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);

        CisErrorMessage cisErrorMessage = CisBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity()));
    }

    @Test
    @TestRail(testCaseId = {"14605"})
    @Description("Delete Invalid Bid Package Item")
    public void testDeleteBidPackageItemWithInvalidData() {
        CisErrorMessage cisErrorMessage = CisBidPackageResources.deleteBidPackageItem(
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            HttpStatus.SC_BAD_REQUEST, currentUser, CisErrorMessage.class);

        assertThat(cisErrorMessage.getMessage(), containsString("'bidPackageIdentity' is not a valid identity."));
    }

    @Test
    @TestRail(testCaseId = {"14602"})
    @Description("Get Bid package Item With valid Data")
    public void testGetBidPackageItem() {
        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        assertThat(updateBidPackageItemResponse.getBidPackageIdentity(), is(equalTo(bidPackageItemResponse.getBidPackageIdentity())));
    }

    @Test
    @TestRail(testCaseId = {"14603", "14607"})
    @Description("Get Bid Package Item with invalid identity")
    public void testGetBidPackageItemWithInvalidData() {
        CisErrorMessage cisInvalidErrorMessage = CisBidPackageResources.getBidPackageItem(
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        assertThat(cisInvalidErrorMessage.getMessage(), is(equalTo("2 validation failures were found:\n" +
            "* 'bidPackageIdentity' is not a valid identity.\n" +
            "* 'identity' is not a valid identity.")));
    }

    @Test
    @TestRail(testCaseId = {"14606"})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void testGetBidPackageItems() {
        BidPackageItemsResponse getBidPackageItemsResponse = CisBidPackageResources.getBidPackageItems(
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageItemsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageItemsResponse.getIsFirstPage()).isTrue();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"13903"})
    @Description("Get Bid package Item with invalid data")
    public void testGetBidPackageItemsByInvalidData() {
        CisErrorMessage cisInvalidErrorMessage = CisBidPackageResources.getBidPackageItems(
            "Invalid bidPackageIdentity",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        assertThat(cisInvalidErrorMessage.getMessage(), is(equalTo("'bidPackageIdentity' is not a valid identity.")));
    }

    @Test
    @TestRail(testCaseId = {"14597"})
    @Description("Create Bid Package Items with Invalid BidPackage ID")
    public void testCreateBidPackItemWithInvalidBidPackageIdentity() {
        CisErrorMessage cisErrorMessage = CisBidPackageResources.createBidPackageItem(
            CisBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            "INVALID",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        assertThat(cisErrorMessage.getMessage(), containsString("'bidPackageIdentity' is not a valid identity."));
    }

    @Test
    @TestRail(testCaseId = {"14598"})
    @Description("Create Bid Package Items with Invalid Data")
    public void testCreateBidPackItemWithInvalidData() {
        CisErrorMessage cisErrorMessage = CisBidPackageResources.createBidPackageItem(
            CisBidPackageResources.bidPackageItemRequestBuilder("Invalid Component ID",
                "Invalid Scenario ID", "Invalid Iteration ID"),
            bidPackageResponse.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        assertThat(cisErrorMessage.getMessage(), containsString("'identity' is not a valid identity"));
    }

    @After
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
    }
}
