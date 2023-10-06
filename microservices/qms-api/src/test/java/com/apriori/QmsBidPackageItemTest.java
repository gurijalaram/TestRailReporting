package com.apriori;

import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.TestUtil;
import com.apriori.models.response.ApwErrorMessage;
import com.apriori.models.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.models.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageItemsResponse;
import com.apriori.qms.models.response.bidpackage.BidPackageResponse;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class QmsBidPackageItemTest extends TestUtil {
    private static SoftAssertions softAssertions;
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static String bidPackageName;
    private static ScenarioItem scenarioItem;
    private static final UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        bidPackageName = "BPN" + new GenerateStringUtil().getRandomNumbers();
        scenarioItem = new CssComponent().getBaseCssComponents(currentUser).get(0);
        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
        bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_CREATED);
    }

    @Test
    @TestRail(id = {13755, 13767, 13768})
    @Description("Create and delete Bid Package Item and verify bid package item is removed")
    public void createAndDeleteBidPackageItem() {
        QmsBidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), currentUser);

        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackageItem for bid package with identity '%s' and identity '%s'",
                bidPackageResponse.getIdentity(), bidPackageItemResponse.getIdentity())
        );
    }

    @Test
    @TestRail(id = {14123})
    @Description("Verify that bid-package item is deleted after deleting bid-package")
    public void verifyBidPackageItemIsDeletedWithBidPackage() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'",
                bidPackageResponse.getIdentity(), bidPackageResponse.getCustomerIdentity()));

        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {13760})
    @Description("update Bid Package Item")
    public void updateBidPackageItem() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        BidPackageItemResponse updateBidPackageItemResponse = QmsBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            BidPackageItemResponse.class, HttpStatus.SC_OK, currentUser
        );

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13902})
    @Description("update Bid Package Item another user under same customer")
    public void updateBidPackageItemByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        BidPackageItemResponse updateBidPackageItemResponse = QmsBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            BidPackageItemResponse.class, HttpStatus.SC_OK, otherUser);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13761})
    @Description("update Bid Package Item with invalid identity")
    public void updateBidPackageItemWithInvalid() {
        BidPackageItemRequest bidPackageItemRequestBuilder = BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build();

        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.updateBidPackageItem(
            bidPackageItemRequestBuilder,
            bidPackageResponse.getIdentity(),
            "INVALID",
            ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'identity' is not a valid identity");

    }

    @Test
    @TestRail(id = {13905})
    @Description("Delete Bid Package Item By Other user under same customer")
    public void deleteBidPackageItemByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        QmsBidPackageResources.deleteBidPackageItem(bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(), otherUser);
    }

    @Test
    @TestRail(id = {13765})
    @Description("Get Bid Package Item")
    public void getBidPackageItem() {
        BidPackageItemResponse updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            currentUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13904})
    @Description("Get Bid Package Item By Other user under same customer")
    public void getBidPackageItemByOtherUser() {
        UserCredentials otherUser = UserUtil.getUser();
        BidPackageItemResponse updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            otherUser,
            BidPackageItemResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getBidPackageIdentity())
            .isEqualTo(bidPackageResponse.getIdentity());
    }

    @Test
    @TestRail(id = {13766})
    @Description("Get Bid Package Item with invalid identity")
    public void getBidPackageItemWithInvalidIdentity() {
        ApwErrorMessage qmsInvalidErrorMessage = QmsBidPackageResources.getBidPackageItem(
            bidPackageResponse.getIdentity(),
            "INVALID",
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(qmsInvalidErrorMessage.getMessage()).contains("'identity' is not a valid identity");
    }

    @Test
    @TestRail(id = {13763, 13764})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void getBidPackageItems() {
        BidPackageItemsResponse updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItems(
            bidPackageResponse.getIdentity(),
            currentUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(updateBidPackageItemResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(id = {13903})
    @Description("Find list of  Bid Package Items by other user under same customer")
    public void getBidPackageItemsByOther() {
        UserCredentials otherUser = UserUtil.getUser();
        BidPackageItemsResponse updateBidPackageItemResponse = QmsBidPackageResources.getBidPackageItems(
            bidPackageResponse.getIdentity(),
            otherUser,
            BidPackageItemsResponse.class, HttpStatus.SC_OK);

        softAssertions.assertThat(updateBidPackageItemResponse.getItems().size()).isGreaterThan(0);
        softAssertions.assertThat(updateBidPackageItemResponse.getIsFirstPage()).isTrue();
    }

    @Test
    @TestRail(id = {13756})
    @Description("Find list of  Bid Package Items and verify pagination")
    public void createBidPackItemWithInvalidBidPackage() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            "INVALID",
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains("'bidPackageIdentity' is not a valid identity");
    }

    @Test
    @TestRail(id = {13757, 13762})
    @Description("Create bid-package Item without created Bid package," +
        "Update bid-package Item without created Bid package")
    public void createBidPackItemWithNonExistBidPackage() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage())
            .contains("Can't find bidPackage with identity '" + bidPackageResponse.getIdentity()
                + "' for customerIdentity '");

        ApwErrorMessage updateErrorMessage = QmsBidPackageResources.updateBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(updateErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'",
                bidPackageResponse.getIdentity(), bidPackageResponse.getCustomerIdentity()));

        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {13762})
    @Description("Update bid-package Item without created Bid package")
    public void updateBidPackItemWithNonExistBidPackage() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        ApwErrorMessage updateErrorMessage = QmsBidPackageResources.updateBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            bidPackageItemResponse.getIdentity(),
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND, currentUser);

        softAssertions.assertThat(updateErrorMessage.getMessage()).contains(
            String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'",
                bidPackageResponse.getIdentity(), bidPackageResponse.getCustomerIdentity()));

        bidPackageResponse = QmsBidPackageResources.createBidPackage(bidPackageName, currentUser);
    }

    @Test
    @TestRail(id = {13901})
    @Description("Create bid-package Item with existing scenario")
    public void createBidPackItemWithExistScenario() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains(
            String.format("BidPackageItem for scenario with identity '%s' already exists for bid package with identity '%s'", scenarioItem.getScenarioIdentity(), bidPackageResponse.getIdentity()));
    }

    @Test
    @TestRail(id = {13758})
    @Description("Create bid-package Item with blank component identity")
    public void createBidPackItemWithEmptyComponentIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder("",
                scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("No message available");
    }

    @Test
    @TestRail(id = {13759})
    @Description("Create bid-package Item with blank Scenario identity")
    public void createBidPackItemWithEmptyScenarioIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                "", scenarioItem.getIterationIdentity()),
            bidPackageResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("No message available");
    }

    @Test
    @TestRail(id = {13761})
    @Description("Create bid-package Item with blank Scenario identity")
    public void createBidPackItemWithInvalidIterationIdentity() {
        ApwErrorMessage qmsErrorMessage = QmsBidPackageResources.createBidPackageItem(
            QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
                scenarioItem.getScenarioIdentity(), "INVALID"),
            bidPackageResponse.getIdentity(),
            currentUser,
            ApwErrorMessage.class, HttpStatus.SC_BAD_REQUEST);

        softAssertions.assertThat(qmsErrorMessage.getMessage()).contains("'identity' is not a valid identity");
    }

    @AfterEach
    public void testCleanup() {
        QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        softAssertions.assertAll();
    }
}
