package com.apriori.cis.ui.tests.explore;

import com.apriori.cis.ui.pageobjects.explore.ExplorePage;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTests extends TestBaseUI {

    public SearchTests() {
        super();
    }

    private CisLoginPage loginPage;
    private ExplorePage explorePage;
    private SoftAssertions softAssertions;

    @BeforeEach
    public void setUp() {
        softAssertions = new SoftAssertions();
    }

    @AfterEach
    public void tearDown() {
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 9556)
    @Description("Component search by name on preset filters")
    public void testSearchByComponentName() {
        String componentName = "ASSEMBLY2";
        loginPage = new CisLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser(), ExplorePage.class)
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