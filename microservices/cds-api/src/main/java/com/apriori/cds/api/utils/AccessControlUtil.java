package com.apriori.cds.api.utils;

import static com.apriori.cds.api.enums.ApplicationEnum.CIS;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.AccessControlRequest;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.cds.api.models.response.AccessControls;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.http.HttpStatus;

public class AccessControlUtil {
    private final ApplicationUtil applicationUtil;
    private RequestEntityUtil requestEntityUtil;

    public AccessControlUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
        this.applicationUtil = new ApplicationUtil(requestEntityUtil);
    }

    /**
     * Post to add out of context access control
     *
     * @param inlineVariables - inline variables
     * @return new object
     */
    public ResponseWrapper<AccessControlResponse> addAccessControl(String... inlineVariables) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControlResponse.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body(
                "accessControl",
                AccessControlRequest.builder()
                    .customerIdentity(RequestEntityUtilBuilder.useRandomUser().getEmbeddedUser().getUserDetails().getCustomerIdentity())
                    .deploymentIdentity(PropertiesContext.get("cds.apriori_production_deployment_identity"))
                    .installationIdentity(PropertiesContext.get("cds.apriori_core_services_installation_identity"))
                    .applicationIdentity(applicationUtil.getApplicationIdentity(CIS))
                    .createdBy("#SYSTEM00000")
                    .roleName("USER")
                    .roleIdentity(PropertiesContext.get("cds.identity_role"))
                    .build()
            );

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Calls an API with get verb
     *
     * @param customerIdentity - the customer id
     * @param userIdentity     - the user id
     * @return new object
     */
    public AccessControls getAccessControl(String customerIdentity, String userIdentity) {
        RequestEntity requestEntity =
            requestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControls.class)
                .inlineVariables(customerIdentity, userIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);
        ResponseWrapper<AccessControls> accessControl = HTTPRequest.build(requestEntity).get();

        return accessControl.getResponseEntity();
    }
}
