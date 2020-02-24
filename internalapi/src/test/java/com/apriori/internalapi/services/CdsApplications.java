package com.apriori.internalapi.services;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.objects.Application;
import com.apriori.apibase.services.objects.Applications;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class CdsApplications {    
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
        Applications response = (Applications)ServiceConnector.getService(url, Applications.class);   
        validateApplications(response);
    }
    

    @Test
    @TestRail(testCaseId = "3700")
    @Description("API returns an application's information based on the supplied identity")
    public void getApplicationById() throws MalformedURLException, URISyntaxException { 
        url = String.format(url, 
            String.format("applications/%s", ServiceConnector.urlEncode(Constants.getCdsIdentityApplication())));
        Application response = (Application)ServiceConnector.getServiceNoEncoding(url, Application.class);
        validateApplication(response);
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
        Application application = (Application)applicationResponse.getResponse();
        validate(application);
    }
    
    private void validate(Object applicationObj) {
        Application application = (Application)applicationObj;
        Assert.assertTrue(application.getIdentity().matches("^#[a-zA-Z0-9]+@[a-zA-Z0-9]+#$"));
    }
}
