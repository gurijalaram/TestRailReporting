package com.projects;

import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.createnewproject.CreateNewProjectsPage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class ProjectsTest extends TestBase {

    public ProjectsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private ProjectsPage projectsPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private CreateNewProjectsPage createNewProjectsPage;
    private File resourceFile;
    private UserCredentials currentUser;
    private String projectParticipant;
    private String secondProjectParticipant;

    @Test
    @TestRail(testCaseId = {"16841","16842","22685"})
    @Description("Verify user can navigate to the projects page")
    public void testProjectPageNavigation() {
        loginPage = new CisLoginPage(driver);
        leftHandNavigationBar = loginPage.cisLogin(UserUtil.getUser());

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(leftHandNavigationBar.isProjectsBtnDisplayed()).isEqualTo(true);

        projectsPage = leftHandNavigationBar.clickProjects();

        softAssertions.assertThat(projectsPage.getPageTitle().contains("Projects"));
        softAssertions.assertThat(projectsPage.isCreateNewProjectsOptionDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22686","22687","22707","17217"})
    @Description("Verify user can access create a new project page and verify page elements")
    public void testCreateAProjectPageContent() {
        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(UserUtil.getUser())
                .clickProjects()
                .clickOnCreateNewProject();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(projectsPage.isProjectNameFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isProjectDescriptionFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isAddPartsAndAssembliesOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isInviteTeamMembersFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isDueDateFieldDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isCreateProjectButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsPage.isCancelProjectCreationDisplayed()).isEqualTo(true);

        projectsPage.clickOnDueDatePicker();

        softAssertions.assertThat(projectsPage.getMonthSelectorStatus("Previous month")).contains("Mui-disabled");
        softAssertions.assertThat(projectsPage.getMonthSelectorStatus("Next month")).doesNotContain("Mui-disabled");

        projectsPage.clickOnCancelProject();

        softAssertions.assertThat(projectsPage.isCreateNewProjectsOptionDisplayed()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22688","22708","24001","24002","17216","17218"})
    @Description("Verify user can save a new project")
    public void testSaveNewProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.getDueDate()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOrganization()).contains("aPriori Internal");
        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("In Negotiation");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22749","22750","22751","22752","22753"})
    @Description("Verify user can search project by project name")
    public void testSearchProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_BRAZIL)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.isSearchProjectButtonDisplayed()).isEqualTo(true);

        projectsPage.searchProject("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.getDueDate()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("In Negotiation");
        softAssertions.assertThat(projectsPage.getProjectOrganization()).contains("aPriori Internal");
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectParticipants()).isNotBlank();

        projectsPage.removeSearch();

        softAssertions.assertThat(projectsPage.isSearchProjectButtonDisplayed()).isEqualTo(true);

        projectsPage.clickOnUnread();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"23609","23610","23717","23725","23779","23780"})
    @Description("Verify user can select parts and assemblies for a new project")
    public void testSelectPartsAndAssembliesForProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_BRAZIL)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton();

        softAssertions.assertThat(createNewProjectsPage.isAddPartsModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isShowHideFieldOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isFilterTableOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isSearchTableOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isPartsAndAssembliesTableDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickSearch();

        softAssertions.assertThat(createNewProjectsPage.isSearchFieldDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickOnSearchField()
                .enterAComponentName(componentName);

        softAssertions.assertThat(createNewProjectsPage.getAddedComponentName()).isEqualTo(componentName);
        softAssertions.assertThat(createNewProjectsPage.getListOfScenarios(componentName,scenarioName)).isEqualTo(1);

        createNewProjectsPage.clickClearOption();

        softAssertions.assertThat(createNewProjectsPage.getListOfScenarios(componentName,scenarioName)).isNotEqualTo(1);

        createNewProjectsPage.selectAPart(scenarioName,componentName)
                .clickAdd();

        softAssertions.assertThat(createNewProjectsPage.isAddedPartComponentAndScenarioNamesDisplayed(componentName)).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isAddedPartComponentAndScenarioNamesDisplayed(scenarioName)).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isRemovePartIconDisplayed()).isEqualTo(true);

        createNewProjectsPage.clickOnRemovePart()
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"23792","23793","23794","23795"})
    @Description("Verify project users can be selected for a new project")
    public void testSelectUsersForProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();
        secondProjectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_BRAZIL)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd();

        softAssertions.assertThat(createNewProjectsPage.isInviteMembersOptionDisplayed()).isEqualTo(true);

        createNewProjectsPage.selectAUser(projectParticipant)
                .selectAUser(secondProjectParticipant);

        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(projectParticipant)).isEqualTo(true);
        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(secondProjectParticipant)).isEqualTo(true);

        createNewProjectsPage.clickOnRemoveUser();

        softAssertions.assertThat(createNewProjectsPage.isAddedUsersDisplayed(projectParticipant)).isEqualTo(false);

        createNewProjectsPage.setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"22909","22910","22911","22912","22913"})
    @Description("Verify user can filter a project by name")
    public void testFilterByProjectName() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
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

    @Test
    @TestRail(testCaseId = {"22914","22915"})
    @Description("Verify user can filter a project by status")
    public void testFilterByStatus() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        projectsPage.clickOnFilterOption()
                .clickOnAddCondition()
                .selectProjectStatus("In Negotiation");

        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("In Negotiation");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"23790","23791"})
    @Description("Verify user can filter a project by due date")
    public void testFilterByDueDate() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User" + currentUser.getEmail())
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser(projectParticipant)
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);

        projectsPage.clickOnFilterOption()
                .clickOnAddCondition()
                .selectProjectDueDate("2028","15");

        softAssertions.assertThat(projectsPage.getFilteredDueDate("Automation Project " + dateTime)).isNotEmpty();

        softAssertions.assertAll();
    }
}