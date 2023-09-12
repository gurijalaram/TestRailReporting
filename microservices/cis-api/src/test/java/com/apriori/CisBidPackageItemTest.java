package com.apriori;

import static com.apriori.enums.CssSearch.SCENARIO_CREATED_AT_GT;

import com.apriori.cis.controller.CisBidPackageItemResources;
import com.apriori.cis.controller.CisBidPackageResources;
import com.apriori.cis.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.cis.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.cis.models.response.bidpackage.BidPackageItemsResponse;
import com.apriori.cis.models.response.bidpackage.BidPackageResponse;
import com.apriori.cis.models.response.bidpackage.CisErrorMessage;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ScenarioItem;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CisBidPackageItemTest extends TestUtil {

    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static UserCredentials currentUser;
    private static ScenarioItem scenarioItem;

    @BeforeAll
    public static void beforeClass() {
        currentUser = UserUtil.getUser();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser, SCENARIO_CREATED_AT_GT.getKey() + DateUtil.getDateDaysBefore(90, DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ)).get(0);
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageResponse = CisBidPackageResources.createBidPackage("BPN" + new GenerateStringUtil().getRandomNumbers(), currentUser);
        bidPackageItemResponse = CisBidPackageItemResources.createBidPackageItem(
            CisBidPackageItemResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(id = {14599, 14604})
    @Description("Create and delete Bid Package Item and verify bid package item is removed")
    public void testCreateBidPackageItemWithValidData() {
        String bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        BidPackageResponse bidPackage = CisBidPackageResources.createBidPackage(bidPackageName, currentUser);
        BidPackageItemResponse bidPackageItem = CisBidPackageItemResources.createBidPackageItem(
            CisBidPackageItemResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackage.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageItemResources.getBidPackageItem(
            bidPackage.getIdentity(),
            bidPackageItem.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);
        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackage.getIdentity());
        CisBidPackageItemResources.deleteBidPackageItem(bidPackage.getIdentity(),
            bidPackageItem.getIdentity(), currentUser);

        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.getBidPackageItem(
            bidPackage.getIdentity(),
            bidPackageItem.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackage.getIdentity(), bidPackageItem.getIdentity()));
    }

    @Test
    @TestRail(id = {14600})
    @Description("Update Bid package Item with Valid Data")
    public void testUpdateBidPackageItem() {
        ScenarioItem updateScenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(1);

        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(updateScenarioItem.getIterationIdentity())
                .build())
            .build();

        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageItemResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            BidPackageItemResponse.class, HttpStatus.SC_OK, currentUser);

        softAssertions.assertThat(updateBidPackageItemResponse.getIterationIdentity()).isNotEqualTo(bidPackageItemResponse.getIterationIdentity());
    }

    @Test
    @TestRail(id = {14601})
    @Description("Update Bid package Item with Invalid Data")
    public void testUpdateBidPackageItemWithInvalidData() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity("Invalid Iteration ID")
                .build())
            .build();

        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains("2 validation failures were found:" +
            "\n* 'bidPackageIdentity' is not a valid identity." +
            "\n* 'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14604})
    @Description("Delete valid Bid Package Item")
    public void testDeleteBidPackageItem() {
        CisBidPackageItemResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);

        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {14605})
    @Description("Delete Invalid Bid Package Item")
    public void testDeleteBidPackageItemWithInvalidData() {
        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.deleteBidPackageItem(
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            HttpStatus.SC_BAD_REQUEST, currentUser, CisErrorMessage.class);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14602})
    @Description("Get Bid package Item With valid Data")
    public void testGetBidPackageItem() {
        BidPackageItemResponse updateBidPackageItemResponse = CisBidPackageItemResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity()).isEqualTo(bidPackageItemResponse.getBidPackageIdentity());
    }

    @Test
    @TestRail(id = {14603, 14607})
    @Description("Get Bid Package Item with invalid identity")
    public void testGetBidPackageItemWithInvalidData() {
        CisErrorMessage cisInvalidErrorMessage = CisBidPackageItemResources.getBidPackageItem(
            "Invalid bidPackageIdentity",
            "Invalid bidPackageItemIdentity",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(cisInvalidErrorMessage.getMessage()).contains("2 validation failures were found:" +
            "\n* 'bidPackageIdentity' is not a valid identity." +
            "\n* 'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14606})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void testGetBidPackageItems() {
        BidPackageItemsResponse getBidPackageItemsResponse = CisBidPackageItemResources.getBidPackageItems(
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(getBidPackageItemsResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(getBidPackageItemsResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(id = {13903})
    @Description("Get Bid package Item with invalid data")
    public void testGetBidPackageItemsByInvalidData() {
        CisErrorMessage cisInvalidErrorMessage = CisBidPackageItemResources.getBidPackageItems(
            "Invalid bidPackageIdentity",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(cisInvalidErrorMessage.getMessage()).contains("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14597})
    @Description("Create Bid Package Items with Invalid BidPackage ID")
    public void testCreateBidPackItemWithInvalidBidPackageIdentity() {
        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.createBidPackageItem(
            CisBidPackageItemResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            "INVALID",
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains("'bidPackageIdentity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {14598})
    @Description("Create Bid Package Items with Invalid Data")
    public void testCreateBidPackItemWithInvalidData() {
        CisErrorMessage cisErrorMessage = CisBidPackageItemResources.createBidPackageItem(
            CisBidPackageItemResources.bidPackageItemRequestBuilder("Invalid Component ID",
                "Invalid Scenario ID", "Invalid Iteration ID"),
            bidPackageResponse.getIdentity(),
            currentUser,
            CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(cisErrorMessage.getMessage()).contains("'identity' is not a valid identity.");
    }

    @AfterEach
    public void testCleanup() {
        CisBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
