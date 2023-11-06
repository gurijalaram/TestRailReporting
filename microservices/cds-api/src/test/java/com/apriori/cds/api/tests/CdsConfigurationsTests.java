package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.ConfigurationResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsConfigurationsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @Disabled("Not relevant for now")
    @TestRail(id = {5966})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getBlacklistedEmailDomains() {
        ResponseWrapper<ConfigurationResponse> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.CONFIGURATIONS_EMAIL, ConfigurationResponse.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getDomain()).isNotNull();
        soft.assertAll();
    }
}
