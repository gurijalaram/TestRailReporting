package com.apriori.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.services.cas.Customer;
import com.apriori.apibase.services.common.objects.ErrorMessage;
import com.apriori.cas.enums.CASAPIEnum;
import com.apriori.cas.utils.CasTestUtil;
import com.apriori.cas.utils.Constants;
import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.entity.IdentityHolder;
import com.apriori.entity.response.CasErrorMessage;
import com.apriori.entity.response.CustomerUser;
import com.apriori.entity.response.LicenseResponse;
import com.apriori.entity.response.Site;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

public class CasLicenseTests {

    private String token;
    private IdentityHolder deleteIdentityHolder;
    private com.apriori.entity.IdentityHolder userIdentityHolder;
    private IdentityHolder customerIdentityHolder;
    private CasTestUtil casTestUtil = new CasTestUtil();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void getToken() {
        token = new AuthorizationUtil().getTokenAsString();
    }

    @After
    public void cleanUp() {
        if (deleteIdentityHolder != null) {
            cdsTestUtil.delete(CDSAPIEnum.DELETE_SPECIFIC_USER_SUB_LICENSE_USERS,
                    deleteIdentityHolder.customerIdentity(),
                    deleteIdentityHolder.siteIdentity(),
                    deleteIdentityHolder.licenseIdentity(),
                    deleteIdentityHolder.subLicenseIdentity(),
                    deleteIdentityHolder.userIdentity()
            );
            if (userIdentityHolder != null) {
                cdsTestUtil.delete(CDSAPIEnum.DELETE_USERS_BY_CUSTOMER_USER_IDS,
                        userIdentityHolder.customerIdentity(),
                        userIdentityHolder.userIdentity()
                );
                if (customerIdentityHolder != null) {
                    cdsTestUtil.delete(CDSAPIEnum.DELETE_CUSTOMER_BY_ID,
                            customerIdentityHolder.customerIdentity()
                    );

                }
            }
        }
    }

    @Test
    @TestRail(testCaseId = {"10877"})
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

        ResponseWrapper<LicenseResponse> licenseResponse = casTestUtil.addLicense(Constants.CAS_EXPIRED_LICENSE, customerIdentity, siteIdentity, customerName, siteID, licenseId, subLicenseId);
        assertThat(licenseResponse.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
        String licenseIdentity = licenseResponse.getResponseEntity().getResponse().getIdentity();
        String subLicenseIdentity = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getIdentity();
        LocalDate expireDate = licenseResponse.getResponseEntity().getResponse().getSubLicenses().get(1).getExpiresAt();

        ResponseWrapper<CasErrorMessage> response = CasTestUtil.addSubLicenseAssociationUser(CasErrorMessage.class, customerIdentity, siteIdentity, licenseIdentity, subLicenseIdentity, userIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CONFLICT)));
        assertThat(response.getResponseEntity().getMessage(), is(equalTo(String.format("Sub License with identity '%s' expired on '%s' and cannot be assigned to a user.", subLicenseIdentity, expireDate))));

        deleteIdentityHolder = IdentityHolder.builder()
                .customerIdentity(customerIdentity)
                .siteIdentity(siteIdentity)
                .licenseIdentity(licenseIdentity)
                .subLicenseIdentity(subLicenseIdentity)
                .userIdentity(userIdentity)
                .build();
    }
}