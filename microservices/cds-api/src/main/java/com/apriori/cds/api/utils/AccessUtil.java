package com.apriori.cds.api.utils;

import static com.apriori.cds.api.enums.ApplicationEnum.CIS;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.AccessControlRequest;
import com.apriori.cds.api.models.response.AccessControlResponse;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.properties.PropertiesContext;

import org.apache.http.HttpStatus;

public class AccessUtil {
    private RequestEntityUtil requestEntityUtil;

    private final ApplicationUtil applicationUtil;

    // constructor that accepts requestEntity (user data) we created in the test
    public AccessUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
        applicationUtil = new ApplicationUtil(requestEntityUtil);
    }

    // no constructor needed in here as this method is only used twice so was easy to change

    /**
     * Post to add out of context access control
     *
     * @return new object
     */
    public ResponseWrapper<AccessControlResponse> addAccessControl(String customerIdentity, String userIdentity) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.ACCESS_CONTROLS, AccessControlResponse.class)
            .inlineVariables(customerIdentity, userIdentity)
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
}
