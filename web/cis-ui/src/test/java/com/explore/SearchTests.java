package com.explore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

public class SearchTests extends TestBase {

    public SearchTests() {
        super();
    }

    private CisLoginPage loginPage;
    private ExplorePage explorePage;

    @Test
    @TestRail(testCaseId = "9556")
    @Description("Component search by name on preset filters")
    public void testSearchByComponentName() {
        String componentName = "ASSEMBLY2";
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectPresetFilter("All")
            .enterKeySearch(componentName);

        assertThat(explorePage.getPresetFilterType(), is(equalTo("All")));

        explorePage = explorePage.selectPresetFilter("Private")
            .enterKeySearch(componentName);

        assertThat(explorePage.getPresetFilterType(), is(equalTo("Private")));

        explorePage = explorePage.selectPresetFilter("Public")
            .enterKeySearch(componentName);

        assertThat(explorePage.getPresetFilterType(), is(equalTo("Public")));

        explorePage = explorePage.selectPresetFilter("Assigned To Me")
            .enterKeySearch(componentName);

        assertThat(explorePage.getPresetFilterType(), is(equalTo("Assigned To Me")));

        explorePage = explorePage.selectPresetFilter("Recent")
            .enterKeySearch(componentName);

        assertThat(explorePage.getPresetFilterType(), is(equalTo("Recent")));
    }
}
