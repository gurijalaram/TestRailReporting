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
    @Description("Verify user can access create a new project page and verify page elements")
    public void testSaveNewProject() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User")
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser("qa-automation-22@apriori.com")
                .setDueDate("2028","15");

        projectsPage = createNewProjectsPage.saveProject()
                .clickOnUnread();

        softAssertions.assertThat(projectsPage.getProjectName()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsPage.getDueDate()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectOwner()).isNotBlank();
        softAssertions.assertThat(projectsPage.getProjectDetails("Automation Project " + dateTime)).contains("In Negotiation");

        softAssertions.assertAll();
    }
}