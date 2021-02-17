package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.entity.response.LicenseResponse;
import com.apriori.cds.objects.request.License;
import com.apriori.cds.objects.request.LicenseRequest;
import com.apriori.cds.objects.response.Customer;
import com.apriori.cds.objects.response.Site;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class CdsLicenseTests extends CdsTestUtil {

    private String url;
    private String userIdentityEndpoint;
    private String customerIdentityEndpoint;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @After
    public void cleanUp() {
        if (userIdentityEndpoint != null) {
            delete(userIdentityEndpoint);
        }
        if (customerIdentityEndpoint != null) {
            delete(customerIdentityEndpoint);
        }
    }

    @Test
    @Description("Post user licenses")
    public void postUserLicense() {
        String customersEndpoint = String.format(url, "customers");

        String customerName = generateStringUtil.generateCustomerName();
        String cloudRef = generateStringUtil.generateCloudReference();
        String salesForceId = generateStringUtil.generateSalesForceId();
        String emailPattern = "\\S+@".concat(customerName);
        String siteName = generateStringUtil.generateSiteName();
        String siteID = generateStringUtil.generateSiteID();
        String licenseId = UUID.randomUUID().toString();
        String subLicenseId = UUID.randomUUID().toString();

        ResponseWrapper<Customer> customer = addCustomer(customersEndpoint, Customer.class, customerName, cloudRef, salesForceId, emailPattern);
        String customerIdentity = customer.getResponseEntity().getResponse().getIdentity();
        customerIdentityEndpoint = String.format(url, String.format("customers/%s", customerIdentity));
        String siteEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat("/sites")));

        ResponseWrapper<Site> site = addSite(siteEndpoint, Site.class, siteName, siteID);
        String siteIdentity = site.getResponseEntity().getResponse().getIdentity();
        String licenseEndpoint = String.format(url, String.format("customers/%s", customerIdentity.concat(String.format("/sites/%s/licenses", siteIdentity))));

        RequestEntity requestEntity = RequestEntity.init(licenseEndpoint, LicenseResponse.class)
            .setHeaders("Content-Type", "application/json")
            .setBody(new LicenseRequest().setLicense(
                new License().setDescription("Test License")
                    .setApVersion("2020 R1")
                    .setCreatedBy("#SYSTEM00000")
                    .setActive("true")
                    .setLicense(String.format(Constants.getLicense(), customerName, siteID, licenseId, subLicenseId))
                    .setLicenseTemplate(String.format(Constants.getLicenseTemplate(), customerName))));

        ResponseWrapper<LicenseResponse> response = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_CREATED)));
    }
}
