package com.apriori.utils.authorization;

import com.apriori.utils.enums.ApplicationMetadataEnum;
import com.apriori.utils.enums.TokenEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationUtil {
    private String username = PropertiesContext.get("${env}.ats.token_username");
    private String email = PropertiesContext.get("${env}.ats.token_email");
    private String issuer = PropertiesContext.get("${env}.ats.token_issuer");
    private String subject = PropertiesContext.get("${customer}.token_subject");

    public AuthorizationUtil(UserCredentials userCredentials) {
        this.username = userCredentials.getUsername();
        this.email = userCredentials.getEmail();
    }

    public AuthorizationUtil() {
    }

    /**
     * POST to get a JWT token
     *
     * @return string
     */
    public ResponseWrapper<Token> getToken() {
        log.info("Getting ATS Token...");

        RequestEntity requestEntity = RequestEntityUtil.init(TokenEnum.POST_TOKEN, Token.class)
            .body(TokenRequest.builder()
                .token(TokenInformation.builder()
                    .issuer(issuer)
                    .subject(subject)
                    .claims(Claims.builder()
                        .name(username)
                        .email(email)
                        .build())
                    .build())
                .build());

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets token as string
     *
     * @return string
     */
    public String getTokenAsString() {
        return getToken().getResponseEntity().getToken();
    }

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
        /*CloudContext cloudContextResponse = getApplicationMetadata(userCredentials).getResponseEntity().getCloudContext();
        String customerIdentity = cloudContextResponse.getCustomerIdentity();
        String deploymentIdentity = cloudContextResponse.getDeploymentIdentity();
        String installationIdentity = cloudContextResponse.getInstallationIdentity();
        String applicationIdentity = cloudContextResponse.getApplicationIdentity();

        return customerIdentity + deploymentIdentity + installationIdentity + applicationIdentity;*/
        // TODO: 30/05/2022 cn - need to speak to John about this
        return "Currently unimplemented";
    }
}
