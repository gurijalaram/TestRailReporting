package com.apriori.apitests.cds;

import com.apriori.apibase.services.cds.objects.Application;
import com.apriori.apibase.services.cds.objects.Applications;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.dao.ServiceConnector;
import com.apriori.utils.http.builder.service.RequestAreaCds;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
                .forEach(this::validate);
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
