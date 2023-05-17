package com.apriori.qms.tests;

import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageProjectItem;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemGetResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsGetResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectItemsPostResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.QmsApiTestDataUtils;
import utils.QmsApiTestUtils;

import java.util.ArrayList;
import java.util.List;

public class BidPackageProjectItemTest extends QmsApiTestDataUtils {
    @BeforeClass
    public static void beforeClass() {
        createTestData();
    }

    @AfterClass
    public static void afterClass() {
        deleteTestDataAndClearEntities();
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

            BidPackageProjectItemGetResponse bpPItemResponse = QmsBidPackageResources.getBidPackageProjectItem(
                bidPackageResponse.getIdentity(),
                bidPackageProjectResponse.getIdentity(),
                bidPackageProjectResponse.getItems().get(0).getIdentity(),
                currentUser,
                BidPackageProjectItemGetResponse.class,
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
        BidPackageProjectItemsGetResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsGetResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"24020","24004"})
    @Description("Verify user is able to create bulk project items by using project-items creation API")
    public void createBulkBidPackageProjectItems() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        ScenarioItem scenarioItem1 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem1.getScenarioIdentity())
                .componentIdentity(scenarioItem1.getComponentIdentity())
                .iterationIdentity(scenarioItem1.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem2 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem2.getScenarioIdentity())
                .componentIdentity(scenarioItem2.getComponentIdentity())
                .iterationIdentity(scenarioItem2.getIterationIdentity())
                .build())
            .build());

        ScenarioItem scenarioItem3 = QmsApiTestUtils.createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem3.getScenarioIdentity())
                .componentIdentity(scenarioItem3.getComponentIdentity())
                .iterationIdentity(scenarioItem3.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectItemsPostResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            currentUser
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
        BidPackageProjectItemsGetResponse bpPItemsResponse = QmsBidPackageResources.getBidPackageProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            currentUser,
            BidPackageProjectItemsGetResponse.class,
            HttpStatus.SC_OK);
        softAssertions.assertThat(bpPItemsResponse.getItems().size()).isEqualTo(4);

        if (scenarioItem1 != null) {
            QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem1, currentUser);
        }
        if (scenarioItem2 != null) {
            QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem2, currentUser);
        }
        if (scenarioItem3 != null) {
            QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem3, currentUser);
        }
    }

    @Test
    @TestRail(testCaseId = {"24003"})
    @Description("Verify user is not able to create  project items by using project-items creation API, when BidPackageItem for scenario identity already exists inside the bid package")
    public void createBidPackageDuplicateProjectItemsForSameProject() {
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .componentIdentity(scenarioItem.getComponentIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        BidPackageProjectItemsPostResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            currentUser
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
        String newProjectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        BidPackageProjectResponse newBidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(newProjectName, bidPackageResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_CREATED, currentUser);

        BidPackageProjectItemsPostResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            newBidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            currentUser
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
        //All Identites null
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity(null)
                .componentIdentity(null)
                .iterationIdentity(null)
                .build())
            .build());

        BidPackageProjectItemsPostResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            currentUser
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
            currentUser
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
            currentUser
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
            currentUser
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
    public void createBidPackageInvaldProjectItems() {
        //All Identites null
        List<BidPackageProjectItem> bidPackageItemList = new ArrayList<>();
        bidPackageItemList.add(BidPackageProjectItem.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .scenarioIdentity("INVALID_ID")
                .componentIdentity("INVALID_ID")
                .iterationIdentity("INVALID_ID")
                .build())
            .build());

        BidPackageProjectItemsPostResponse bulkBidPackageProjectItemsResponse = QmsBidPackageResources.createBidPackageBulkProjectItems(
            bidPackageResponse.getIdentity(),
            bidPackageProjectResponse.getIdentity(),
            bidPackageItemList,
            currentUser
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
            currentUser
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
            currentUser
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
            currentUser
        );

        softAssertions.assertThat(bulkBidPackageProjectItemsResponse.getFailedProjectItem().stream()
            .anyMatch(fi -> fi.getIdentity()
                .contains(String.format("scenarioIdentity: %s componentIdentity: %s iterationIdentity:%s", scenarioItem.getScenarioIdentity(),
                    scenarioItem.getComponentIdentity(), "INVALID_ID")) &&
                fi.getError()
                    .contains("'identity' is not a valid identity")
            )).isTrue();
    }
}

