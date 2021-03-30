package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.CustomerAssociationResponse;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.AssociationUserResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsAssociationUserTests  {
    private String url;

    private String customerIdentity;
    private String customerIdentityEndpoint;
    private String customerAssociationUserIdentity;
    private String customerAssociationUserIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(customerAssociationUserIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }

    }

    @Test
    @TestRail(testCaseId = "5959")
    @Description("Get customer association for apriori Internal")
    public void addCustomerUserAssociation() {

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String aPStaffIdentity = Constants.getUserIdentity();
        String aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String associationsEndpoint = String.format(url + "&targetCustomer.identity[EQ]=" + customerIdentity, String.format("customers/%s/customer-associations", aPCustomerIdentity));
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(associationsEndpoint, CustomerAssociationResponse.class);
        String associationIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getResponse().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @Test
    @TestRail(testCaseId = "5965")
    @Description("Get users associated for customer")
    public void getAssociationUsers() {

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String aPStaffIdentity = Constants.getUserIdentity();
        String aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String associationsEndpoint = String.format(url + "&targetCustomer.identity[EQ]=" + customerIdentity, String.format("customers/%s/customer-associations", aPCustomerIdentity));
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(associationsEndpoint, CustomerAssociationResponse.class);
        String associationIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();
        String associationEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users", aPCustomerIdentity, associationIdentity));

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getResponse().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));

        ResponseWrapper<AssociationUserResponse> users = cdsTestUtil.getCommonRequest(associationEndpoint, AssociationUserResponse.class);
        assertThat(users.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(users.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "5964")
    @Description("Get user details for association")
    public void getAssociationByUserIdentity() {

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String aPStaffIdentity = Constants.getUserIdentity();
        String aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        assertThat(customer.getResponseEntity().getResponse().getName(), is(equalTo(customerName)));

        customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        String associationsEndpoint = String.format(url + "&targetCustomer.identity[EQ]=" + customerIdentity, String.format("customers/%s/customer-associations", aPCustomerIdentity));
        ResponseWrapper<CustomerAssociationResponse> response = cdsTestUtil.getCommonRequest(associationsEndpoint, CustomerAssociationResponse.class);
        String associationIdentity = response.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getResponse().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));

        ResponseWrapper<AssociationUserItems> associationUserIdentity = cdsTestUtil.getCommonRequest(customerAssociationUserIdentityEndpoint, AssociationUserItems.class);
        assertThat(associationUserIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(associationUserIdentity.getResponseEntity().getResponse().getUserIdentity(), is(equalTo(aPStaffIdentity)));
    }
}
