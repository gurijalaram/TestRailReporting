package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.response.AssociationUserItems;
import com.apriori.cds.api.models.response.AssociationUserResponse;
import com.apriori.cds.api.models.response.CustomerAssociationResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.Constants;
import com.apriori.cds.api.utils.CustomerUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsAssociationUserTests {
    private String customerAssociationUserIdentity;
    private String customerAssociationUserIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil;
    private CustomerUtil customerUtil;
    private ResponseWrapper<Customer> customer;
    private String customerName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String url;
    private String customerIdentityEndpoint;
    private String apCustomerIdentity;
    private String associationIdentity;
    private ResponseWrapper<CustomerAssociationResponse> customerAssociationResponse;
    private SoftAssertions soft = new SoftAssertions();
    private User user = UserUtil.getUser().getUserDetails();

    @BeforeEach
    public void setDetails() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        cdsTestUtil = new CdsTestUtil(requestEntityUtil);
        customerUtil = new CustomerUtil(requestEntityUtil);

        url = Constants.getServiceUrl();

        customerName = generateStringUtil.generateAlphabeticString("Customer", 6);
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateNumericString("SFID", 10);
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = customerUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));

        apCustomerIdentity = user.getCustomerIdentity();

        customerAssociationResponse = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMERS_ASSOCIATIONS, CustomerAssociationResponse.class, HttpStatus.SC_OK, apCustomerIdentity);
        associationIdentity = customerAssociationResponse.getResponseEntity().getItems().stream().filter(target -> target.getTargetCustomerIdentity().equals(customerIdentity))
            .toList().get(0).getIdentity();
    }

    @AfterEach
    public void cleanUp() {
        if (customerAssociationUserIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
                apCustomerIdentity, associationIdentity, customerAssociationUserIdentity);
        }
        if (customerIdentityEndpoint != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {5959})
    @Description("Get customer association for apriori Internal")
    public void addCustomerUserAssociation() {
        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(apCustomerIdentity, associationIdentity, user.getIdentity());
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();
        customerAssociationUserIdentityEndpoint = String.format(url, String.format("customers/%s/customer-associations/%s/customer-association-users/%s",
            apCustomerIdentity, associationIdentity, customerAssociationUserIdentity));
    }

    @Test
    @TestRail(id = {5965})
    @Description("Get users associated for customer")
    public void getAssociationUsers() {
        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(apCustomerIdentity, associationIdentity, user.getIdentity());
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserResponse> associationUsers = cdsTestUtil.getCommonRequest(CDSAPIEnum.ASSOCIATIONS_BY_CUSTOMER_ASSOCIATIONS_IDS,
            AssociationUserResponse.class,
            HttpStatus.SC_OK,
            apCustomerIdentity,
            associationIdentity
        );

        soft.assertThat(associationUsers.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);

        ResponseWrapper<AssociationUserItems> users = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            HttpStatus.SC_OK,
            apCustomerIdentity,
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
        ResponseWrapper<AssociationUserItems> associationUser = cdsTestUtil.addAssociationUser(apCustomerIdentity, associationIdentity, user.getIdentity());
        customerAssociationUserIdentity = associationUser.getResponseEntity().getIdentity();

        ResponseWrapper<AssociationUserItems> associationUserIdentity = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOMER_ASSOCIATION_USER_BY_ID,
            AssociationUserItems.class,
            HttpStatus.SC_OK,
            apCustomerIdentity,
            associationIdentity,
            customerAssociationUserIdentity
        );

        soft.assertThat(associationUserIdentity.getResponseEntity().getUserIdentity()).isEqualTo(user.getIdentity());
        soft.assertAll();
    }
}
