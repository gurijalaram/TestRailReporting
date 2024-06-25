package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.FeatureRequest;
import com.apriori.cds.api.models.response.ErrorResponse;
import com.apriori.cds.api.models.response.FeatureResponse;
import com.apriori.cds.api.models.response.InstallationItems;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.Features;

import org.apache.http.HttpStatus;

public class InstallationUtil {
    private final RequestEntityUtil requestEntityUtil;

    public InstallationUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * POST call to add an installation to a customer
     *
     * @param customerIdentity   - the customer id
     * @param deploymentIdentity - the deployment id
     * @param siteIdentity       - the site Identity
     * @param realmKey           - the realm key
     * @param cloudReference     - the cloud reference
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addInstallation(
        String customerIdentity,
        String deploymentIdentity,
        String name,
        String realmKey,
        String cloudReference,
        String siteIdentity,
        Boolean highMem) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "installation",
                InstallationItems.builder()
                    .name(name)
                    .description("Installation added by API automation")
                    .active(true)
                    .region("na-1")
                    .realm(realmKey)
                    .url("https://na-1.qa.apriori.net")
                    .s3Bucket("apriori-qa-blue-fms")
                    .tenant("default")
                    .tenantGroup("default")
                    .clientId("apriori-web-cost")
                    .clientSecret("donotusethiskey")
                    .createdBy("#SYSTEM00000")
                    .cidGlobalKey("donotusethiskey")
                    .siteIdentity(siteIdentity)
                    .cloudReference(cloudReference)
                    .apVersion("2023 R1")
                    .highMem(highMem)
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call to add an installation with feature to a customer
     *
     * @param customerIdentity   - the customer id
     * @param deploymentIdentity - the deployment id
     * @param siteIdentity       - the site Identity
     * @param realmKey           - the realm key
     * @param cloudReference     - the cloud reference
     * @return new object
     */
    public ResponseWrapper<InstallationItems> addInstallationWithFeature(
        String customerIdentity,
        String deploymentIdentity,
        String realmKey,
        String cloudReference,
        String siteIdentity,
        Boolean bulkCostingEnabled) {

        InstallationItems installationItems = JsonManager.deserializeJsonFromInputStream(
            FileResourceUtil.getResourceFileStream("InstallationItems" + ".json"), InstallationItems.class);
        installationItems.setRealm(realmKey);
        installationItems.setSiteIdentity(siteIdentity);
        installationItems.setCloudReference(cloudReference);
        installationItems.setHighMem(false);
        installationItems.setFeatures(Features
            .builder()
            .bulkCostingEnabled(bulkCostingEnabled)
            .build());

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.INSTALLATIONS_BY_CUSTOMER_DEPLOYMENT_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("installation", installationItems);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Patch installation
     *
     * @param customerIdentity     - the customer id
     * @param deploymentIdentity   - the deployment id
     * @param installationIdentity - the installation id
     * @return new object
     */
    public ResponseWrapper<InstallationItems> patchInstallation(
        String customerIdentity,
        String deploymentIdentity,
        String installationIdentity) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.INSTALLATION_BY_CUSTOMER_DEPLOYMENT_INSTALLATION_IDS, InstallationItems.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(
                "installation",
                InstallationItems.builder()
                    .cloudReference("eu-1")
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST call to add a feature to Installation
     *
     * @return new object
     */
    public ResponseWrapper<FeatureResponse> addFeature(
        String customerIdentity,
        String deploymentIdentity,
        String installationIdentity,
        Boolean bulkCostingEnabled) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, FeatureResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .bulkCostingEnabled(bulkCostingEnabled)
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * POST call trying to add invalid feature to Installation
     *
     * @return ErrorResponse
     */
    public ErrorResponse addFeatureWrongResponse(
        String customerIdentity,
        String deploymentIdentity,
        String installationIdentity,
        Boolean bulkCostingEnabled) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, ErrorResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .bulkCostingEnabled(bulkCostingEnabled)
                    .build())
                .build());

        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).post();

        return errorResponse.getResponseEntity();
    }

    /**
     * PUT call to update a feature to Installation
     *
     * @return new object
     */
    public ResponseWrapper<FeatureResponse> updateFeature(String customerIdentity, String deploymentIdentity, String installationIdentity, boolean bulkCosting) {
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, FeatureResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .bulkCostingEnabled(bulkCosting)
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * PUT call to update a feature to Installation - wrong response
     *
     * @return new ErrorResponse
     */
    public ErrorResponse updateFeatureWrongResponse(
        String customerIdentity,
        String deploymentIdentity,
        String installationIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.INSTALLATION_FEATURES, ErrorResponse.class)
            .inlineVariables(customerIdentity, deploymentIdentity, installationIdentity)
            .expectedResponseCode(HttpStatus.SC_BAD_REQUEST)
            .body(FeatureRequest.builder()
                .features(Features.builder()
                    .build())
                .build());

        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).put();

        return errorResponse.getResponseEntity();
    }
}
