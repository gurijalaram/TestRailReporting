package com.projectdetails;

import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.createnewproject.CreateNewProjectsPage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.pageobjects.pages.projectsdetails.ProjectsDetailsPage;
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

import com.utils.CisColumnsEnum;
import com.utils.CisProjectStatusEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class ProjectsDetailsTest extends TestBase {

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private ProjectsPage projectsPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private CreateNewProjectsPage createNewProjectsPage;
    private ProjectsDetailsPage projectsDetailsPage;
    private File resourceFile;
    private UserCredentials currentUser;
    private String projectParticipant;
    private String updatedProjectParticipant;

    public ProjectsDetailsTest() {
        super();
    }

    public void validateProjectDetailsTabDetails(String dateTime, UserCredentials currentUser, ProjectsDetailsPage projectsDetailsPage) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplays("Owner")).isNotEmpty();
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplays("Due Date")).isNotEmpty();
        softAssertions.assertThat(projectsDetailsPage.getProjectDetailsTabTitle()).contains("Details");
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplays("Name")).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplays("Description")).contains("This Project is created by Automation User " + currentUser.getEmail());
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"23727","23728","23729","23730"})
    @Description("Verify the project detailed page content of a selected project")
    public void testProjectDetailedPageSkeleton() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-32@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details");

        softAssertions.assertThat(projectsDetailsPage.getProjectDetailsPageTitle()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsDetailsPage.isAllProjectsNavigationDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Details")).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Parts & Assemblies")).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Users")).isEqualTo(true);

        projectsPage = projectsDetailsPage.clickOnAllProjects();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"23907","24445","24446","22447"})
    @Description("Verify the detail and parts and assembly table details in project page")
    public void testProjectDetailsAndPartsAndAssemblies() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-32@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details");

        this.validateProjectDetailsTabDetails(dateTime,currentUser,projectsDetailsPage);

        projectsDetailsPage.clickDetailsPageTab("Parts & Assemblies");

        softAssertions.assertThat(projectsDetailsPage.isShowHideOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isSearchOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isFilterOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(),CisColumnsEnum.SCENARIO_NAME.getColumns(),
                CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
                CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns());

        softAssertions.assertThat(projectsDetailsPage.getListOfScenarios(componentName, scenarioName)).isEqualTo(1);
        softAssertions.assertThat(projectsDetailsPage.getPinnedTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(),CisColumnsEnum.SCENARIO_NAME.getColumns());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"24465","24466","24467"})
    @Description("Verify the user can edit the project details")
    public void testEditProjectDetails() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-32@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();
        updatedProjectParticipant = UserUtil.getUser().getEmail();


        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details")
                .clickEditDetails();

        softAssertions.assertThat(projectsDetailsPage.isEditDetailsModalDisplayed()).isEqualTo(true);

        projectsDetailsPage.clickCancel();

        softAssertions.assertThat(projectsDetailsPage.isEditDetailsModalDisplayed()).isEqualTo(false);

        projectsDetailsPage.clickEditDetails()
                .editProjectName("Automation Project " + dateTime + " Edited")
                .editProjectOwner(updatedProjectParticipant)
                .editDueDate("2029","20")
                .editProjectDescription("This Project is edited by Automation User " + currentUser.getEmail());

        softAssertions.assertThat(projectsDetailsPage.getSaveButtonStatus()).doesNotContain("Mui-disabled");

        projectsDetailsPage.clickSave();

        this.validateProjectDetailsTabDetails(dateTime,currentUser,projectsDetailsPage);

        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsDisplays("Description")).contains("This Project is edited by Automation User " + currentUser.getEmail());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"24471","24472","24473","24474"})
    @Description("Verify the user can see the project user details")
    public void testProjectUserDetails() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-32@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Users");

        softAssertions.assertThat(projectsDetailsPage.isDetailsShowHideOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getUserTableHeaders()).contains("Full Name","Job title");
        softAssertions.assertThat(projectsDetailsPage.isOwnerEmailDisplayed(currentUser.getEmail())).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getProjectOwnerName(currentUser.getEmail())).contains("QA Automation Account");
        softAssertions.assertThat(projectsDetailsPage.isOwnerLabelDisplayed()).isEqualTo(true);

        projectsDetailsPage.hideProjectUserDetails("Full Name");

        softAssertions.assertThat(projectsDetailsPage.getUserTableHeaders()).doesNotContain("Full Name");

        projectsDetailsPage.clickShowAll();
        softAssertions.assertThat(projectsDetailsPage.getUserTableHeaders()).contains("Full Name","Job title");

        projectsDetailsPage.clickHideAll();
        softAssertions.assertThat(projectsDetailsPage.getUserTableHeaders()).doesNotContain("Full Name","Job title");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"25826","25827"})
    @Description("Verify the user can change the project status")
    public void testProjectStatus() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-38@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details");

        softAssertions.assertThat(projectsDetailsPage.isProjectStatusDroDownDisplayed()).isEqualTo(true);

        projectsPage = projectsDetailsPage.changeProjectStatus(CisProjectStatusEnum.IN_PROGRESS.getStatus())
                .navigateToAllProjects()
                .clickOnUnread()
                .searchProject("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getProjectStatus()).contains(CisProjectStatusEnum.IN_PROGRESS.getStatus());

        projectsDetailsPage = projectsPage.clickOnCreatedProject()
                .clickDetailsPageTab("Details");

        projectsPage = projectsDetailsPage.changeProjectStatus(CisProjectStatusEnum.COMPLETED.getStatus())
                .navigateToAllProjects()
                .clickOnUnread()
                .searchProject("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getProjectStatus()).contains(CisProjectStatusEnum.COMPLETED.getStatus());

        projectsDetailsPage = projectsPage.clickOnCreatedProject()
                .clickDetailsPageTab("Details");

        projectsPage = projectsDetailsPage.changeProjectStatus(CisProjectStatusEnum.OPEN.getStatus())
                .navigateToAllProjects()
                .clickOnUnread()
                .searchProject("Automation Project " + dateTime);

        softAssertions.assertThat(projectsPage.getProjectStatus()).contains(CisProjectStatusEnum.OPEN.getStatus());

        projectsPage.clickOnRead();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"25925","25926"})
    @Description("Verify the user can delete the project")
    public void testDeleteProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser().setEmail("qa-automation-38@apriori.com");
        projectParticipant = UserUtil.getUser().getEmail();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details");

        softAssertions.assertThat(projectsDetailsPage.isDeleteButtonDisplayed()).isEqualTo(true);

        projectsDetailsPage.clickDeleteProject();

        softAssertions.assertThat(projectsDetailsPage.isDeleteModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.getDeleteConfirmation()).contains("This will permanently delete this Project and all data.");
        softAssertions.assertThat(projectsDetailsPage.isModalDeleteButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isModalCancelButtonDisplayed()).isEqualTo(true);

        projectsPage = projectsDetailsPage.clickModalCancelProject();

        softAssertions.assertThat(projectsDetailsPage.isDeleteModalDisplayed()).isEqualTo(false);

        projectsPage = projectsDetailsPage.clickModalDeleteProject();

        softAssertions.assertThat(projectsPage.getPageTitle().contains("Projects"));

        projectsPage.clickOnRead();

        softAssertions.assertAll();
    }
}