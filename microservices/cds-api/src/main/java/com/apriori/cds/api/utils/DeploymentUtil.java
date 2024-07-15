package com.apriori.cds.api.utils;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.AddDeployment;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.Deployment;

import org.apache.http.HttpStatus;

public class DeploymentUtil {
    private RequestEntityUtil requestEntityUtil;

    public DeploymentUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Calls an API with GET verb
     *
     * @param customerIdentity   - the customer id
     * @param deploymentIdentity - the deployment id
     * @return new object
     */
    public Deployment getDeployment(String customerIdentity, String deploymentIdentity) {
        RequestEntity requestEntityDep = requestEntityUtil.init(CDSAPIEnum.DEPLOYMENT_BY_CUSTOMER_DEPLOYMENT_IDS, Deployment.class)
            .inlineVariables(customerIdentity, deploymentIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<Deployment> response = HTTPRequest.build(requestEntityDep).get();

        return response.getResponseEntity();
    }

    /**
     * POST call to add a deployment to a customer
     *
     * @param customerIdentity - the customer id
     * @param siteIdentity     - the site Identity
     * @return new object
     */
    public ResponseWrapper<Deployment> addDeployment(
        String customerIdentity,
        String deploymentName,
        String siteIdentity,
        String deploymentType) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.DEPLOYMENTS_BY_CUSTOMER_ID, Deployment.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "deployment",
                AddDeployment.builder()
                    .name(deploymentName)
                    .description("Deployment added by API automation")
                    .deploymentType(deploymentType)
                    .siteIdentity(siteIdentity)
                    .active("true")
                    .isDefault("true")
                    .createdBy("#SYSTEM00000")
                    .apVersion("2020 R1")
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }
}
