package com.apriori.utils.authorization;

import com.apriori.utils.ApplicationItem;
import com.apriori.utils.DeploymentItem;
import com.apriori.utils.GetDeploymentsResponse;
import com.apriori.utils.InstallationItem;
import com.apriori.utils.enums.DeploymentsAPIEnum;
import com.apriori.utils.enums.TokenEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class AuthorizationUtil {
    private String username = PropertiesContext.get("${env}.ats.token_username");
    private String email = PropertiesContext.get("${env}.ats.token_email");
    private String issuer = PropertiesContext.get("${env}.ats.token_issuer");
    private String subject = PropertiesContext.get("${customer}.token_subject");
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    private ResponseWrapper<String> getDeployments(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil
            .init(DeploymentsAPIEnum.DEPLOYMENTS, null)
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
        return JsonManager.convertBodyToJson(getDeployments(userCredentials), GetDeploymentsResponse.class);
    }

    /**
     * Gets Authorisation Target Cloud Context
     *
     * @param userCredentials - user credentials
     * @return String - cloud context
     */
    public String getAuthTargetCloudContext(UserCredentials userCredentials) {
        String cloudContext = PropertiesContext.get("${env}.customer_identity");
        String deploymentNameFromConfig = PropertiesContext.get("${env}.deployment_name");
        String installationNameFromConfig = PropertiesContext.get("${env}.installation_name");
        String applicationNameFromConfig = PropertiesContext.get("${env}.application_name");

        DeploymentItem deploymentItem = getDeploymentsResponse(userCredentials).getItems()
            .stream()
            .filter(element -> element.getName().equalsIgnoreCase(deploymentNameFromConfig))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        InstallationItem installationItem = deploymentItem.getInstallations()
            .stream()
            .filter(element -> element.getName().equals(installationNameFromConfig))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        ApplicationItem applicationItem = installationItem.getApplications()
            .stream()
            .filter(element -> element.getServiceName().equalsIgnoreCase(applicationNameFromConfig))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        return cloudContext.concat(deploymentItem.getIdentity()).concat(installationItem.getIdentity()).concat(applicationItem.getIdentity());
    }
}
