package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.ApVersions;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesApi.class)
public class ApVersionsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {5958})
    @Description("Get a list of ap Versions in CDSDb")
    public void getApVersions() {
        ResponseWrapper<ApVersions> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.AP_VERSION, ApVersions.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getVersion()).isNotEmpty();
        soft.assertAll();
    }
}
