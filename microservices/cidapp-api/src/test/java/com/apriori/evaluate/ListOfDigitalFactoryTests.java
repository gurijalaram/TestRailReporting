package com.apriori.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.cidappapi.entity.response.customizations.DigitalFactories;
import com.apriori.cidappapi.utils.CustomizationUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.stream.Collectors;

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