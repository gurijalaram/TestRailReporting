package com.apriori.cis.ui.tests.userpreference;

import com.apriori.cis.ui.navtoolbars.LeftHandNavigationBar;
import com.apriori.cis.ui.pageobjects.login.CisLoginPage;
import com.apriori.cis.ui.pageobjects.myuser.MyUserPage;
import com.apriori.cis.ui.pageobjects.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
import com.apriori.cis.ui.pageobjects.settings.UserPreferencePage;
import com.apriori.cis.ui.utils.CisDisplayPreferenceEnum;
import com.apriori.cis.ui.utils.CisUserPreferenceItemsEnum;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserPreferenceTest extends TestBaseUI {

    public UserPreferenceTest() {
        super();
    }

    private CisLoginPage loginPage;
    private UserPreferencePage userPreferencePage;
    private PartsAndAssembliesDetailsPage partsAndAssembliesDetailsPage;
    private File resourceFile;
    private UserCredentials currentUser;

    @Test
    @TestRail(id = {16464, 16465, 16466})
    @Description("Verify user can view the user preferences")
    public void testUserPreferenceModal() {
        loginPage = new CisLoginPage(driver);
        userPreferencePage = loginPage.cisLogin(UserUtil.getUser())
                .clickUserIcon()
                .clickSettings();

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(userPreferencePage.isPreferencesModalDisplayed()).isTrue();
        softAssertions.assertThat(userPreferencePage.getSubmitButtonState()).contains("Mui-disabled");
        softAssertions.assertThat(userPreferencePage.getUserPreferenceItems()).contains(CisUserPreferenceItemsEnum.DISPLAY_PREFERENCES.getItems());
        softAssertions.assertThat(userPreferencePage.getUserPreferenceItems()).contains(CisUserPreferenceItemsEnum.PROFILE.getItems());

        userPreferencePage.clickCancelButton();
        softAssertions.assertThat(userPreferencePage.isPreferencesModalDisplayed()).isFalse();

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {16674, 16677, 16679, 16681, 16683, 16686, 16687, 16688})
    @Description("Verify user can change the display preferences")
    public void testDisplayPreferences() {
        String scenarioName = new GenerateStringUtil().generateStringForAutomation("Scenario");
        String componentName = "ChampferOut";

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, componentName + ".SLDPRT");
        currentUser = UserUtil.getUser();

        loginPage = new CisLoginPage(driver);
        partsAndAssembliesDetailsPage = loginPage.cisLogin(currentUser)
                .uploadAndCostScenario(componentName, scenarioName, resourceFile, currentUser, ProcessGroupEnum.SHEET_METAL, DigitalFactoryEnum.APRIORI_USA)
                .clickPartsAndAssemblies()
                .sortDownCreatedAtField()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName).clickOnComponentName(componentName);

        userPreferencePage = new MyUserPage(driver)
                .selectUserPreference()
                .selectUnits("Custom")
                .selectLength("Meter")
                .selectMass("Gram")
                .selectTime("Hour")
                .selectDecimalPlace("2");

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(userPreferencePage.getSubmitButtonState()).doesNotContain("Mui-disabled");
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.UNITS.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.LENGTH.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.MASS.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.TIME.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.DECIMAL_PLACE.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.LANGUAGE.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.CURRENCY.getFields());
        softAssertions.assertThat(userPreferencePage.getDisplayPreferenceFields()).contains(CisDisplayPreferenceEnum.EXCHANGE_RATE_TABLE.getFields());

        userPreferencePage.clickSubmitButton();

        partsAndAssembliesDetailsPage = new LeftHandNavigationBar(driver)
                .clickPartsAndAssemblies()
                .sortDownCreatedAtField()
                .clickSearchOption()
                .clickOnSearchField()
                .enterAComponentName(componentName)
                .clickOnComponentName(componentName)
                .clickMaterialStockIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getLengthType()).contains("m");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getCostType()).contains("kg");

        partsAndAssembliesDetailsPage.clickMaterialPropertiesIcon();

        softAssertions.assertThat(partsAndAssembliesDetailsPage.getTimeType()).contains("h");
        softAssertions.assertThat(partsAndAssembliesDetailsPage.getDecimalPlaces()).contains(".");

        userPreferencePage = new MyUserPage(driver)
                .selectUserPreference()
                .selectLength("Millimeter")
                .selectMass("Kilogram")
                .selectTime("Millisecond")
                .selectDecimalPlace("0")
                .clickSubmitButton();

        softAssertions.assertAll();
    }
}