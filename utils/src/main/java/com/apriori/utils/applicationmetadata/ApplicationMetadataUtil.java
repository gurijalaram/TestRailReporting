package com.apriori.utils.applicationmetadata;

import com.apriori.utils.enums.ApplicationMetadataEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

public class ApplicationMetadataUtil {

    /**
     * GET application metadata
     *
     * @param userCredentials - the user credentials
     * @return application metadata object
     */
    public ResponseWrapper<ApplicationMetadata> getApplicationMetadata(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(ApplicationMetadataEnum.GET_APPLICATION_METADATA, ApplicationMetadata.class)
            .token(userCredentials.getToken());

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return string
     */
    public String getAuthTargetCloudContext(UserCredentials userCredentials) {
        CloudContext cloudContextResponse = getApplicationMetadata(userCredentials).getResponseEntity().getCloudContext();
        String customerIdentity = cloudContextResponse.getCustomerIdentity();
        String deploymentIdentity = cloudContextResponse.getDeploymentIdentity();
        String installationIdentity = cloudContextResponse.getInstallationIdentity();
        String applicationIdentity = cloudContextResponse.getApplicationIdentity();

        return customerIdentity + deploymentIdentity + installationIdentity + applicationIdentity;
    }
}
