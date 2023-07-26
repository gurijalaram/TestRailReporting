package com.apriori.cds.tests;

import com.apriori.GenerateStringUtil;
import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.entity.response.Customer;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.CustomAttribute;
import com.apriori.cds.objects.response.CustomAttributesResponse;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CdsCustomAttributesTests {
    private IdentityHolder customAttributesIdentityHolder;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private ResponseWrapper<Customer> customer;
    private ResponseWrapper<User> user;
    private String customerName;
    private String userName;
    private String cloudRef;
    private String salesForceId;
    private String emailPattern;
    private String customerIdentity;
    private String userIdentity;
    private SoftAssertions soft = new SoftAssertions();

    @Before
    public void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);
        String customerType = Constants.CLOUD_CUSTOMER;

        customer = cdsTestUtil.addCustomer(customerName, customerType, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @After
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
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(id = {12365, 12366, 12367})
    @Description("Adding a CustomAttribute for a user and getting it")
    public void addCustomAttribute() {
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
}
