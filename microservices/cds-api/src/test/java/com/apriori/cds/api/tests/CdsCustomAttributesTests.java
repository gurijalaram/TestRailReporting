package com.apriori.cds.api.tests;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.IdentityHolder;
import com.apriori.cds.api.models.response.CustomAttribute;
import com.apriori.cds.api.models.response.CustomAttributesResponse;
import com.apriori.cds.api.utils.CdsTestUtil;
import com.apriori.cds.api.utils.CustomerInfrastructure;
import com.apriori.cds.api.utils.RandomCustomerData;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Customer;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CdsCustomAttributesTests {
    private IdentityHolder customAttributesIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CustomerInfrastructure customerInfrastructure = new CustomerInfrastructure();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();

    @AfterEach
    public void cleanUp() {
        if (customAttributesIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID,
                customAttributesIdentityHolder.customerIdentity(),
                customAttributesIdentityHolder.userIdentity(),
                customAttributesIdentityHolder.customAttributeIdentity()
            );
        }
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        customerInfrastructure.cleanUpCustomerInfrastructure(customerIdentity);
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {12365, 12366, 12367})
    @Description("Adding a CustomAttribute for a user and getting it")
    public void addCustomAttribute() {
        setCustomerData();
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);
        soft.assertThat(customAttributeAdded.getResponseEntity().getIdentity()).isNotNull();

        ResponseWrapper<CustomAttributesResponse> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOM_ATTRIBUTES, CustomAttributesResponse.class, HttpStatus.SC_OK, customerIdentity, userIdentity);
        soft.assertThat(customAttributes.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);

        ResponseWrapper<CustomAttribute> putCustomAttribute = cdsTestUtil.putCustomAttribute(customerIdentity, userIdentity, updatedDepartment);
        String customAttributeIdentity = putCustomAttribute.getResponseEntity().getIdentity();

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(customAttributeIdentity)
            .build();

        soft.assertThat(putCustomAttribute.getResponseEntity().getValue()).isEqualTo(updatedDepartment);
        soft.assertAll();
    }

    @Test
    @TestRail(id = {12369, 12370})
    @Description("Get a CustomAttribute by its identity and update")
    public void getCustomAttributeByIdAndUpdate() {
        setCustomerData();
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);

        String attributeIdentity = customAttributeAdded.getResponseEntity().getIdentity();
        ResponseWrapper<CustomAttribute> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class, HttpStatus.SC_OK, customerIdentity, userIdentity, attributeIdentity);

        soft.assertThat(customAttributes.getResponseEntity().getIdentity()).isEqualTo(attributeIdentity);

        ResponseWrapper<CustomAttribute> updateCustomAttribute = cdsTestUtil.updateAttribute(customerIdentity, userIdentity, attributeIdentity, updatedDepartment);

        soft.assertThat(updateCustomAttribute.getResponseEntity().getValue()).isEqualTo(updatedDepartment);
        soft.assertAll();

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(attributeIdentity)
            .build();
    }

    private void setCustomerData() {
        RandomCustomerData rcd = new RandomCustomerData();
        ResponseWrapper<Customer> customer = cdsTestUtil.createCustomer(rcd);
        customerIdentity = customer.getResponseEntity().getIdentity();

        customerInfrastructure.createCustomerInfrastructure(rcd, customerIdentity);

        String userName = generateStringUtil.generateUserName();
        ResponseWrapper<User> user = cdsTestUtil.addUser(customerIdentity, userName, customer.getResponseEntity().getName());
        userIdentity = user.getResponseEntity().getIdentity();
    }
}
