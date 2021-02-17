package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.CustomerAssociationItems;
import com.apriori.cds.entity.response.CustomerAssociationResponse;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CdsCustomerAssociationTests extends CdsTestUtil {
    private String url;

    private String customerIdentity;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = "5387")
    @Description("Get customer association for apriori Internal")
    public void getCustomerAssociations() {
        url = String.format(url, "customers/L2H992828N8M/customer-associations");

        ResponseWrapper<CustomerAssociationResponse> response = getCommonRequest(url, true, CustomerAssociationResponse.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getTargetCustomerIdentity(), is(not(nullValue())));
    }

    @Test
    @TestRail(testCaseId = "5387")
    @Description("Get customer association by association Identity")
    public void getCustomerAssociationByIdentity() {
        String associationEndpoint = String.format(url, "customers/L2H992828N8M/customer-associations");

        ResponseWrapper<CustomerAssociationResponse> response = getCommonRequest(associationEndpoint, true, CustomerAssociationResponse.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));

        String associationIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String associationIdentityEndpoint = String.format(url, ("customers/L2H992828N8M/customer-associations/".concat(associationIdentity)));
        ResponseWrapper<CustomerAssociationItems> association = getCommonRequest(associationIdentityEndpoint, true, CustomerAssociationItems.class);

        assertThat(association.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(association.getResponseEntity().getResponse().getIdentity(), is(equalTo(associationIdentity)));
        assertThat(association.getResponseEntity().getResponse().getDescription(), containsString("is a customer of aPriori"));
    }
}
