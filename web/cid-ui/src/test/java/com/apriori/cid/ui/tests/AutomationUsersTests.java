package com.apriori.cid.ui.tests;

import com.apriori.cid.api.models.response.preferences.PreferenceResponse;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.utils.CurrencyEnum;
import com.apriori.cid.ui.utils.DecimalPlaceEnum;
import com.apriori.cid.ui.utils.LengthEnum;
import com.apriori.cid.ui.utils.MassEnum;
import com.apriori.cid.ui.utils.TimeEnum;
import com.apriori.shared.util.enums.UnitsEnum;
import com.apriori.shared.util.file.user.UserUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AutomationUsersTests {

    @Test
    @Description("Resets automation users preferences")
    public void resetAutomationsUsersPreferences() {
        SoftAssertions softAssertions = new SoftAssertions();

        UserUtil.getUsers().forEach(user -> {
            new UserPreferencesUtil().resetSettings(user);

            List<PreferenceResponse> userPreferencesUtilList = new UserPreferencesUtil().getPreferences(user);
            softAssertions.assertThat(userPreferencesUtilList.stream()
                    .map(PreferenceResponse::getValue)
                    .collect(Collectors.toList()))
                .containsAll(Arrays.asList(CurrencyEnum.USD.getCurrency(),
                    UnitsEnum.MMKS.getUnits(),
                    LengthEnum.MILLIMETER.getLength(),
                    MassEnum.KILOGRAM.getMass(),
                    TimeEnum.SECOND.getTime(),
                    DecimalPlaceEnum.TWO.getDecimalPlaces()));

            softAssertions.assertAll();
        });
    }
}
