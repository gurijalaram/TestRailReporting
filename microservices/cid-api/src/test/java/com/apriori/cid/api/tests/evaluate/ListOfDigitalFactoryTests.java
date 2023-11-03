package com.apriori.cid.api.tests.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import com.apriori.cid.api.models.response.customizations.Customizations;
import com.apriori.cid.api.models.response.customizations.DigitalFactories;
import com.apriori.cid.api.utils.CustomizationUtil;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class ListOfDigitalFactoryTests {

    private CustomizationUtil customizationUtil = new CustomizationUtil();

    @Test
    @TestRail(id = 5415)
    @Description("Get List of Digital Factory")
    public void getListOfDigitalFactory() {
        final UserCredentials user = UserUtil.getUser();

        ResponseWrapper<Customizations> customizations = customizationUtil.getCustomizations(user);

        assertThat(customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getDigitalFactories().stream()
                .map(DigitalFactories::getName))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList()), hasItems(DigitalFactoryEnum.getNames()));
    }
}