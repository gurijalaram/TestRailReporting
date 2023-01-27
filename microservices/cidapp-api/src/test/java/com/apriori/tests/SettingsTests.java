package com.apriori.tests;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ColourEnum;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SettingsTests {

    final String asmPreferenceDefault = "";
    final String batchSizeDefault = "458";
    final String selectionColourDefault = ColourEnum.YELLOW.getColour();

    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

    UserCredentials currentUser;
    SoftAssertions softAssertions;
    Map<PreferencesEnum, String> preferencesToReset = new HashMap<>();

    @After
    public void resetSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSpecificSettings(currentUser, preferencesToReset);
        }
        preferencesToReset.clear();
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
        preferencesToReset.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmPreferenceDefault);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

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

        preferencesToReset.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmPreferenceDefault);
        preferencesToReset.put(PreferencesEnum.DEFAULT_BATCH_SIZE, batchSizeDefault);

        userPreferencesUtil.updatePreferences(currentUser, updatedPreferences);

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

        preferencesToReset.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmPreferenceDefault);
        preferencesToReset.put(PreferencesEnum.DEFAULT_BATCH_SIZE, batchSizeDefault);
        preferencesToReset.put(PreferencesEnum.SELECTION_COLOUR, selectionColourDefault);

        userPreferencesUtil.updatePreferences(currentUser, updatedPreferences);

        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.ASSEMBLY_STRATEGY).getValue())
            .as("Updated Assembly Strategy").isEqualTo(asmStrategy);
        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.DEFAULT_BATCH_SIZE).getValue())
            .as("Updated Batch Size").isEqualTo(batchSize);
        softAssertions.assertThat(userPreferencesUtil.getPreference(currentUser, PreferencesEnum.SELECTION_COLOUR).getValue())
            .as("Updated Batch Size").isEqualTo(selectionColour);


        softAssertions.assertAll();
    }
}