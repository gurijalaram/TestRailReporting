package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.objects.response.Application;
import com.apriori.cds.objects.response.Applications;
import com.apriori.cds.objects.response.Customers;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class CdsApplicationsTests {
    private String url;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3251")
    @Description("API returns a list of all the available applications in the CDS DB")
    public void getAllApplications() {
        url = String.format(url, "applications");

        ResponseWrapper<Applications> response = cdsTestUtil.getResponse(url, Applications.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getIsSingleTenant(), CoreMatchers.is(notNullValue()));
    }

    @Test
    @TestRail(testCaseId = "3700")
    @Description("API returns an application's information based on the supplied identity")
    public void getApplicationById() {
        url = String.format(url, String.format("applications/%s", Constants.getApProApplicationIdentity()));

        ResponseWrapper<Application> response = cdsTestUtil.getResponse(url, Application.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getName(), is(equalTo("aPriori Professional")));
    }

    @Test
    @TestRail(testCaseId = "5811")
    @Description(" API returns a paged list of customers authorized to use a particular application")
    public void getCustomersAuthorizedForApplication() {
        url = String.format(url, String.format("applications/%s/customers", Constants.getApProApplicationIdentity()));

        ResponseWrapper<Customers> response = cdsTestUtil.getResponse(url, Customers.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
    }
}
