package com;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

public class AutomationUsersTests {
    @Test
    @Description("Resets automation users preferences")
    public void resetAutomationsUsersPreferences() {
        UserUtil.getUsers().forEach(user -> new UserPreferencesUtil().resetSettings(user));
    }
}
