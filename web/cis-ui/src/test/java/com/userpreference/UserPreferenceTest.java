package com.userpreference;

import com.apriori.pageobjects.pages.login.CisLoginPage;
import com.apriori.pageobjects.pages.settings.UserPreferencePage;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CisUserPreferenceItemsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class UserPreferenceTest extends TestBase {

    public UserPreferenceTest() {
        super();
    }

    private CisLoginPage loginPage;
    private UserPreferencePage userPreferencePage;

    @Test
    @TestRail(testCaseId = {"16464","16465","16466"})
    @Description("Verify user can view the user preferences")
    public void testUserPreferenceModal() {
        loginPage = new CisLoginPage(driver);
        userPreferencePage = loginPage.cisLogin(UserUtil.getUser())
                .clickUserIcon()
                .clickSettings();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(userPreferencePage.isPreferencesModalDisplayed()).isTrue();
        softAssertions.assertThat(userPreferencePage.getUserPreferenceItems().contains(CisUserPreferenceItemsEnum.DISPLAY_PREFERENCES.getItems()));
        softAssertions.assertThat(userPreferencePage.getUserPreferenceItems().contains(CisUserPreferenceItemsEnum.PROFILE.getItems()));

        userPreferencePage.clickCancelButton();
        softAssertions.assertThat(userPreferencePage.isPreferencesModalDisplayed()).isFalse();

        softAssertions.assertAll();

    }
}