package com.apriori;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.models.response.AssociationUserItems;
import com.apriori.cds.models.response.AssociationUserResponse;
import com.apriori.models.response.Customer;
import com.apriori.cds.models.response.CustomerAssociationResponse;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.properties.PropertiesContext;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class CdsAssociationUserTests {
    private String customerAssociationUserIdentity;
    private String customerAssociationUserIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String url;
    private String customerIdentityEndpoint;
    private String aPCustomerIdentity;
    private String associationIdentity;
    private ResponseWrapper<CustomerAssociationResponse> customerAssociationResponse;
    private SoftAssertions soft = new SoftAssertions();

    @BeforeEach
    public void setDetails() {
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

        customerAssociationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS, CustomerAssociationResponse.class, HttpStatus.SC_OK, aPCustomerIdentity);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity)).collect(Collectors.toList()).get(0).getIdentity();

    }

    @AfterEach
    public void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
                aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5959})
    @Description("Get customer association for apriori Internal")
    public void addCustomerUserAssociation() {
        String aPStaffIdentity = PropertiesContext.get("user_identity");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s", aPCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @Test
    @TestRail(id = {5965})
    @Description("Get users associated for customer")
    public void getAssociationUsers() {
        String aPStaffIdentity = PropertiesContext.get("cds.automation_user_identity02");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserResponse> associationUsers = cdsTestUtil.getCommonRequest(CDSAPIEnum.ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS,
            AssociationUserResponse.class,
            HttpStatus.SC_OK,
            aPCustomerIdentity,
            associationIdentity
        );

        soft.assertThat(associationUsers.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);

        ResponseWrapper<AssociationUserItems> users = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            HttpStatus.SC_OK,
            aPCustomerIdentity,
            associationIdentity,
            customerAssociationUserIdentity
        );
        soft.assertThat(users.getResponseEntity().getIdentity()).isEqualTo(customerAssociationUserIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {5964})
    @Description("Get user details for association")
    public void getAssociationByUserIdentity() {
        String aPStaffIdentity = PropertiesContext.get("cds.automation_user_identity03");

        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(aPCustomerIdentity, associationIdentity, aPStaffIdentity);
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserItems> associationUserIdentity = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            HttpStatus.SC_OK,
            aPCustomerIdentity,
            associationIdentity,
            customerAssociationUserIdentity
        );

        soft.assertThat(associationUserIdentity.getResponseEntity().getUserIdentity()).isEqualTo(aPStaffIdentity);
        soft.assertAll();
    }
}
