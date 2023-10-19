package com.aprioi;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.dfs.models.response.DigitalFactories;
import com.apriori.dfs.models.utils.DigitalFactoryUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.http.HttpStatusCode;

@ExtendWith(TestRulesAPI.class)
public class DigitalFactoriesTests {

    private DigitalFactoryUtil digitalFactoryUtil = new DigitalFactoryUtil();

    @Test
    @TestRail(id = {})
    @Description("Gets a list of digital factories for a specific customer")
    public void getDigitalFactoryTest() {

        ResponseWrapper<DigitalFactories> getComparisonsResponse = digitalFactoryUtil.getDigitalFactories();

        assertThat(getComparisonsResponse.getStatusCode(), is(equalTo(HttpStatusCode.OK)));
    }
}
