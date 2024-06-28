package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.ApVersions;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class ApVersionsTests {
    private CdsTestUtil cdsTestUtil;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void init() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
    }

    @Test
    @TestRail(id = {5958})
    @Description("Get a list of ap Versions in CDSDb")
    public void getApVersions() {
        ApVersions response = cdsTestUtil.getCommonRequest(CDSAPIEnum.AP_VERSION, ApVersions.class, HttpStatus.SC_OK).getResponseEntity();

        soft.assertThat(response.getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getItems().stream().findAny().get().getVersion()).isNotEmpty();
        soft.assertAll();
    }
}
