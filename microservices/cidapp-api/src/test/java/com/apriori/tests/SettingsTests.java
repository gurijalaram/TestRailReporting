package com.apriori.tests;

import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsTests {

    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

    UserCredentials currentUser;
    SoftAssertions softAssertions;

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @TestRail(testCaseId = "17225")
    @Description("Update Assembly Strategy Only")
    public void testUpdateAsmStrategy() {

        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();

        String asmStrategy = "PREFER_PUBLIC";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.putPreferences(currentUser, updateStrategy);

        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.ASSEMBLY_STRATEGY).getValue())
            .as("Updated Assembly Strategy").isEqualTo(asmStrategy);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "21544")
    @Description("Update Assembly Strategy Only")
    public void testUpdateAsmStrategyWithOtherPreference() {

        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();

        String asmStrategy = "PREFER_PRIVATE";
        String batchSize = "600";

        Map<PreferencesEnum, String> updatedPreferences = new HashMap<>();
        updatedPreferences.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);
        updatedPreferences.put(PreferencesEnum.DEFAULT_BATCH_SIZE, batchSize);

        userPreferencesUtil.putPreferences(currentUser, updatedPreferences);

        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.ASSEMBLY_STRATEGY).getValue())
            .as("Updated Assembly Strategy").isEqualTo(asmStrategy);
        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.DEFAULT_BATCH_SIZE).getValue())
                .as("Updated Batch Size").isEqualTo(batchSize);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "21545")
    @Description("Update Assembly Strategy Only")
    public void testUpdateAsmStrategyWithOtherPreferences() {

        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();

        String asmStrategy = "PREFER_PUBLIC";
        String batchSize = "750";
        String selectionColour = "#061282";

        Map<PreferencesEnum, String> updatedPreferences = new HashMap<>();
        updatedPreferences.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);
        updatedPreferences.put(PreferencesEnum.DEFAULT_BATCH_SIZE, batchSize);
        updatedPreferences.put(PreferencesEnum.SELECTION_COLOUR, selectionColour);

        userPreferencesUtil.putPreferences(currentUser, updatedPreferences);

        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.ASSEMBLY_STRATEGY).getValue())
            .as("Updated Assembly Strategy").isEqualTo(asmStrategy);
        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.DEFAULT_BATCH_SIZE).getValue())
            .as("Updated Batch Size").isEqualTo(batchSize);
        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.SELECTION_COLOUR).getValue())
            .as("Updated Batch Size").isEqualTo(selectionColour);


        softAssertions.assertAll();
    }
}
