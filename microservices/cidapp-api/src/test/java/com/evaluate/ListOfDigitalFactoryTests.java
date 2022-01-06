package com.evaluate;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidappapi.entity.response.customizations.Customizations;
import com.apriori.cidappapi.entity.response.customizations.DigitalFactories;
import com.apriori.cidappapi.utils.CustomizationUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import io.qameta.allure.Description;
import org.junit.Test;

import java.util.stream.Collectors;

public class ListOfDigitalFactoryTests {

    private CustomizationUtil customizationUtil;

    @Test
    @TestRail(testCaseId = {"5415"})
    @Description("Get List of Digital Factory")
    public void getListOfDigitalFactory() {
        customizationUtil = new CustomizationUtil(new JwtTokenUtil(UserUtil.getUser()).retrieveJwtToken());

        ResponseWrapper<Customizations> customizations = customizationUtil.getCustomizations();

        assertThat(customizations.getResponseEntity().getItems().stream()
            .map(x -> x.getDigitalFactories().stream()
                .map(DigitalFactories::getName))
            .findAny()
            .orElseThrow(AssertionError::new)
            .collect(Collectors.toList()), hasItems(DigitalFactoryEnum.getNames()));
    }
}