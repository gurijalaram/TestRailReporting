package com.apriori.utils.authorization;

import com.apriori.utils.DeploymentItem;
import com.apriori.utils.GetDeploymentsResponse;
import com.apriori.utils.enums.DeploymentsAPIEnum;
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
     * Gets deployments
     *
     * @param userCredentials - UserCredentials instance containing user details to use in api call
     * @return Instance of GetDeploymentsResponse with ResponseWrapper
     */
    private ResponseWrapper<GetDeploymentsResponse> getDeployments(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil
            .init(DeploymentsAPIEnum.DEPLOYMENTS, GetDeploymentsResponse.class)
            .token(userCredentials.getToken())
            .inlineVariables(
                PropertiesContext.get("${env}.customer_identity"),
                PropertiesContext.get("${env}.secret_key")
            );

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets deployments response object to allow for usage of response
     *
     * @param userCredentials UserCredentials instance containing user details to use in api call
     * @return GetDeploymentsResponse instance
     */
    private GetDeploymentsResponse getDeploymentsResponse(UserCredentials userCredentials) {
        return getDeployments(userCredentials).getResponseEntity();
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return string
     */
    public String getAuthTargetCloudContext(UserCredentials userCredentials) {
        GetDeploymentsResponse getDeploymentsResponse = getDeploymentsResponse(userCredentials);
        DeploymentItem deploymentItem = getDeploymentsResponse.getItems().get(0);

        String cloudContext = PropertiesContext.get("${env}.customer_identity");
        String deploymentInstallation = "";
        String deploymentApplication = "";

        String deploymentItemName = deploymentItem.getName();
        String installationItemName = deploymentItem.getInstallations().get(4).getName();
        String applicationItemName = deploymentItem.getInstallations().get(4).getApplications().get(2).getServiceName();

        boolean correctInstallation = deploymentItemName.equalsIgnoreCase(PropertiesContext.get("${env}.deployment_name")) &&
            installationItemName.equalsIgnoreCase(PropertiesContext.get("${env}.installation_name")) &&
            applicationItemName.equalsIgnoreCase(PropertiesContext.get("${env}.application_name"));

        if (correctInstallation) {
            deploymentInstallation = deploymentItem.getInstallations().get(3).getIdentity();
            deploymentApplication = deploymentItem.getInstallations().get(3).getApplications().get(2).getIdentity();
        }

        return cloudContext.concat(deploymentItem.getIdentity()).concat(deploymentInstallation).concat(deploymentApplication);
    }
}
