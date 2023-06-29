package utils;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.scenarios.ScenarioResponse;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.entity.response.ScenarioItem;
import com.apriori.qms.controller.QmsBidPackageResources;
import com.apriori.qms.controller.QmsProjectResources;
import com.apriori.qms.controller.QmsScenarioDiscussionResources;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemParameters;
import com.apriori.qms.entity.request.bidpackage.BidPackageItemRequest;
import com.apriori.qms.entity.request.scenariodiscussion.Attributes;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionParameters;
import com.apriori.qms.entity.request.scenariodiscussion.ScenarioDiscussionRequest;
import com.apriori.qms.entity.response.bidpackage.BidPackageItemResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageProjectsResponse;
import com.apriori.qms.entity.response.bidpackage.BidPackageResponse;
import com.apriori.qms.entity.response.scenariodiscussion.DiscussionCommentResponse;
import com.apriori.qms.entity.response.scenariodiscussion.ScenarioDiscussionResponse;
import com.apriori.utils.CssComponent;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.ScenarioStateEnum;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Qms api test utils.
 */
public class QmsApiTestUtils {

    /**
     * setup header information for DDS API Authorization
     *
     * @param cloudContext the cloud context
     * @return Map up header
     */
    public static Map<String, String> setUpHeader(String cloudContext) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json");
        header.put("ap-cloud-context", cloudContext);
        return header;
    }

    /**
     * Gets customer user.
     *
     * @return the customer user
     */
    public static UserCredentials getCustomerUser() {
        if (PropertiesContext.get("customer").startsWith("ap-int")) {
            return new UserCredentials().setEmail("testUser1@widgets.aprioritest.com");
        }
        return new UserCredentials().setEmail("qa-automation-01@apriori.com");
    }

    /**
     * Gets scenario discussion request.
     *
     * @param assignedUser the assigned user
     * @param scenarioItem the scenario item
     * @param description  the description
     * @return the scenario discussion request
     */
    public static ScenarioDiscussionRequest getScenarioDiscussionRequest(UserCredentials assignedUser, ScenarioItem scenarioItem, String description) {
        return
            ScenarioDiscussionRequest.builder()
                .scenarioDiscussion(ScenarioDiscussionParameters.builder()
                    .status("ACTIVE")
                    .type("SCENARIO")
                    .assigneeEmail(assignedUser.getEmail())
                    .description(description)
                    .componentIdentity(scenarioItem.getComponentIdentity())
                    .scenarioIdentity(scenarioItem.getScenarioIdentity())
                    .attributes(Attributes.builder()
                        .attribute("materialName")
                        .subject("4056-23423-003")
                        .build())
                    .build())
                .build();
    }

    /**
     * Create & Publish Scenario via Cid-app
     *
     * @param processGroupEnum - ProcessGroupEnum
     * @param componentName    - component name
     * @param currentUser      - UserCredentials
     * @return ScenarioItem object
     */
    public static ScenarioItem createAndPublishScenarioViaCidApp(ProcessGroupEnum processGroupEnum, String componentName, UserCredentials currentUser) {
        final File resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        ComponentInfoBuilder componentInfoBuilder = ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(currentUser)
            .build();
        new ComponentsUtil().postComponent(componentInfoBuilder);
        ScenarioItem scenarioItem = new CssComponent().getWaitBaseCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
                SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)
            .get(0);
        if (scenarioItem != null) {
            componentInfoBuilder.setComponentIdentity(scenarioItem.getComponentIdentity());
            componentInfoBuilder.setScenarioIdentity(scenarioItem.getScenarioIdentity());
            new ScenariosUtil().publishScenario(componentInfoBuilder, ScenarioResponse.class, HttpStatus.SC_CREATED);
            scenarioItem = new CssComponent().getWaitBaseCssComponents(currentUser, COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
                    SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)
                .get(0);
        }
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

    /**
     * Create test data bid package response.
     *
     * @param currentUser    the current user
     * @param softAssertions the soft assertions
     * @return the bid package response
     */
    public static BidPackageResponse createTestDataBidPackage(UserCredentials currentUser, SoftAssertions softAssertions) {
        BidPackageResponse bidPackageResponse = QmsBidPackageResources.createBidPackage(new GenerateStringUtil().getRandomNumbers(), currentUser);
        if (bidPackageResponse == null) {
            softAssertions.fail("Bid Package Creation FAILED.");
        }
        return bidPackageResponse;
    }

    /**
     * Create test data bid package item response.
     *
     * @param scenarioItem       the scenario item
     * @param bidPackageResponse the bid package response
     * @param currentUser        the current user
     * @param softAssertions     the soft assertions
     * @return the bid package item response
     */
    public static BidPackageItemResponse createTestDataBidPackageItem(ScenarioItem scenarioItem, BidPackageResponse bidPackageResponse, UserCredentials currentUser, SoftAssertions softAssertions) {
        if (bidPackageResponse == null || scenarioItem == null) {
            softAssertions.fail("Bid Package Item can not be created. Parameter is null .");
            return null;
        }

        BidPackageItemResponse bidPackageItemResponse = QmsBidPackageResources.createBidPackageItem(QmsBidPackageResources.bidPackageItemRequestBuilder(scenarioItem.getComponentIdentity(),
            scenarioItem.getScenarioIdentity(), scenarioItem.getIterationIdentity()), bidPackageResponse.getIdentity(), currentUser, BidPackageItemResponse.class, HttpStatus.SC_CREATED);

        if (bidPackageItemResponse == null) {
            softAssertions.fail("Bid Package Item Creation FAILED.");
        }
        return bidPackageItemResponse;
    }

    /**
     * Create test data bid package project response.
     *
     * @param bidPackageResponse the bid package response
     * @param currentUser        the current user
     * @param softAssertions     the soft assertions
     * @return the bid package project response
     */
    public static BidPackageProjectResponse createTestDataBidPackageProject(BidPackageResponse bidPackageResponse, UserCredentials currentUser, SoftAssertions softAssertions) {
        if (bidPackageResponse == null) {
            softAssertions.fail("Bid Package Project can not be created. Parameter is null .");
            return null;
        }

        BidPackageProjectResponse bidPackageProjectResponse = QmsBidPackageResources.createBidPackageProject(new HashMap<>(), bidPackageResponse.getIdentity(), BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED, currentUser);

        if (bidPackageProjectResponse == null) {
            softAssertions.fail("Bid Package Project Creation FAILED.");
        }
        return bidPackageProjectResponse;
    }

    /**
     * Create test data scenario discussion response.
     *
     * @param scenarioItem   the scenario item
     * @param currentUser    the current user
     * @param softAssertions the soft assertions
     * @return the scenario discussion response
     */
    public static ScenarioDiscussionResponse createTestDataScenarioDiscussion(ScenarioItem scenarioItem, UserCredentials currentUser, SoftAssertions softAssertions) {
        if (scenarioItem == null) {
            softAssertions.fail("Scenario discussion can not be created. Parameter is null .");
            return null;
        }

        ScenarioDiscussionResponse scenarioDiscussionResponse = QmsScenarioDiscussionResources.createScenarioDiscussion(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Scenario Discussion Creation FAILED.");
        }

        return scenarioDiscussionResponse;
    }

    /**
     * Create test data add comment to discussion response.
     *
     * @param scenarioDiscussionResponse the scenario discussion response
     * @param currentUser                the current user
     * @param softAssertions             the soft assertions
     * @return the discussion comment response
     */
    public static DiscussionCommentResponse createTestDataAddCommentToDiscussion(ScenarioDiscussionResponse scenarioDiscussionResponse, UserCredentials currentUser, SoftAssertions softAssertions) {
        if (scenarioDiscussionResponse == null) {
            softAssertions.fail("Comment can not be added to the discussion. Parameter is null .");
            return null;
        }

        DiscussionCommentResponse discussionCommentResponse = QmsScenarioDiscussionResources.addCommentToDiscussion(scenarioDiscussionResponse.getIdentity(), RandomStringUtils.randomAlphabetic(12), "ACTIVE", currentUser);
        if (discussionCommentResponse == null) {
            softAssertions.fail("Add Comment to Discussion FAILED.");
        }

        return discussionCommentResponse;
    }

    /**
     * Delete test data.
     *
     * @param scenarioItem       the scenario item
     * @param bidPackageResponse the bid package response
     * @param currentUser        the current user
     */
    public static void deleteTestData(ScenarioItem scenarioItem, BidPackageResponse bidPackageResponse, UserCredentials currentUser) {
        if (bidPackageResponse != null) {
            QmsBidPackageResources.deleteBidPackage(bidPackageResponse.getIdentity(), null, HttpStatus.SC_NO_CONTENT, currentUser);
        }
        if (scenarioItem != null) {
            QmsApiTestUtils.deleteScenarioViaCidApp(scenarioItem, currentUser);
        }
    }

    /**
     * Create and verify project with status scenario item.
     *
     * @param currentUser          the current user
     * @param softAssertions       the soft assertions
     * @param projectAttributesMap the project attributes map
     * @return the scenario item
     */
    public static ScenarioItem createAndVerifyProjectWithStatus(UserCredentials currentUser, SoftAssertions softAssertions, HashMap<String, String> projectAttributesMap) {
        ScenarioItem scenarioItem = createAndPublishScenarioViaCidApp(ProcessGroupEnum.CASTING_DIE, "Casting", currentUser);
        List<BidPackageItemRequest> itemsList = new ArrayList<>();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        QmsProjectResources.createProject(projectAttributesMap,
            itemsList,
            null,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        BidPackageProjectsResponse filteredProjectsResponse = QmsProjectResources.getFilteredProjects(currentUser, "pageNumber,1", "status[EQ]," + projectAttributesMap.get("projectStatus"));
        verifyStatusForFilteredProjects(filteredProjectsResponse, softAssertions, "ALL", projectAttributesMap.get("projectStatus"));
        return scenarioItem;
    }

    /**
     * Verify owner for filtered projects.
     *
     * @param filteredProjectsResponse the filtered projects response
     * @param softAssertions           the soft assertions
     * @param match                    the match
     * @param projectOwner             the project owner
     */
    public static void verifyOwnerForFilteredProjects(BidPackageProjectsResponse filteredProjectsResponse, SoftAssertions softAssertions, String match, String projectOwner) {
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            if (match.equals("ALL")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .allMatch(i -> i.getOwner()
                        .equals(projectOwner))).isTrue();
            } else if (match.equals("NONE")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .noneMatch(i -> i.getOwner()
                        .equals(projectOwner))).isTrue();
            }
        }
    }

    /**
     * Verify status for filtered projects.
     *
     * @param filteredProjectsResponse the filtered projects response
     * @param softAssertions           the soft assertions
     * @param match                    the match
     * @param projectStatus            the project status
     */
    public static void verifyStatusForFilteredProjects(BidPackageProjectsResponse filteredProjectsResponse, SoftAssertions softAssertions, String match, String projectStatus) {
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            if (match.equals("ALL")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .allMatch(i -> i.getStatus()
                        .equals(projectStatus))).as(projectStatus).isTrue();
            } else if (match.equals("NONE")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .noneMatch(i -> i.getStatus()
                        .equals(projectStatus))).as(projectStatus).isTrue();
            }
        }
    }

    /**
     * Verify multiple statuses for filtered projects.
     *
     * @param filteredProjectsResponse the filtered projects response
     * @param softAssertions           the soft assertions
     * @param match                    the match
     * @param projectStatus1           the project status 1
     * @param projectStatus2           the project status 2
     */
    public static void verifyMultipleStatusesForFilteredProjects(BidPackageProjectsResponse filteredProjectsResponse, SoftAssertions softAssertions,
                                                                 String match, String projectStatus1, String projectStatus2) {
        softAssertions.assertThat(filteredProjectsResponse.getIsFirstPage()).isTrue();
        softAssertions.assertThat(filteredProjectsResponse.getItems().size()).isGreaterThan(0);
        if (softAssertions.wasSuccess()) {
            if (match.equals("ALL")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .allMatch(i -> i.getStatus().equals(projectStatus1) ||
                        i.getStatus().equals(projectStatus2))).isTrue();
            } else if (match.equals("NONE")) {
                softAssertions.assertThat(filteredProjectsResponse.getItems().stream()
                    .noneMatch(i -> i.getStatus().equals(projectStatus1) ||
                        i.getStatus().equals(projectStatus2))).isTrue();
            }
        }
    }
}

