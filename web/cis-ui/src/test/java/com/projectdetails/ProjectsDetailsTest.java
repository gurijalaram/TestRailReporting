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
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details")
                .validateProjectDetails(dateTime);

        projectsPage = projectsDetailsPage.clickOnAllProjects();
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

        loginPage = new CisLoginPage(driver);
        projectsDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Details")
                .validateProjectDetailsTabDetails(dateTime,currentUser)
                .clickDetailsPageTab("Parts & Assemblies")
                .validateProjectPartsAndAssembliesTabDetails(componentName,scenarioName);
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

        projectsDetailsPage.clickSave()
                        .validateProjectDetailsAfterEdit(dateTime,currentUser);

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
                .createANewProjectAndOpen("Automation Project " + dateTime,"This Project is created by Automation User " + currentUser.getEmail(), scenarioName,componentName, projectParticipant, "2028","15","Users")
                .validateProjectUserTabDetails(currentUser);

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

        projectsDetailsPage.clickDeleteProject()
                .validateProjectDeleteModalDetails();

        projectsPage = projectsDetailsPage.clickModalCancelProject();

        softAssertions.assertThat(projectsDetailsPage.isDeleteModalDisplayed()).isEqualTo(false);

        projectsPage = projectsDetailsPage.clickModalDeleteProject();

        softAssertions.assertThat(projectsPage.getPageTitle().contains("Projects"));

        projectsPage.clickOnRead();

        softAssertions.assertAll();
    }
}