package com.explore;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.pageobjects.navtoolbars.ExploreTabToolbar;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class StartComparisonTests extends TestBase {

    public StartComparisonTests() {
        super();
    }

    private CisLoginPage loginPage;
    private ExploreTabToolbar exploreTabToolbar;
    private ExplorePage explorePage;


    @Test
    @TestRail(testCaseId = "9411")
    @Description("Verify Sub Header for Explore View")
    public void testStartComparison() {
        loginPage = new CisLoginPage(driver);
        exploreTabToolbar = loginPage.login(UserUtil.getUser());

        assertThat(exploreTabToolbar.isStartComparisonEnabled(), not(true));
    }

    @Test
    @Description("Verify Start Comparison button is enabled")
    public void testCreateStartComparison() {
        String componentName = "SS - HOURLY: 2021-11-05T02:57:16";
        String scenarioName = "CIG_06d49e7fa28d";

        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .clickSearch(componentName)
            .highlightScenario(componentName, scenarioName);

        assertThat(explorePage.isStartComparisonEnabled(), is(true));

        exploreTabToolbar = explorePage.clickStartComparison();
    }
}
