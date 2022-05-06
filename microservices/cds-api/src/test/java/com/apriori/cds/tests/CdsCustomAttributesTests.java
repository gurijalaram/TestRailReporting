package com.apriori.cds.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cds.entity.IdentityHolder;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.CustomAttribute;
import com.apriori.cds.objects.response.CustomAttributesResponse;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CdsCustomAttributesTests {
    private IdentityHolder customAttributesIdentityHolder;
    private static GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private static CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private static ResponseWrapper<Customer> customer;
    private static ResponseWrapper<User> user;
    private static String customerName;
    private static String userName;
    private static String cloudRef;
    private static String salesForceId;
    private static String emailPattern;
    private static String customerIdentity;
    private static String userIdentity;

    @BeforeClass
    public static void setDetails() {
        customerName = generateStringUtil.generateCustomerName();
        userName = generateStringUtil.generateUserName();
        cloudRef = generateStringUtil.generateCloudReference();
        salesForceId = generateStringUtil.generateSalesForceId();
        emailPattern = "\\S+@".concat(customerName);

        customer = cdsTestUtil.addCustomer(customerName, cloudRef, salesForceId, emailPattern);
        customerIdentity = customer.getResponseEntity().getIdentity();

        user = cdsTestUtil.addUser(customerIdentity, userName, customerName);
        userIdentity = user.getResponseEntity().getIdentity();
    }

    @After
    public void deleteAttributes() {
        if (customAttributesIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOM_ATTRIBUTE,
                    customAttributesIdentityHolder.customerIdentity(),
                    customAttributesIdentityHolder.userIdentity(),
                    customAttributesIdentityHolder.customAttributeIdentity()
            );
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (customerIdentity != null && userIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS, customerIdentity, userIdentity);
        }
        if (customerIdentity != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID, customerIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = {"12365", "12366", "12367"})
    @Description("Adding a CustomAttribute for a user and getting it")
    public void addCustomAttribute() {
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);

        assertThat(customAttributeAdded.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));

        ResponseWrapper<CustomAttributesResponse> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOM_ATTRIBUTES, CustomAttributesResponse.class, customerIdentity, userIdentity);

        assertThat(customAttributes.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customAttributes.getResponseEntity().getTotalItemCount(), is(greaterThanOrEqualTo(1)));

        ResponseWrapper<CustomAttribute> putCustomAttribute = cdsTestUtil.putCustomAttribute(customerIdentity, userIdentity, updatedDepartment);
        String customAttributeIdentity = putCustomAttribute.getResponseEntity().getIdentity();

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(customAttributeIdentity)
            .build();

        assertThat(putCustomAttribute.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(putCustomAttribute.getResponseEntity().getValue(), is(equalTo(updatedDepartment)));
    }

    @Test
    @TestRail(testCaseId = {"12369", "12370"})
    @Description("Get a CustomAttribute by its identity and update")
    public void getCustomAttributeByIdAndUpdate() {
        String updatedDepartment = generateStringUtil.getRandomString();
        ResponseWrapper<CustomAttribute> customAttributeAdded = cdsTestUtil.addCustomAttribute(customerIdentity, userIdentity);

        String attributeIdentity = customAttributeAdded.getResponseEntity().getIdentity();
        ResponseWrapper<CustomAttribute> customAttributes = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_CUSTOM_ATTRIBUTE_BY_ID, CustomAttribute.class, customerIdentity, userIdentity, attributeIdentity);

        assertThat(customAttributes.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(customAttributes.getResponseEntity().getIdentity(), is(equalTo(attributeIdentity)));

        ResponseWrapper<CustomAttribute> updateCustomAttribute = cdsTestUtil.updateAttribute(customerIdentity, userIdentity, attributeIdentity, updatedDepartment);

        assertThat(updateCustomAttribute.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        assertThat(updateCustomAttribute.getResponseEntity().getValue(), is(equalTo(updatedDepartment)));

        customAttributesIdentityHolder = IdentityHolder.builder()
            .customerIdentity(customerIdentity)
            .userIdentity(userIdentity)
            .customAttributeIdentity(attributeIdentity)
            .build();
    }
}
