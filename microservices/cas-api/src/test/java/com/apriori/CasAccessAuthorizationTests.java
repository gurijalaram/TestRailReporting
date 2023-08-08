package com.apriori;

import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.models.response.AccessAuthorization;
import com.apriori.cas.models.response.AccessAuthorizations;
import com.apriori.cas.models.response.CasErrorMessage;
import com.apriori.cas.models.response.Customer;
import com.apriori.cas.models.response.CustomerAssociation;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.IdentityHolder;
import com.apriori.cds.objects.response.AssociationUserItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.models.AuthorizationUtil;
import com.apriori.properties.PropertiesContext;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CasAccessAuthorizationTests {
    private final CasTestUtil casTestUtil = new CasTestUtil();
    private final CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private IdentityHolder accessAuthorizationIdentityHolder;
    private String customerAssociationUserIdentity;
    private String customerAssociationUserIdentityEndpoint;
    private Customer customer;
    private String url;
    private String customerIdentity;
    private String aPCustomerIdentity;
    private CustomerAssociation customerAssociationToAprioriInternal;
    private ResponseWrapper<AssociationUserItems> associationUser;
    private String associationIdentity;
    private String aPStaffIdentity;
    private SoftAssertions soft = new SoftAssertions();


    @BeforeEach
    public void setup() {
        url = Constants.getServiceUrl();
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        aPCustomerIdentity = casTestUtil.getAprioriInternal().getIdentity();
        aPStaffIdentity = PropertiesContext.get("user_identity");
        customer = casTestUtil.createCustomer().getResponseEntity();
        customerIdentity = customer.getIdentity();
        customerAssociationToAprioriInternal = casTestUtil.findCustomerAssociation(casTestUtil.getAprioriInternal(), customer);
        associationIdentity = customerAssociationToAprioriInternal.getIdentity();
        associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));

    }

    @AfterEach
    public void cleanUp() {
        if (accessAuthorizationIdentityHolder != null) {
            casTestUtil.delete(CASAPIEnum.ACCESS_AUTHORIZATION_BY_ID,
                accessAuthorizationIdentityHolder.customerIdentity(),
                accessAuthorizationIdentityHolder.accessAuthorizationIdentity());
        }
        if (customerAssociationUserIdentityEndpoint != null) {
            casTestUtil.delete(CASAPIEnum.CUSTOMER_ASSOCIATIONS_USER,
                aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @Description("Creating a new access authorization for customer and getting it")
    @TestRail(id = {5586, 12177})
    public void addAccessAuthorization() {
        ResponseWrapper<AccessAuthorization> requestAccess = casTestUtil.addAccessAuthorization(AccessAuthorization.class, customerIdentity, aPStaffIdentity, "service-account.1", HttpStatus.SC_CREATED);
        soft.assertThat(requestAccess.getResponseEntity().getUserIdentity()).isEqualTo(aPStaffIdentity);
        String accessAuthorizationId = requestAccess.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorizations> accessAuthorizations = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_AUTHORIZATIONS, AccessAuthorizations.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(accessAuthorizations.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();

        accessAuthorizationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .accessAuthorizationIdentity(accessAuthorizationId)
            .build();
    }

    @Test
    @Description("Return a single access authorization by its identity.")
    @TestRail(id = {16547, 12183})
    public void getAccessAuthorizationById() {
        ResponseWrapper<AccessAuthorization> requestAccess = casTestUtil.addAccessAuthorization(AccessAuthorization.class, customerIdentity, aPStaffIdentity, "service-account.1", HttpStatus.SC_CREATED);
        String accessAuthorizationId = requestAccess.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorization> authorizationResponse = casTestUtil.getCommonRequest(CASAPIEnum.ACCESS_AUTHORIZATION_BY_ID, AccessAuthorization.class, HttpStatus.SC_OK, customerIdentity, accessAuthorizationId);
        soft.assertThat(authorizationResponse.getResponseEntity().getUserIdentity()).isEqualTo(aPStaffIdentity);
        soft.assertAll();

        casTestUtil.delete(CASAPIEnum.ACCESS_AUTHORIZATION_BY_ID, customerIdentity, accessAuthorizationId);
    }

    @Test
    @Description("Add access authorization for a user other than yourself")
    @TestRail(id = {12181})
    public void authorizeAnotherUser() {
        ResponseWrapper<CasErrorMessage> requestAccess = casTestUtil.addAccessAuthorization(CasErrorMessage.class, customerIdentity, "000000000000", "service-account.1", HttpStatus.SC_FORBIDDEN);
        soft.assertThat(requestAccess.getResponseEntity().getMessage()).isEqualTo("You are not allowed to manage access authorizations for a user other than yourself");
        soft.assertAll();
    }
}