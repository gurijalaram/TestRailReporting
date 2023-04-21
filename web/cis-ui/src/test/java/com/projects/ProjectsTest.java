package com.projects;

import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;

public class ProjectsTest extends TestBase {

    public ProjectsTest() {
        super();
    }

    private CisLoginPage loginPage;
    private LeftHandNavigationBar leftHandNavigationBar;
    private ProjectsPage projectsPage;

    @Ignore("Disabled until 1.2.0 release")
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
}