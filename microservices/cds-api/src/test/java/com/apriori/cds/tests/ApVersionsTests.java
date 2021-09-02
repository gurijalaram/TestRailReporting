package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.objects.response.ApVersions;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class ApVersionsTests {
    private String url;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = {"5958"})
    @Description("Get a list of ap Versions in CDSDb")
    public void getApVersions() {
        url = String.format(url, "ap-versions");

        ResponseWrapper<ApVersions> response = cdsTestUtil.getCommonRequest(url, ApVersions.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getVersion(), is(not(emptyString())));
    }
}
