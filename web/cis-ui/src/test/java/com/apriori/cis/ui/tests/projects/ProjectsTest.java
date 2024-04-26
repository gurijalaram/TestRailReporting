package com.apriori.cis.ui.tests.projects;

import static com.apriori.css.api.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.css.api.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.shared.util.enums.ScenarioStateEnum.COST_COMPLETE;

import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.createnewproject.CreateNewProjectsPage;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.pageobjects.projects.ProjectsPage;
import com.apriori.cis.ui.utils.CisScenarioResultsEnum;
import com.apriori.css.api.utils.CssComponent;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ProjectsTest extends TestBaseUI {

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private ProjectsPage projectsPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private CreateNewProjectsPage createNewProjectsPage;
    private File resourceFile;
    private UserCredentials currentUser;
    private String projectParticipant;
    private String secondProjectParticipant;
    private ComponentInfoBuilder componentInfoBuilder;
    private ScenariosUtil scenarioUtil;
    private ScenarioItem scenarioItem;
    private SoftAssertions softAssertions;
    private String userComment;
    private static final String componentName = "ChampferOut";

    public ProjectsTest() {
        super();
    }

    @BeforeEach
    public void setUp() {
        softAssertions = new SoftAssertions();
        userComment = new GenerateStringUtil().getRandomString();
        componentInfoBuilder = new ComponentRequestUtil().getComponentWithProcessGroup(componentName, ProcessGroupEnum.SHEET_METAL);
        currentUser = componentInfoBuilder.getUser();
        leftHandNavigationBar = new CisLoginPage(driver).cisLogin(currentUser);
    }

    @Test
    @TestRail(id = {16841, 16842, 22685})
    @Description("Verify user can navigate to the projects page")
    public void testProjectPageNavigation() {
        softAssertions.assertThat(leftHandNavigationBar.isProjectsBtnDisplayed()).isEqualTo(true);
        projectsPage = leftHandNavigationBar.clickProjects();
        softAssertions.assertThat(projectsPage.getPageTitle()).contains("Projects");
        softAssertions.assertThat(projectsPage.isCreateNewProjectsOptionDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {22686, 22687, 22707, 17217})
    @Description("Verify user can access create a new project page and verify page elements")
    public void testCreateAProjectPageContent() {
        createNewProjectsPage = leftHandNavigationBar.clickProjects().clickOnCreateNewProject();

        softAssertions.assertThat(createNewProjectsPage.isProjectNameFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isProjectDescriptionFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isAddPartsAndAssembliesOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isInviteTeamMembersFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isDueDateFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isCreateProjectButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isCancelProjectCreationDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickOnDueDatePicker();

        softAssertions.assertThat(createNewProjectsPage.getMonthSelectorStatus("Previous month")).contains("Mui-disabled");
        softAssertions.assertThat(createNewProjectsPage.getMonthSelectorStatus("Next month")).doesNotContain("Mui-disabled");

        projectsPage = createNewProjectsPage.clickOnCancelProject();

        softAssertions.assertThat(projectsPage.isCreateNewProjectsOptionDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {22688, 22708, 24002, 17216, 17218, 25957})
    @Description("Verify user can save a new project")
    public void testSaveNewProject() {
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProject("Automation Project " + dateTime, "This Project is created by Automation User" + currentUser.getEmail(),
                componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName(), projectParticipant, "2028", "15")
            .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.getDueDate()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOrganization()).contains("aPriori Internal");
        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("Open");

        projectsPage.clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();
    }

    @Test
    @TestRail(id = {22749, 22750, 22751, 22752, 22753})
    @Description("Verify user can search project by project name")
    public void testSearchProject() {
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectParticipant = currentUser.getEmail();
        projectsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProject("Automation Project " + dateTime, "This Project is created by Automation User" + currentUser.getEmail(),
                componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName(), projectParticipant, "2028", "15");

        softAssertions.assertThat(projectsPage.isSearchProjectButtonDisplayed()).isEqualTo(true);

        projectsPage.searchProject("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.getDueDate()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("Open");
        softAssertions.assertThat(projectsPage.getProjectOrganization()).contains("aPriori Internal");
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectParticipants()).isNotBlank();

        projectsPage.removeSearch();
        softAssertions.assertThat(projectsPage.isSearchProjectButtonDisplayed()).isEqualTo(true);
        projectsPage.clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();
    }

    @Test
    @TestRail(id = {23609, 23610, 23717, 23725, 23779, 23780})
    @Description("Verify user can select parts and assemblies for a new project")
    public void testSelectPartsAndAssembliesForProject() {
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectParticipant = currentUser.getEmail();
        createNewProjectsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickProjects()
            .clickOnCreateNewProject()
            .typeProjectName("Automation Project " + dateTime)
            .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
            .clickOnAddNewButton();

        softAssertions.assertThat(createNewProjectsPage.isAddPartsModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isShowHideFieldOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isSearchTableOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isPartsAndAssembliesTableDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickSearch();

        softAssertions.assertThat(createNewProjectsPage.isSearchFieldDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickOnSearchField().enterAComponentName(componentInfoBuilder.getComponentName());

        softAssertions.assertThat(createNewProjectsPage.getAddedComponentName()).isEqualTo(componentInfoBuilder.getComponentName());
        softAssertions.assertThat(createNewProjectsPage.getListOfScenarios(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName())).isEqualTo(1);

        createNewProjectsPage.clickClearOption();

        softAssertions.assertThat(createNewProjectsPage.getTableRecordsCount()).isNotEqualTo(1);

        createNewProjectsPage.selectAPart(componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName()).clickAdd();

        softAssertions.assertThat(createNewProjectsPage.isAddedPartComponentAndScenarioNamesDisplayed(componentInfoBuilder.getComponentName())).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isAddedPartComponentAndScenarioNamesDisplayed(componentInfoBuilder.getComponentName())).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isRemovePartIconDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickOnRemovePart()
            .clickOnAddNewButton()
            .selectAPart(componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName())
            .clickAdd()
            .selectAUser(projectParticipant)
            .setDueDate("2028", "15");

        projectsPage = createNewProjectsPage.saveProject();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        projectsPage.clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();

    }

    @Test
    @TestRail(id = {23792, 23793, 23794, 23795})
    @Description("Verify project users can be selected for a new project")
    public void testSelectUsersForProject() {
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectParticipant = currentUser.getEmail();
        secondProjectParticipant = "qa-automation-22@apriori.com";
        createNewProjectsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickProjects()
            .clickOnCreateNewProject()
            .typeProjectName("Automation Project " + dateTime)
            .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
            .clickOnAddNewButton()
            .selectAPart(componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName())
            .clickAdd();

        softAssertions.assertThat(createNewProjectsPage.isInviteMembersOptionDisplayed()).isEqualTo(true);

        createNewProjectsPage.selectAUser(projectParticipant)
            .selectAUser(secondProjectParticipant);

        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(projectParticipant)).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(secondProjectParticipant)).isEqualTo(true);

        createNewProjectsPage.clickOnRemoveUser();

        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(projectParticipant)).isEqualTo(false);

        createNewProjectsPage.setDueDate("2028", "15");

        projectsPage = createNewProjectsPage.saveProject();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        projectsPage.clickOnCreatedProject().clickDeleteProject().clickModalDeleteProject();
    }

    @Disabled("Disabled after COL-1952 released to QA")
    @Test
    @TestRail(id = {22909, 22910, 22911, 22912, 22913})
    @Description("Verify user can filter a project by name")
    public void testFilterByProjectName() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        projectParticipant = currentUser.getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProject("Automation Project " + dateTime, "This Project is created by Automation User" + currentUser.getEmail(), scenarioName, componentName, projectParticipant, "2028", "15")
            .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.isProjectFilterOptionDisplayed()).isEqualTo(true);

        projectsPage.clickOnFilterOption();

        softAssertions.assertThat(projectsPage.isFilterAddConditionDisplayed()).isEqualTo(true);

        projectsPage.clickOnAddCondition();

        softAssertions.assertThat(projectsPage.isFilterFiledDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isFilterConditionTypeDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isFilterValueDisplayed()).isEqualTo(true);

        projectsPage.addProjectNameToFilter("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getListOfProject("Automation Project " + dateTime)).isEqualTo(1);
        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        softAssertions.assertAll();
    }

    @Disabled("Disabled after COL-1952 released to QA")
    @Test
    @TestRail(id = {22914, 22915})
    @Description("Verify user can filter a project by status")
    public void testFilterByStatus() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        projectParticipant = currentUser.getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProject("Automation Project " + dateTime, "This Project is created by Automation User" + currentUser.getEmail(), scenarioName, componentName, projectParticipant, "2028", "15")
            .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        projectsPage.clickOnFilterOption()
            .clickOnAddCondition()
            .selectProjectStatus("Open");

        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("Open");

        softAssertions.assertAll();
    }

    @Disabled("Disabled after COL-1952 released to QA")
    @Test
    @TestRail(id = {23790, 23791})
    @Description("Verify user can filter a project by due date")
    public void testFilterByDueDate() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        projectParticipant = currentUser.getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsPage = loginPage.cisLogin(currentUser)
            .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
            .clickProjects()
            .clickOnCreateNewProject()
            .createANewProject("Automation Project " + dateTime, "This Project is created by Automation User" + currentUser.getEmail(), scenarioName, componentName, projectParticipant, "2028", "15")
            .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        projectsPage.clickOnFilterOption()
            .clickOnAddCondition()
            .selectProjectDueDate("2028", "15");

        softAssertions.assertThat(projectsPage.getFilteredDueDate("Automation Project " + dateTime)).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {22688, 24001})
    @Description("Verify create new project page validations")
    public void testCreateProjectValidations() {
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectParticipant = currentUser.getEmail();
        createNewProjectsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickProjects()
            .clickOnCreateNewProject();

        softAssertions.assertThat(createNewProjectsPage.getProjectCreateStatus()).contains("Mui-disabled");

        createNewProjectsPage.typeProjectName("Automation Project " + dateTime)
            .clearProjectName();

        softAssertions.assertThat(createNewProjectsPage.isProjectNameRequiredValidationDisplayed()).isEqualTo(true);

        createNewProjectsPage.typeProjectName("Automation Project " + dateTime)
            .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
            .clickOnAddNewButton().selectAPart(componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName())
            .clickAdd();

        softAssertions.assertThat(createNewProjectsPage.getProjectCreateStatus()).contains("Mui-disabled");

        createNewProjectsPage.selectAUser(projectParticipant)
            .setDueDate("2028", "15");

        softAssertions.assertThat(createNewProjectsPage.getProjectCreateStatus()).doesNotContain("Mui-disabled");
    }

    @Test
    @TestRail(id = {23611})
    @Description("Verify existing project components are disabled for parts selection")
    public void testPartsAddingValidationForANewProject() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);
        projectParticipant = currentUser.getEmail();
        partsAndAssembliesDetailsPage = leftHandNavigationBar.uploadAndCostScenario(componentInfoBuilder)
            .clickPartsAndAssemblies()
            .sortDownCreatedAtField()
            .searchAndClickComponent(componentInfoBuilder.getComponentName(), componentInfoBuilder.getScenarioName())
            .clickMessageIconOnCommentSection()
            .clickOnAttribute()
            .selectAttribute(CisScenarioResultsEnum.ANNUAL_VOLUME.getFieldName())
            .addComment(userComment)
            .clickComment()
            .selectCreatedDiscussion();

        createNewProjectsPage = leftHandNavigationBar.clickProjects()
            .clickOnCreateNewProject()
            .typeProjectName("Automation Project " + dateTime)
            .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
            .clickOnAddNewButton();

        softAssertions.assertThat(createNewProjectsPage.getComponentStatus(componentInfoBuilder.getScenarioName(), componentInfoBuilder.getComponentName())).contains("Mui-disabled");
    }

    @AfterEach
    public void testCleanUp() {
        softAssertions.assertAll();
        scenarioItem = new CssComponent().getWaitBaseCssComponents(componentInfoBuilder.getUser(), COMPONENT_NAME_EQ.getKey() + componentInfoBuilder.getComponentName(),
            SCENARIO_NAME_EQ.getKey() + componentInfoBuilder.getScenarioName(), SCENARIO_STATE_EQ.getKey() + COST_COMPLETE).get(0);
        new ScenariosUtil().deleteScenario(scenarioItem.getComponentIdentity(), scenarioItem.getScenarioIdentity(), currentUser);
    }
}