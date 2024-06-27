package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.ActivateLicense;
import com.apriori.cds.api.models.request.ActivateLicenseRequest;
import com.apriori.cds.api.models.request.License;
import com.apriori.cds.api.models.request.LicenseRequest;
import com.apriori.cds.api.models.response.AssociationUserItems;
import com.apriori.cds.api.models.response.LicenseResponse;
import com.apriori.cds.api.models.response.SubLicenseAssociationUser;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import lombok.SneakyThrows;
import org.apache.http.HttpStatus;

import java.nio.charset.StandardCharsets;

public class LicenseUtil {
    private RequestEntityUtil requestEntityUtil;

    public LicenseUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * POST call to add a sub-license association user
     *
     * @param inlineVariables - the inline variables
     * @param userIdentity    - the user id
     * @return new object
     */
    public ResponseWrapper<SubLicenseAssociationUser> addSubLicenseAssociationUser(String userIdentity, String... inlineVariables) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.SUBLICENSE_ASSOCIATIONS_USERS, SubLicenseAssociationUser.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "userAssociation",
                AssociationUserItems.builder()
                    .userIdentity(userIdentity)
                    .createdBy("#SYSTEM00000")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post to add site license
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @param customerName     - the customer name
     * @param siteId           - the site id
     * @param licenseId        - the license id
     * @param subLicenseId     - the sublicense id
     * @return new object
     */
    @SneakyThrows
    public ResponseWrapper<LicenseResponse> addLicense(String customerIdentity, String siteIdentity, String customerName, String siteId, String licenseId, String subLicenseId) {

        String licenseXml = new String(FileResourceUtil.getResourceFileStream("CdsLicense.xml").readAllBytes(), StandardCharsets.UTF_8);
        String licenseTemplate = new String(FileResourceUtil.getResourceFileStream("CdsLicenseTemplate.xml").readAllBytes(), StandardCharsets.UTF_8);

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.LICENSE_BY_CUSTOMER_SITE_IDS, LicenseResponse.class)
            .inlineVariables(customerIdentity, siteIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(LicenseRequest.builder()
                .license(
                    License.builder()
                        .description("Test License")
                        .apVersion("2020 R1")
                        .createdBy("#SYSTEM00000")
                        .active("false")
                        .license(String.format(licenseXml, customerName, siteId, licenseId, subLicenseId))
                        .licenseTemplate(String.format(licenseTemplate, customerName))
                        .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Post request to activate license
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site id
     * @param licenseIdentity  - the license identity
     * @param userIdentity     - the user identity
     */
    public void activateLicense(String customerIdentity, String siteIdentity, String licenseIdentity, String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.LICENSE_ACTIVATE, LicenseResponse.class)
            .inlineVariables(customerIdentity, siteIdentity, licenseIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(ActivateLicenseRequest.builder()
                .license(ActivateLicense.builder()
                    .active(true)
                    .updatedBy(userIdentity)
                    .build())
                .build());

        HTTPRequest.build(requestEntity).post();
    }
}
