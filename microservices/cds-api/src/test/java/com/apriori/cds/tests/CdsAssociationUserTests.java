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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.Collectors;

public class CdsAssociationUserTests {
    private static String customerAssociationUserIdentity;
    private static String customerAssociationUserIdentityEndpoint;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String url;
    private static String customerIdentityEndpoint;
    private static String aPCustomerIdentity;
    private static String associationsEndpoint;
    private static String associationIdentity;
    private static ResponseWrapper<CustomerAssociationResponse> customerAssociationResponse;
    private static String aPStaffIdentity;

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        aPStaffIdentity = Constants.getUserIdentity();
        aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();

        associationsEndpoint = String.format(url + "&pageSize=5000", String.format("customers/%s/customer-associations", aPCustomerIdentity));
        customerAssociationResponse = cdsTestUtil.getCommonRequest(associationsEndpoint, CustomerAssociationResponse.class);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity)).collect(Collectors.toList()).get(0).getIdentity();

    }

    @AfterClass
    public static void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(customerAssociationUserIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(customerIdentityEndpoint);
        }

    }

    @Test
    @TestRail(testCaseId = {"5959"})
    @Description("Get customer association for apriori Internal")
    public void addCustomerUserAssociation() {
        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @Test
    @TestRail(testCaseId = {"5965"})
    @Description("Get users associated for customer")
    public void getAssociationUsers() {
        String associationEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users", aPCustomerIdentity, associationIdentity));

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));

        ResponseWrapper<AssociationUserResponse> users = cdsTestUtil.getCommonRequest(associationEndpoint, AssociationUserResponse.class);
        assertThat(users.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(users.getResponseEntity().getResponse().getTotalItemCount(), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = {"5964"})
    @Description("Get user details for association")
    public void getAssociationByUserIdentity() {
        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));

        ResponseWrapper<AssociationUserItems> associationUserIdentity = cdsTestUtil.getCommonRequest(customerAssociationUserIdentityEndpoint, AssociationUserItems.class);
        assertThat(associationUserIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(associationUserIdentity.getResponseEntity().getUserIdentity(), is(equalTo(aPStaffIdentity)));
    }
}
