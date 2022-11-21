package com.apriori.cds.tests;

import com.apriori.cds.entity.response.ConfigurationResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;

public class CdsConfigurationsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @Ignore("Not relevant for now")
    @TestRail(testCaseId = {"5966"})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getBlacklistedEmailDomains() {
        ResponseWrapper<ConfigurationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CONFIGURATIONS_EMAIL, ConfigurationResponse.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getDomain()).isNotNull();
        soft.assertAll();
    }
}
