package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.apriori.cds.entity.response.ConfigurationResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class CdsConfigurationsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"5966"})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getBlacklistedEmailDomains() {
        ResponseWrapper<ConfigurationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CONFIGURATIONS_EMAIL, ConfigurationResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getDomain(), is(not(nullValue())));
    }
}
