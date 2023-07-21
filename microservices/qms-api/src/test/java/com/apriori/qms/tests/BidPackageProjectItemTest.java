package com.apriori.qms.tests;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItem;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsBulkResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.ApwErrorMessage;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BidPackageProjectItemTest extends TestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static SoftAssertions softAssertions = new SoftAssertions();
    private static BidPackageResponse bidPackageResponse;
    private static BidPackageItemResponse bidPackageItemResponse;
    private static BidPackageProjectResponse bidPackageProjectResponse;
    private static ScenarioItem scenarioItem;

    @BeforeClass
    public static void beforeClass() {
        scenarioItem = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageResponse = QmsApiTestUtils.createTestDataBidPackage(currentUser, softAssertions);
        bidPackageItemResponse = QmsApiTestUtils
            .createTestDataBidPackageItem(scenarioItem, bidPackageResponse, currentUser, softAssertions);
        bidPackageProjectResponse = QmsApiTestUtils
            .createTestDataBidPackageProject(bidPackageResponse, currentUser, softAssertions);
    }

    @AfterClass
    public static void afterClass() {
        QmsApiTestUtils.deleteTestData(scenarioItem, bidPackageResponse, currentUser);
        softAssertions.assertAll();
    }

    @Before
    public void setupTest() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void tearTest() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"14685", "14912"})
    @Description("Verify that Project Item creates automatically with Project creation")
    public void createAndGetProjectItem() {
        softAssertions.assertThat(bidPackageProjectResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                .getComponentIdentity()).isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                    .getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bidPackageProjectResponse.getItems().get(0).getBidPackageItem()
                .getIterationIdentity()).isEqualTo(bidPackageItemResponse.getIterationIdentity());

            BidPackageProjectItemResponse bpPItemResponse = QmsBidPackageResources.getBidPackageProjectItem(
                bidPackageResponse.getIdentity(),
                bidPackageProjectResponse.getIdentity(),
                bidPackageProjectResponse.getItems().get(0).getIdentity(),
                currentUser,
                BidPackageProjectItemResponse.class,
                HttpStatus.SC_OK);
            softAssertions.assertThat(bpPItemResponse.getBidPackageIdentity())
                .isEqualTo(bidPackageResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getIdentity())
                .isEqualTo(bidPackageItemResponse.getIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getComponentIdentity())
                .isEqualTo(bidPackageItemResponse.getComponentIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getScenarioIdentity())
                .isEqualTo(bidPackageItemResponse.getScenarioIdentity());
            softAssertions.assertThat(bpPItemResponse.getBidPackageItem().getIterationIdentity())
                .isEqualTo(bidPackageItemResponse.getIterationIdentity());
        }
    }

    @Test
    @TestRail(testCaseId = {"14911"})
    @Description("Find all project Items for particular project using bid-package URL")
    public void getAllProjectItems() {
        BidPackageProjectItemsResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"24020", "24004"})
    @Description("Verify user is able to create bulk project items by using project-items creation API")
    public void createBulkBidPackageProjectItems() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem scenarioItem1 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem1.getScenarioIdentity())
                .componentIdentity(scenarioItem1.getComponentIdentity())
                .iterationIdentity(scenarioItem1.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem2 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem2.getScenarioIdentity())
                .componentIdentity(scenarioItem2.getComponentIdentity())
                .iterationIdentity(scenarioItem2.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem3 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem3.getScenarioIdentity())
                .componentIdentity(scenarioItem3.getComponentIdentity())
                .iterationIdentity(scenarioItem3.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getBidPackageIdentity().equals(bidPackageResponse.getIdentity()))).isTrue();
        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getProjectIdentity().equals(bidPackageProjectResponse.getIdentity()))).isTrue();

        for (BidPackageProjectItem bpi : bidPackageItemList) {
            softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
                .anyMatch(pi -> pi.getBidPackageItem().getComponentIdentity()
                    .equals(bpi.getBidPackageItem().getComponentIdentity()) &&
                    pi.getBidPackageItem().getScenarioIdentity()
                        .equals(bpi.getBidPackageItem().getScenarioIdentity()) &&
                    pi.getBidPackageItem().getIterationIdentity()
                        .equals(bpi.getBidPackageItem().getIterationIdentity())
                )).isTrue();
        }

        //Get project-items
        BidPackageProjectItemsResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isEqualTo(4);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem1, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem2, currentUser);
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem3, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"24003"})
    @Description("Verify user is not able to create project items by using project-items creation API, with duplicate (already used) BidPackageItem")
    public void createBidPackageDuplicateProjectItemsForSameProject() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem scenarioItemDuplicate = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItemDuplicate.getScenarioIdentity())
                .componentIdentity(scenarioItemDuplicate.getComponentIdentity())
                .iterationIdentity(scenarioItemDuplicate.getIterationIdentity())
                .build())
            .build());

        QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        //Reuse BidPackage Item
        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItemDuplicate.getScenarioIdentity(),
                    scenarioItemDuplicate.getComponentIdentity(), scenarioItemDuplicate.getIterationIdentity())) &&
                fi.getError()
                    .contains(String.format("BidPackageItem for scenario with identity '%s' already exists for bid package with identity '%s'", scenarioItemDuplicate.getScenarioIdentity(),
                        bidPackageResponse.getIdentity()))
            )).isTrue();
        QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItemDuplicate, currentUser);
    }

    @Test
    @TestRail(testCaseId = {"24019"})
    @Description("Verify user is not able to create  project items by using project-items creation API for new project inside the existing  bidPackage if BidPackageItem for scenario already exists")
    public void createBidPackageDuplicateProjectItemsForNewProject() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        //New project
        BidPackageProjectResponse newBidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            newBidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    scenarioItem.getComponentIdentity(), scenarioItem.getIterationIdentity())) &&
                fi.getError()
                    .contains(String.format("BidPackageItem for scenario with identity '%s' already exists for bid package with identity '%s'", scenarioItem.getScenarioIdentity(),
                        bidPackageResponse.getIdentity()))
            )).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24006"})
    @Description("Verify user is not able to create project items by using project-items creation API, for null component, scenario and iteration identity")
    public void createBidPackageNullProjectItems() {
        //All Identities null
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(null)
                .componentIdentity(null)
                .iterationIdentity(null)
                .build())
            .build());

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", "null", "null", "null")) &&
                fi.getError().contains("'identity' is not a valid identity")
            )).isTrue();

        //Scenario Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(null)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", "null",
                    scenarioItem.getComponentIdentity(), scenarioItem.getIterationIdentity())) &&
                fi.getError()
                    .contains("No message available")
            )).isTrue();

        //Component Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity(null)
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    "null", scenarioItem.getIterationIdentity())) &&
                fi.getError()
                    .contains("No message available")
            )).isTrue();

        //Iteration Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity(null)
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    scenarioItem.getComponentIdentity(), "null")) &&
                fi.getError()
                    .contains("'iterationIdentity' should not be null")
            )).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24005"})
    @Description("Verify user is not able to create  project items by using project-items creation API,   for invalid  component, scenario and iteration identity")
    public void createBidPackageInvalidProjectItems() {
        //All Identities null
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity("INVALID_ID")
                .componentIdentity("INVALID_ID")
                .iterationIdentity("INVALID_ID")
                .build())
            .build());

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", "INVALID_ID", "INVALID_ID", "INVALID_ID")) &&
                fi.getError().contains("'identity' is not a valid identity")
            )).isTrue();

        //Scenario Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity("INVALID_ID")
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", "INVALID_ID",
                    scenarioItem.getComponentIdentity(), scenarioItem.getIterationIdentity())) &&
                fi.getError()
                    .contains("'identity' is not a valid identity")
            )).isTrue();

        //Component Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity("INVALID_ID")
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    "INVALID_ID", scenarioItem.getIterationIdentity())) &&
                fi.getError()
                    .contains("'identity' is not a valid identity")
            )).isTrue();

        //Iteration Identity is null
        bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity("INVALID_ID")
                .build())
            .build());

        bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    scenarioItem.getComponentIdentity(), "INVALID_ID")) &&
                fi.getError()
                    .contains("'identity' is not a valid identity")
            )).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24032", "24025"})
    @Description("Verify user is able to delete multiple/bulk project items and Verify user is not able to delete already deleted project items by API for bulk deleting functionality for project identity")
    public void deleteBulkBidPackageProjectItems() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem scenarioItem1 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem1.getScenarioIdentity())
                .componentIdentity(scenarioItem1.getComponentIdentity())
                .iterationIdentity(scenarioItem1.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem2 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem2.getScenarioIdentity())
                .componentIdentity(scenarioItem2.getComponentIdentity())
                .iterationIdentity(scenarioItem2.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem3 = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem3.getScenarioIdentity())
                .componentIdentity(scenarioItem3.getComponentIdentity())
                .iterationIdentity(scenarioItem3.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getBidPackageIdentity().equals(bidPackageResponse.getIdentity()))).isTrue();
        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getProjectIdentity().equals(bidPackageProjectResponse.getIdentity()))).isTrue();

        //Delete project-items
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity())
            .build());
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bulkBidPackageProjectItemsResponse.getProjectItem().get(1).getIdentity())
            .build());
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bulkBidPackageProjectItemsResponse.getProjectItem().get(2).getIdentity())
            .build());

        BidPackageProjectItemsBulkResponse deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getProjectItem().stream()
            .anyMatch(pi -> pi.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity()))).isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getProjectItem().stream()
            .anyMatch(pi -> pi.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(1).getIdentity()))).isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getProjectItem().stream()
            .anyMatch(pi -> pi.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(2).getIdentity()))).isTrue();

        //C24025
        deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity()) &&
                fp.getError()
                    .contains(String.format("Can't find ProjectItem for Project with identity '%s' and identity '%s",
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getProjectIdentity(),
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity())))).isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(1).getIdentity()) &&
                fp.getError()
                    .contains(String.format("Can't find ProjectItem for Project with identity '%s' and identity '%s",
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(1).getProjectIdentity(),
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(1).getIdentity())))).isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(2).getIdentity()) &&
                fp.getError()
                    .contains(String.format("Can't find ProjectItem for Project with identity '%s' and identity '%s",
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(2).getProjectIdentity(),
                        bulkBidPackageProjectItemsResponse.getProjectItem().get(2).getIdentity())))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24021"})
    @Issue("COL-1858")
    @Description("Verify the error message when user tries to delete project items by  passing invalid, null or empty project-item identity")
    public void deleteInvalidBulkBidPackageProjectItems() {
        //null
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(null)
            .build());

        BidPackageProjectItemsBulkResponse deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getError().contains("Request method 'DELETE' not supported"))).isTrue();

        //empty
        prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity("")
            .build());

        deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getError().contains("Request method 'DELETE' not supported"))).isTrue();

        //Invalid
        prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity("1234567891234")
            .build());

        deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fp -> fp.getIdentity().equals("1234567891234") &&
                fp.getError().contains("'identity' is not a valid identity"))).isTrue();

    }

    @Test
    @TestRail(testCaseId = {"24416"})
    @Description("Verify error message when either bidPackage or project identity is invalid while deleting bulk project items")
    public void deleteBulkBidPackageProjectItemsWithInvalidBidPackageAndProject() {
        //Invalid Bidpackage
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(null)
            .build());

        ApwErrorMessage deleteProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            "INVALID_BP_ID",
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser
        );
        softAssertions.assertThat(deleteProjectItemsErrorResponse.getMessage())
            .contains("'bidPackageIdentity' is not a valid identity");

        //Invalid Project
        prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(null)
            .build());

        deleteProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            "INVALID_PRJ_ID",
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser
        );
        softAssertions.assertThat(deleteProjectItemsErrorResponse.getMessage())
            .contains("projectIdentity' is not a valid identity");
    }

    @Test
    @TestRail(testCaseId = {"24030"})
    @Description("Verify error message when system is unable to find either bidPackage or project while deleting bulk project items")
    public void deleteBulkBidPackageProjectItemsWithDeletedBidPackageAndProject() {
        //Create & Delete Bidpackage/Project
        BidPackageResponse deleteBidPackageResponse = QmsBidPackageResources.createBidPackage("BPN" + new GenerateStringUtil().getRandomNumbers(), currentUser);
        BidPackageProjectResponse deleteBidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), deleteBidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);
        QmsBidPackageResources.deleteBidPackage(deleteBidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);

        //Deleted Bidpackage
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(null)
            .build());

        ApwErrorMessage deleteProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            deleteBidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsErrorResponse.getMessage()).contains(
            String.format("Can't find bidPackage with identity '%s' for customerIdentity '%s'",
                deleteBidPackageResponse.getIdentity(), PropertiesContext.get("${env}.customer_identity")));

        //Deleted Project
        prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(null)
            .build());

        deleteProjectItemsErrorResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            deleteBidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            ApwErrorMessage.class,
            HttpStatus.SC_NOT_FOUND,
            currentUser
        );
        softAssertions.assertThat(deleteProjectItemsErrorResponse.getMessage()).contains(
            String.format("Resource 'Project' with identity '%s' was not found", deleteBidPackageProjectResponse.getIdentity()));
    }

    @Test
    @TestRail(testCaseId = {"24031"})
    @Description("Verify duplicate project items gets discarded while performing bulk deletion of project-item identity")
    public void deleteDuplicateBulkBidPackageProjectItems() {
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bidPackageProjectResponse.getItems().get(0).getIdentity())
            .build());
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bidPackageProjectResponse.getItems().get(0).getIdentity())
            .build());
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bidPackageProjectResponse.getItems().get(0).getIdentity())
            .build());

        BidPackageProjectItemsBulkResponse deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );
        softAssertions.assertThat(deleteProjectItemsResponse.getProjectItem().stream()
                .anyMatch(pi -> pi.getIdentity().equals(bidPackageProjectResponse.getItems().get(0).getIdentity())))
            .isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().stream()
            .allMatch(fpi -> fpi.getIdentity().equals(bidPackageProjectResponse.getItems().get(0).getIdentity()) &&
                fpi.getError()
                    .contains(String.format("Can't find ProjectItem for Project with identity '%s' and identity '%s'",
                        bidPackageProjectResponse.getIdentity(), bidPackageProjectResponse.getItems().get(0)
                            .getIdentity())))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"26293"})
    @Description("Delete Project Item without discussions inside of it")
    public void deleteBulkBidPackageProjectItemWithNoDiscussion() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem firstScenarioItemNoDiscussions = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(firstScenarioItemNoDiscussions.getScenarioIdentity())
                .componentIdentity(firstScenarioItemNoDiscussions.getComponentIdentity())
                .iterationIdentity(firstScenarioItemNoDiscussions.getIterationIdentity())
                .build())
            .build());

        ScenarioItem secondScenarioItemWithDiscussions = QmsApiTestUtils
            .createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(secondScenarioItemWithDiscussions.getScenarioIdentity())
                .componentIdentity(secondScenarioItemWithDiscussions.getComponentIdentity())
                .iterationIdentity(secondScenarioItemWithDiscussions.getIterationIdentity())
                .build())
            .build());

        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsApiTestUtils.createTestDataScenarioDiscussion(secondScenarioItemWithDiscussions, currentUser, softAssertions);
        QmsApiTestUtils.createTestDataAddCommentToDiscussion(scenarioDiscussionResponse, currentUser, softAssertions);

        BidPackageProjectItemsBulkResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            BidPackageProjectItemsBulkResponse.class,
            currentUser,
            HttpStatus.SC_OK
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getBidPackageIdentity().equals(bidPackageResponse.getIdentity()))).isTrue();
        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getProjectItem().stream()
            .allMatch(pi -> pi.getProjectIdentity().equals(bidPackageProjectResponse.getIdentity()))).isTrue();

        //Delete project-items
        List<BidPackageProjectItem> prjItemIdentiesList = new ArrayList<>();
        prjItemIdentiesList.add(BidPackageProjectItem.builder()
            .identity(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity())
            .build());

        BidPackageProjectItemsBulkResponse deleteProjectItemsResponse = QmsBidPackageResources.deleteBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            prjItemIdentiesList,
            BidPackageProjectItemsBulkResponse.class,
            HttpStatus.SC_OK,
            currentUser
        );

        softAssertions.assertThat(deleteProjectItemsResponse.getProjectItem().stream()
            .anyMatch(pi -> pi.getIdentity()
                .equals(bulkBidPackageProjectItemsResponse.getProjectItem().get(0).getIdentity()))).isTrue();
        softAssertions.assertThat(deleteProjectItemsResponse.getFailedProjectItem().size()).isZero();
    }
}

