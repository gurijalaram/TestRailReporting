package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.response.CustomerAssociationResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.AssociationUserResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;

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

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();

        customerAssociationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS, CustomerAssociationResponse.class, aPCustomerIdentity);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity)).collect(Collectors.toList()).get(0).getIdentity();

    }

    @AfterClass
    public static void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
                aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"5959"})
    @Description("Get customer association for apriori Internal")
    public void addCustomerUserAssociation() {
        String aPStaffIdentity = PropertiesContext.get("${env}.user_identity");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @Test
    @TestRail(testCaseId = {"5965"})
    @Description("Get users associated for customer")
    public void getAssociationUsers() {
        String aPStaffIdentity = PropertiesContext.get("${env}.cds.automation_user_identity02");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserItems> users = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            aPCustomerIdentity,
            associationIdentity,
            customerAssociationUserIdentity
        );

        assertThat(users.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }

    @Test
    @TestRail(testCaseId = {"5964"})
    @Description("Get user details for association")
    public void getAssociationByUserIdentity() {
        String aPStaffIdentity = PropertiesContext.get("${env}.cds.automation_user_identity03");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        assertThat(associationUser.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserItems> associationUserIdentity = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            aPCustomerIdentity,
            associationIdentity,
            customerAssociationUserIdentity
        );

        assertThat(associationUserIdentity.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(associationUserIdentity.getResponseEntity().getUserIdentity(), is(equalTo(aPStaffIdentity)));
    }
}
