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

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.io.File;

public class ProjectsDetailsTest extends TestBase {

    public ProjectsDetailsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private ProjectsPage projectsPage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private CreateNewProjectsPage createNewProjectsPage;
    private ProjectsDetailsPage projectsDetailsPage;
    private File resourceFile;
    private UserCredentials currentUser;

    @Test
    @TestRail(testCaseId = {"23727","23728","23729","23730"})
    @Description("Verify the project detailed page content of a selected project")
    public void testProjectDetailedPageSkeleton() {

        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "ChampferOut";
        String dateTime = DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ);

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        SoftAssertions softAssertions = new SoftAssertions();

        loginPage = new CisLoginPage(driver);
        createNewProjectsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName,scenarioName,resourceFile,currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_BRAZIL)
                .clickProjects()
                .clickOnCreateNewProject()
                .typeProjectName("Automation Project " + dateTime)
                .typeProjectDescription("This Project is created by Automation User")
                .clickOnAddNewButton()
                .selectAPart(scenarioName,componentName)
                .clickAdd()
                .selectAUser("qa-automation-22@apriori.com")
                .setDueDate("2028","15");

        projectsDetailsPage = createNewProjectsPage.saveProject()
                .clickOnUnread()
                .clickOnCreatedProject();

        softAssertions.assertThat(projectsDetailsPage.getProjectDetailsPageTitle()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(projectsDetailsPage.isAllProjectsNavigationDisplayed()).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Details")).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Parts & Assemblies")).isEqualTo(true);
        softAssertions.assertThat(projectsDetailsPage.isProjectDetailsPageTabsDisplayed("Users")).isEqualTo(true);

        projectsPage = projectsDetailsPage.clickOnAllProjects();

        softAssertions.assertAll();
    }
}