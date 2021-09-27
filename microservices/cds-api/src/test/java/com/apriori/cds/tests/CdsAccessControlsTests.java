package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.AccessControlResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

public class CdsAccessControlsTests  {
    private String userIdentity;
    private String customerIdentity;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private IdentityHolder accessControlIdentityHolder;
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();


    @After
    public void cleanUp() {
        if (accessControlIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_ACCESS_CONTROL_BY_CUSTOMER_USER_CONTROL_IDS,
                accessControlIdentityHolder.customerIdentity(),
                accessControlIdentityHolder.userIdentity(),
                accessControlIdentityHolder.accessControlIdentity()
            );
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"3294"})
    @Issue("MIC-1972")
    @Description("Adding out of context access control")
    public void postAccessControl() {

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String userName = generateStringUtil.generateUserName();

        ResponseWrapper<Customer> customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();

        ResponseWrapper<AccessControlResponse> accessControlResponse = cdsTestUtil.addAccessControl(customerIdentity, userIdentity);
        String accessControlIdentity = accessControlResponse.getResponseEntity().getResponse().getIdentity();

        accessControlIdentityHolder =  IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .accessControlIdentity(accessControlIdentity)
            .build();

        assertThat(accessControlResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(accessControlResponse.getResponseEntity().getResponse().getOutOfContext(), is(true));
    }
}
