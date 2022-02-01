package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.entity.IdentityHolder;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.Licenses;
import com.apriori.entity.response.Site;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CasLicenseTests {

    private String token;
    private IdentityHolder deleteIdentityHolder;
    private com.apriori.entity.IdentityHolder userIdentityHolder;
    private IdentityHolder customerIdentityHolder;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @After
    public void cleanUp() {
        if (deleteIdentityHolder != null) {
            casTestUtil.delete(CASAPIEnum.DELETE_SPECIFIC_USER_SUB_LICENSE_USERS,
                    deleteIdentityHolder.customerIdentity(),
                    deleteIdentityHolder.siteIdentity(),
                    deleteIdentityHolder.licenseIdentity(),
                    deleteIdentityHolder.subLicenseIdentity(),
                    deleteIdentityHolder.userIdentity()
            );
            if (userIdentityHolder !=null) {
                casTestUtil.delete(CASAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS,
                        userIdentityHolder.customerIdentity(),
                        userIdentityHolder.userIdentity()
                );
                if (customerIdentityHolder !=null) {
                    casTestUtil.delete(CASAPIEnum.CUSTOMER,
                            customerIdentityHolder.customerIdentity()
                    );

                }
            }
        }
    }

    @Test
    @Description("Make sure users cannot be assigned to a sublicense under an expired license")
    public void expiredLicense() {
        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String email = customerName.toLowerCase();
        String description = customerName + " Description";
        String userName = generateStringUtil.generateUserName();
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = CasTestUtil.addCustomer(customerName, cloudRef, description, email);
        String customerIdentity = customer.getResponseEntity().getIdentity();
        customerIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .build();

        ResponseWrapper<CustomerUser> user = CasTestUtil.addUser(customerIdentity, userName, customerName);
        String userIdentity = user.getResponseEntity().getIdentity();
        userIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .userIdentity(userIdentity)
                .build();

        ResponseWrapper<Site> site = CasTestUtil.addSite(customerIdentity, siteID, siteName);
        String siteIdentity = site.getResponseEntity().getIdentity();

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(customerIdentity, siteIdentity, customerName, siteID, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();

        ResponseWrapper<Licenses> license = casTestUtil.getCommonRequest(CASAPIEnum.GET_LICENSES_BY_CUSTOMER_ID,
                Licenses.class,
                customerIdentity
        );
        String licenseIdentity = license.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<ErrorMessage> response = CasTestUtil.addSubLicenseAssociationUser(ErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_BAD_REQUEST)));

        deleteIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .siteIdentity(siteIdentity)
                .licenseIdentity(licenseIdentity)
                .subLicenseIdentity(subLicenseIdentity)
                .userIdentity(userIdentity)
                .build();
    }
}