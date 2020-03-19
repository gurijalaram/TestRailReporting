package com.apriori.internalapi.services;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.http.builder.service.RequestAreaCds;
import com.apriori.apibase.services.objects.Application;
import com.apriori.apibase.services.objects.Applications;
import com.apriori.apibase.utils.ResponseWrapper;
import com.apriori.internalapi.util.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

import java.util.Arrays;

public class CdsApplications extends TestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3251")
    @Description("API returns a list of all the available applications in the CDS DB")
    public void getAllApplications() {
        url = String.format(url, "applications");

        ResponseWrapper<Applications> response = getRequest(Applications.class, true);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateApplications(response.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = "3700")
    @Description("API returns an application's information based on the supplied identity")
    public void getApplicationById() {
        url = String.format(url,
                String.format("applications/%s", ServiceConnector.urlEncode(Constants.getCdsIdentityApplication())));

        ResponseWrapper<Application> response =  getRequest(Application.class, false);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateApplication(response.getResponseEntity());
    }

    private <T> ResponseWrapper<T> getRequest(Class klass, boolean urlEncoding) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaCds()
        );
    }

    /*
     * Application Validation
     */
    private void validateApplications(Applications applicationsResponse) {
        Object[] applications = applicationsResponse.getResponse().getItems().toArray();
        Arrays.stream(applications)
                .forEach(a -> validate(a));
    }

    private void validateApplication(Application applicationResponse) {
        Application application = applicationResponse.getResponse();
        validate(application);
    }

    private void validate(Object applicationObj) {
        Application application = (Application) applicationObj;
        Assert.assertTrue(application.getIdentity().matches("^#[a-zA-Z0-9]+@[a-zA-Z0-9]+#$"));
    }
}
