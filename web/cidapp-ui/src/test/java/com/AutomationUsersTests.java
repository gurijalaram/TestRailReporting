package com;

import com.apriori.cidappapi.entity.response.preferences.PreferenceResponse;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.enums.UnitsEnum;
import com.apriori.reader.file.user.UserUtil;

import com.utils.CurrencyEnum;
import com.utils.DecimalPlaceEnum;
import com.utils.LengthEnum;
import com.utils.MassEnum;
import com.utils.TimeEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

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
