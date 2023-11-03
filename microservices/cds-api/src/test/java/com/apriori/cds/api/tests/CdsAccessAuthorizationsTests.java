package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.AccessAuthorization;
import com.apriori.cds.api.models.response.AccessAuthorizations;
import com.apriori.cds.api.models.response.AssociationUserItems;
import com.apriori.cds.api.models.response.CustomerAssociationResponse;
import com.apriori.cds.api.models.response.StatusAccessAuthorization;
import com.apriori.cds.api.models.response.StatusAccessAuthorizations;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsAccessAuthorizationsTests {
    private IdentityHolder accessAuthorizationIdentityHolder;
    private String customerAssociationUserIdentity;
    private String customerAssociationUserIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String emailPattern;
    private String customerIdentity;
    private String aPCustomerIdentity;
    private String associationIdentity;
    private String aPStaffIdentity;
    private String url;
    private ResponseWrapper<CustomerAssociationResponse> customerAssociationResponse;
    private ResponseWrapper<AssociationUserItems> associationUser;
    private SoftAssertions soft = new SoftAssertions();
    private UserCredentials currentUser = UserUtil.getUser();

    @BeforeEach
    public void setDetails() {
        url = Constants.getServiceUrl();
        customerName = generateStringUtil.generateCustomerName();
        cloudRef = generateStringUtil.generateCloudReference();
        emailPattern = "\\S+@".concat(customerName);
        aPStaffIdentity = PropertiesContext.get("user_identity");

        customer = cdsTestUtil.addCASCustomer(customerName, cloudRef, emailPattern, currentUser);
        customerIdentity = customer.getResponseEntity().getIdentity();
        aPCustomerIdentity = Constants.getAPrioriInternalCustomerIdentity();
        customerAssociationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS, CustomerAssociationResponse.class, HttpStatus.SC_OK, aPCustomerIdentity);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity)).collect(Collectors.toList()).get(0).getIdentity();
        associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @AfterEach
    public void cleanUp() {
        if (accessAuthorizationIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.ACCESS_AUTHORIZATION_BY_ID,
                accessAuthorizationIdentityHolder.customerIdentity(),
                accessAuthorizationIdentityHolder.accessAuthorizationIdentity()
            );
        }
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
                aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {13115, 13116})
    @Description("Creating a new access authorization for customer and getting it")
    public void addAccessAuthorization() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");
        soft.assertThat(accessAuthorization.getResponseEntity().getUserIdentity()).isEqualTo(aPStaffIdentity);
        String accessAuthorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorizations> authorizationsResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATIONS, AccessAuthorizations.class, HttpStatus.SC_OK, customerIdentity);
        soft.assertThat(authorizationsResponse.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertAll();

        accessAuthorizationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .accessAuthorizationIdentity(accessAuthorizationIdentity)
            .build();
    }

    @Test
    @TestRail(id = {13117})
    @Description("Getting status report of access authorizations")
    public void getStatusOfAccessAuthorization() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");
        LocalDateTime creationDate = accessAuthorization.getResponseEntity().getCreatedAt();
        String accessAuthorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();

        ResponseWrapper<StatusAccessAuthorizations> statusResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATION_STATUS, StatusAccessAuthorizations.class, HttpStatus.SC_OK, customerIdentity);
        List<StatusAccessAuthorization> assigned = statusResponse.getResponseEntity().getResponse().stream().filter(status -> status.getStatus().equals("ASSIGNED")).collect(Collectors.toList());
        soft.assertThat(assigned.get(0).getAssignedAt()).isEqualTo(creationDate);
        soft.assertAll();

        accessAuthorizationIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .accessAuthorizationIdentity(accessAuthorizationIdentity)
            .build();
    }

    @Test
    @TestRail(id = {13118, 13119})
    @Description("Getting status report of access authorizations")
    public void getAccessAuthorizationByID() {
        ResponseWrapper<AccessAuthorization> accessAuthorization = cdsTestUtil.addAccessAuthorization(customerIdentity, aPStaffIdentity, "service-account.1");
        String authorizationIdentity = accessAuthorization.getResponseEntity().getIdentity();

        ResponseWrapper<AccessAuthorization> authorizationsResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.ACCESS_AUTHORIZATION_BY_ID, AccessAuthorization.class, HttpStatus.SC_OK, customerIdentity, authorizationIdentity);
        soft.assertThat(authorizationsResponse.getResponseEntity().getIdentity()).isEqualTo(authorizationIdentity);
        soft.assertAll();

        cdsTestUtil.delete(CDSAPIEnum.ACCESS_AUTHORIZATION_BY_ID, customerIdentity, authorizationIdentity);
    }
}