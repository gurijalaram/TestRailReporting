package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.ApVersions;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class ApVersionsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"5958"})
    @Description("Get a list of ap Versions in CDSDb")
    public void getApVersions() {
        ResponseWrapper<ApVersions> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.AP_VERSION, ApVersions.class, HttpStatus.SC_OK);

        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getVersion(), is(not(emptyString())));
    }
}
