package com.apriori.explore;

import com.apriori.TestBaseUI;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchTests extends TestBaseUI {

    public SearchTests() {
        super();
    }

    private CisLoginPage loginPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {
        softAssertions = new SoftAssertions();
    }

    @After
    public void tearDown() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 9556)
    @Description("Component search by name on preset filters")
    public void testSearchByComponentName() {
        String componentName = "ASSEMBLY2";
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .selectPresetFilter("All")
            .enterKeySearch(componentName);

        softAssertions.assertThat(explorePage.getPresetFilterType()).isEqualTo("All");

        explorePage = explorePage.selectPresetFilter("Private")
            .enterKeySearch(componentName);

        softAssertions.assertThat(explorePage.getPresetFilterType()).isEqualTo("Private");

        explorePage = explorePage.selectPresetFilter("Public")
            .enterKeySearch(componentName);

        softAssertions.assertThat(explorePage.getPresetFilterType()).isEqualTo("Public");

        explorePage = explorePage.selectPresetFilter("Assigned To Me")
            .enterKeySearch(componentName);

        softAssertions.assertThat(explorePage.getPresetFilterType()).isEqualTo("Assigned To Me");

        explorePage = explorePage.selectPresetFilter("Recent")
            .enterKeySearch(componentName);

        softAssertions.assertThat(explorePage.getPresetFilterType()).isEqualTo("Recent");
    }
}
