package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Application;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class CdsApplicationsTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"3251"})
    @Description("API returns a list of all the available applications in the CDS DB")
    public void getAllApplications() {
        ResponseWrapper<Applications> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_APPLICATION, Applications.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getItems().get(0).getIsSingleTenant(), CoreMatchers.is(notNullValue()));
    }

    @Test
    @TestRail(testCaseId = {"3700"})
    @Description("API returns an application's information based on the supplied identity")
    public void getApplicationById() {
        ResponseWrapper<Application> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_APPLICATION_BY_ID,
            Application.class,
            Constants.getApProApplicationIdentity()
        );

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo("aPriori Professional")));
    }

    @Test
    @TestRail(testCaseId = {"5811"})
    @Description(" API returns a paged list of customers authorized to use a particular application")
    public void getCustomersAuthorizedForApplication() {
        ResponseWrapper<Customers> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOMER_APPLICATION_BY_ID,
            Customers.class,
            Constants.getApProApplicationIdentity()
        );

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}
