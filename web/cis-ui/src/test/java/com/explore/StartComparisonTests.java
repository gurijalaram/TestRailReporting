package com.explore;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.apriori.pageobjects.navtoolbars.CompareTabToolbar;
import com.apriori.pageobjects.navtoolbars.ExploreTabToolbar;
import com.apriori.pageobjects.pages.compare.ModifyComparisonPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
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
    private CompareTabToolbar compareTabToolbar;
    private ModifyComparisonPage modifyComparisonPage;

    @Test
    @TestRail(testCaseId = "9411")
    @Description("Verify Sub Header for Explore View")
    public void testStartComparison() {
        loginPage = new CisLoginPage(driver);
        exploreTabToolbar = loginPage.login(UserUtil.getUser());

        assertThat(exploreTabToolbar.isStartComparisonEnabled(), not(true));
    }

    @Test
    @TestRail(testCaseId = "9410")
    @Description("Verify Start Comparison button is enabled")
    public void testCreateStartComparison() {
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .openFirstScenario();

        assertThat(explorePage.isStartComparisonEnabled(), is(true));

        modifyComparisonPage = explorePage
            .clickStartComparison()
            .clickModify();

        assertThat(modifyComparisonPage.getModifyComparisonHeaderText(), startsWith("Modify Comparison"));
    }
}
