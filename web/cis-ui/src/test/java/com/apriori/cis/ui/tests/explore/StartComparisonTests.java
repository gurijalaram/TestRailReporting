package com.apriori.cis.ui.tests.explore;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.cis.ui.navtoolbars.ExploreTabToolbar;
import com.apriori.cis.ui.pageobjects.compare.ModifyComparisonPage;
import com.apriori.cis.ui.pageobjects.explore.ExplorePage;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

public class StartComparisonTests extends TestBaseUI {

    public StartComparisonTests() {
        super();
    }

    private CisLoginPage loginPage;
    private ExploreTabToolbar exploreTabToolbar;
    private ExplorePage explorePage;
    private ModifyComparisonPage modifyComparisonPage;

    @Test
    @TestRail(id = 9411)
    @Description("Verify Sub Header for Explore View")
    public void testStartComparison() {
        loginPage = new CisLoginPage(driver);
        exploreTabToolbar = loginPage.login(UserUtil.getUser(), ExploreTabToolbar.class);

        assertThat(exploreTabToolbar.isStartComparisonEnabled(), not(true));
    }

    @Test
    @TestRail(id = 9410)
    @Description("Verify Start Comparison button is enabled")
    public void testCreateStartComparison() {
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser(), ExplorePage.class)
            .openFirstScenario();

        assertThat(explorePage.isStartComparisonEnabled(), is(true));

        modifyComparisonPage = explorePage
            .clickStartComparison()
            .clickModify();

        assertThat(modifyComparisonPage.getModifyComparisonHeaderText(), startsWith("Modify Comparison"));
    }
}