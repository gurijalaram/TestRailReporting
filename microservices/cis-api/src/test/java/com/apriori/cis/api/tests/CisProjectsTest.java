package com.apriori.cis.api.tests;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.api.controller.CisBidPackageProjectResources;
import com.apriori.cis.api.controller.CisProjectResources;
import com.apriori.cis.api.enums.ProjectStatusEnum;
import com.apriori.cis.api.enums.ProjectTypeEnum;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemParameters;
import com.apriori.cis.api.models.request.bidpackage.BidPackageItemRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectRequest;
import com.apriori.cis.api.models.request.bidpackage.BidPackageProjectUserParameters;
import com.apriori.cis.api.models.request.bidpackage.ProjectNotificationRequest;
import com.apriori.cis.api.models.response.bidpackage.BidPackageProjectResponse;
import com.apriori.cis.api.models.response.bidpackage.CisErrorMessage;
import com.apriori.cis.api.models.response.bidpackage.ProjectNotificationResponse;
import com.apriori.cis.api.util.CISTestUtil;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.CustomerUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CisProjectsTest extends CISTestUtil {

    private static SoftAssertions softAssertions;
    private static UserCredentials currentUser;
    private static ScenarioItem scenarioItem;
    private static ComponentInfoBuilder componentInfoBuilder;
    private static BidPackageProjectRequest projectRequestBuilder;
    private String projectName;
    private List<BidPackageItemRequest> itemsList = new ArrayList<>();
    private List<BidPackageProjectUserParameters> usersList = new ArrayList<>();

    @BeforeAll
    public static void beforeClass() {
        currentUser = UserUtil.getUser();
        componentInfoBuilder = new ScenariosUtil().uploadAndPublishComponent(new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING, currentUser));
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName()).get(0);
    }

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        projectName = "PROJ" + new GenerateStringUtil().getRandomNumbers();
        itemsList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .iterationIdentity(scenarioItem.getIterationIdentity())
                .build())
            .build());

        usersList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
            .customerIdentity(CustomerUtil.getCurrentCustomerIdentity())
            .build());
    }

    @Test
    @TestRail(id = {22898, 22904})
    @Description("Create Get and Delete Project")
    public void testCreateGetAndDeleteProject() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.OPEN, ProjectTypeEnum.INTERNAL, itemsList, usersList);

        BidPackageProjectResponse bppResponse = CisProjectResources.createProject(projectRequestBuilder,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);

        BidPackageProjectResponse getProjectResponse = CisProjectResources.getProject(bppResponse.getIdentity(), BidPackageProjectResponse.class, HttpStatus.SC_OK, currentUser);
        softAssertions.assertThat(bppResponse.getName()).isEqualTo(getProjectResponse.getName());

        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);

    }

    @Test
    @TestRail(id = {22900})
    @Description("Create Project with Empty Ids ")
    public void testCreateProjectWithEmptyId() {
        List<BidPackageItemRequest> itemList = new ArrayList<>();
        List<BidPackageProjectUserParameters> userList = new ArrayList<>();
        itemList.add(BidPackageItemRequest.builder()
            .bidPackageItem(BidPackageItemParameters.builder()
                .componentIdentity("")
                .scenarioIdentity("")
                .iterationIdentity("")
                .build())
            .build());

        userList.add(BidPackageProjectUserParameters.builder()
            .userIdentity(new AuthUserContextUtil().getAuthUserIdentity(currentUser.getEmail()))
            .customerIdentity(PropertiesContext.get("${env}.customer_identity"))
            .build());
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.OPEN, ProjectTypeEnum.INTERNAL, itemList, userList);

        CisErrorMessage errorMessageResponse = CisProjectResources.createProject(projectRequestBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {22901})
    @Description("Create Internal Project Without Name ")
    public void testCreateProjectWithOutName() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder("", ProjectStatusEnum.OPEN, ProjectTypeEnum.INTERNAL, itemsList, usersList);

        CisErrorMessage errorMessageResponse = CisProjectResources.createProject(projectRequestBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_BAD_REQUEST,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage()).isEqualTo("One of fields: name, description, status, type, displayName or items aren't provided");
    }

    @Test
    @TestRail(id = {22902})
    @Description("Create Internal Project with Invalid Status ")
    public void testCreateProjectWithInvalidStatus() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.INVALID, ProjectTypeEnum.INTERNAL, itemsList, usersList);

        CisErrorMessage errorMessageResponse = CisProjectResources.createProject(projectRequestBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage().contains("ProjectStatusType.INVALID")).isTrue();
    }

    @Test
    @TestRail(id = {22903})
    @Description("Create Internal Project with Invalid Type ")
    public void testCreateProjectWithInvalidType() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.IN_NEGOTIATION, ProjectTypeEnum.INVALID, itemsList, usersList);

        CisErrorMessage errorMessageResponse = CisProjectResources.createProject(projectRequestBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_INTERNAL_SERVER_ERROR,
            currentUser);

        softAssertions.assertThat(errorMessageResponse.getMessage().contains("ProjectType.INVALID")).isTrue();
    }

    @Test
    @TestRail(id = {22905})
    @Description("Get Internal Project ith Invalid Project ID")
    public void testCreateProjectWithInvalidProjectId() {
        CisErrorMessage getErrorResponse = CisProjectResources.getProject("INVALID", CisErrorMessage.class, HttpStatus.SC_BAD_REQUEST, currentUser);
        softAssertions.assertThat(getErrorResponse.getMessage()).isEqualTo("'identity' is not a valid identity.");
    }

    @Test
    @TestRail(id = {22907})
    @Description("Retrieve Project Based Unread Notifications")
    public void testGetProjectNotifications() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.OPEN, ProjectTypeEnum.INTERNAL, itemsList, usersList);

        BidPackageProjectResponse bppResponse = CisProjectResources.createProject(projectRequestBuilder,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);
        ProjectNotificationRequest projectNotificationRequest = ProjectNotificationRequest.builder()
            .projects(Collections.singletonList(bppResponse.getIdentity()))
            .build();
        ProjectNotificationResponse projectNotificationResponse = CisProjectResources.getProjectNotifications(projectNotificationRequest,
            ProjectNotificationResponse.class,
            HttpStatus.SC_OK,
            currentUser);

        softAssertions.assertThat(projectNotificationResponse.size()).isEqualTo(1);
        softAssertions.assertThat(projectNotificationResponse.get(0).getUnreadNotificationsCount()).isEqualTo(0);

        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @Test
    @TestRail(id = {22899})
    @Description("Create Internal Project using Already used Compoenent and Scenario ID")
    public void testGetProjectAlreadyUsedComponent() {
        projectRequestBuilder = CisProjectResources.getProjectRequestBuilder(projectName, ProjectStatusEnum.OPEN, ProjectTypeEnum.INTERNAL, itemsList, usersList);
        BidPackageProjectResponse bppResponse = CisProjectResources.createProject(projectRequestBuilder,
            BidPackageProjectResponse.class,
            HttpStatus.SC_CREATED,
            currentUser);

        softAssertions.assertThat(bppResponse.getUsers().size()).isEqualTo(1);

        CisErrorMessage cisErrorMessage = CisProjectResources.createProject(projectRequestBuilder,
            CisErrorMessage.class,
            HttpStatus.SC_CONFLICT,
            currentUser);

        softAssertions.assertThat(cisErrorMessage.getMessage().contains("are already in use")).isTrue();
        CisBidPackageProjectResources.deleteBidPackageProject(bppResponse.getBidPackageIdentity(), bppResponse.getIdentity(),
            HttpStatus.SC_NO_CONTENT, currentUser);
    }

    @AfterEach
    public void testCleanup() {
        softAssertions.assertAll();
    }

    @AfterAll
    public static void afterClass() {
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}
