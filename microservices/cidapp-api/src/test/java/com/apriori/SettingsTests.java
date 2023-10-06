package com.apriori;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.enums.ColourEnum;
import com.apriori.enums.PreferencesEnum;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestRulesAPI.class)
public class SettingsTests {

    private final String asmPreferenceDefault = "";
    private final String batchSizeDefault = "458";
    private final String selectionColourDefault = ColourEnum.YELLOW.getColour();
    private UserCredentials currentUser;
    private SoftAssertions softAssertions;
    private Map<PreferencesEnum, String> preferencesToReset = new HashMap<>();
    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();

    @AfterEach
    public void resetSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSpecificSettings(currentUser, preferencesToReset);
        }
        preferencesToReset.clear();
    }

    @Test
    @TestRail(id = 17225)
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
    @TestRail(id = 21544)
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
    @TestRail(id = 21545)
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
