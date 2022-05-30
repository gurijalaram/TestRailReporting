package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.CustomerAssociationResponse;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AccessAuthorization;
import com.apriori.cds.objects.response.AccessAuthorizations;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.StatusAccessAuthorizations;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import com.apriori.utils.properties.PropertiesContext;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class CdsAccessAuthorizationsTests {
    private IdentityHolder accessAuthorizationIdentityHolder;
    private static String customerAssociationUserIdentity;
    private static String customerAssociationUserIdentityEndpoint;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static String customerName;
    private static String cloudRef;
    private static String emailPattern;
    private static String customerIdentity;
    private static String aPCustomerIdentity;
    private static String associationIdentity;
    private static String aPStaffIdentity;
    private static String url;
    private static ResponseWrapper<CustomerAssociationResponse> customerAssociationResponse;
    private static ResponseWrapper<AssociationUserItems> associationUser;

    @BeforeClass
    public static void setDetails() {
        url = Constants.getServiceUrl();
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        emailPattern = "\\S+@".concat(customerName);
        aPStaffIdentity = PropertiesContext.get("${env}.user_identity");

        customer = cdsTestUtil.addCASCustomer(customerName, cloudRef, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();
        customerAssociationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS, CustomerAssociationResponse.class,aPCustomerIdentity);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity)).collect(Collectors.toList()).get(0).getIdentity();
        associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @After
    public void deleteAccessAuthorization() {
        if (accessAuthorizationIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.ACCESS_AUTHORIZATION_By_ID,
                accessAuthorizationIdentityHolder.customerIdentity(),
                accessAuthorizationIdentityHolder.accessAuthorizationIdentity()
            );
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
                aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"13115", "13116"})
    @Description("Creating a new access authorization for customer and getting it")
    public void addAccessAuthorization() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");

        assertThat(accessAuthorization.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(accessAuthorization.getResponseEntity().getUserIdentity(), is(equalTo(aPStaffIdentity)));
        String accessAuthorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorizations> authorizationsResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATIONS, AccessAuthorizations.class, customerIdentity);

        assertThat(authorizationsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(authorizationsResponse.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        accessAuthorizationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .accessAuthorizationIdentity(accessAuthorizationIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"13117"})
    @Description("Getting status report of access authorizations")
    public void getStatusOfAccessAuthorization() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");
        LocalDateTime creationDate = accessAuthorization.getResponseEntity().getCreatedAt();
        String accessAuthorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();
        ResponseWrapper<StatusAccessAuthorizations> statusResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATION_STATUS, StatusAccessAuthorizations.class, customerIdentity);

        assertThat(statusResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(statusResponse.getResponseEntity().getResponse().get(0).getAssignedAt(), is(equalTo(creationDate)));

        accessAuthorizationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .accessAuthorizationIdentity(accessAuthorizationIdentity)
            .build();
    }

    @Test
    @TestRail(testCaseId = {"13118", "13119"})
    @Description("Getting status report of access authorizations")
    public void getAccessAuthorizationByID() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");
        String authorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorization> authorizationsResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATION_By_ID, AccessAuthorization.class, customerIdentity, authorizationIdentity);

        assertThat(authorizationsResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(authorizationsResponse.getResponseEntity().getIdentity(), is(equalTo(authorizationIdentity)));

        ResponseWrapper<String> deleteAuthorizationAccess = cdsTestUtil.delete(CDSAPIEnum.ACCESS_AUTHORIZATION_By_ID, customerIdentity, authorizationIdentity);

        assertThat(deleteAuthorizationAccess.getStatusCode(), is(equalTo(HttpStatus.SC_NO_CONTENT)));
    }
}